package cat.itacademy.s05.t02.virtualpet.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MinValueException extends RuntimeException {
    public MinValueException(String action) {
        super("Not enough " + action + ".");
    }
}