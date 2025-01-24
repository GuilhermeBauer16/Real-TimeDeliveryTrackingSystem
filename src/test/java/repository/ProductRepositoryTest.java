package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ProductRepository;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import testContainers.AbstractionIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = RealTimeDeliveryTrackingSystemApplication.class)
class ProductRepositoryTest extends AbstractionIntegrationTest {


    private ProductEntity productEntity;

    private final ProductRepository repository;


    @Autowired
    ProductRepositoryTest(ProductRepository repository) {
        this.repository = repository;

    }


    @BeforeEach
    void setUp() {

        productEntity = new ProductEntity(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE,TestConstants.PRODUCT_QUANTITY);
        repository.save(productEntity);

    }


    @Test
    void testFindByProductName_WhenTheProductsWasFound_ShouldReturnProductsPageableList() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductEntity> products = repository.findByProductName(productEntity.getName(), pageable);

        ProductEntity product = products.getContent().getFirst();
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(TestConstants.ID, product.getId());
        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE, product.getPrice());
        assertEquals(TestConstants.PRODUCT_QUANTITY, product.getQuantity());

    }

}
