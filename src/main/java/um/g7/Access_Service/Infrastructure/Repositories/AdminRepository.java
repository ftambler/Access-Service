package um.g7.Access_Service.Infrastructure.Repositories;

import org.springframework.core.annotation.MergedAnnotationPredicates;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import um.g7.Access_Service.Domain.Entities.Admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
    
    Optional<Admin> getByEmail(String email);

    @Query(value = """
        SELECT a.email
        FROM admins a
        WHERE a.email LIKE CONCAT('%', :nameLookUp, '%')
        """,
            countQuery = """
        SELECT COUNT(*) FROM admins a
        WHERE a.email LIKE CONCAT('%', :nameLookUp, '%')
        """, nativeQuery = true)
    Page<String> paginatedAdmins(@Param("nameLookUp") String nameLookUp, Pageable pageable);
}
