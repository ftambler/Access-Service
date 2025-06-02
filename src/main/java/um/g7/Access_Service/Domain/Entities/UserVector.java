package um.g7.Access_Service.Domain.Entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "user_vector")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserVector {
    @Id
    private UUID userId;
    @Column(name = "vector", columnDefinition = "vector(768)")
    private float[] vector;

}
