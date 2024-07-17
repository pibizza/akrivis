# Akrivis Scorecards

### Run the example: Compile

* Compile the yard repository with **mvn clean install**
  * https://github.com/kiegroup/yard
* Compile this project **mvn clean install**

### Setting up the database
You need to have Docker or Podman running for the database.
* Either start it with a simple command line command:
  * **docker run -it --rm=true --name quarkus_test -e POSTGRES_USER=sarah -e POSTGRES_PASSWORD=connor -e POSTGRES_DB=quarkus_test -p 5432:5432 postgres:13.3**
* Or use with docker compose
  * Inside folder **database**
  * Run **docker compose up**
  * Starting with compose also adds adminer. You can access it from localhost:8085

### Alternative 1: Run what you need
* Start the *akrivis-processor*
  * **mvn compile quarkus:dev**
* Use a different port or the quarkus runtimes will collide. The following port selections will make the default example settings work.
* Start the *rest-service-mock*
  * **mvn compile quarkus:dev -Dquarkus.port.http=8081**
* Start the *akrivis-evaluator*
  * **mvn compile quarkus:dev -Dquarkus.port.http=8082**
* Start the *akrivis-ingestor*
  * **mvn compile quarkus:dev -Dquarkus.port.http=8083**

### Alternative 2: Start all with tmux
* Install **tmux** if you do not already have it
* Run **./start-in-tmux.sh**

# Usage
## Akrivis Evaluator
#### Configuration
* The *akrivis-processor* looks for the scorecard files from a folder
  defined with the property **akrivis.scorecards.folder**. By default the dev
  environment uses files located in *examples/example-files/jira*
* 
REST API provides two end points.
* **/scorecards/list*
  * Lists the scorecards
* **/scorecards/run*
  * Runs the scorecards



