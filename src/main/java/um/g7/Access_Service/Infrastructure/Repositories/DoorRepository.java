package um.g7.Access_Service.Infrastructure.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import um.g7.Access_Service.Domain.Entities.Door;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoorRepository extends JpaRepository<Door, UUID> {


    Optional<Door> findByName(String doorName);

    @Query(value = """
        SELECT *
        FROM door d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :nameLookUp, '%'))
        """,
            countQuery = """
        SELECT COUNT(*) FROM door d
        WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :nameLookUp, '%'))
        """, nativeQuery = true)
    Page<Door> findAllByDoorNameLookUp(@Param("nameLookUp")String nameLookUp, Pageable pageable);
}
