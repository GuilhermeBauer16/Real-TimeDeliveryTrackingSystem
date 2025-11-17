# Real-Time Delivery Tracking System
## Configurations 

* Bellow is the necessary configuratitions to the implementation work.
* It is necessary to configure the mail sender. Here is an explanation of how to do this: [Mail Sender Explanation](https://github.com/GuilhermeBauer16/Real-TimeDeliveryTrackingSystem/tree/mailSenderExplanation)
* It is necessary to configure the mercado pago. Here is an explanation of how to do this: [Mercado Pago Explanation](https://github.com/GuilhermeBauer16/Real-TimeDeliveryTrackingSystem/tree/mercadoPagoExplanation)

## Kafka
 * Here is the Kafka documentation: [Kafka Documentation](https://kafka.apache.org/intro)

## Necessary dependencies 
  * The necessary depedencies to the implementation work.



```

        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
        </dependency>


```
## NGROk

 * For testing purposes, I use Ngrok to start a server. However, I recommend using it only for testing.
Here is the Ngrok documentation to implement it: [NgRok Documentation](https://dashboard.ngrok.com/get-started/setup/linux)

## Docker 

* You can pull the Docker image using this command:
 ```dotdocker
docker pull guilhermebauer/real-timedeliverytrackingsystem
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
MERCADO_PAGO_ACCESS_TOKEN= your access token
MERCADO_PAGO_PUBLIC_KEY= your public key
MERCADO_PAGO_WEBHOOK_SECRET= your webhook secret
TEST_MAIL= email for test propouse
NROK_URL= your nrok url
KAFKA_BOOTSTRAP_SERVERS= your server # If you use the docker to test the application use the server kafka:9092
```

## Swagger

* Do not forget to change the port in the URL by the port you chose.

* [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

## Author
 www.linkedin.com/in/guilherme-bauer-desenvolvedor
