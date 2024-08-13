package org.kie.akrivis.scheduler;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.kie.akrivis.scheduler.JobResource.JobRequest;
import org.kie.akrivis.scheduler.JobResource.JobResponse;
import uk.co.datumedge.hamcrest.json.SameJSONAs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@QuarkusTest
class JobResourceTest {

    @Test
    void testCreateJobDelete() {
        JobRequest jobRequest =
                new JobRequest("https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues", "GET",
                               "0 0 12 * * ?");

        JobResponse jobResponse = given()
                .contentType(ContentType.JSON)
                .body(jobRequest)
                .when().post("/job")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().body().as(JobResponse.class);

        Long id = jobResponse.id();

        JobResponse found = given()
                .when().get("/job/" + id)
                .then()
                .statusCode(200)
                .extract()
                .body().as(JobResponse.class);

        assertThat(found.id(), is(id));

        given()
                .when().delete("/job/" + id)
                .then()
                .statusCode(204);

        given()
                .when().get("/job/" + id)
                .then()
                .statusCode(404);

    }

    @Test
    void testMockGithubClient() {
        given()
                .when().post("/job/1/test")
                .then()
                .statusCode(200)
                .body(SameJSONAs.sameJSONAs("""
                                                    {"results":{
                                                                                      "data": [
                                                                                        {
                                                                                          "title": "Found a bug"
                                                                                        }
                                                                                      ]
                                                                                    }
                                                                                    }
                                                    """).allowingExtraUnexpectedFields());
    }

}
