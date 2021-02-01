import config.PayoutsConfiguration;
import config.PayoutsEndpoints;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import requestBodys.PayoutBodyRequests;
import stubs.Stubbling;

import static io.restassured.RestAssured.given;

public class PayoutsRequestTests extends PayoutsConfiguration {

    @Test
    public void checkPayoutIsAccepted() {
        new Stubbling(wireMockServer).payout_requests_stub();

        Response response = given().
        body(PayoutBodyRequests.request_200_accepted).
        when().
        post(PayoutsEndpoints.PAYOUTS).
        then().
        statusCode(200).
        extract().response();

        String status = response.jsonPath().get("status");
        String payoutId = response.jsonPath().get("payoutId");

        Assert.assertEquals("ACCEPTED", status);
        Assert.assertNotSame("", payoutId);
    }

    @Test
    public void checkPayoutIsDuplicated() {
        new Stubbling(wireMockServer).payout_requests_stub();

        given().
        body(PayoutBodyRequests.request_200_accepted).
        when().
        post(PayoutsEndpoints.PAYOUTS).
        then().
        statusCode(200);

        Response response = given().
        body(PayoutBodyRequests.request_200_accepted).
        when().
        post(PayoutsEndpoints.PAYOUTS).
        then().
        statusCode(200).
        extract().response();

        String status = response.jsonPath().get("status");
        String payoutId = response.jsonPath().get("payoutId");

        Assert.assertEquals("DUPLICATE_IGNORED", status);
        Assert.assertNotSame("", payoutId);
    }

    @Test
    public void checkRejectedPayout() {
        new Stubbling(wireMockServer).payout_wrong_fields_stub();

        Response response = given().
        body(PayoutBodyRequests.request_200_rejected).
        when().
        post(PayoutsEndpoints.PAYOUTS).
        then().
        statusCode(200).
        extract().response();

        String status = response.jsonPath().get("status");
        String payoutId = response.jsonPath().get("payoutId");
        String rejectionCode = response.jsonPath().get("rejectionReason.rejectionCode");
        String rejectionMessage = response.jsonPath().get("rejectionReason.rejectionMessage");

        Assert.assertEquals("REJECTED", status);
        Assert.assertNotSame("", payoutId);
        Assert.assertEquals("AMOUNT_TOO_LARGE", rejectionCode);
        Assert.assertEquals("Amount should not be greater than 1000", rejectionMessage);
    }

    @Test
    public void checkMissingAmountInPayout() {
        new Stubbling(wireMockServer).payout_wrong_fields_stub();

        Response response = given().
        when().
        body(PayoutBodyRequests.request_200_missing_amount).
        post(PayoutsEndpoints.PAYOUTS).
        then().
        statusCode(400).
        extract().response();

        String payoutId = response.jsonPath().get("payoutId");
        Integer errorCode = response.jsonPath().get("errorCode");
        String errorMessage = response.jsonPath().get("errorMessage");

        Assert.assertNotSame("", payoutId);
        Assert.assertEquals("1", String.valueOf(errorCode));
        Assert.assertEquals("Invalid input: Missing required creator property 'amount'", errorMessage);

    }

    @Test
    public void checkAuthorizationError() {
        new Stubbling(wireMockServer).payout_wrong_authorization();

        Response response = given().
        auth().
        preemptive().
        basic("wrong_user", "wrong_password").
        when().
        get(PayoutsEndpoints.PAYOUTS).
        then().
        statusCode(403).
        extract().response();

        String errorId = response.jsonPath().get("errorId");
        Integer errorCode = response.jsonPath().get("errorCode");
        String errorMessage = response.jsonPath().get("errorMessage");

        Assert.assertNotSame("", errorId);
        Assert.assertEquals("3", String.valueOf(errorCode));
        Assert.assertEquals("Authorization error", errorMessage);

    }

    @Test
    public void checkSystemError() {
        new Stubbling(wireMockServer).payout_wrong_fields_stub();

        Response response = given().
        body(PayoutBodyRequests.request_500_missing_recipient).
        when().
        post(PayoutsEndpoints.PAYOUTS).
        then().
        statusCode(500).
        extract().response();

        String errorId = response.jsonPath().get("errorId");
        Integer errorCode = response.jsonPath().get("errorCode");
        String errorMessage = response.jsonPath().get("errorMessage");

        Assert.assertNotSame("", errorId);
        Assert.assertEquals("0", String.valueOf(errorCode));
        Assert.assertEquals("Internal error", errorMessage);
    }
}
