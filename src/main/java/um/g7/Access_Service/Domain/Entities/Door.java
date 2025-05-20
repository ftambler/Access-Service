package um.g7.Access_Service.Domain.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = {"name"}) })
public class Door {

    @Id
    private UUID id;
    private String name;
    private int accessLevel;
}
