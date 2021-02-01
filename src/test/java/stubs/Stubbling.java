package stubs;

import com.github.tomakehurst.wiremock.WireMockServer;
import requestBodys.PayoutBodyRequests;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;

public class Stubbling {

    WireMockServer wireMockServer;

    public Stubbling(WireMockServer wireMockServer){
        this.wireMockServer = wireMockServer;
    }

    public void payout_requests_stub() {

        wireMockServer.
                givenThat(post(urlMatching("/payouts")).
                withRequestBody(equalToJson(PayoutBodyRequests.request_200_accepted)).
                inScenario("Payout request").
                willReturn(aResponse().
                withHeader("Content-Type", "application/json").
                withStatus(200)
                .withBody(
                        "{\n" +
                        "  \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
                        "  \"status\": \"ACCEPTED\",\n" +
                        "  \"created\": \"2020-10-19T11:17:01Z\"\n" +
                        "}"
                )).willSetStateTo("Get duplicate attempt"));

        wireMockServer.
                givenThat(post(urlMatching("/payouts")).
                withRequestBody(equalToJson(PayoutBodyRequests.request_200_accepted)).
                inScenario("Payout request").
                whenScenarioStateIs("Get duplicate attempt").
                willReturn(aResponse().
                withHeader("Content-Type", "application/json").
                withStatus(200)
                .withBody(
                        "{\n" +
                        "  \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
                        "  \"status\": \"DUPLICATE_IGNORED\",\n" +
                        "  \"created\": \"2020-10-19T11:17:01Z\"\n" +
                        "}"
                )).willSetStateTo("Duplicate request complete"));
    }

    public void payout_wrong_fields_stub() {

        wireMockServer.
                givenThat(post(urlMatching("/payouts")).
                withRequestBody(equalToJson(PayoutBodyRequests.request_200_rejected)).
                willReturn(aResponse().
                withHeader("Content-Type", "application/json").
                withStatus(200).
                withBody(
                        "{\n" +
                        "  \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
                        "  \"status\": \"REJECTED\",\n" +
                        "  \"rejectionReason\": {\n" +
                        "    \"rejectionCode\": \"AMOUNT_TOO_LARGE\",\n" +
                        "    \"rejectionMessage\": \"Amount should not be greater than 1000\"\n" +
                        "  }\n" +
                        "}"
                )));

        wireMockServer.
                givenThat(post(urlMatching("/payouts")).
                withRequestBody(equalToJson(PayoutBodyRequests.request_200_missing_amount)).
                willReturn(aResponse().
                withHeader("Content-Type", "application/json").
                withStatus(400).
                withBody(
                        "{\n" +
                        "  \"errorId\": \"4f0d5e13-7e88-4cc6-827c-8c0640dc2cd3\",\n" +
                        "  \"errorCode\": 1,\n" +
                        "  \"errorMessage\": \"Invalid input: Missing required creator property 'amount'\"\n" +
                        "}"
                )));

        wireMockServer.
                givenThat(post(urlMatching("/payouts")).
                withRequestBody(equalToJson(PayoutBodyRequests.request_500_missing_recipient)).
                willReturn(aResponse().
                withHeader("Content-Type", "application/json").
                withStatus(500).
                withBody(
                        "{\n" +
                        "  \"errorId\": \"d428a89e-fa8b-42b8-ba20-68be20d50af1\",\n" +
                        "  \"payoutId\": \"\",\n" +
                        "  \"errorCode\": 0,\n" +
                        "  \"errorMessage\": \"Internal error\"\n" +
                        "}"
                )));
    }

    public void payout_wrong_authorization() {

        wireMockServer.
                givenThat(get(urlMatching("/payouts")).
                withBasicAuth("wrong_user", "wrong_password").
                willReturn(aResponse().
                withHeader("Content-Type", "application/json").
                withStatus(403).
                withBody(
                        "{\n" +
                        "  \"errorId\": \"daa495f0-541d-4192-b636-a8877b25a510\",\n" +
                        "  \"errorCode\": 3,\n" +
                        "  \"errorMessage\": \"Authorization error\"\n" +
                        "}"
                )));

    }

