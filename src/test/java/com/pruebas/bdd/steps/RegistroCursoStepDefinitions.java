package com.pruebas.bdd.steps;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class RegistroCursoStepDefinitions {

    private static final String BASE_URL = "http://localhost:8080";

    private Response response;

    @Given("el servicio de cursos está disponible")
    public void servicioDisponible() {
        int status = RestAssured.given()
                .baseUri(BASE_URL)
                .when()
                .get("/api/cursos")
                .statusCode();
        assertEquals(200, status);
    }

    @When("registro el curso {string} con precio {double} y {int} vacantes")
    public void registroElCurso(String nombre, double precio, int vacantes) {
        Map<String, Object> body = new HashMap<>();
        body.put("nombre", nombre);
        body.put("precio", precio);
        body.put("vacantes", vacantes);

        response = RestAssured.given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/api/cursos");
    }

    @Then("el sistema responde con código {int}")
    public void elSistemaRespondeConCodigo(int codigo) {
        assertEquals(codigo, response.statusCode());
    }

    @Then("la respuesta contiene el nombre {string}")
    public void laRespuestaContieneElNombre(String nombre) {
        assertEquals(nombre, response.jsonPath().getString("nombre"));
    }

    @Then("el sistema rechaza el registro con un código de error 4xx o 5xx")
    public void elSistemaRechazaElRegistro() {
        assertTrue(response.statusCode() >= 400,
                "Se esperaba un error 4xx o 5xx pero fue " + response.statusCode());
    }
}