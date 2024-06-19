package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/jira")
public class JiraResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/example1")
    public String jira() {
        return """
                {
                  "expand": "names,schema",
                  "startAt": 0,
                  "maxResults": 50,
                  "total": 0,
                  "issues": [
                    {
                      "expand": "",
                      "id": "10001",
                      "self": "http://www.example.com/jira/rest/api/2/issue/10001",
                      "key": "HSP-1"
                    }
                  ]
                }""";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/example2")
    public String jira2() {
        return """
                {
                  "expand": "names,schema",
                  "startAt": 0,
                  "maxResults": 50,
                  "total": 0,
                  "issues": [
                    {
                      "expand": "",
                      "id": "10001",
                      "self": "http://www.example.com/jira/rest/api/2/issue/10001",
                      "key": "HSP-1"
                    }
                  ]
                }""";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/example3")
    public String jira3() {
        return """
                {
                  "expand": "names,schema",
                  "startAt": 0,
                  "maxResults": 50,
                  "total": 0,
                  "issues": [
                    {
                      "expand": "",
                      "id": "10001",
                      "self": "http://www.example.com/jira/rest/api/2/issue/10001",
                      "key": "HSP-1"
                    }
                  ]
                }""";
    }
}
