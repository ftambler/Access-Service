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
public class UserDTO {
    private UUID uuid;
    private String fullName;
    private String cid;
    private int accessLevel;
    private boolean hasRfid;
    private boolean hasFace;
}
