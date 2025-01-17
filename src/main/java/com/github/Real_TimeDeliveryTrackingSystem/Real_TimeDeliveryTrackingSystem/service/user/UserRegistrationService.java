package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.user;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.UserVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.EmailAllReadyRegisterException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.utils.FieldNotFound;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.user.UserNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.factory.UserFactory;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.mapper.BuildMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract.UserRegistrationServiceContract;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.EmailValidatorUtils;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserRegistrationService implements UserRegistrationServiceContract {

    private static final String USER_NOT_FOUND_MESSAGE = "That User was not found";
    private static final String EMAIL_ALREADY_REGISTER_MESSAGE = "The email %s is already registered for another user!";


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailSenderService emailSenderService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailSenderService = emailSenderService;
    }

    @Override
    @Transactional
    public UserVO createUser(UserVO userVO) throws MessagingException {

        ValidatorUtils.checkObjectIsNullOrThrowException(userVO,USER_NOT_FOUND_MESSAGE, UserNotFoundException.class);
        userVO.setPassword(passwordEncoder.encode(userVO.getPassword()));

        EmailValidatorUtils.verifyEmailPattern(userVO.getEmail());
        checkIfEmailAlreadyRegistered(userVO.getEmail());

        UserEntity userFactory = UserFactory.create(userVO.getName(),userVO.getEmail(),userVO.getPassword(),userVO.getUserProfile());
        userFactory.setAuthenticated(false);
        userFactory.setVerifyCode("null");
        userFactory.setCodeExpiration(LocalDateTime.now());
        ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(userFactory,USER_NOT_FOUND_MESSAGE, FieldNotFound.class);
        UserEntity userEntity = userRepository.save(userFactory);
        emailSenderService.sendEmailWithValidatorCodeToUser(userFactory.getEmail());
        return BuildMapper.parseObject(new UserVO(),userEntity);
    }

    private void checkIfEmailAlreadyRegistered(String email) {

        if(userRepository.findUserByEmail(email).isPresent()) {

            throw new EmailAllReadyRegisterException(String.format(EMAIL_ALREADY_REGISTER_MESSAGE, email));
        }
    }
}
