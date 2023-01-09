package nextstep.reservation;

import io.restassured.RestAssured;
import nextstep.reservation.entity.Reservation;
import nextstep.reservation.entity.Theme;
import nextstep.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import static org.hamcrest.core.IsNull.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationTest {
    @LocalServerPort
    int port;

    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        reservationRepository.add(
                1L,
                new Reservation(
                        1L,
                        LocalDate.of(1982, 2, 19),
                        LocalTime.of(2, 2),
                        "name",
                        new Theme("워너고홈 ", "병맛 어드벤처 회사 코믹물", 29000)
                )
        );
    }

    @Test
    void 예약_생성() {
        Map<String, String> reservationRequest = new HashMap<>();
        reservationRequest.put("date", "2022-08-01");
        reservationRequest.put("time", "13:00");
        reservationRequest.put("name", "name");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/reservations/1");
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

}
