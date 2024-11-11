package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity()
@Table(name= "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

    @Id
    private String id;
    private String name;
    @Column(name = "license_plate")
    private String licensePlate;
    @Enumerated(EnumType.STRING)
    private Type type;
    @Enumerated(EnumType.STRING)
    private Status status;

}
