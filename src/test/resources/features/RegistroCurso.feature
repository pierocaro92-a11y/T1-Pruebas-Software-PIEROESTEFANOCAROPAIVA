Feature: Registro de cursos
  Como administrador academico
  Quiero registrar cursos en el sistema
  Para que los estudiantes puedan inscribirse

  Scenario: Registro de curso válido
    Given el servicio de cursos está disponible
    When registro el curso "Selenium Avanzado" con precio 400.0 y 20 vacantes
    Then el sistema responde con código 200
    And la respuesta contiene el nombre "Selenium Avanzado"

  Scenario: Registro con precio inválido
    Given el servicio de cursos está disponible
    When registro el curso "Curso Gratis" con precio 0.0 y 20 vacantes
    Then el sistema rechaza el registro con un código de error 4xx o 5xx