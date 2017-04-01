CREATE TABLE basket (
     id BIGINT(20) NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (id)
);

CREATE TABLE basket_product (
     id BIGINT(20) NOT NULL AUTO_INCREMENT,
     product_id VARCHAR(100) NOT NULL,
     quantity INT NOT NULL,
     basket_id BIGINT(20) NOT NULL,
 PRIMARY KEY (id),    
 FOREIGN KEY (basket_id) REFERENCES basket(id) ON DELETE CASCADE
);