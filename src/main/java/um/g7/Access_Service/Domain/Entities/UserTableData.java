package um.g7.Access_Service.Domain.Entities;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
