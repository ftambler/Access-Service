package um.g7.Access_Service.Domain.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserEntity {
    @Id
    private UUID userId;
    private String fullName;
    private String cid;
    private int accessLevel;
}
