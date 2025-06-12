package um.g7.Access_Service.Domain.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DayAccessCounter {
    private long timestamp;
    private long accessCount;
}
