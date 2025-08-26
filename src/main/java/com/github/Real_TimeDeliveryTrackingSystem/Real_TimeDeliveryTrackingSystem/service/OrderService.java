package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.OrderEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.OrderNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.OrderFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.OrderRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.OrderRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.OrderResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.OrderServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer.CustomerService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements OrderServiceContract {

    private static final String ORDER_NOT_FOUND_MESSAGE = "These order was not found!";

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final TemporaryProductService temporaryProductService;

    @Autowired
    public OrderService(OrderRepository orderRepository, CustomerService customerService, TemporaryProductService temporaryProductService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.temporaryProductService = temporaryProductService;
    }


    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        ValidatorUtils.checkObjectIsNullOrThrowException(orderRequest, ORDER_NOT_FOUND_MESSAGE, OrderNotFoundException.class);
        List<TemporaryProductEntity> temporaryProductEntities = new ArrayList<>();

        CustomerVO customerByEmail = customerService.findCustomerByEmail(orderRequest.getCustomerEmail());

        for (TemporaryProductVO temporaryProductVO : orderRequest.getTemporaryProductVOS()) {

            TemporaryProductVO temporaryProductById = temporaryProductService.findTemporaryProductById(temporaryProductVO.getId());
            temporaryProductEntities.add(BuildMapper.parseObject(new TemporaryProductEntity(), temporaryProductById));
        }

        CustomerEntity customerEntity = BuildMapper.parseObject(new CustomerEntity(), customerByEmail);
        OrderEntity orderEntity = OrderFactory.create(customerEntity, temporaryProductEntities,
                orderRequest.getTotalPrice(), orderRequest.getOrderStatus());

        orderRepository.save(orderEntity);

        return BuildMapper.parseObject(new OrderResponse(), orderEntity);

    }

    @Override
    @Transactional
    public OrderResponse findOrderById(String id) {

        OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() ->
                new OrderNotFoundException(ORDER_NOT_FOUND_MESSAGE));

        return BuildMapper.parseObject(new OrderResponse(), orderEntity);
    }

    @Override
    public Page<OrderResponse> findAllProducts(Pageable pageable) {

        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = principal.getUsername();

        ArrayList<OrderResponse> orderResponses = new ArrayList<>();

        Page<OrderEntity> orderEntities = orderRepository.findAllByCustomerEmail(email,pageable);

        for (OrderEntity orderEntity : orderEntities.getContent()) {

            orderResponses.add(BuildMapper.parseObject(new OrderResponse(), orderEntity));
        }
        return new PageImpl<>(orderResponses, pageable, orderEntities.getTotalElements());
    }


}
