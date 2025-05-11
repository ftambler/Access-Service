package um.g7.Access_Service.Domain.Services;

import org.springframework.stereotype.Service;

@Service
public class DoorService {
    
    private final String secretKey = "agnip";

    public boolean isValidDoor(String doorname, String passcode) {
        return secretKey.equals(passcode);
    }

}
