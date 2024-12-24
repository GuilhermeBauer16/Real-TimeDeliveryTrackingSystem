package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<DriverEntity, String> {

    @Query("SELECT d FROM DriverEntity d WHERE d.driverLicense =:driverLicense")
    Optional<DriverEntity> findDriverByDriverLicense(@Param("driverLicense")String driverLicense);

    @Query("SELECT a FROM DriverEntity d JOIN d.addresses a WHERE d.user.email = :driverEmail")
    Page<AddressEntity> findAddressesByDriverEmail(@Param("driverEmail") String driverEmail, Pageable pageable);

    @Query("SELECT d FROM DriverEntity d WHERE d.user.email = :userEmail")
    Optional<DriverEntity> findDriverByUserEmail(@Param("userEmail") String userEmail);
}
