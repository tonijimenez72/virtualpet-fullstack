package cat.itacademy.s05.t02.virtualpet.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidGadgetException extends RuntimeException {
    public InvalidGadgetException() {
        super("This gadget cannot be used in the current location");
    }
}