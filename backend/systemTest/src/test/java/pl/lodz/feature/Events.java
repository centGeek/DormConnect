package pl.lodz.feature;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import pl.lodz.JwtService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class Events {
    private final JwtService jwtService;

    public Events() {
        this.jwtService = new JwtService();  // tworzysz JwtService ręcznie
    }

    private final String baseUrl = "http://localhost:8000";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;

    private String email;

    private Map<String, Object> registrationData = new HashMap<>();
    private Map<String, Object> userMap = new HashMap<>();

    private Map<String, String> loginData = new HashMap<>();

    // Next

    private String token;

    private static Long eventId;


    @Given("zalogowany użytkownik z mailem {string} i haśle {string}")
    public void zalogowanyUzytkownikZMailemIHasle(String arg0, String arg1) {
        loginData.clear();
        loginData.put("email", arg0);
        loginData.put("password", arg1);

        try {
            String json = objectMapper.writeValueAsString(loginData);
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/api/auth/login");

            token = response.jsonPath().getString("token");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Given("zalogowany admin użytkownik z mailem {string} i haśle {string}")
    public void zalogowanyAdminUzytkownikZMailemIHasle(String arg0, String arg1) {
        loginData.clear();
        loginData.put("email", arg0);
        loginData.put("password", arg1);

        try {
            String json = objectMapper.writeValueAsString(loginData);
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/api/auth/login");

            tokenAdmin = response.jsonPath().getString("token");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String tokenStudent;

    @Given("zalogowany student użytkownik z mailem {string} i haśle {string}")
    public void zalogowanyStudentUzytkownikZMailemIHasle(String arg0, String arg1) {
        loginData.clear();
        loginData.put("email", arg0);
        loginData.put("password", arg1);

        try {
            String json = objectMapper.writeValueAsString(loginData);
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/api/auth/login");

            tokenStudent = response.jsonPath().getString("token");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @When("dodaje event")
    public void dodajeEvent() {
        Map<String, Object> event = new HashMap<>();
        event.put("eventName", "Testowy Event");
        event.put("description", "Opis eventu");
        event.put("startDateTime", "2025-07-01T10:00:00");
        event.put("endDateTime", "2025-07-01T12:00:00");
        event.put("location", "Online");
        event.put("eventType", "Informatyka");
        event.put("maxParticipants", 100);
        event.put("imageUrl", "https://example.com/image.jpg");
        event.put("organizerId", jwtService.getIdFromToken(token));
        event.put("participantId", new ArrayList<Long>());

        try {
            String json = objectMapper.writeValueAsString(event);
            response = RestAssured.given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/api/event/create");

            eventId = response.jsonPath().getLong("eventId");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("ma swój event")
    public void maSwojEvent() {
        assertThat(response.getStatusCode(), is(201));
        assertThat(response.jsonPath().getString("eventName"), is("Testowy Event"));
    }



    // Next

    private Map<String, String> loginData1 = new HashMap<>();
    private String tokenAdmin;

    @When("zatwierdza event stworzony użytkownika user1@gmail.com")
    public void zatwierdzaEventOId() {

        loginData1.clear();
        loginData1.put("email", "admin@edu.p.lodz.pl");
        loginData1.put("password", "test");

        try {
            String json = objectMapper.writeValueAsString(loginData1);
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/api/auth/login");

            tokenAdmin = response.jsonPath().getString("token");
        } catch (Exception e) {
            e.printStackTrace();
        }

        response = RestAssured.given()
                .header("Authorization", "Bearer " + tokenAdmin)
                .put(baseUrl + "/api/event/administrate/" + eventId + "/approve");

    }

    @Then("event jest zatwierdzony")
    public void eventJestZatwierdzony() {
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.asString(), containsString("approved"));
    }

    @When("pobiera oczekujące eventy")
    public void pobieraOczekujaceEventy() {
        response = RestAssured.given()
                .header("Authorization", "Bearer " + tokenAdmin)
                .get(baseUrl + "/api/event/administrate/waiting");
    }

    @Then("otrzymuje listę oczekujących eventów")
    public void otrzymujeListeOczekujacychEventow() {
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.jsonPath().getList("content"), is(not(empty())));
    }

    @When("dołącza do eventu użytkownika user1@gmail.com")
    public void dolaczaDoEventuOId() {
        response = RestAssured.given()
                .header("Authorization", "Bearer " + tokenStudent)
                .put(baseUrl + "/api/event/participant/" + eventId);
    }

    @Then("dołączył do eventu")
    public void dolaczylDoEventu() {
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.jsonPath().getString("eventName"), is(notNullValue()));
    }

    @When("opuszcza event użytkownika user1@gmail.com")
    public void opuszczaEventOId() {
        response = RestAssured.given()
                .header("Authorization", "Bearer " + tokenStudent)
                .delete(baseUrl + "/api/event/participant/" + eventId);
    }

    @Then("opuścił event")
    public void opuscilEvent() {
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.jsonPath().getString("eventName"), is(notNullValue()));
    }

}