package um.g7.Access_Service.Application.DTOs;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID uuid;
    private String fullName;
    private String cid;
    private int accessLevel;
}
