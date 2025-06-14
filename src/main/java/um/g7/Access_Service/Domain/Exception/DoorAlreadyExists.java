package um.g7.Access_Service.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DoorAlreadyExists extends RuntimeException {
    public DoorAlreadyExists(String s) {
        super(s);
    }
}
