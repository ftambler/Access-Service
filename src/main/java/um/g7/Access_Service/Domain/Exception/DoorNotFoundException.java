package um.g7.Access_Service.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class DoorNotFoundException extends RuntimeException {
    public DoorNotFoundException(String message) {
        super(message);
    }
}
