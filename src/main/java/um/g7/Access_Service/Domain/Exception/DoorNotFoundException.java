package um.g7.Access_Service.Domain.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class DoorNotFoundException extends RuntimeException{
  public DoorNotFoundException(String msg) {
    super(msg);
  }
}