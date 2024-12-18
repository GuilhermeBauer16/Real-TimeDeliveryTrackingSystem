package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<DriverEntity, String> {
}