    public void payout_get_status() {

        wireMockServer.
                givenThat(
                get(urlMatching("/payouts/f4401bd2-1568-4140-bf2d-eb77d2b2b639")).
                inScenario("Get payout status").
                whenScenarioStateIs(STARTED).
                willReturn(aResponse().
                withStatus(200).
                withBody(
                        "  {\n" +
                        "    \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
                        "    \"status\": \"SUBMITTED\",\n" +
                        "    \"amount\": \"123.45\",\n" +
                        "    \"currency\": \"ZMW\",\n" +
                        "    \"country\": \"ZMB\",\n" +
                        "    \"correspondent\": \"MTN_MOMO_ZMB\",\n" +
                        "    \"recipient\": {\n" +
                        "      \"type\": \"MSISDN\",\n" +
                        "      \"address\": {\n" +
                        "        \"value\": \"256780334452\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"customerTimestamp\": \"2020-10-19T08:17:00Z\",\n" +
                        "    \"statementDescription\": \"From ACME company\",\n" +
                        "    \"created\": \"2020-10-19T08:17:01Z\"\n" +
                        "  }").
                withHeader("Content-Type", "application/json").
                withHeader("state", "SUBMITTED")).
                willSetStateTo("Second get status attempt"));

        wireMockServer.
                givenThat(
                get(urlMatching("/payouts/f4401bd2-1568-4140-bf2d-eb77d2b2b639")).
                inScenario("Get payout status").
                whenScenarioStateIs("Second get status attempt").
                willReturn(aResponse().
                withStatus(200).
                withBody(
                        "  {\n" +
                        "    \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
                        "    \"status\": \"PENDING\",\n" +
                        "    \"amount\": \"123.45\",\n" +
                        "    \"currency\": \"ZMW\",\n" +
                        "    \"country\": \"ZMB\",\n" +
                        "    \"correspondent\": \"MTN_MOMO_ZMB\",\n" +
                        "    \"recipient\": {\n" +
                        "      \"type\": \"MSISDN\",\n" +
                        "      \"address\": {\n" +
                        "        \"value\": \"256780334452\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"customerTimestamp\": \"2020-10-19T08:17:00Z\",\n" +
                        "    \"statementDescription\": \"From ACME company\",\n" +
                        "    \"created\": \"2020-10-19T08:17:01Z\"\n" +
                        "  }").
                withHeader("Content-Type", "application/json").
                withHeader("state", "PENDING")).
                willSetStateTo("Third get status attempt"));

        wireMockServer.
                givenThat(
                get(urlMatching("/payouts/f4401bd2-1568-4140-bf2d-eb77d2b2b639")).
                inScenario("Get payout status").
                whenScenarioStateIs("Third get status attempt").
                willReturn(aResponse().
                withStatus(200).
                withBody(
                        "  {\n" +
                        "    \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
                        "    \"status\": \"ACCEPTED\",\n" +
                        "    \"amount\": \"123.45\",\n" +
                        "    \"currency\": \"ZMW\",\n" +
                        "    \"country\": \"ZMB\",\n" +
                        "    \"correspondent\": \"MTN_MOMO_ZMB\",\n" +
                        "    \"recipient\": {\n" +
                        "      \"type\": \"MSISDN\",\n" +
                        "      \"address\": {\n" +
                        "        \"value\": \"256780334452\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"customerTimestamp\": \"2020-10-19T08:17:00Z\",\n" +
                        "    \"statementDescription\": \"From ACME company\",\n" +
                        "    \"created\": \"2020-10-19T08:17:01Z\"\n" +
                        "  }").
                withHeader("Content-Type", "application/json").
                withHeader("state", "ACCEPTED")).
                willSetStateTo("Fourth get status attempt"));

        wireMockServer.
                givenThat(
                get(urlMatching("/payouts/f4401bd2-1568-4140-bf2d-eb77d2b2b639")).
                inScenario("Get payout status").
                whenScenarioStateIs("Fourth get status attempt").
                willReturn(aResponse().
                withStatus(200).
                withBody(
                        "  {\n" +
                        "    \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
                        "    \"status\": \"COMPLETED\",\n" +
                        "    \"amount\": \"123.45\",\n" +
                        "    \"currency\": \"ZMW\",\n" +
                        "    \"country\": \"ZMB\",\n" +
                        "    \"correspondent\": \"MTN_MOMO_ZMB\",\n" +
                        "    \"recipient\": {\n" +
                        "      \"type\": \"MSISDN\",\n" +
                        "      \"address\": {\n" +
                        "        \"value\": \"256780334452\"\n" +
                        "      }\n" +
                        "    },\n" +
                        "    \"customerTimestamp\": \"2020-10-19T08:17:00Z\",\n" +
                        "    \"statementDescription\": \"From ACME company\",\n" +
                        "    \"created\": \"2020-10-19T08:17:01Z\",\n" +
                        "    \"receivedByRecipient\": \"2020-10-19T08:17:02Z\",\n" +
                        "    \"correspondentIds\": {\n" +
                        "      \"SOME_CORRESPONDENT_ID\": \"12356789\"\n" +
                        "    }\n" +
                        "  }"
                        ).
                withHeader("Content-Type", "application/json").
                withHeader("state", "COMPLETED")).
                willSetStateTo("Ended."));

        wireMockServer.
                givenThat(
                get(urlMatching("/payouts/")).
                willReturn(aResponse().
                withStatus(200).
                withBody("[]").
                withHeader("Content-Type", "application/json")));
    }
}
