# Akrivis Scorecards and Reporting
This is Akrivis project that is used to produce reporting data for the Akrivis Backstage plugin.
Akrivis consists of the following components:

* Akrivis Backstage plugin 
  * Provides UI controls and visualization frontend
  * Not stored in this repository
* [Ingestor](akrivis-ingestor/README.md)
  * Responsible for fetching data from given data sources
  * Provides a REST API for fetching reporting data and creating Akrivis assets
* [Processor](akrivis-processor/README.md)
  * Responsible for activating the Cards reactively based on Data changes
* [Evaluator](akrivis-evaluator/README.md)
  * Used by Processor to run the Yard files with a given data.
* [Postgres Database](database/README.md)
  * Database when resulting reporting data, jobs and cards are stored.
* [Quarkus REST service for testing](examples/rest-service-mock/README.md)
  * Something the help the development environment to run. Few mocked services.

## Components:
### Card
Card is used to form a result based on given data. The data comes from a Data source.
Card contains the calculation needed to process the given data to a desired format for the visual presentation.
#### Card configuration 
Specifies how the card is visualised. For example what are the max-min values, 
are there thresholds and what the x and y-axis are called.

### Data source
Data source defines the REST e

### Job

## Contributing
* [How to test and contribute](CONTRIBUTE.md)

