package um.g7.Access_Service.Application.DTOs;

import lombok.Data;

@Data
public class UserDTO {
    private String fullName;
    private String cid;
    private int accessLevel;
}
