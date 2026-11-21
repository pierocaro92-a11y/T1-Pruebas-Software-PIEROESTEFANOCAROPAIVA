package com.pruebas.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pruebas.model.Inscripcion;
import com.pruebas.model.services.InscripcionService;
import com.pruebas.repository.InscripcionRepository;

@ExtendWith(MockitoExtension.class)
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository repository;

    @InjectMocks
    private InscripcionService service;

    @BeforeAll
    static void identificacion() {
        System.out.println("=====================================");
        System.out.println("Alumno: TU NOMBRE COMPLETO");
        System.out.println("Codigo: TU_CODIGO_DE_ESTUDIANTE");
        System.out.println("=====================================");
    }

    // ---------- CASO 1: inscribir correctamente (I2024077, curso 3) ----------
    @Test
    void inscribirEstudianteCorrectamente() {
        Inscripcion nueva = new Inscripcion();
        nueva.setCodigoEstudiante("I2024077");
        nueva.setCursoId(3L);

        when(repository.existsByCodigoEstudianteAndCursoId("I2024077", 3L)).thenReturn(false);
        when(repository.save(any(Inscripcion.class))).thenAnswer(inv -> inv.getArgument(0));

        Inscripcion resultado = service.inscribir(nueva);

        // el resultado no es nulo
        assertNotNull(resultado);

        // se invoca save() una vez y la inscripción registrada es la esperada
        ArgumentCaptor<Inscripcion> captor = ArgumentCaptor.forClass(Inscripcion.class);
        verify(repository, times(1)).save(captor.capture());
        assertEquals("I2024077", captor.getValue().getCodigoEstudiante());
        assertEquals(Long.valueOf(3L), captor.getValue().getCursoId());
    }

    // ---------- CASO 2: estudiante ya inscrito ----------
    @Test
    void noInscribirSiYaEstaInscrito() {
        Inscripcion existente = new Inscripcion();
        existente.setCodigoEstudiante("I2024001");
        existente.setCursoId(1L);

        when(repository.existsByCodigoEstudianteAndCursoId("I2024001", 1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.inscribir(existente));

        assertEquals("Estudiante ya inscrito", ex.getMessage());
        verify(repository, never()).save(any(Inscripcion.class));
    }

    // ---------- CASO 3: cancelar inscripción existente (id 1) ----------
    @Test
    void cancelarInscripcionExistente() {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setCodigoEstudiante("I2024001");
        inscripcion.setCursoId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(inscripcion));

        assertDoesNotThrow(() -> service.cancelar(1L));

        verify(repository, times(1)).delete(inscripcion);
    }
}