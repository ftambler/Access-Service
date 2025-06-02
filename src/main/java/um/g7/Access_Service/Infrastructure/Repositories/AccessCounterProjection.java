package um.g7.Access_Service.Infrastructure.Repositories;

public interface AccessCounterProjection {
    String getDoorName();
    Long getDateTime();
    Integer getCameraAccessCount();
    Integer getRfidAccessCount();
}
