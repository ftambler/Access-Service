package um.g7.Access_Service.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.PAYMENT_REQUIRED)
public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String err) {
        super(err);
    }
}
