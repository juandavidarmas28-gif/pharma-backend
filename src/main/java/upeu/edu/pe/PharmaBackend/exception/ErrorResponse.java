package upeu.edu.pe.PharmaBackend.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Estructura de respuesta controlada para todos los errores de la API.
 * Evita exponer trazas internas (stack traces) al cliente.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> detalles
) {
    public ErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    public ErrorResponse(int status, String error, String message, String path, Map<String, String> detalles) {
        this(LocalDateTime.now(), status, error, message, path, detalles);
    }
}
