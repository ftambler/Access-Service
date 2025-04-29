package um.g7.Access_Service.Domain.Entities;

import java.util.UUID;

import org.checkerframework.common.aliasing.qual.Unique;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "admins")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Admin {
    @Id
    private UUID id;
    @Unique
    private String email;
    private String password;
}
