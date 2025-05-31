package um.g7.Access_Service.Application.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class DoorDTO {

    private UUID id;
    private String name;
    private int accessLevel;
}
