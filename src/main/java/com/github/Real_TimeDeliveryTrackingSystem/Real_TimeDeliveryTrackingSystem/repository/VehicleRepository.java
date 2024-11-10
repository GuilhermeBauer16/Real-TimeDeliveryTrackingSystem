package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity,String> {

    @Query("SELECT v FROM VehicleEntity v WHERE v.licensePlate =:licensePlate")
    Optional<VehicleEntity> findByLicensePlate(@Param("licensePlate") String licensePlate);
}
