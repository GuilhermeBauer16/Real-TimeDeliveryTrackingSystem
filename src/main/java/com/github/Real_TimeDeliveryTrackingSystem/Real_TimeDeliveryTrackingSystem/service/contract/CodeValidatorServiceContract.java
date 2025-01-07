package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.VerificationCodeRequest;



public interface CodeValidatorServiceContract {

    void evaluatedVerifyCode(VerificationCodeRequest verifyCodeRequest);


    void verifyIfUserIsAuthenticated(String email);


}
