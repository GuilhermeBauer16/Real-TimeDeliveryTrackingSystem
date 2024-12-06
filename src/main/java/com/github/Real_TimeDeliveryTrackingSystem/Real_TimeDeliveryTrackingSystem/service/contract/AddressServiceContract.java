package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.AddressVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AddressServiceContract {

    AddressVO create(AddressVO addressVO);

    AddressVO update(AddressVO addressVO);

    AddressVO findById(String id);

    Page<AddressVO> findAll(Pageable pageable);

    void delete(String id);
}
