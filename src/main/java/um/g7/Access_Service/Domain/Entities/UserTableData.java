package um.g7.Access_Service.Domain.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTableData {
    private UUID id;
    private String fullName;
    private String cid;
    private int accessLevel;
    private boolean hasRfid;
    private boolean hasFace;
}
