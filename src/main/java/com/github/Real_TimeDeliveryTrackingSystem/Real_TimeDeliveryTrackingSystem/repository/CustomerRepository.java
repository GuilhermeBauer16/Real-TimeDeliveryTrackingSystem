package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity,String> {

    @Query("SELECT a FROM CustomerEntity c JOIN c.addresses a WHERE c.user.email = :customerEmail")
    Page<AddressEntity> findAddressesByCustomerEmail(@Param("customerEmail") String customerEmail, Pageable pageable);


    @Query("SELECT c FROM CustomerEntity c WHERE c.id = :customerId AND c.user.email = :userEmail")
    Optional<CustomerEntity> findByIdAndUserEmail(@Param("customerId") String customerId, @Param("userEmail") String userEmail);

    @Query("SELECT c FROM CustomerEntity c WHERE c.user.email = :userEmail")
    Optional<CustomerEntity> findCustomerByUserEmail(@Param("userEmail") String userEmail);

    @Modifying
    @Query("DELETE FROM CustomerEntity c WHERE c.user.email = :email")
    void deleteCustomerByEmail(@Param("email") String email);




}
