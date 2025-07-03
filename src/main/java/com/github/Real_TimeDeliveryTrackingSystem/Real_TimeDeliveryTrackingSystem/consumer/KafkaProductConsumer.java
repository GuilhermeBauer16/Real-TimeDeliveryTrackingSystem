package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.consumer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.PaymentProcessedRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.email.EmailSenderService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.shoppingCart.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class KafkaProductConsumer {

    private static final String KAFKA_PRODUCT_TOPIC = "product-topic";
    private static final String KAFKA_PRODUCT_CONTAINER_FACTORY = "productKafkaListenerContainerFactory";
    private static final String PRODUCT_GROUP_ID = "product-group";

    private final String testMail;

    private final ProductService productService;
    private final TemporaryProductService temporaryProductService;
    private final EmailSenderService emailSenderService;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public KafkaProductConsumer(@Value("${mercado-pago.test-mail}") String testMail, ProductService productService,
                                TemporaryProductService temporaryProductService, EmailSenderService emailSenderService,
                                ShoppingCartService shoppingCartService) {
        this.testMail = testMail;
        this.productService = productService;
        this.temporaryProductService = temporaryProductService;

        this.emailSenderService = emailSenderService;
        this.shoppingCartService = shoppingCartService;
    }

    @KafkaListener(topics = KAFKA_PRODUCT_TOPIC, groupId = PRODUCT_GROUP_ID,
            containerFactory = KAFKA_PRODUCT_CONTAINER_FACTORY)
    public void listenProductUpdate(PaymentProcessedRequest paymentProcessedRequest) {


        List<TemporaryProductVO> temporaryProductVOS = new ArrayList<>();
        for (String productId : paymentProcessedRequest.getProductIds()) {


            TemporaryProductVO temporaryProductById = temporaryProductService.findTemporaryProductById(productId);
            ProductVO productById = productService.findProductById(productId);
            productById.setQuantity(productById.getQuantity() - temporaryProductById.getQuantity());
            productService.updateProduct(productById);
            temporaryProductVOS.add(temporaryProductById);

        }

        shoppingCartService.deleteShoppingCart(testMail);
        emailSenderService.sendMailWithApprovedPaymentToKafkaProducer(testMail, temporaryProductVOS, paymentProcessedRequest.getTransitionalAmount());


    }


//    private void handlerWithProductProcess(Payment payment) throws MessagingException {
//        List<PaymentItem> items = payment.getAdditionalInfo().getItems();
//
//
//        List<TemporaryProductVO> temporaryProductVOS = new ArrayList<>();
//        for (PaymentItem paymentItem : items) {
//
//
//            TemporaryProductVO temporaryProductById = temporaryProductService.findTemporaryProductById(paymentItem.getId());
//            ProductVO productById = productService.findProductById(paymentItem.getId());
//            productById.setQuantity(productById.getQuantity() - temporaryProductById.getQuantity());
//            productService.updateProduct(productById);
//            temporaryProductVOS.add(temporaryProductById);
//
//        }
//
//        shoppingCartService.deleteShoppingCart(testMail);
//        emailSenderService.sendMailWithApprovedPaymentToKafkaProducer(testMail, temporaryProductVOS, payment.getTransactionAmount());
//
//
//    }
}
