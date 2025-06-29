package guru.qa.niffler.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.eclipse.jetty.http.HttpStatus;

import java.util.Objects;

/**
 * @author Alexander
 */
public class GhApiClient extends ApiClient {

    private final GhApi ghApi = retrofit.create(GhApi.class);

    public GhApiClient() {
        super(CFG.ghUrl());
    }

    public String issueState(String issueNumber) {
        var token = "Bearer " + System.getenv("GH_TOKEN_ENV");
        JsonNode responseBody =  executeApiCall(() -> ghApi.issue(token, issueNumber), HttpStatus.OK_200);
        return Objects.requireNonNull(responseBody).get("state").asText();
    }

}
