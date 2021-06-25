# spring-login
This is login registration system using:
- [x] Spring Boot
- [x] Spring Security
- [x] Java Mail
- [x] Email verification link with expiry

Database - PostgreSQL running with Docker. 
UserApp Table 
``` 
    create table user_app (
       id int8 not null,
        email varchar(255),
        enabled boolean,
        locked boolean,
        name varchar(255),
        password varchar(255),
        user_role varchar(255),
        username varchar(255),
        primary key (id)
    )
```
Test application via postman
```
POST localhost:8080/api/v1/registration
{
  "name" : example,
  "username" : example,
  "email" : "example@mail.com",
  "password" : "password"
}
```
Email verification was tested with MailDev. 

To run need create database and change configuration in src\main\resources\application.yml. 
