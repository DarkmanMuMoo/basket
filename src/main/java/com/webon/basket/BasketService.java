package com.webon.basket;

import static java.util.stream.Collectors.toList;
import static com.webon.exception.APIException.notFound;
import static com.webon.exception.APIException.badRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.webon.client.ValidationService;
import com.webon.exception.APIException;
import com.webon.product.Product;
import com.webon.product.ProductDTO;
import com.webon.product.ProductRepository;

@Service
@Transactional
public class BasketService {

	BasketRepository basketRepository;

	ProductRepository productRepository;

	ValidationService validationService;

	@Autowired
	public BasketService(BasketRepository basketRepository, ProductRepository productRepository,
			ValidationService validationService) {
		this.basketRepository = basketRepository;
		this.productRepository = productRepository;
		this.validationService = validationService;
	}

	public Long create() {
		return basketRepository.insert();
	}

	public BasketDTO get(Long id) throws APIException {
		checkExist(id);
		BasketDTO dto = new BasketDTO();
		dto.setProductList(productRepository.getBasketProduct(id).stream()
				.map(p -> new ProductDTO(p.getProductId(), p.getQuantity())).collect(toList()));
		return dto;
	}

	public void delete(Long id) throws APIException {
		checkExist(id);
		basketRepository.delete(id);
	}

	public void updateOrAddProduct(Long basketId, ProductDTO dto) throws APIException {
		checkExist(basketId);
		Product product = productRepository.get(dto.getId(), basketId);
		Integer quantity = dto.getQuantity();
		if (product == null) {
			if (quantity > 0) {
				productRepository.insert(new Product(dto.getId(), dto.getQuantity(), basketId));
			}
		} else if (quantity > 0) {
			if (!quantity.equals(product.getQuantity())) {
				product.setQuantity(dto.getQuantity());
				productRepository.update(product);
			}
		} else {
			productRepository.delete(product.getId());
		}

	}

	public void submit(Long id, String vat) throws APIException {
		checkExist(id);

		boolean isValidVat = validationService.validateVat(vat);
		if (!isValidVat) {
			throw badRequest("vat format not correct");
		}
		// assume order complete we remove basket
		basketRepository.delete(id);

	}

	private void checkExist(Long id) throws APIException {
		try {
			basketRepository.get(id);
		} catch (EmptyResultDataAccessException ex) {
			throw notFound("not basket found");
		}
	}

}
