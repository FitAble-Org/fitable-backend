package com.fitable.backend.facilitytraining.repository;

import com.fitable.backend.facilitytraining.entity.SportsFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SportsFacilityRepository extends JpaRepository<SportsFacility, Long> {

    @Query(value = "SELECT * FROM sports_facility f " +
            "WHERE (6371 * acos(cos(radians(:y)) * cos(radians(f.fcltyycrdnt_value)) " +
            "* cos(radians(f.fcltyxcrdnt_value) - radians(:x)) + sin(radians(:y)) " +
            "* sin(radians(f.fcltyycrdnt_value)))) < :radiusKm", nativeQuery = true)
    List<SportsFacility> findFacilitiesWithinRadius(
            @Param("x") double x,
            @Param("y") double y,
            @Param("radiusKm") double radiusKm
    );
}
