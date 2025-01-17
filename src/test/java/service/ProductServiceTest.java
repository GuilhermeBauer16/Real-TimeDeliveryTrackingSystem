package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.InvalidProductException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ProductRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.ValidatorUtils;
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
public class ProductServiceTest {

    private static final String PRODUCT_NOT_FOUND = "This product was not found.";
    private static final String INVALID_PRODUCT_MESSAGE = "This product is invalid, please verify the fields and try again.";

    private static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    private static final String NAME = "Shoes";
    private static final String DESCRIPTION = "That is the new version of the Shoes";
    private static final Double PRICE = 100D;

    private ProductVO productVO;
    private ProductEntity productEntity;
    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() {

        productVO = new ProductVO(ID, NAME, DESCRIPTION, PRICE);
        productEntity = new ProductEntity(ID, NAME, DESCRIPTION, PRICE);

    }

    @Test
    void testCreateProduct_WhenSuccessful_ShouldReturnProductObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(),
                    anyString(), any())).thenAnswer(invocation -> null);
            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(
                    any(), anyString(), any())).thenAnswer(invocation -> null);

            when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);

            ProductVO product = service.createProduct(productVO);

            verify(repository, times(1)).save(any(ProductEntity.class));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkObjectIsNullOrThrowException(any(), anyString(), any()));
            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));

            assertNotNull(product);
            assertNotNull(product.getId());
            assertEquals(ID, product.getId());
            assertEquals(NAME, product.getName());
            assertEquals(DESCRIPTION, product.getDescription());
            assertEquals(PRICE, product.getPrice());
        }
    }

    @Test
    void testCreateProduct_WhenProductIsNull_ShouldThrowInvalidProductException() {

        InvalidProductException exception = assertThrows(InvalidProductException.class,
                () -> service.createProduct(null));

        assertNotNull(exception);
        assertEquals(InvalidProductException.ERROR.formatErrorMessage(INVALID_PRODUCT_MESSAGE)
                , exception.getMessage());
    }

    @Test
    void testUpdateProduct_WhenSuccessful_ShouldReturnUpdatedProductObject() {

        try (MockedStatic<ValidatorUtils> mockedValidatorUtils = mockStatic(ValidatorUtils.class)) {

            mockedValidatorUtils.when(() -> ValidatorUtils.updateFieldIfNotNull(any(ProductEntity.class)
                    , any(), anyString(), any())).thenAnswer(invocation -> productEntity);

            mockedValidatorUtils.when(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(
                    any(), anyString(), any())).thenAnswer(invocation -> null);

            when(repository.findById(anyString())).thenReturn(Optional.of(productEntity));
            when(repository.save(any(ProductEntity.class))).thenReturn(productEntity);

            ProductVO product = service.updateProduct(productVO);

            verify(repository, times(1)).save(any(ProductEntity.class));
            verify(repository, times(1)).findById(anyString());

            mockedValidatorUtils.verify(() -> ValidatorUtils.updateFieldIfNotNull(any()
                    , any(), anyString(), any()));

            mockedValidatorUtils.verify(() -> ValidatorUtils.checkFieldNotNullAndNotEmptyOrThrowException(any(), anyString(), any()));

            assertNotNull(product);
            assertNotNull(product.getId());
            assertEquals(ID, product.getId());
            assertEquals(NAME, product.getName());
            assertEquals(DESCRIPTION, product.getDescription());
            assertEquals(PRICE, product.getPrice());
        }
    }


    @Test
    void testUpdateProduct_WhenProductIsNotFound_ShouldThrowProductNotFoundException() {

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> service.updateProduct(productVO));

        assertNotNull(exception);
        assertEquals(ProductNotFoundException.ERROR.formatErrorMessage(PRODUCT_NOT_FOUND)
                , exception.getMessage());
    }

    @Test
    void testFindProductById_WhenProductWasFoundById_ShouldReturnProductObject() {

        when(repository.findById(anyString())).thenReturn(Optional.of(productEntity));

        ProductVO product = service.findProductById(ID);

        verify(repository, times(1)).findById(anyString());

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(ID, product.getId());
        assertEquals(NAME, product.getName());
        assertEquals(DESCRIPTION, product.getDescription());
        assertEquals(PRICE, product.getPrice());
    }

    @Test
    void testFindProductById_WhenProductWasNotFoundById_ShouldThrowProductNotFoundException() {

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> service.findProductById(ID));

        assertNotNull(exception);
        assertEquals(ProductNotFoundException.ERROR.formatErrorMessage(PRODUCT_NOT_FOUND)
                , exception.getMessage());

    }

    @Test
    void testFindProductsByName_WhenProductsAreFounded_ShouldReturnPaginatedProducts() {

        List<ProductEntity> products = List.of(productEntity);
        when(repository.findByProductName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(products));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductVO> paginatedProducts = service.findProductsByName(NAME, pageRequest);

        verify(repository, times(1)).findByProductName(anyString(), any(Pageable.class));
        ProductVO product = paginatedProducts.getContent().getFirst();

        assertNotNull(paginatedProducts);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(1, paginatedProducts.getContent().size());

        assertEquals(ID, product.getId());
        assertEquals(NAME, product.getName());
        assertEquals(DESCRIPTION, product.getDescription());
        assertEquals(PRICE, product.getPrice());
    }

    @Test
    void testFindAllProducts_WhenProductsAreFounded_ShouldReturnPaginatedProducts() {

        List<ProductEntity> products = List.of(productEntity);
        when(repository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(products));

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<ProductVO> paginatedProducts = service.findAllProducts(pageRequest);

        verify(repository, times(1)).findAll(any(Pageable.class));
        ProductVO product = paginatedProducts.getContent().getFirst();

        assertNotNull(paginatedProducts);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(1, paginatedProducts.getContent().size());

        assertEquals(ID, product.getId());
        assertEquals(NAME, product.getName());
        assertEquals(DESCRIPTION, product.getDescription());
        assertEquals(PRICE, product.getPrice());
    }

    @Test
    void testDeleteProduct_WhenProductWasDeleted_ShouldReturnNothing(){

        when(repository.findById(anyString())).thenReturn(Optional.of(productEntity));
        doNothing().when(repository).delete(any());

        service.deleteProduct(ID);

        verify(repository,times(1)).findById(anyString());
        verify(repository,times(1)).delete(any());
    }

    @Test
    void TestDeleteProduct_WhenProductWasFoundById_ShouldThrowProductNotFoundException() {

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> service.deleteProduct(ID));

        assertNotNull(exception);
        assertEquals(ProductNotFoundException.ERROR.formatErrorMessage(PRODUCT_NOT_FOUND),exception.getMessage());
    }

}
