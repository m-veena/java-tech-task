# Lunch Microservice

The service provides an endpoint that will determine, from a set of recipes, what I can have for lunch at a given date, based on my fridge ingredient's expiry date, so that I can quickly decide what I’ll be having to eat, and the ingredients required to prepare the meal.

## Prerequisites

* [Java 11 Runtime](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Docker](https://docs.docker.com/get-docker/) & [Docker-Compose](https://docs.docker.com/compose/install/)

*Note: Docker is used for the local MySQL database instance, feel free to use your own instance or any other SQL database and insert data from lunch-data.sql script* 


### Run

1. Start database:

    ```
    docker-compose up -d
    ```
   
2. Add test data from  `sql/lunch-data.sql` to the database. Here's a helper script if you prefer:


    ```
    CONTAINER_ID=$(docker inspect --format="{{.Id}}" lunch-db)
    ```
    
    ```
    docker cp sql/lunch-data.sql $CONTAINER_ID:/lunch-data.sql
    ```
    
    ```
    docker exec $CONTAINER_ID /bin/sh -c 'mysql -u root -prezdytechtask lunch </lunch-data.sql'
    ```
    
3. Run Springboot LunchApplication
Endpoints
1.  http://localhost:8080/lunch/{date} - To return recipes based on availability - (I assumed it means recipes based on best before date)
2.  http://localhost:8080/lunch/useBy/{date} : To return recipes available based on ingredients having useby date greater than passed
3. http://localhost:8080/lunch/afterBestBefore/{date} : To return recipes available based on ingredients best before date expired and under used by. NOTE: Ordering is not implemented for it.
4. http://localhost:8080/lunch//filterOnIngredients/{List , separated} : To return recipes available based on exluding ingredients passed
5. http://localhost:8080/lunch/recipe/{title} : To return recipes based the title. will return error to client if value not found
 

NOTE: 
- I have used local postgres without Docker
- Test cases are not written for all cases because of time constraints

