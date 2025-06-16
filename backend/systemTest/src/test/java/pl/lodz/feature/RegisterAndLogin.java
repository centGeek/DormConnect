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

    private String email;

    private Map<String, Object> registrationData = new HashMap<>();
    private Map<String, Object> userMap = new HashMap<>();

    @Given("dane do rejestracji email i haśle {string}")
    public void daneDoRejestracjiIHasle(String password) {

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

    // Next

    private Map<String, Object> registrationData2 = new HashMap<>();
    private Map<String, Object> userMap2 = new HashMap<>();

    @Given("dane do rejestracji {string} i haśle {string}")
    public void istniejeUzytkownikOUsernameUserIHasleHaslo(String _email, String _password) {

        userMap2.clear();
        userMap2.put("email", _email);
        userMap2.put("userName", "student1");
        userMap2.put("password", _password);

        registrationData2.clear();
        registrationData2.put("id", null);
        registrationData2.put("name", "Jan");
        registrationData2.put("surname", "Kowalski");
        registrationData2.put("user", userMap2);

        try {
            String json = objectMapper.writeValueAsString(registrationData2);
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/register/student");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @When("uzytkownik próbuje się zarejestrować")
    public void uzytkownikPrboujeSieZarejestrowac() {
        try {
            String json = objectMapper.writeValueAsString(registrationData2);
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/register/student");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Then("uzytkownik dostaje błąd")
    public void uzytkownikMaDostepDoSystemu() {
        assertThat(response.getStatusCode(), is(409));
    }

    // Next

    private Map<String, String> loginData = new HashMap<>();
    private String token;

    @Given("istnieje użytkownik {string} i haśle {string}")
    public void istnieje_uzytkownik_i_hasle(String email, String password) {
        loginData.clear();
        loginData.put("email", email);
        loginData.put("password", password);
    }

    @When("wykonuję logowanie do systemu")
    public void wykonuje_logowanie_do_systemu() {
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
    public void uzytkownik_ma_dostep_do_systemu() {
        assertThat(response.getStatusCode(), is(200));
        assertThat(token, notNullValue());
        assertThat(token, not(isEmptyString()));
    }

    // Next

    private String token1;

    @Given("zalogowany użytkownik {string} i haśle {string} posiada token JWT")
    public void zalogowanyUzytkownikIHaslePosiadaTokenJWT(String email, String password) {
        Map<String, String> login = new HashMap<>();
        login.put("email", email);
        login.put("password", password);

        try {
            String json = objectMapper.writeValueAsString(login);
            response = RestAssured.given()
                    .contentType("application/json")
                    .body(json)
                    .post(baseUrl + "/api/auth/login");

            token1 = response.jsonPath().getString("token");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertThat(token1, notNullValue());
        assertThat(token1, not(isEmptyString()));
    }

    @When("wylogowuje się z systemu")
    public void wylogowujeSięZSystemu() {
    }

    @Then("jest wylogowany i nie posiada tokenu")
    public void jestWylogowanyINiePosiadaTokenu() {
    }

}