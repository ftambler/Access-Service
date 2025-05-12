package um.g7.Access_Service.Infrastructure.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import um.g7.Access_Service.Domain.Entities.Door;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoorRepository extends JpaRepository<Door, UUID> {


    Optional<Door> findByName(String doorName);
}
