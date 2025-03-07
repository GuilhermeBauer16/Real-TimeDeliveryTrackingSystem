//package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;
//
//
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ProductRequest;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.StripeResponse;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
//import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
//import com.stripe.Stripe;
//import com.stripe.exception.StripeException;
//import com.stripe.model.checkout.Session;
//import com.stripe.param.checkout.SessionCreateParams;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class StripeService {
//
//
//    private final String secretKey;
//
//    private final TemporaryProductService temporaryProductService;
//    private final ProductService productService;
//    @Autowired
//    public StripeService(@Value("${stripe.stripe-secret-key}") String secretKey, TemporaryProductService temporaryProductService, ProductService productService) {
//        this.secretKey = secretKey;
//        this.temporaryProductService = temporaryProductService;
//        this.productService = productService;
//    }
//
//
//    public StripeResponse paymentOfSimpleProduct(ProductRequest productRequest) {
//
//        Stripe.apiKey = secretKey;
//        TemporaryProductVO temporaryProductVO = temporaryProductService.findTemporaryProductById(productRequest.getProductId());
//
//        SessionCreateParams.LineItem.PriceData.ProductData productData =
//                SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                        .setName(temporaryProductVO.getName())
//                        .build();
//
//
//        SessionCreateParams.LineItem.PriceData priceData =
//                SessionCreateParams.LineItem.PriceData.builder()
//                        .setCurrency(productRequest.getCurrency() != null ? productRequest.getCurrency() : "BRL")
//                        .setUnitAmount(temporaryProductVO.getPrice().longValue() * 10)
//                        .setProductData(productData)
//                        .build();
//
//
//        SessionCreateParams.LineItem lineItem =
//                SessionCreateParams
//                        .LineItem.builder()
//                        .setQuantity(temporaryProductVO.getQuantity().longValue())
//                        .setPriceData(priceData)
//                        .build();
//
//
//        SessionCreateParams params =
//                SessionCreateParams.builder()
//                        .setMode(SessionCreateParams.Mode.PAYMENT)
//                        .setSuccessUrl("http://localhost:8080/success")
//                        .setCancelUrl("http://localhost:8080/cancel")
//                        .addLineItem(lineItem)
//                        .putMetadata("productId", productRequest.getProductId())
//                        .putMetadata("quantity", String.valueOf(temporaryProductVO.getQuantity()))
//                        .build();
//
//
//        Session session = null;
//        try {
//            session = Session.create(params);
//        } catch (StripeException e) {
//            //log the error
//        }
//
//
//        return StripeResponse
//                .builder()
//                .status("SUCCESS")
//                .message("Payment session created ")
//                .sessionId(session.getId())
//                .sessionUrl(session.getUrl())
//                .build();
//    }
//
//    public void updateProductQuantity(String productId) {
//        TemporaryProductVO temporaryProductVO = temporaryProductService.findTemporaryProductById(productId);
//        ProductVO product = productService.findProductById(temporaryProductVO.getId());
//        product.setQuantity(product.getQuantity() - temporaryProductVO.getQuantity() );
//        productService.updateProduct(product);
//    }
//
//}