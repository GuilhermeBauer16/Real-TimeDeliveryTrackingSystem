package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.UserNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.UserUpdateRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.UserServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceContract {

    private final UserRepository userRepository;

    private final String USER_NOT_FOUND_MESSAGE = "User was not found!, Please verify and try again.";

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserVO findUserByEmail(String email) {

        UserEntity userEntity = userRepository.findUserByEmail(email).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        return BuildMapper.parseObject(new UserVO(), userEntity);
    }

    @Override
    public UserVO updateUser(UserUpdateRequest userUpdateRequest) {

        UserEntity userEntity = userRepository.findUserByEmail(userUpdateRequest.getEmail())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        UserVO userVO = BuildMapper.parseObject(new UserVO(), userUpdateRequest);

        UserEntity updatedUser = ValidatorUtils.updateFieldIfNotNull(userEntity, userVO, USER_NOT_FOUND_MESSAGE, FieldNotFound.class);
        ValidatorUtils.checkObjectIsNullOrThrowException(updatedUser,USER_NOT_FOUND_MESSAGE, FieldNotFound.class);
        userRepository.save(updatedUser);

        return BuildMapper.parseObject(new UserVO(), updatedUser);
    }
}
