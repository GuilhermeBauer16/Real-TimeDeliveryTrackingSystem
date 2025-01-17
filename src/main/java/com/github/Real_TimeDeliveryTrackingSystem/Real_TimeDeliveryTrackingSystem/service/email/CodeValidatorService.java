package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.ExpiredVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.InvalidVerificationCodeException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserAlreadyAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserNotAuthenticatedException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.UserUpdateRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerificationCodeRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.CodeValidatorServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CodeValidatorService implements CodeValidatorServiceContract {

    private final UserService userService;

    private static final String USER_ALREADY_AUTHENTICATED_MESSAGE = "This user is already authenticated.";
    private static final String USER_NOT_AUTHENTICATED_MESSAGE = "This user is not authenticated, please authenticate the user and try again.";
    private static final String EXPIRED_VERIFICATION_CODE_MESSAGE = "This verification code is expired.";
    private static final String INVALID_VERIFICATION_CODE_MESSAGE = "This verification code %s is not valid.";

    @Autowired
    public CodeValidatorService(UserService userService) {
        this.userService = userService;
    }
    @Override
    @Transactional
    public void evaluatedVerifyCode(VerificationCodeRequest verifyCodeRequest) {

        UserVO userVO = userService.findUserByEmail(verifyCodeRequest.getEmail());
        System.out.println(userVO.getCodeExpiration());


        if (userVO.isAuthenticated()) {
            throw new UserAlreadyAuthenticatedException(USER_ALREADY_AUTHENTICATED_MESSAGE);
        }


        if (LocalDateTime.now().isAfter(userVO.getCodeExpiration())) {
            throw new ExpiredVerificationCodeException(EXPIRED_VERIFICATION_CODE_MESSAGE);
        }


        if (!userVO.getVerifyCode().strip().equals(verifyCodeRequest.getCode())) {
            throw new InvalidVerificationCodeException(String.format(INVALID_VERIFICATION_CODE_MESSAGE, verifyCodeRequest.getCode()));
        }


        userVO.setAuthenticated(true);
        UserUpdateRequest userUpdateRequest = BuildMapper.parseObject(new UserUpdateRequest(), userVO);
        userService.updateUser(userUpdateRequest);
    }
    @Override
    public void verifyIfUserIsAuthenticated(String email) {

        UserVO userVO = userService.findUserByEmail(email);


        if (!userVO.isAuthenticated()) {
            throw new UserNotAuthenticatedException(USER_NOT_AUTHENTICATED_MESSAGE);
        }
    }
}
