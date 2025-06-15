package um.g7.Access_Service.Infrastructure.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import um.g7.Access_Service.Domain.Entities.UserVector;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface UserVectorRepository extends JpaRepository<UserVector, UUID> {

    @Query(value = """
            SELECT uv.user_id FROM user_vector uv WHERE uv.user_id = :userId
            """, nativeQuery = true)
    Optional<UUID> findByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query(value = """
            DELETE FROM user_vector uv WHERE uv.user_id = :userId
            """, nativeQuery = true)
    void deleteByUserId(@Param("userId") UUID userId);
}
