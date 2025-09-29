package kr.ac.kopo.ctc.kopo21.board.repository;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SampleRestAssuredControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        RestAssured.baseURI = "http://localhost";
}

    @Test
    void testGetUserById() {
        given()
                .pathParam("id",1)
                .when()
                .get("/sample/selectOne?id={id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("title",equalTo("t1"));
    }
}
