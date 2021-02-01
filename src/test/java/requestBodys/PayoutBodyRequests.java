package requestBodys;

public class PayoutBodyRequests {
    public static String request_200_accepted =
            "{\n" +
            "  \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
            "  \"amount\": \"15.21\",\n" +
            "  \"currency\": \"ZMW\",\n" +
            "  \"country\": \"ZMB\",\n" +
            "  \"correspondent\": \"MTN_MOMO_ZMB\",\n" +
            "  \"recipient\": {\n" +
            "    \"type\": \"MSISDN\",\n" +
            "    \"address\": {\n" +
            "      \"value\": \"256780334452\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"customerTimestamp\": \"2020-02-21T17:32:28Z\",\n" +
            "  \"statementDescription\": \"Up to 22 chars note\"\n" +
            "}";

    public static String request_200_rejected =
            "{\n" +
            "  \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
            "  \"amount\": \"2000\",\n" +
            "  \"currency\": \"ZMW\",\n" +
            "  \"country\": \"ZMB\",\n" +
            "  \"correspondent\": \"MTN_MOMO_ZMB\",\n" +
            "  \"recipient\": {\n" +
            "    \"type\": \"MSISDN\",\n" +
            "    \"address\": {\n" +
            "      \"value\": \"256780334452\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"customerTimestamp\": \"2020-02-21T17:32:28Z\",\n" +
            "  \"statementDescription\": \"Up to 22 chars note\"\n" +
            "}";

    public static String request_200_missing_amount =
            "{\n" +
            "  \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
            "  \"currency\": \"ZMW\",\n" +
            "  \"country\": \"ZMB\",\n" +
            "  \"correspondent\": \"MTN_MOMO_ZMB\",\n" +
            "  \"recipient\": {\n" +
            "    \"type\": \"MSISDN\",\n" +
            "    \"address\": {\n" +
            "      \"value\": \"256780334452\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"customerTimestamp\": \"2020-02-21T17:32:28Z\",\n" +
            "  \"statementDescription\": \"Up to 22 chars note\"\n" +
            "}";

    public static String request_500_missing_recipient =
            "{\n" +
            "  \"payoutId\": \"f4401bd2-1568-4140-bf2d-eb77d2b2b639\",\n" +
            "  \"currency\": \"ZMW\",\n" +
            "  \"country\": \"ZMB\",\n" +
            "  \"correspondent\": \"MTN_MOMO_ZMB\"\n" +
            "}";
}