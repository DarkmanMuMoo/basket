package com.shop.product;

public class Product {

	private Long id;
	private String productId;
	private Integer quantity;
	private Long basketId;

	public Product() {
	}

	public Product(String productId, Integer quantity, Long basketId) {
		super();
		this.productId = productId;
		this.quantity = quantity;
		this.basketId = basketId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getBasketId() {
		return basketId;
	}

	public void setBasketId(Long basketId) {
		this.basketId = basketId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

}
