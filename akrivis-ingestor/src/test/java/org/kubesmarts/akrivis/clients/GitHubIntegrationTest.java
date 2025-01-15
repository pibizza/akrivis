package org.kubesmarts.akrivis.clients;

public class GitHubIntegrationTest {

    public static void main(String[] args) {
        GitHubClientImpl gitHubClient = new GitHubClientImpl();

        String response = gitHubClient.fetchData("https://api.github.com/repos/lucamolteni/test-scorecard-repository/issues");

        System.out.println("Response:");
        System.out.println(response);

    }
}
