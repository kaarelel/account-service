A Spring Boot RESTful application for managing accounts using CRUD operations. Built with Java LTS, Gradle, Liquibase, and H2 database.

**Run the App**: 
bash ./gradlew bootRun

**Swagger UI**: 
POST(CreateAccount), GET(AccountInfoViaId), PUT(UpdateAccount), DELETE(DeleteAccount)

http://localhost:8080/swagger-ui/index.html

**H2 Console**: 

http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb ; 
Username: sa ; 
Password: (leave empty); 

**Technologies**:

Java 17 (LTS); 
Spring Boot 3; 
H2 Database; 
Liquibase; 
Gradle; 
Swagger (springdoc-openapi); 
JUnit + MockMvc (for testing); 
