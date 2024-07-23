package org.kie.akrivis.utility;

/*
 * In backstage API are expected to be wrapped in a similar shape JSON response.
 * So the actual JSON returned will be always inside the results field.
 * This class is currently duplicated in the akrivis-quarkus-runtime and akrivis-ingestor modules.¬
 */
public record BackstageResponse<T>(T results) {

}
