//package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;
//
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.PayPalService;
//import com.paypal.api.payments.Links;
//import com.paypal.api.payments.Payment;
//import com.paypal.base.exception.PayPalException;
//import com.paypal.base.rest.PayPalRESTException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.view.RedirectView;
//
//@Controller
//@RequiredArgsConstructor
//@Slf4j
//public class PayPalController {
//
//    private final PayPalService payPalService;
//
//    @GetMapping("/")
//    public String home() {
//        return "payPal/index";
//    }
//
//    @PostMapping("/payment/create")
//    public RedirectView createPayment() {
//
//        try {
//
//            String cancelUrl = "http://localhost:8080/payment/cancel";
//            String successUrl = "http://localhost:8080/payment/success";
//            Payment payment = payPalService.createPayment(
//                    10D,
//                    "USD",
//                    "paypal",
//                    "sale",
//                    "Payment description",
//                    cancelUrl,
//                    successUrl);
//
//            for(Links link : payment.getLinks()) {
//
//                if(link.getRel().equals("approval_url")) {
//                    return new RedirectView(link.getHref());
//                }
//            }
//        } catch (PayPalRESTException e) {
//            log.error("Error occured: ", e.getMessage());
//
//
//        }
//
//        return new RedirectView("/payment/error");
//    }
//
//    @GetMapping("/payment/success")
//    public  String paymentSuccess(
//            @RequestParam("paymentId") String paymentId,
//            @RequestParam("payerId") String payerId
//    ) {
//
//        try {
//
//            Payment payment = payPalService.executePayment(paymentId, payerId);
//            if(payment.getState().equals("approved")) {
//
//                return "paymentSuccess";
//            }
//
//
//        }catch (PayPalRESTException e) {
//            log.error("Error occured: ", e.getMessage());
//
//
//        }
//
//        return "paymentSuccess";
//
//    }
//
//    @GetMapping("/payment/cancel")
//    public String paymentCancel(){
//        return "paymentError";
//    }
//}
