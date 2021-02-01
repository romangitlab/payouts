package config;

import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.Matchers.lessThan;

public class PayoutsConfiguration {

    public WireMockServer wireMockServer;
    public static RequestSpecification payouts_requestSpec;
    public static ResponseSpecification payouts_responseSpec;

    @BeforeEach
    public void setup() {

        wireMockServer = new WireMockServer(8090);
        wireMockServer.start();

        payouts_requestSpec = new RequestSpecBuilder()
                .setBaseUri("http://localhost")
                .setBasePath("/")
                .setPort(8090)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        payouts_responseSpec = new ResponseSpecBuilder()
                .expectResponseTime(lessThan(3000L))
                .build();

        RestAssured.requestSpecification = payouts_requestSpec;
        RestAssured.responseSpecification = payouts_responseSpec;

    }

    @AfterEach
    public void teardown () {
        wireMockServer.stop();
    }
}
