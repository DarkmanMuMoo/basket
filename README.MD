Require tool 
- java 8
- maven 3.3.9
- docker 1.13.0

Stack
- Spring boot 
- H2 (in memory db)
- Mysql (for production use)
- Flyway (DB version control)
- Retrofit (http client) 

How to run 

local profile 
  use h2 in memory DB  for  test in local machine

  mvn clean install;
  mvn spring-boot:run;

prod profile
  use mysql for db  and use docker for setup the enviroment 

  mvn clean install;
  mvn docker:build; 
  docker-compose pull basket-db;
  docker-compose up;


How to test 

  I have provided posman collection that you can use for test application.
  you can connect to DB to view the data directly.

  h2 
   go to http://localhost:8080/h2-console
   put jdbc:h2:mem:testdb  in JDBC URL
   click connect  

  mysql 
   connect to  localhost:3306
   usename: root
   password: admin 
   schema: basket
    