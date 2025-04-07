package um.g7.Access_Service.Domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Access")
public class Access {
    @Id
    private UUID accessId;
    private LocalDateTime accessDate;
    private String firstName;
    private String lastName;
    private String cid;
}
