package um.g7.Access_Service.Domain.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "successful_access")
public class SuccessfulAccess {
    @Id
    private UUID accessId;
    private LocalDateTime accessDate;
    private String fullName;
    private String cid;
    private String accessType;
    private String doorName;
}
