package com.shop.product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {
	private static final String DELETE_FROM_BASKET = "delete from basket_product where id = ?";
	private static final String SELECT_FROM_BASKET = "select * from basket_product where basket_id = ?";
	private static final String UPDATE_QUANTITY = "update basket_product set quantity = ? where id = ?";
	private static final String INSERT = "insert into basket_product values(null,?,?,?);";
	private static final String SELECT_FROM_PRODUCT_WHERE_PRODUCT_ID_AND_BASKET_ID = "select * from basket_product where product_id = ? and basket_id = ?";
	private JdbcTemplate template;

	private static final RowMapper<Product> mapper = new RowMapper<Product>() {
		@Override
		public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
			Product product = new Product();
			product.setId(rs.getLong("id"));
			product.setProductId(rs.getString("product_id"));
			product.setQuantity(rs.getInt("quantity"));
			product.setBasketId(rs.getLong("basket_id"));
			return product;
		}
	};

	@Autowired
	public ProductRepository(JdbcTemplate template) {
		this.template = template;
	}

	public Product get(String productId, Long basketId) {

		List<Product> list = template.query(SELECT_FROM_PRODUCT_WHERE_PRODUCT_ID_AND_BASKET_ID, mapper, productId,
				basketId);

		return list.isEmpty() ? null : list.get(0);
	}

	public List<Product> getBasketProduct(Long basketId) {
		return template.query(SELECT_FROM_BASKET, mapper, basketId);
	}

	public void insert(Product product) {

		template.update(INSERT, product.getProductId(), product.getQuantity(), product.getBasketId());

	}

	public void update(Product product) {
		template.update(UPDATE_QUANTITY, product.getQuantity(), product.getId());
	}

	public void delete(Long id) {
		template.update(DELETE_FROM_BASKET, id);
	}

}
