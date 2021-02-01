import com.github.tomakehurst.wiremock.WireMockServer;
import config.PayoutsConfiguration;
import config.PayoutsEndpoints;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import requestBodys.PayoutBodyRequests;
import stubs.Stubbling;

import static io.restassured.RestAssured.given;

public class PayoutsStatusTests extends PayoutsConfiguration {

    @Test
    public void testPayoutsStatusCompleted() {
        new Stubbling(wireMockServer).payout_requests_stub();

        String payoutId = given().
        body(PayoutBodyRequests.request_200_accepted).
        when().
        post("payouts").
        then().
        statusCode(200).
        extract().response().jsonPath().getString("payoutId");

        new Stubbling(wireMockServer).payout_get_status();

        Response status_submitted = given().pathParam("payoutId", payoutId).
        when().
        get(PayoutsEndpoints.PAYOUTS_STATUS).
        then().
        statusCode(200).
        extract().response();

        String state = status_submitted.getHeader("state");
        String status = status_submitted.jsonPath().get("status");
        Assert.assertEquals("SUBMITTED", state);
        Assert.assertEquals("SUBMITTED", status);

        Response status_pending = given().pathParam("payoutId", payoutId).
                when().
                get(PayoutsEndpoints.PAYOUTS_STATUS).
                then().
                statusCode(200).
                extract().response();

        state = status_pending.getHeader("state");
        status = status_pending.jsonPath().get("status");
        Assert.assertEquals("PENDING", state);
        Assert.assertEquals("PENDING", status);

        Response status_accepted = given().pathParam("payoutId", payoutId).
                when().
                get(PayoutsEndpoints.PAYOUTS_STATUS).
                then().
                statusCode(200).
                extract().response();

        state = status_accepted.getHeader("state");
        status = status_accepted.jsonPath().get("status");
        Assert.assertEquals("ACCEPTED", state);
        Assert.assertEquals("ACCEPTED", status);

        Response status_completed = given().pathParam("payoutId", payoutId).
                when().
                get(PayoutsEndpoints.PAYOUTS_STATUS).
                then().
                statusCode(200).
                extract().response();

        state = status_completed.getHeader("state");
        status = status_completed.jsonPath().get("status");
        Assert.assertEquals("COMPLETED", state);
        Assert.assertEquals("COMPLETED", status);
    }

    @Test
    public void testPayoutsStatusRecipientNotFound() {
        new Stubbling(wireMockServer).payout_wrong_fields_stub();

        String errorId = given().
                body(PayoutBodyRequests.request_500_missing_recipient).
                when().
                post("payouts").
                then().
                statusCode(500).
                extract().response().jsonPath().getString("errorId");

        new Stubbling(wireMockServer).payout_get_status();

        Response response = given().pathParam("payoutId", errorId).
                when().
                get(PayoutsEndpoints.PAYOUTS_STATUS).
                then().
                statusCode(200).
                extract().response();

        String status = response.jsonPath().get("status");
        String failureCode = response.jsonPath().get("failureReason.failureCode");
        String failureMessage = response.jsonPath().get("failureReason.failureMessage");

        Assert.assertEquals("FAILED", status);
        Assert.assertEquals("RECIPIENT_NOT_FOUND", failureCode);
        Assert.assertEquals("Recipient not found", failureMessage);

    }

    @Test
    public void testPayoutsStatusNotFound() {
        new Stubbling(wireMockServer).payout_wrong_fields_stub();

        String payoutId = given().
                body(PayoutBodyRequests.request_500_missing_recipient).
                when().
                post("payouts").
                then().
                statusCode(500).
                extract().response().jsonPath().getString("payoutId");

        new Stubbling(wireMockServer).payout_get_status();

        Response response = given().pathParam("payoutId", payoutId).
                when().
                get(PayoutsEndpoints.PAYOUTS_STATUS).
                then().
                statusCode(200).
                extract().response();

        String status = response.jsonPath().get().toString();
        Assert.assertEquals("[]", status);
    }
}
