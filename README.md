# Mail Sender Explanation


## Objective  
The application allows user to create their study material with a title and description. The user can personalize 
the study material by adding some links to reference the topic he is studying. 
The user also can relate that study material with a specific course, being easy to find in the future.
But before accessing the app, sign up or log in. 

## Working Example  

![Screenshot from 2024-10-30 18-41-22](https://github.com/user-attachments/assets/71d80af9-ef8a-4797-a2d0-598e86a54088)

## Sending 
![Screenshot from 2024-10-30 18-44-08](https://github.com/user-attachments/assets/f93c62a6-96f2-4aa1-a526-ee5b0e60a588)

## Response
![Screenshot from 2024-10-30 18-46-34](https://github.com/user-attachments/assets/8192ba06-2fc2-4f33-9439-71a5dfe245e5)



## Learnings 
In that project, I put into practice the topics I recently learned. The first was to create a login system using Spring Security and JWT(JSON Web Token ) 
tokens to structure a login system with more security with the implementation of CRSF(Cross-site request forgery) and CORS(Cross-origin resource sharing). 
I implemented pipelines using GitHub actions in the project to deploy in Docker Hub when the main branch receives a pull request. Another pipeline 
I implemented only permitted pull requests to the main branch when sent by the develop branch. Implement the Sonar Cloud to control the code quality.

## Functionalities

  

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
```

## Swagger

* [Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

## Author
 www.linkedin.com/in/guilherme-bauer-desenvolvedor

