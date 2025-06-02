package um.g7.Access_Service.Infrastructure.Repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import um.g7.Access_Service.Domain.Entities.FailedAccess;

@Repository
public interface FailedAccessRepository extends JpaRepository<FailedAccess, UUID> {

    @Query(value = """
            SELECT 
                a.door_name AS doorName,
                EXTRACT(EPOCH FROM date_trunc('hour', a.access_date)) * 1000 AS dateTime,
                COUNT(*) FILTER (WHERE a.access_type = 'CAMERA') AS cameraAccessCount,
                COUNT(*) FILTER (WHERE a.access_type = 'RFID') AS rfidAccessCount
            FROM failed_access a
            WHERE a.access_date BETWEEN :startDate AND :endDate
            GROUP BY a.door_name, date_trunc('hour', a.access_date)
            ORDER BY a.door_name, dateTime
            """, nativeQuery = true)
    List<AccessCounterProjection> findDoorAccessStatsGroupedByHour(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}