package com.shop.basket;

import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BasketRepository {

	private static final String GET = "select * from basket where id = ?";
	private static final String INSERT = "insert into basket values (null) ;";
	private JdbcTemplate template;

	@Autowired
	public BasketRepository(JdbcTemplate template) {
		this.template = template;
	}

	public List<Basket> list(){
		
		return template.query("select * from basket ", new BeanPropertyRowMapper<Basket>(Basket.class));
		
	}
	public Long insert() {
		KeyHolder holder = new GeneratedKeyHolder();
		template.update(con -> con.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS), holder);

		return holder.getKey().longValue();
	}

	public Basket get(Long id) {

		Basket basket = template.queryForObject(GET, new Object[] { id },
				new BeanPropertyRowMapper<Basket>(Basket.class));

		return basket;
	}

	public void delete(Long id) {
		template.update("Delete from basket where id = ? ;", id);
	}

}
