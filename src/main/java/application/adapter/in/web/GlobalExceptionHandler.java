package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para traducir errores de dominio y validacion
 * en respuestas HTTP estandarizadas con ApiResponse.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura violaciones a las reglas de negocio del dominio y retorna HTTP 400 Bad Request.
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainException(DomainException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage()));
    }

    /**
     * Captura argumentos invalidos (por ejemplo, valores de Enum desconocidos) y retorna HTTP 400.
     */
    @ExceptionHandler({IllegalArgumentException.class, NullPointerException.class})
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Parametro invalido en la solicitud: " + ex.getMessage()));
    }

    /**
     * Captura cuerpos JSON malformados o tipos de datos incompatibles.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("El cuerpo JSON de la peticion es invalido o esta incompleto"));
    }
}
