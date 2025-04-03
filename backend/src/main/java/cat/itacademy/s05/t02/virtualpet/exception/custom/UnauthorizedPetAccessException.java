package cat.itacademy.s05.t02.virtualpet.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UnauthorizedPetAccessException extends RuntimeException {
    public UnauthorizedPetAccessException() {
        super("You do not have access to this pet");
    }
}
