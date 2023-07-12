# Challenge técnico Mercado Libre

El proyecto consiste en desarrollar un acortador de URLs utilizando un sistema de base 62. La aplicación permitirá a los usuarios ingresar una URL larga y generará una URL corta única utilizando el algoritmo de conversión ya mencionado. Esta URL corta puede ser utilizada para acceder a la página original.
El sistema asigna a partir del Id de la Url original,un identificador único almacenado en la base de datos.Este identificador es reutilizado para redireccionar la nueva Url a su dirección original.

# Tecnologías utilizadas:

Para la implementación se utilizó Java, Springboot, JPA, y mySql.  


# DATABASE 
```java
#spring.datasource.url=jdbc:mysql://localhost/challenge?useSSL=false
spring.datasource.url=jdbc:mysql://localhost/<nombreDeBaseDeDatos>?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrival=true
#spring.datasource.dbname=<nombreDeBaseDeDatos>
spring.datasource.username=root
spring.datasource.password="Su-clave"
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
server.error.include-message=always

```


# Rutas


Crear Url corta:
```java

http://localhost:8080/api/url/create-short-url

```
Cantidad creadas:

```java

http://localhost:8080/api/url/get-amount-url-created

```

Cantidad inactivas:

```java

http://localhost:8080/api/url/get-amount-url-inactives

```

Cantidad activas:

```java

http://localhost:8080/api/url/get-amount-url-inactives

```
Probar Url corta:
```java

http://localhost:8080/api/url/{caracter-generado}

```

Traer información sobre Urls:
```java

http://localhost:8080/api/url/get-url-created

```
