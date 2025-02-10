# Publizr Backend

Basic blogging REST API powered by Spring Boot

### Steps to run db script

```bash
  # create container
  docker container run --name <container-name> -e POSTGRES_PASSWORD=password -d -p 5431:5431 postgres:latest
  
  # list containers
  docker container 
  docker ps -a
  
  # copy SQL script into container
  docker cp src/main/resources/schema.sql <container-name>:/
  
  # docker exec into container to execute copied script
  docker container exec -it <container-name> bash
  
  # use pqsl cmd to execute SQL script
  psql -U postgres --file schema.sql
  
  #  exit and exec into the container with psql
  exit

  # use below command for subsequent logins
  psql -U postgres
  # view running containers
  docker ps -a
  
 # exec into the running container with psql
 docker exec -it <container-name> psql -U postgres
 
 # connect to db
 \connect publizr_db;
 
 # run SQL CMDS
 SELECT * FROM USERS;
```

```bash
# default server port
server.port=8080

# Swagger UI API Documentation
http://localhost:8080/swagger-ui/index.html

# OpenAPI Documentation
# http://localhost:8080/publizr/api-docs
```