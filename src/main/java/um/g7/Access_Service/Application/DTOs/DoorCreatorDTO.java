package um.g7.Access_Service.Application.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoorCreatorDTO {

    private String doorName;
    private String passcode;
    private int accessLevel;
}
