package um.g7.Access_Service.Domain;

import java.sql.Date;

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
@Table(name = "Accesses")
public class Access {
    @Id
    private int accessId;
    private Date accessDate;
    private String firstName;
    private String lastName;
    private String cid;
}
