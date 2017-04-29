package com.shop.basket;

import java.util.List;

import com.shop.product.ProductDTO;

public class BasketDTO {

	List<ProductDTO> productList;

	public List<ProductDTO> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductDTO> productList) {
		this.productList = productList;
	}

	
}
