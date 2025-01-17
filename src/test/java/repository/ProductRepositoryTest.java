package repository;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ProductRepository;
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


    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String NAME = "Shoes";
    private static final String DESCRIPTION = "That is the new version of the Shoes";
    private static final Double PRICE = 100D;


    private ProductEntity productEntity;

    private final ProductRepository repository;


    @Autowired
    ProductRepositoryTest(ProductRepository repository) {
        this.repository = repository;

    }


    @BeforeEach
    void setUp() {

        productEntity = new ProductEntity(ID, NAME, DESCRIPTION, PRICE);
        repository.save(productEntity);

    }


    @Test
    void testFindByProductName_WhenTheProductsWasFound_ShouldReturnProductsPageableList() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductEntity> products = repository.findByProductName(productEntity.getName(), pageable);

        ProductEntity product = products.getContent().getFirst();
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(ID, product.getId());
        assertEquals(NAME, product.getName());
        assertEquals(DESCRIPTION, product.getDescription());
        assertEquals(PRICE, product.getPrice());


    }

}
