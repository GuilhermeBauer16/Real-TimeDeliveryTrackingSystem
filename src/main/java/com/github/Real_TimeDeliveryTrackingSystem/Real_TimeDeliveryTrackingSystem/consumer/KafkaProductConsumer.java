package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.consumer;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.consumer.contract.KafkaProductConsumerContract;
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
public class KafkaProductConsumer implements KafkaProductConsumerContract {

    private static final String KAFKA_PRODUCT_TOPIC = "product-topic";
    private static final String KAFKA_PRODUCT_CONTAINER_FACTORY = "productKafkaListenerContainerFactory";
    private static final String PRODUCT_GROUP_ID = "product-group";

    /**
     * <p>This variable is only used in test environment,
     * in production environment change the use for the method</p>
     * <h3>Production Example:</h3>
     * <pre> {@code
     *  PaymentClient paymentClient = new PaymentClient();
     *  Payment payment = paymentClient.get(Long.parseLong(id));
     *  payment.getPayer().getEmail();}</pre>
     * <h4>Put the <pre>{@code payment.getPayer().getEmail(); } </pre> in the place of the variable testMail</h4>
     */
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
    @Override
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

}
