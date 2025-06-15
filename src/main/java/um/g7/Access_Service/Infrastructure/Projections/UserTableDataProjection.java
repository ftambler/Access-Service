package um.g7.Access_Service.Infrastructure.Projections;

import java.util.UUID;

public interface UserTableDataProjection {
    UUID getUserId();
    String getFullName();
    String getCid();
    Integer getAccessLevel();
    Boolean getRfid();
    Boolean getFace();
}
