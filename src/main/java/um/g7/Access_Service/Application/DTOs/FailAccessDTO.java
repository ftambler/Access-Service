package um.g7.Access_Service.Application.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FailAccessDTO {
    private long accessDate;
    private String accessType;
    private String doorName;
}
