package um.g7.Access_Service.Domain.Entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessCounterDetails {

    private String doorName;
    private long dateTime;
    private int cameraAccessCount;
    private int rfidAccessCount;
}
