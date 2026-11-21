package com.pruebas.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

class CursoApiTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
        System.out.println("Alumno: TU NOMBRE COMPLETO - Codigo: TU_CODIGO_DE_ESTUDIANTE");
    }

    // Escenario 1: consultar todos los cursos
    @Test
    void consultarTodosLosCursos() {
        given()
                .when()
                .get("/api/cursos")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(3));
    }

    // Escenario 2: consultar un curso por id
    // NOTA: el enunciado dice id 1, pero el DataLoader carga
    // "Seguridad de Aplicaciones" con id 2 (el id 1 es "Pruebas de Software").
    @Test
    void consultarCursoPorId() {
        given()
                .when()
                .get("/api/cursos/2")
                .then()
                .statusCode(200)
                .body("nombre", equalTo("Seguridad de Aplicaciones"))
                .body("vacantes", greaterThan(0));
    }
}