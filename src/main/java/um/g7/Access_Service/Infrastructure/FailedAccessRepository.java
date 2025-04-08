package um.g7.Access_Service.Infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import um.g7.Access_Service.Domain.Entities.FailedAccess;

@Repository
public interface FailedAccessRepository extends JpaRepository<FailedAccess, UUID> {
    
}