package um.g7.Access_Service.Application.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DoorDTO {

    private String name;
    private int accessLevel;
}
