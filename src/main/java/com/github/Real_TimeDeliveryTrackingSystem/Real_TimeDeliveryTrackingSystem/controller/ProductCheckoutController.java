//package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;
//
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ProductRequest;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.StripeResponse;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.StripeService;
//import com.stripe.model.Event;
//import com.stripe.model.checkout.Session;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/payment/v1")
//public class ProductCheckoutController {
//
//
//    private final StripeService stripeService;
//    private final String secretKey;
//
//    @Autowired
//    public ProductCheckoutController(StripeService stripeService, @Value("${stripe.stripe-secret-key}") String secretKey) {
//        this.stripeService = stripeService;
//        this.secretKey = secretKey;
//    }
//
//    @PostMapping("/checkout")
//    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody ProductRequest productRequest) {
//
//        StripeResponse stripeResponse = stripeService.paymentOfSimpleProduct(productRequest);
//        return ResponseEntity.status(HttpStatus.CREATED).body(stripeResponse);
//
//
//    }
//
//    ;
//
//    @PostMapping("/webhook")
//    public String handleStripeWebhook(@RequestBody String payload, HttpServletRequest request) {
//        String sigHeader = request.getHeader("Stripe-Signature");
//        Event event = null;
//
//        try {
//            event = constructEvent(payload, sigHeader);
//        } catch (Exception e) {
//            System.err.println("⚠️  Webhook signature verification failed: " + e.getMessage()); // Include error message
//            return "Invalid signature";
//        }
//
//        if ("checkout.session.completed".equals(event.getType())) {
//            try {
//                Session session = (Session) event.getData().getObject();
//                String productId = session.getMetadata().get("productId");
//
//                if (productId == null) {
//                    System.err.println("Missing productId or quantity in metadata");
//                    return "Error processing event: Missing metadata"; // More specific error
//                }
//
//                stripeService.updateProductQuantity(productId);
//                return "Success"; // Explicitly return "Success"
//
//            } catch (Exception e) { // Catch any exception during processing
//                System.err.println("Error processing checkout.session.completed event: " + e.getMessage());
//                return "Error processing event: " + e.getMessage(); // Return error message
//            }
//        } else if ("checkout.session.async_payment_succeeded".equals(event.getType())) {
//            try {
//                Session session = (Session) event.getData().getObject();
//                String productId = session.getMetadata().get("productId");
//
//
//                if (productId == null) {
//                    System.err.println("Missing productId or quantity in metadata");
//                    return "Error processing event: Missing metadata"; // More specific error
//                }
//
//
//                stripeService.updateProductQuantity(productId);
//                return "Success"; // Explicitly return "Success"
//
//            } catch (Exception e) { // Catch any exception during processing
//                System.err.println("Error processing checkout.session.async_payment_succeeded event: " + e.getMessage());
//                return "Error processing event: " + e.getMessage(); // Return error message
//            }
//        }
//
//        return "Success"; // Return "Success" for other event types (or handle them if needed)
//    }
//
//
//    private Event constructEvent(String payload, String sigHeader) {
//        try {
//            return com.stripe.net.Webhook.constructEvent(payload, sigHeader, secretKey);
//        } catch (Exception e) {
//            System.err.println("Stripe Event Construct Error" + e.getMessage());
//            throw new RuntimeException("Invalid Stripe event"); // More descriptive exception
//        }
//    }
//}
