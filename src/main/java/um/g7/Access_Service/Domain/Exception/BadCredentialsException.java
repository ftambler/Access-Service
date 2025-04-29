package um.g7.Access_Service.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BadCredentialsException extends Exception{
    
    public BadCredentialsException(String err) {
        super(err);
    }
}
