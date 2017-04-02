package com.webon.basket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.webon.product.Product;
import com.webon.product.ProductDTO;
import com.webon.product.ProductRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Category(value = IntegrationTest.class)
public class BasketControllerTest {
	private static final String BASKET = "basket";
	private static final String BASKET_PRODUCT = "basket_product";
	@Autowired
	private TestRestTemplate template;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private BasketRepository basketRepository;
	@Autowired
	private ProductRepository productRepository;

	@LocalServerPort
	int port;

	@Test
	public void testCreate_success() {
		ResponseEntity<Map> res = this.template.postForEntity("/basket", null, Map.class);
		assertEquals(201, res.getStatusCodeValue());
		assertNotNull(res.getBody().get("id"));
		assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, BASKET));
	}

	@Test
	public void testGet_success() {

		Long id = addBasket();
		ResponseEntity<BasketDTO> res = this.template.getForEntity("/basket/" + id, null, BasketDTO.class);
		assertEquals(200, res.getStatusCodeValue());

	}

	@Test
	public void testGet_failed() {
		ResponseEntity<BasketDTO> res = this.template.getForEntity("/basket/1", null, BasketDTO.class);
		assertEquals(404, res.getStatusCodeValue());
	}

	@Test
	public void testDelete_success() {
		Long id = addBasket();
		addProductToBasket("aaa", 10, id);

		this.template.delete("/basket/{id}", id);
		JdbcTestUtils.countRowsInTable(jdbcTemplate, BASKET);
		JdbcTestUtils.countRowsInTable(jdbcTemplate, BASKET_PRODUCT);

	}

	@Test
	public void testProduct_addProduct() {

		Long id = addBasket();
		ProductDTO product = new ProductDTO("aaa", 10);
		ResponseEntity<ProductDTO> res = this.template.postForEntity("/basket/" + id + "/product", product,
				ProductDTO.class);
		assertEquals(200, res.getStatusCodeValue());

		BasketDTO expectBasket = this.template.getForObject("/basket/" + id, BasketDTO.class);

		assertNotNull(expectBasket);
		assertEquals(1, expectBasket.getProductList().size());
		assertEquals(10, expectBasket.getProductList().get(0).getQuantity().intValue());

	}

	@Test
	public void testProduct_updateProduct() {

		Long id = addBasket();
		addProductToBasket("aaa", 10, id);

		ProductDTO updateProduct = new ProductDTO("aaa", 12);

		ResponseEntity<String> res = this.template.postForEntity("/basket/" + id + "/product", updateProduct,
				String.class);
		assertEquals(200, res.getStatusCodeValue());

		assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "basket_product"));

		BasketDTO expectBasket = this.template.getForObject("/basket/" + id, BasketDTO.class);

		assertNotNull(expectBasket);
		assertEquals(1, expectBasket.getProductList().size());
		assertEquals(12, expectBasket.getProductList().get(0).getQuantity().intValue());

	}

	@Test
	public void testProduct_deleteProduct() {
		Long id = addBasket();
		addProductToBasket("aaa", 10, id);

		ProductDTO updateProduct = new ProductDTO("aaa", 0);

		ResponseEntity<String> res = this.template.postForEntity("/basket/" + id + "/product", updateProduct,
				String.class);
		assertEquals(200, res.getStatusCodeValue());

		assertEquals(0, JdbcTestUtils.countRowsInTable(jdbcTemplate, "basket_product"));

		BasketDTO expectBasket = this.template.getForObject("/basket/" + id, BasketDTO.class);

		assertNotNull(expectBasket);
		assertEquals(0, expectBasket.getProductList().size());
	}

	@Test
	public void submit_notvalidFormat() {
		Long id = addBasket();
		addProductToBasket("aaa", 10, id);

		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("vat", "sadas");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(request,
				headers);

		ResponseEntity<String> res = this.template.postForEntity("/basket/{id}/submit", entity, String.class, id);
		assertEquals(400, res.getStatusCodeValue());

	}

	@Test
	public void submit_success() {
		Long id = addBasket();
		addProductToBasket("aaa", 10, id);

		MultiValueMap<String, String> request = new LinkedMultiValueMap<String, String>();
		request.add("vat", "LU26375245");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(request,
				headers);

		ResponseEntity<String> res = this.template.postForEntity("/basket/{id}/submit", entity, String.class, id);
		assertEquals(200, res.getStatusCodeValue());

	}

	@After
	public void cleanUp() {

		JdbcTestUtils.deleteFromTables(jdbcTemplate, BASKET);
		JdbcTestUtils.deleteFromTables(jdbcTemplate, BASKET_PRODUCT);

	}

	private Long addBasket() {
		Long id = basketRepository.insert();
		return id;
	}

	private void addProductToBasket(String productID, int qunatity, Long basketId) {
		productRepository.insert(new Product(productID, qunatity, basketId));
	}

}
