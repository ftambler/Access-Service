package um.g7.Access_Service.Domain.Entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessCounterDetails {

    private String doorName;
    private long dateTime;
    private int cameraAccessCount;
    private int rfidAccessCount;
}
