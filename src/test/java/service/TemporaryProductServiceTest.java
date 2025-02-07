package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.InvalidProductException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.TemporaryProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemporaryProductServiceTest {

    private static final String PRODUCT_NOT_FOUND = "This product was not found.";
    private static final String INVALID_PRODUCT_MESSAGE = "This product is invalid, please verify the fields and try again.";

    private TemporaryProductVO temporaryProductVO;
    private TemporaryProductEntity temporaryProductEntity;
    @Mock
    private TemporaryProductRepository repository;

    @InjectMocks
    private TemporaryProductService service;

    @BeforeEach
    void setUp() {

        temporaryProductVO = new TemporaryProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE,TestConstants.PRODUCT_QUANTITY);

        temporaryProductEntity = new TemporaryProductEntity(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE,TestConstants.PRODUCT_QUANTITY);

    }

    @Test
    void testCreateTemporaryProduct_WhenSuccessful_ShouldReturnTemporaryProductObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(),
                    anyString(), any())).thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(
                    any(), anyString(), any())).thenAnswer(invocation -> null);

            when(repository.save(any(TemporaryProductEntity.class))).thenReturn(temporaryProductEntity);

            TemporaryProductVO temporaryProduct = service.createTemporaryProduct(temporaryProductVO);

            verify(repository, times(1)).save(any(TemporaryProductEntity.class));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));

            assertNotNull(temporaryProduct);
            assertNotNull(temporaryProduct.getId());
            assertEquals(TestConstants.ID, temporaryProduct.getId());
            assertEquals(TestConstants.PRODUCT_NAME, temporaryProduct.getName());
            assertEquals(TestConstants.PRODUCT_DESCRIPTION, temporaryProduct.getDescription());
            assertEquals(TestConstants.PRODUCT_PRICE, temporaryProduct.getPrice());
            assertEquals(TestConstants.PRODUCT_QUANTITY, temporaryProduct.getQuantity());
        }
    }

    @Test
    void testCreateProduct_WhenProductIsNull_ShouldThrowInvalidProductException() {

        InvalidProductException exception = assertThrows(InvalidProductException.class,
                () -> service.createTemporaryProduct(null));

        assertNotNull(exception);
        assertEquals(InvalidProductException.ERROR.formatErrorMessage(INVALID_PRODUCT_MESSAGE)
                , exception.getMessage());
    }

    @Test
    void testUpdateTemporaryProduct_WhenSuccessful_ShouldReturnUpdatedTemporaryProductObject() {


        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {

            temporaryProductEntity.setName(TestConstants.PRODUCT_UPDATED_NAME);
            temporaryProductEntity.setPrice(TestConstants.PRODUCT_UPDATED_PRICE);

            mockedValidatorUtils.when(() -> ValidatorUtils.updateFieldIfNotNull(any(TemporaryProductEntity.class)
                    , any(), anyString(), any())).thenAnswer(invocation -> temporaryProductEntity);

            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(
                    any(), anyString(), any())).thenAnswer(invocation -> temporaryProductEntity);

            when(repository.findById(anyString())).thenReturn(Optional.of(temporaryProductEntity));
            when(repository.save(any(TemporaryProductEntity.class))).thenReturn(temporaryProductEntity);

            TemporaryProductVO product = service.updateTemporaryProduct(temporaryProductVO);

            verify(repository, times(1)).save(any(TemporaryProductEntity.class));
            verify(repository, times(1)).findById(anyString());

            mockedValidatorUtils.verify(() -> ValidatorUtils.updateFieldIfNotNull(any()
                    , any(), anyString(), any()));

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));

            assertNotNull(product);
            assertNotNull(product.getId());
            assertEquals(TestConstants.ID, product.getId());
            assertEquals(TestConstants.PRODUCT_UPDATED_NAME, product.getName());
            assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
            assertEquals(TestConstants.PRODUCT_UPDATED_PRICE, product.getPrice());
            assertEquals(TestConstants.PRODUCT_QUANTITY, product.getQuantity());

        }
    }


    @Test
    void testUpdateTemporaryProduct_WhenProductIsNotFound_ShouldThrowTemporaryProductNotFoundException() {

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> service.updateTemporaryProduct(temporaryProductVO));

        assertNotNull(exception);
        assertEquals(ProductNotFoundException.ERROR.formatErrorMessage(PRODUCT_NOT_FOUND)
                , exception.getMessage());
    }

    @Test
    void testFindProductById_WhenProductWasFoundById_ShouldReturnProductObject() {

        when(repository.findById(anyString())).thenReturn(Optional.of(temporaryProductEntity));

        TemporaryProductVO product = service.findTemporaryProductById(TestConstants.ID);

        verify(repository, times(1)).findById(anyString());

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(TestConstants.ID, product.getId());
        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE, product.getPrice());
        assertEquals(TestConstants.PRODUCT_QUANTITY, product.getQuantity());
    }

    @Test
    void testFindProductById_WhenProductWasNotFoundById_ShouldThrowProductNotFoundException() {

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> service.findTemporaryProductById(TestConstants.ID));

        assertNotNull(exception);
        assertEquals(ProductNotFoundException.ERROR.formatErrorMessage(PRODUCT_NOT_FOUND)
                , exception.getMessage());

    }


    @Test
    void testFindAllProducts_WhenProductsAreFounded_ShouldReturnPaginatedProducts() {

        List<TemporaryProductEntity> products = List.of(temporaryProductEntity);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(products));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductVO> paginatedProducts = service.findAllProducts(pageRequest);

        verify(repository, times(1)).findAll(any(Pageable.class));
        ProductVO product = paginatedProducts.getContent().getFirst();

        assertNotNull(paginatedProducts);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(1, paginatedProducts.getContent().size());

        assertEquals(TestConstants.ID, product.getId());
        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE, product.getPrice());
        assertEquals(TestConstants.PRODUCT_QUANTITY, product.getQuantity());

    }

    @Test
    void testDeleteProduct_WhenProductWasDeleted_ShouldReturnNothing() {

        when(repository.findById(anyString())).thenReturn(Optional.of(temporaryProductEntity));
        doNothing().when(repository).delete(any());

        service.deleteTemporaryProduct(TestConstants.ID);

        verify(repository, times(1)).findById(anyString());
        verify(repository, times(1)).delete(any());
    }

    @Test
    void TestDeleteProduct_WhenProductWasFoundById_ShouldThrowProductNotFoundException() {

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> service.deleteTemporaryProduct(TestConstants.ID));

        assertNotNull(exception);
        assertEquals(ProductNotFoundException.ERROR.formatErrorMessage(PRODUCT_NOT_FOUND), exception.getMessage());
    }


}
