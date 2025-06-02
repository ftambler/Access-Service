package um.g7.Access_Service.Infrastructure.Projections;

public interface AccessCounterProjection {
    String getDoorName();
    Long getDateTime();
    Integer getCameraAccessCount();
    Integer getRfidAccessCount();
}
