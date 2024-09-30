package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Random;

@Path("/github")
public class GitHubResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/example1")
    public String jira() {
        return """
                  {
                    "url": "https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues/1",
                    "repository_url": "https://api.github.com/repos/lucamolteni/test-scorecard-repository",
                    "labels_url": "https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues/1/labels{/name}",
                    "comments_url": "https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues/1/comments",
                    "events_url": "https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues/1/events",
                    "html_url": "https://github.com/lucamolteni/test-scorecard-repository/issues/1",
                    "id": 2385458067,
                    "node_id": "I_kwDOMQ2NXs6OLzOT",
                    "number": 1,
                    "title": "First issue",
                    "user": {
                      "login": "lucamolteni",
                      "id": 454752,
                      "node_id": "MDQ6VXNlcjQ1NDc1Mg==",
                      "avatar_url": "https://avatars.githubusercontent.com/u/454752?v=4",
                      "gravatar_id": "",
                      "url": "https://api.github.com/users/lucamolteni",
                      "html_url": "https://github.com/lucamolteni",
                      "followers_url": "https://api.github.com/users/lucamolteni/followers",
                      "following_url": "https://api.github.com/users/lucamolteni/following{/other_user}",
                      "gists_url": "https://api.github.com/users/lucamolteni/gists{/gist_id}",
                      "starred_url": "https://api.github.com/users/lucamolteni/starred{/owner}{/repo}",
                      "subscriptions_url": "https://api.github.com/users/lucamolteni/subscriptions",
                      "organizations_url": "https://api.github.com/users/lucamolteni/orgs",
                      "repos_url": "https://api.github.com/users/lucamolteni/repos",
                      "events_url": "https://api.github.com/users/lucamolteni/events{/privacy}",
                      "received_events_url": "https://api.github.com/users/lucamolteni/received_events",
                      "type": "User",
                      "site_admin": false
                    },
                    "labels": [
                               
                    ],
                    "state": "open",
                    "locked": false,
                    "assignee": null,
                    "assignees": [
                               
                    ],
                    "milestone": null,
                    "comments": 0,
                    "created_at": "2024-07-02T07:26:38Z",
                    "updated_at": "2024-07-02T07:26:39Z",
                    "closed_at": null,
                    "author_association": "OWNER",
                    "active_lock_reason": null,
                    "body": null,
                    "closed_by": null,
                    "reactions": {
                      "url": "https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues/1/reactions",
                      "total_count": 0,
                      "+1": 0,
                      "-1": 0,
                      "laugh": 0,
                      "hooray": 0,
                      "confused": 0,
                      "heart": 0,
                      "rocket": 0,
                      "eyes": 0
                    },
                    "timeline_url": "https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues/1/timeline",
                    "performed_via_github_app": null,
                    "state_reason": null
                  }
               """;
    }

}
