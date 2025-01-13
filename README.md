# Mail Sender Explanation


## Objective  
The implementation goal is to send this email with a verification code to authenticate a new user,
working like a two-step verification. However, to send mail is necessary for some configurations in your email.

## Working Example  

![Screenshot from 2024-10-30 18-41-22](https://github.com/user-attachments/assets/71d80af9-ef8a-4797-a2d0-598e86a54088)

## Sending 
![Screenshot from 2024-10-30 18-44-08](https://github.com/user-attachments/assets/f93c62a6-96f2-4aa1-a526-ee5b0e60a588)

## Response
![Screenshot from 2024-10-30 18-46-34](https://github.com/user-attachments/assets/8192ba06-2fc2-4f33-9439-71a5dfe245e5)



## Learnings 
In this implementation, I learned how to use the class JavaMailSender with the SMTP protocol to send email.
The JavaMailSender is responsible for sending an email to the user. I also learned how to use Thymeleaf
with the SpringTemplateEngine to create an HTML template for creating a more personalized template. 
I also learned how to use GreenMail, which is necessary to develop integration tests.

## Configurations 
* Bellow is the necessary configuratitions to the implementation work.

###  Necessary dependencies 
  * The necessary depedencies to the implementation work.


```

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>


		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>

    
		<dependency>
			<groupId>com.icegreen</groupId>
			<artifactId>greenmail-junit5</artifactId>
			<version>2.0.1</version>
			<scope>test</scope>
		</dependency>


```

### Gmail Configuration

  * This pdf has an explanation of how to configure your Gmail to use this service for sending email.
  * [GMAIL - SMTP Service](https://drive.google.com/file/d/1S3_o-Jwl6JWUR3mIQ-PYwRb5PCspDi-B/view?usp=sharing)



### GreenMail Configuration
  * The two archives below explain how to add and configure GreenMail in your project.
  * [GreenMail For Spring Mail](https://rieckpil.de/use-greenmail-for-spring-mail-javamailsender-junit-5-integration-tests/)
  * [GitHub Repository](https://github.com/rieckpil/blog-tutorials/tree/master/spring-boot-test-mail-sending)


  

### Docker 

* You can pull the Docker image using this command:
 ```dotdocker
docker pull guilhermebauer/study_management
```

* <span style="color:red;"> But remember, for Docker to work, you need to have Docker installed on your machine. Here is the link to the official documentation to install Docker: [Docker Installation Guide](https://docs.docker.com/get-docker/)</span>


## Configuration of the Environment

* To run the project, you need a .env file on your machine with the following configuration:

```dotenv
DB_NAME= your_database_name
DB_USERNAME= your_username
DB_PASSWORD= your_password
DB_PORT= your_database_port
EXPOSE_PORT= your_expose_port
CONTAINER_PORT= your_container_port
SECRET_KEY= your_secret
jAVA_MAIL_EMAIL= your email
jAVA_MAIL_PASSWORD= your password
```

## Swagger

* Do not forget to change the port in the URL by the port you chose.

* [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

## Author
 www.linkedin.com/in/guilherme-bauer-desenvolvedor

