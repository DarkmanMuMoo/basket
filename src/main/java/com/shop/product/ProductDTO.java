package com.shop.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class ProductDTO {
	@NotNull
	private String id;
	@NotNull
	@Min(0)
	private Integer quantity;

	public ProductDTO() {
	}

	public ProductDTO(String id, Integer quantity) {
		super();
		this.id = id;
		this.quantity = quantity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
