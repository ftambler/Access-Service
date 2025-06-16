package um.g7.Access_Service.Domain.Services;

import org.springframework.stereotype.Service;
import um.g7.Access_Service.Domain.Entities.AccessCounterDetails;
import um.g7.Access_Service.Domain.Entities.DayAccessCounter;
import um.g7.Access_Service.Domain.Entities.FailedAccess;
import um.g7.Access_Service.Domain.Entities.SuccessfulAccess;
import um.g7.Access_Service.Infrastructure.Repositories.FailedAccessRepository;
import um.g7.Access_Service.Infrastructure.Repositories.SuccessfulAccessRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StatisticsService {

    private final SuccessfulAccessRepository successfulAccessRepository;
    private final FailedAccessRepository failedAccessRepository;

    private final ZoneOffset ZONE_OFFSET = ZoneOffset.of("Z");

    public StatisticsService(SuccessfulAccessRepository successfulAccessRepository, FailedAccessRepository failedAccessRepository) {
        this.successfulAccessRepository = successfulAccessRepository;
        this.failedAccessRepository = failedAccessRepository;
    }

    public List<AccessCounterDetails> successfulAccessStatistics(long startDate, long endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.ofEpochSecond(startDate, 0, ZONE_OFFSET);
        LocalDateTime endLocalDateTime = LocalDateTime.ofEpochSecond(endDate, 0, ZONE_OFFSET);

        return successfulAccessRepository.findDoorAccessStatsGroupedByHour(startLocalDateTime, endLocalDateTime).stream()
                .map(accessCounterProjection ->
                        AccessCounterDetails.builder()
                                .dateTime(accessCounterProjection.getDateTime())
                                .cameraAccessCount(accessCounterProjection.getCameraAccessCount())
                                .rfidAccessCount(accessCounterProjection.getRfidAccessCount())
                                .doorName(accessCounterProjection.getDoorName()).build()).toList();
    }

    public List<AccessCounterDetails> failedAccessStatistics(long startDate, long endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.ofEpochSecond(startDate, 0, ZONE_OFFSET);
        LocalDateTime endLocalDateTime = LocalDateTime.ofEpochSecond(endDate, 0, ZONE_OFFSET);

        return failedAccessRepository.findDoorAccessStatsGroupedByHour(startLocalDateTime, endLocalDateTime).stream()
                .map(accessCounterProjection ->
                        AccessCounterDetails.builder()
                                .dateTime(accessCounterProjection.getDateTime())
                                .cameraAccessCount(accessCounterProjection.getCameraAccessCount())
                                .rfidAccessCount(accessCounterProjection.getRfidAccessCount())
                                .doorName(accessCounterProjection.getDoorName()).build()).toList();
    }

    public List<SuccessfulAccess> successfulAccessStatisticsList(long startDate, long endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.ofEpochSecond(startDate, 0, ZONE_OFFSET);
        LocalDateTime endLocalDateTime = LocalDateTime.ofEpochSecond(endDate, 0, ZONE_OFFSET);

        return successfulAccessRepository.findAllByAccessDateBetween(startLocalDateTime, endLocalDateTime);
    }

    public List<FailedAccess> failedAccessStatisticsList(long startDate, long endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.ofEpochSecond(startDate, 0, ZONE_OFFSET);
        LocalDateTime endLocalDateTime = LocalDateTime.ofEpochSecond(endDate, 0, ZONE_OFFSET);

        return failedAccessRepository.findAllByAccessDateBetween(startLocalDateTime, endLocalDateTime);
    }

    public List<DayAccessCounter> successfulAccessesByDay(long startDate, long endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.ofEpochSecond(startDate/1000, 0, ZONE_OFFSET);
        LocalDateTime endLocalDateTime = LocalDateTime.ofEpochSecond(endDate/1000, 0, ZONE_OFFSET);

        List<DayAccessCounter> list = successfulAccessRepository.countAccessesGroupedByDayMillisOnlyWithData(startLocalDateTime, endLocalDateTime)
                .stream().map(dailyCounter -> new DayAccessCounter(dailyCounter.getAccessDayMillis(), dailyCounter.getAccessCount())).toList();

        return fillEmptyDays(list, startDate, endDate);
    }

    public List<DayAccessCounter> failedAccessesByDay(long startDate, long endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.ofEpochSecond(startDate/1000, 0, ZONE_OFFSET);
        LocalDateTime endLocalDateTime = LocalDateTime.ofEpochSecond(endDate/1000, 0, ZONE_OFFSET);

        List<DayAccessCounter> list = failedAccessRepository.countAccessesGroupedByDayMillisOnlyWithData(startLocalDateTime, endLocalDateTime)
                .stream().map(dailyCounter -> new DayAccessCounter(dailyCounter.getAccessDayMillis(), dailyCounter.getAccessCount())).toList();

        return fillEmptyDays(list, startDate, endDate);
    }

    private List<DayAccessCounter> fillEmptyDays(List<DayAccessCounter> list, long startDate, long endDate) {
        Set<Long> existingDays = list.stream()
                .map(m -> m.getTimestamp())
                .collect(Collectors.toSet());

        List<DayAccessCounter> fullList = new ArrayList<>();
        
        long oneDayMillis = 86_400_000L;
        long current = startDate - (startDate % oneDayMillis);
        long endMillis = endDate - (endDate % oneDayMillis);

        while (current <= endMillis) {
            if (!existingDays.contains(current)) {
                fullList.add(new DayAccessCounter(current, 0L));
            }
            current += oneDayMillis;
        }

        fullList.addAll(list);

        fullList.sort(Comparator.comparingLong(m -> m.getTimestamp()));

        return fullList;
    }

}
