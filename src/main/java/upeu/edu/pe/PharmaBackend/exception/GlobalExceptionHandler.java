package upeu.edu.pe.PharmaBackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Centraliza el manejo de errores de toda la API para que el cliente
 * siempre reciba una respuesta JSON controlada (nunca un stack trace).
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Referencia inexistente (ej: categoriaId que no existe)
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> handleRecursoNoEncontrado(RecursoNoEncontradoException ex,
                                                                    HttpServletRequest request) {
        log.warn("Recurso no encontrado: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // 409 - Violacion de una regla de negocio
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ErrorResponse> handleReglaNegocio(ReglaNegocioException ex,
                                                             HttpServletRequest request) {
        log.warn("Regla de negocio incumplida: {}", ex.getMessage());
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                "Regla de negocio incumplida",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    // 400 - Errores de validacion de @Valid en los DTO de entrada
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidacion(MethodArgumentNotValidException ex,
                                                           HttpServletRequest request) {
        Map<String, String> detalles = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fieldError ->
                detalles.put(fieldError.getField(), fieldError.getDefaultMessage()));

        log.warn("Error de validacion en {}: {}", request.getRequestURI(), detalles);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Error de validacion",
                "Uno o mas campos no cumplen las reglas de validacion",
                request.getRequestURI(),
                detalles
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 500 - Cualquier otro error no controlado explicitamente
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("Error interno no controlado en {}", request.getRequestURI(), ex);
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error interno del servidor",
                "Ocurrio un error inesperado. Contacte al administrador.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
