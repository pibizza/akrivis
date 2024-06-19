# Akrivis Scorecards

To run the example:
* Compile the yard repository with **mvn clean install**
    * https://github.com/kiegroup/yard
* Compile this project **mvn clean install**
* Start the *akrivis-quarkus-runtime*
  * **mvn compile quarkus:dev**
* Start the *rest-service-mock*
  * **mvn compile quarkus:dev -Dquarkus.port.http=8081**
  * Use a different port or the quarkus runtimes will collide
* Call http://localhost:8080/scorecards/run to see the resulting scores

### Configuration
* The *akrivis-quarkus-runtime* looks for the scorecard files from a folder
defined with the property **akrivis.scorecards.folder**. By default the dev 
environment uses files located in *examples/example-files/jira*

### Usage
Akrivis REST API provides two end points.
* **/scorecards/list*
  * Lists the scorecards
* **/scorecards/run*
  * Runs the scorecards



