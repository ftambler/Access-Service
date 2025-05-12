package um.g7.Access_Service.Application.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import um.g7.Access_Service.Application.DTOs.DoorDTO;
import um.g7.Access_Service.Domain.Entities.AccessCounterDetails;
import um.g7.Access_Service.Domain.Services.DoorService;
import um.g7.Access_Service.Domain.Services.StatisticsService;

import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    private final DoorService doorService;
    private final StatisticsService statisticsService;

    public StatisticsController(DoorService doorService, StatisticsService statisticsService) {
        this.doorService = doorService;
        this.statisticsService = statisticsService;
    }

    @GetMapping("/doors")
    public ResponseEntity<List<DoorDTO>> listDoors() {
        List<DoorDTO> doorsDTO = doorService.fetchDoors().stream()
                .map(door -> new DoorDTO(door.getName(), door.getAccessLevel())).toList();
        return ResponseEntity.ok(doorsDTO);
    }

    @GetMapping("/successful-access-count")
    public ResponseEntity<List<AccessCounterDetails>> successfulAccessCount(@RequestParam Long startDate, @RequestParam Long endDate) {
        return ResponseEntity.ok(statisticsService.successfulAccessStatistics(startDate, endDate));
    }

    @GetMapping("/failed-access-count")
    public ResponseEntity<List<AccessCounterDetails>> failedAccessCount(@RequestParam Long startDate, @RequestParam Long endDate) {
        return ResponseEntity.ok(statisticsService.failedAccessStatistics(startDate, endDate));
    }
}
