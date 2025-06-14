package um.g7.Access_Service.Infrastructure.Repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import um.g7.Access_Service.Domain.Entities.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    @Query(value = """
            SELECT u.user_id, u.full_name, u.access_level, u.cid,
            CASE WHEN un.user_id IS NOT NULL THEN true ELSE false END AS rfid,
            CASE WHEN uv.user_id IS NOT NULL THEN true ELSE false END AS face
            FROM users u
            LEFT JOIN user_rfid un ON u.user_id = un.user_id
            LEFT JOIN user_vector uv ON u.user_id = uv.user_id
            GROUP BY u.user_id, u.full_name, un.user_id, uv.user_id
            """, nativeQuery = true)
        List<UserTableDataProjection> findAllUsersWRfidFace();

    @Query(value = """
        SELECT u.user_id, u.full_name, u.access_level, u.cid,
        CASE WHEN un.user_id IS NOT NULL THEN true ELSE false END AS rfid,
        CASE WHEN uv.user_id IS NOT NULL THEN true ELSE false END AS face
        FROM users u
        LEFT JOIN user_rfid un ON u.user_id = un.user_id
        LEFT JOIN user_vector uv ON u.user_id = uv.user_id
        WHERE LOWER(u.full_name) LIKE LOWER(CONCAT('%', :nameLookUp, '%'))
        GROUP BY u.user_id, u.full_name, un.user_id, uv.user_id
        """,
            countQuery = """
        SELECT COUNT(*) FROM users u
        WHERE LOWER(u.full_name) LIKE LOWER(CONCAT('%', :nameLookUp, '%'))
        """, nativeQuery = true)
    Page<UserTableDataProjection> findAllUsersWRfidFacePageable(@Param("nameLookUp") String nameLookUp, Pageable pageable);

    Optional<UserEntity> findByCid(String cid);
}