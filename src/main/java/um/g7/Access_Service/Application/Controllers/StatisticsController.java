package um.g7.Access_Service.Application.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import um.g7.Access_Service.Application.DTOs.FailAccessDTO;
import um.g7.Access_Service.Application.DTOs.SuccessAccessDTO;
import um.g7.Access_Service.Domain.Entities.AccessCounterDetails;
import um.g7.Access_Service.Domain.Services.StatisticsService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/successful-access-count")
    public ResponseEntity<List<AccessCounterDetails>> successfulAccessCount(@RequestParam(name = "startDate") Long startDate, @RequestParam(name = "endDate") Long endDate) {
        return ResponseEntity.ok(statisticsService.successfulAccessStatistics(startDate/1000, endDate/1000));
    }

    @GetMapping("/failed-access-count")
    public ResponseEntity<List<AccessCounterDetails>> failedAccessCount(@RequestParam(name = "startDate") Long startDate, @RequestParam(name = "endDate") Long endDate) {
        return ResponseEntity.ok(statisticsService.failedAccessStatistics(startDate/1000, endDate/1000));
    }

    @GetMapping("/successful-access-list")
    public ResponseEntity<List<SuccessAccessDTO>> successfulAccessList(@RequestParam(name = "startDate") Long startDate, @RequestParam(name = "endDate") Long endDate) {
        ZoneId zoneId = ZoneId.systemDefault();

        List<SuccessAccessDTO> successfulAccessList = statisticsService.successfulAccessStatisticsList(startDate/1000, endDate/1000).stream().map(successfulAccess -> {
            ZonedDateTime zonedDateTime = successfulAccess.getAccessDate().atZone(zoneId);
            Instant instant = zonedDateTime.toInstant();

            return SuccessAccessDTO.builder()
                    .fullName(successfulAccess.getFullName())
                    .accessDate(instant.toEpochMilli())
                    .accessType(successfulAccess.getAccessType())
                    .doorName(successfulAccess.getDoorName()).build();
        }).toList();

        return ResponseEntity.ok(successfulAccessList);
    }

    @GetMapping("/failed-access-list")
    public ResponseEntity<List<FailAccessDTO>> failedAccessList(@RequestParam(name = "startDate") Long startDate, @RequestParam(name = "endDate") Long endDate) {
        ZoneId zoneId = ZoneId.systemDefault();

        List<FailAccessDTO> failedAccessList = statisticsService.failedAccessStatisticsList(startDate/1000, endDate/1000).stream().map(failedAccess -> {
            ZonedDateTime zonedDateTime = failedAccess.getAccessDate().atZone(zoneId);
            Instant instant = zonedDateTime.toInstant();

            return FailAccessDTO.builder()
                    .accessDate(instant.toEpochMilli())
                    .accessType(failedAccess.getAccessType())
                    .doorName(failedAccess.getDoorName()).build();
        }).toList();

        return ResponseEntity.ok(failedAccessList);
    }
}
