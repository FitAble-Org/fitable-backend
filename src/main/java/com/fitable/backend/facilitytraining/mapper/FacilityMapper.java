package com.fitable.backend.facilitytraining.mapper;

import com.fitable.backend.facilitytraining.entity.Facility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FacilityMapper {

    Facility findById(@Param("id") long id);

    void saveFacilities(@Param("facilities") List<Facility> facilities);

    List<Facility> findFacilitiesWithinRadius(@Param("x") double x,
                                              @Param("y") double y,
                                              @Param("radiusKm") double radiusKm);
}
