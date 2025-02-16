//package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class CallBackController {
//
//
//    @GetMapping("/success")
//    public ResponseEntity<String> success(@RequestParam(value = "payment_id", required = false) String paymentId,
//                                          @RequestParam(value = "payment_type", required = false) String paymentType,
//                                          @RequestParam(value = "collection_id", required = false) String collectionId,
//                                          @RequestParam(value = "collection_status", required = false) String collectionStatus,
//                                          @RequestParam(value = "merchant_order_id", required = false) String merchantOrderId,
//                                          @RequestParam(value = "preference_id", required = false) String preferenceId) {
//        // Log the parameters for debugging
//        System.out.println("Success Callback Received:");
//        System.out.println("payment_id: " + paymentId);
//        System.out.println("payment_type: " + paymentType);
//        System.out.println("collection_id: " + collectionId);
//        System.out.println("collection_status: " + collectionStatus);
//        System.out.println("merchant_order_id: " + merchantOrderId);
//        System.out.println("preference_id: " + preferenceId);
//
//        // Here you can update your order status, send confirmation emails, etc.
//        // based on the successful payment.
//
//        return ResponseEntity.status(HttpStatus.OK).body("Payment Successful!"); // Or a thank you page
//    }
//
//    @GetMapping("/failure")
//    public ResponseEntity<String> failure(@RequestParam(value = "payment_id", required = false) String paymentId,
//                                          @RequestParam(value = "payment_type", required = false) String paymentType,
//                                          @RequestParam(value = "collection_id", required = false) String collectionId,
//                                          @RequestParam(value = "collection_status", required = false) String collectionStatus,
//                                          @RequestParam(value = "merchant_order_id", required = false) String merchantOrderId,
//                                          @RequestParam(value = "preference_id", required = false) String preferenceId) {
//        // Log the parameters for debugging
//        System.out.println("Failure Callback Received:");
//        System.out.println("payment_id: " + paymentId);
//        System.out.println("payment_type: " + paymentType);
//        System.out.println("collection_id: " + collectionId);
//        System.out.println("collection_status: " + collectionStatus);
//        System.out.println("merchant_order_id: " + merchantOrderId);
//        System.out.println("preference_id: " + preferenceId);
//
//        // Handle failed payment (e.g., show an error message to the user)
//
//        return ResponseEntity.status(HttpStatus.OK).body("Payment Failed!"); // Or an error page
//    }
//
//    @GetMapping("/pending")
//    public ResponseEntity<String> pending(@RequestParam(value = "payment_id", required = false) String paymentId,
//                                          @RequestParam(value = "payment_type", required = false) String paymentType,
//                                          @RequestParam(value = "collection_id", required = false) String collectionId,
//                                          @RequestParam(value = "collection_status", required = false) String collectionStatus,
//                                          @RequestParam(value = "merchant_order_id", required = false) String merchantOrderId,
//                                          @RequestParam(value = "preference_id", required = false) String preferenceId) {
//        // Log the parameters for debugging
//        System.out.println("Pending Callback Received:");
//        System.out.println("payment_id: " + paymentId);
//        System.out.println("payment_type: " + paymentType);
//        System.out.println("collection_id: " + collectionId);
//        System.out.println("collection_status: " + collectionStatus);
//        System.out.println("merchant_order_id: " + merchantOrderId);
//        System.out.println("preference_id: " + preferenceId);
//
//        // Handle pending payment (e.g., display a message to the user)
//
//        return ResponseEntity.status(HttpStatus.OK).body("Payment Pending!"); // Or a pending page
//    }
//
//
//    @PostMapping("/webhook")
//    public ResponseEntity<String> webhook(@RequestBody String requestBody,
//                                          @RequestHeader("x-signature") String signature) {
//
//        System.out.println("Webhook Request Body:");
//        System.out.println(requestBody);
//        System.out.println("X-Signature Header:");
//        System.out.println(signature);
//
//
//        // 1. Get the notification ID (from the request body if needed)
//        // Example (adapt to the actual structure of the webhook payload):
//        // You'll likely need a JSON parsing library (like Jackson or Gson)
//        // to extract the relevant data.
//        /*
//        try {
//            ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper
//            JsonNode jsonNode = objectMapper.readTree(requestBody);
//            String notificationId = jsonNode.get("id").asText(); // Example
//            String topic = jsonNode.get("topic").asText();
//            // ... get other data ...
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid webhook payload");
//        }
//        */
//
//        // 2. Get the topic (payment, etc.)
//        //String topic = request.getParameter("topic"); // if is comming in query parameters
//
//        // 3. Get the payment information using the notification ID (using the Mercado Pago API)
//        // You'll need to use the MercadoPago SDK to fetch the payment details
//        // based on the notification ID.
//
//        // 4. Process the payment information (update your database, etc.)
//        // ... your logic to update orders, send confirmations, etc. ...
//
//        return ResponseEntity.status(HttpStatus.OK).body("Webhook Received!"); // Acknowledge the notification
//    }
//}
