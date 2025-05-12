package um.g7.Access_Service.Domain.Services;

import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import um.g7.Access_Service.Domain.Entities.AccessCounterDetails;
import um.g7.Access_Service.Infrastructure.Repositories.FailedAccessRepository;
import um.g7.Access_Service.Infrastructure.Repositories.SuccessfulAccessRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class StatisticsService {

    private final SuccessfulAccessRepository successfulAccessRepository;
    private final FailedAccessRepository failedAccessRepository;

    public StatisticsService(SuccessfulAccessRepository successfulAccessRepository, FailedAccessRepository failedAccessRepository) {
        this.successfulAccessRepository = successfulAccessRepository;
        this.failedAccessRepository = failedAccessRepository;
    }

    public List<AccessCounterDetails> successfulAccessStatistics(long startDate, long endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.of("-03:00"));
        LocalDateTime endLocalDateTime = LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.of("-03:00"));

        return successfulAccessRepository.findDoorAccessStatsGroupedByHour(startLocalDateTime, endLocalDateTime).stream()
                .map(accessCounterProjection ->
                        AccessCounterDetails.builder()
                                .dateTime(accessCounterProjection.getDateTime())
                                .cameraAccessCount(accessCounterProjection.getCameraAccessCount())
                                .rfidAccessCount(accessCounterProjection.getRfidAccessCount())
                                .doorName(accessCounterProjection.getDoorName()).build()).toList();
    }

    public List<AccessCounterDetails> failedAccessStatistics(long startDate, long endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.of("-03:00"));
        LocalDateTime endLocalDateTime = LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.of("-03:00"));

        return failedAccessRepository.findDoorAccessStatsGroupedByHour(startLocalDateTime, endLocalDateTime).stream()
                .map(accessCounterProjection ->
                        AccessCounterDetails.builder()
                                .dateTime(accessCounterProjection.getDateTime())
                                .cameraAccessCount(accessCounterProjection.getCameraAccessCount())
                                .rfidAccessCount(accessCounterProjection.getRfidAccessCount())
                                .doorName(accessCounterProjection.getDoorName()).build()).toList();
    }
}
