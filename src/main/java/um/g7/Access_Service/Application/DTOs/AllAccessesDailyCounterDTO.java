package um.g7.Access_Service.Application.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import um.g7.Access_Service.Domain.Entities.DayAccessCounter;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AllAccessesDailyCounterDTO {
    private List<DayAccessCounter> successful;
    private List<DayAccessCounter> failed;
}
