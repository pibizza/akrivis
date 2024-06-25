package org.kie.akrivis.github;

import com.jcabi.github.Issue;
import com.jcabi.github.Issues;
import com.jcabi.github.Repo;
import com.jcabi.github.Repos.RepoCreate;
import com.jcabi.github.mock.MkGithub;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.sameJSONAs;

public class GithubTest1Test {


    @Test
    public void submitsCommentToGithubIssue() throws Exception {

        final Repo repo = new MkGithub().repos().create(
                new RepoCreate("firstrepo", false)
        );
        final Issue issue = repo.issues().create("how are you?", "");


        assertThat(issue.json().toString(),
                   sameJSONAs("{\"title\":\"how are you?\"}")
                           .allowingExtraUnexpectedFields()
                           .allowingAnyArrayOrdering());

        List<Issues> issues = Arrays.asList(repo.issues());
        assertThat(issues, hasSize(1));

    }
}