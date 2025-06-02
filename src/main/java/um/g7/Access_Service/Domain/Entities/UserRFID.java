package um.g7.Access_Service.Domain.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "user_rfid")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserRFID {
    @Id
    private UUID userId;
    private String rfid;
}
