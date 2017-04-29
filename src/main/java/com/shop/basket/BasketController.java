package com.shop.basket;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shop.exception.APIException;
import com.shop.product.ProductDTO;

@RestController
@RequestMapping("basket")
public class BasketController {
	private BasketService basketService;

	@Autowired
	public BasketController(BasketService basketService) {
		super();
		this.basketService = basketService;
	}

	@PostMapping
	public ResponseEntity<Map<String, Long>> create(HttpServletRequest request) {
		Long id = basketService.create();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set(HttpHeaders.LOCATION, getURLBase(request) + "/basket/" + id);

		Map<String, Long> map = new HashMap<String, Long>();
		map.put("id", id);
		return new ResponseEntity<Map<String, Long>>(map, responseHeaders, HttpStatus.CREATED);
	}

	@GetMapping("{id}")
	public BasketDTO get(@PathVariable Long id) throws APIException {
		return basketService.get(id);

	}

	@DeleteMapping("{id}")
	public void delete(@PathVariable Long id) throws APIException {

		basketService.delete(id);
	}

	@PostMapping(value = "{id}/product", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ProductDTO product(@PathVariable(name = "id") Long basketId, @RequestBody ProductDTO product)
			throws APIException {

		basketService.updateOrAddProduct(basketId, product);

		return product;

	}

	@PostMapping(value = "{id}/submit", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public void submit(@PathVariable(name = "id") Long id, @RequestParam("vat") String vat) throws APIException {

		basketService.submit(id, vat);
	}

	private String getURLBase(HttpServletRequest request) {

		URL requestURL;
		try {
			requestURL = new URL(request.getRequestURL().toString());
			String port = requestURL.getPort() == -1 ? "" : ":" + requestURL.getPort();
			return requestURL.getProtocol() + "://" + requestURL.getHost() + port;
		} catch (MalformedURLException e) {
			return "";
		}

	}

}
