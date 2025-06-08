package um.g7.Access_Service.Application.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoorDTO {

    private UUID id;
    private String name;
    private int accessLevel;
}
