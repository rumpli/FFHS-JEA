## Setup

### Database connection
Update the file `application.properties` in the folder `./src/main/resources/`.

```properties
spring.application.name=${ApplicationName}

server.port=${ApplicationPort}

spring.jpa.hibernate.ddl-auto=update

spring.datasource.url=jdbc:mysql://${DatabaseHostNameOrIP}:3306/${DatabaseName}
spring.datasource.username=${DatabaseUsername}
spring.datasource.password=${DatabasePassword}
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

spring.jpa.properties.hibernate.globally_quoted_identifiers=true
```

### Deployment
Run gradle task to create OCI image (For amd64)
```Bash
./gradlew run bootBuildImage
```
or create boot jar for other deployments

```Bash
./gradlew run bootJar
```