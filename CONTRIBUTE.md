
# Run the demo environment 

# Step 1: Setup and Compile

## Setup requires
* Java 21
* Maven 3.9.6
* Docker or Podman 

## Compile
* Compile the yard repository with **mvn clean install**
    * https://github.com/cubesmarts/yard
* Compile this project **mvn clean install**
* In order to run the Backstage plugin the repository with the Akrivis plugin is required
  * Current Backstage plugins repository 
    * https://github.com/kiegroup/backstage-plugins
    * Branch name: **akrivis**
  * Run **yarn install** in Backstage plugins repository

# Step 2:

## Option 1: Start all with tmux
* Install **tmux** if you do not already have it
* Run **./start.sh**
* [Check Backstage Connection Workaround](#backstage-connection-workaround)
* The Backstage instance can now be opened from **http://localhost:3000/**

## Option 2: Run what you need
### Setting up the database
You need to have **Docker** or **Podman** running for the database.
* Either start it with a simple command line command:
  * **docker run -it --rm=true --name quarkus_test -e POSTGRES_USER=sarah -e POSTGRES_PASSWORD=connor -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:13.3**
* Or use with docker compose
  * Inside folder **/database**
  * Run **docker compose up**
  * Starting with compose also adds adminer. You can access it from **localhost:8085**

### Start the Quarkus applications
* Start the **akrivis-processor**
    * **mvn compile quarkus:dev**
* Use a different port or the quarkus runtimes will collide. The following port selections will make the default example settings work.
* Start the **rest-service-mock**
    * **mvn compile quarkus:dev -Dquarkus.port.http=8081**
* Start the **akrivis-evaluator**
    * **mvn compile quarkus:dev -Dquarkus.port.http=8082**
* Start the **akrivis-ingestor**
    * **mvn compile quarkus:dev -Dquarkus.port.http=8083**

### Start Backstage
* Go into the Backstage plugins repository directory.
* Start the Backstage instance with **yarn start:backstage**
* The Backstage instance can now be opened from **http://localhost:3000/**
* [Check Backstage Connection Workaround](#backstage-connection-workaround)

## Backstage Connection Workaround
 If Backstage instance fails to connect to Akrivis backend. Find the existing **backend** line 
 from **app-config.yaml** and add the two lines **auth** and **dangerouslyDisableDefaultAuthPolicy**. 
   ```
   backend:
     auth:
       dangerouslyDisableDefaultAuthPolicy: true
   ```

