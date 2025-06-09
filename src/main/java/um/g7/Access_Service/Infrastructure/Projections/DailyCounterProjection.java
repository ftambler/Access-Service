package um.g7.Access_Service.Infrastructure.Projections;

public interface DailyCounterProjection {
    Long getAccessDayMillis();
    Long getAccessCount();
}
