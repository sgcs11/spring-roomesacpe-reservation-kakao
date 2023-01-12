package nextstep.reservations;

import io.restassured.RestAssured;
import nextstep.reservations.domain.repository.reservation.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"classpath:schema/reservation_schema.sql", "classpath:data/reservation_init.sql"})
public class ReservationTest {
    @LocalServerPort
    int port;

    @Autowired
    @Qualifier("webReservationRepository")
    ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 예약_생성() {
        Map<String, Object> reservationRequest = new HashMap<>();
        reservationRequest.put("date", "2022-08-01");
        reservationRequest.put("time", "14:00");
        reservationRequest.put("name", "name");
        reservationRequest.put("themeName", "워너고홈");
        reservationRequest.put("themeDesc", "병맛 어드벤처 회사 코믹물");
        reservationRequest.put("themePrice", 29000);


        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/reservations/3");
    }

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("id", notNullValue())
                .body("date", notNullValue())
                .body("time", notNullValue())
                .body("name", notNullValue())
                .body("themeName", notNullValue())
                .body("themeDesc", notNullValue())
                .body("themePrice", notNullValue());
    }

    @Test
    void 예약_삭제() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 중복_예약_오류() {
        Map<String, Object> reservationRequest = new HashMap<>();
        reservationRequest.put("date", "1982-02-19");
        reservationRequest.put("time", "02:02");
        reservationRequest.put("name", "name");
        reservationRequest.put("themeName", "워너고홈");
        reservationRequest.put("themeDesc", "병맛 어드벤처 회사 코믹물");
        reservationRequest.put("themePrice", 29000);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(is("해당 시간에 해당 테마의 중복된 예약이 있습니다."));
    }
}