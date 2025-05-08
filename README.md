# Mercado Pago Explanation

## Objective
The project objective is to implement a payment API.I chose Mercado Pago Checkout Pro for payment
implementation because of their facility, the number of payment methods supported, and well-detailed documentation.

## Working Example

![Screenshot from 2025-05-07 15-07-13](https://github.com/user-attachments/assets/6f9f6a4e-7b56-4d66-ac78-e37997c09c17)

![Screenshot from 2025-05-07 15-08-40](https://github.com/user-attachments/assets/d9fe4f85-2840-420f-ba14-1971451b5824)


## Learning

In this implementation, I learned to implement a Mercado Pago payment API. I learned how to handle payments and implement a webhook.
Payment methods, like credit cards, can customize the maximum number of installments allowed.
As is possible, deny the payment method will not be used. The webhook implementation has the goal of verifying the payment 
status is approved. When the payment is approved, the product quantity is reduced in the stock, and the shopping cart is deleted.
It also sends a mail to the user with the items brought and the total price paid. 

## Configurations 
* Bellow is the necessary configuratitions to the implementation work.

###  Necessary dependencies 
  * The necessary depedencies to the implementation work.
  * It is necessary to configure the mail sender. Here is an explanation of how to do this: [Mail Sender Explanation](https://github.com/GuilhermeBauer16/Real-TimeDeliveryTrackingSystem/tree/mailSenderExplanation)


```

        <dependency>
            <groupId>com.mercadopago</groupId>
            <artifactId>sdk-java</artifactId>
            <version>2.1.29</version>
        </dependency>

        <dependency>
            <groupId>com.mercadopago</groupId>
            <artifactId>dx-java</artifactId>
            <version>1.8.0</version> </dependency>


```

## Docker 

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
MERCADO_PAGO_ACCESS_TOKEN= your access token
MERCADO_PAGO_PUBLIC_KEY= your public key
MERCADO_PAGO_WEBHOOK_SECRET= your webhook secret
TEST_MAIL= email for test propouse
NROK_URL= your nrok url
```

## Swagger

* Do not forget to change the port in the URL by the port you chose.

* [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

## Author
 www.linkedin.com/in/guilherme-bauer-desenvolvedor

