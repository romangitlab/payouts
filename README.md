### Tools:
`WireMock, RestAssured, Java, Maven`

Workflow:

Create wiremock stub to check following payout requests:
- Check payout is accepted
- Check payout is duplicated
- Check rejected payout
- Check missing amount in payout
- Check authorization error
- Check system error

Create wiremock stub to check following payouts statuses:
- Check payouts status completed
- Check payouts status recipient not found
- Check payouts status not found
