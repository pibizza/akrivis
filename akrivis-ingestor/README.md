# akrivis-ingestor

## Test

```shell

mvn compile quarkus:dev -Dquarkus.http.port=8082

jo endpoint="https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues" type=GitHub cron="0 0 12 * * ?" | http POST localhost:8082/job

http POST localhost:8082/job/<ID_CREATED>/test

http GET localhost:8082/job

http GET localhost:8082/job/<ID_CREATED>/data

```