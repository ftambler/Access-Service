package um.g7.Access_Service.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class AdminAlreadyExists extends Exception{
    public AdminAlreadyExists(String err) {
        super(err);
    }
}
