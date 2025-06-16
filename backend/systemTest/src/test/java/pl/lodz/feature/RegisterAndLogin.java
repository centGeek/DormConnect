package pl.lodz.feature;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class RegisterAndLogin {
    private final String baseUrl = "http://localhost:8000";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Response response;
    private String token;
    private String email;

    private Map<String, String> loginData = new HashMap<>();
    Map<String, Object> registrationData = new HashMap<>();
    Map<String, Object> userMap = new HashMap<>();

    @Given("dane do rejestracji email i haśle {string}")
    public void daneDoRejestracjiIHaśle(String password) {

        email = "user" + System.currentTimeMillis() + "@gmail.com";

        userMap.clear();
        userMap.put("email", email);
        userMap.put("userName", "student1");
        userMap.put("password", password);

        registrationData.clear();
        registrationData.put("id", null);
        registrationData.put("name", "Jan");
        registrationData.put("surname", "Kowalski");
        registrationData.put("user", userMap);
    }

    @When("uzytkownik wykonuje rejestracje")
    public void uzytkownikWykonujeRejestracje() {
        try {
            String json = objectMapper.writeValueAsString(registrationData);
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/register/student");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("uzytkownik stworzyl konto studenta")
    public void uzytkownikStworzylKontoStudenta() {
        assertThat(response.getStatusCode(), is(201));
    }

    // Next bc i am sooooo sleepy

    @Given("istnieje użytkownik o emailu i haśle {string}")
    public void istniejeUżytkownikOUsernameUserIHaśleHaslo(int userNum, int passNum) {
        loginData.put("username", "user" + userNum);
        loginData.put("password", "haslo" + passNum);
    }

    @When("wykonuję logowanie do systemu")
    public void wykonujęLogowanieDoSystemu() {
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

    @Then("uzytkownik ma dostęp do systemu")
    public void uzytkownikMaDostępDoSystemu() {
        assertThat(response.getStatusCode(), is(200));
        assertThat(token, notNullValue());
        assertThat(token, not(isEmptyString()));
    }

    // Next

    @Given("nie istnieje uzytkowni o username {string} i haśle {string}")
    public void nieIstniejeUzytkowniOUsernameIHaśle(String username, String password) {
        loginData.put("username", username);
        loginData.put("password", password);
    }



    @When("wykonuję PUT na {string} z body:")
    public void wykonuję_put_na_z_body(String path, String body) {
        response = RestAssured.given()
                .contentType("application/json")
                .body(body)
                .when()
                .put(baseUrl + path)
                .andReturn();
    }

    @When("wykonuję GET na {string}")
    public void wykonuję_get_na(String path) {
        response = RestAssured.given()
                .when()
                .get(baseUrl + path)
                .andReturn();
    }

    @Then("odpowiedź ma status {int}")
    public void odpowiedź_ma_status(Integer status) {
        assertThat(response.getStatusCode(), is(status));
    }

    @Then("odpowiedź zawiera {string}")
    public void odpowiedź_zawiera(String text) {
        assertThat(response.getBody().asString(), containsString(text));
    }

    @Given("istnieje użytkownik o username {string} i haśle {string}")
    public void istnieje_użytkownik_o_username_i_haśle(String username, String password) {
        // Można dodać przygotowanie użytkownika w bazie, lub pominąć jeśli dane testowe istnieją
    }

    @Then("odpowiedź zawiera token autoryzacji")
    public void odpowiedź_zawiera_token_autoryzacji() {
        String token = response.jsonPath().getString("token");
        assertThat(token, notNullValue());
    }

    @Given("zalogowany użytkownik posiada token {string}")
    public void zalogowany_użytkownik_posiada_token(String token) {
        // Przechowujemy token do kolejnych zapytań
        this.token = token;
    }

    @When("wykonuję POST na {string} z header Authorization {string}")
    public void wykonuję_post_na_z_header_authorization(String path, String authHeader) {
        response = RestAssured.given()
                .header("Authorization", authHeader)
                .when()
                .post(baseUrl + path)
                .andReturn();
    }


}