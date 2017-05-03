package com.shop.basket;

import java.util.List;

import com.shop.product.ProductDTO;

public class BasketDTO {
    private Long id;
    private List<ProductDTO> productList;
    public BasketDTO(){
    	
    }
	public BasketDTO(Long id) {
		super();
		this.id = id;
	}
    
	public BasketDTO(Long id, List<ProductDTO> productList) {
		super();
		this.id = id;
		this.productList = productList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ProductDTO> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductDTO> productList) {
		this.productList = productList;
	}

	
}
