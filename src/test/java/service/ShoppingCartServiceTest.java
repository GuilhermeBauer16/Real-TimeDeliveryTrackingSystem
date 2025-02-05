package service;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.AddressEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.CustomerEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ShoppingCartEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.UserEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.CustomerVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.UserProfile;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotAssociatedWithTheCustomerException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ShoppingCartNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.ShoppingCartRepository;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.ShoppingCartService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.customer.CustomerService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.ProductService;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.product.TemporaryProductService;
import constants.TestConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;


    @Mock
    private ShoppingCartRepository repository;
    @Mock
    private CustomerService customerService;

    @Mock
    private ProductService productService;

    @Mock
    private TemporaryProductService temporaryProductService;

    @InjectMocks
    private ShoppingCartService service;

    private CustomerEntity customerEntity;
    private CustomerVO customerVO;

    private ProductVO productVO;
    private ProductEntity productEntity;

    private TemporaryProductVO temporaryProductVO;
    private TemporaryProductEntity temporaryProductEntity;

    private ShoppingCartEntity shoppingCartEntity;
    private ShoppingCartRequest shoppingCartRequest;
    private ShoppingCartResponse shoppingCartResponse;


    private static final String SHOPPING_CART_NOT_FOUND_MESSAGE = "The ShoppingCart was not found!";
    private static final String PRODUCT_NOT_ASSOCIATED_WITH_USER_MESSAGE = "This product is not associated with this Customer!";

    private static final String EMAIL = "usershopingcart@example.com";
    private static final UserProfile ROLE_NAME = UserProfile.ROLE_DRIVER;
    private static final String PHONE_NUMBER = "+5511998775432";

    @BeforeEach
    void setUp() {


        AddressEntity addressEntity = new AddressEntity(TestConstants.ID, TestConstants.ADDRESS_STREET, TestConstants.ADDRESS_CITY
                , TestConstants.ADDRESS_STATE, TestConstants.ADDRESS_POSTAL_CODE, TestConstants.ADDRESS_COUNTRY);


        UserEntity userEntity = new UserEntity(TestConstants.ID, TestConstants.USER_USERNAME,
                EMAIL, TestConstants.USER_PASSWORD, ROLE_NAME);

        customerEntity = new CustomerEntity(TestConstants.ID, PHONE_NUMBER, new ArrayList<>(Arrays.asList(addressEntity)), userEntity);
        customerVO = new CustomerVO(TestConstants.ID, PHONE_NUMBER, new ArrayList<>(Arrays.asList(addressEntity)), userEntity);

        temporaryProductVO = new TemporaryProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);

        temporaryProductEntity = new TemporaryProductEntity(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY);

        productVO = new ProductVO(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY + TestConstants.PRODUCT_QUANTITY);

        productEntity = new ProductEntity(TestConstants.ID, TestConstants.PRODUCT_NAME,
                TestConstants.PRODUCT_DESCRIPTION, TestConstants.PRODUCT_PRICE, TestConstants.PRODUCT_QUANTITY + TestConstants.PRODUCT_QUANTITY);

        shoppingCartEntity = new ShoppingCartEntity(TestConstants.ID, customerEntity, new ArrayList<>(Arrays.asList(productEntity)), TestConstants.SHOPPING_CART_TOTAL_PRICE,
                new ArrayList<>(Arrays.asList(temporaryProductEntity)));

        shoppingCartRequest = new ShoppingCartRequest(TestConstants.ID, TestConstants.PRODUCT_QUANTITY);

        shoppingCartResponse = new ShoppingCartResponse(TestConstants.ID, TestConstants.SHOPPING_CART_TOTAL_PRICE,
                new ArrayList<>(Arrays.asList(temporaryProductEntity)));

        SecurityContextHolder.setContext(securityContext);


    }

    @Test
    void addToShoppingCart_WhenShoppingCartIsNotCreatedYet_ShouldReturnACreatedShoppingCartWithAProductAdded(){

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        when(customerService.findCustomerByEmail(anyString())).thenReturn(customerVO);
        when(productService.findProductById(anyString())).thenReturn(productVO);
        when(repository.findShoppingCartByCustomerEmail(anyString())).thenReturn(Optional.empty());

        when(temporaryProductService.createTemporaryProduct(any(TemporaryProductVO.class))).thenReturn(temporaryProductVO);
        when(repository.save(any(ShoppingCartEntity.class))).thenReturn(shoppingCartEntity);

        ProductVO product = service.addToShoppingCart(shoppingCartRequest);

        verify(customerService, times(1)).findCustomerByEmail(anyString());
        verify(productService, times(1)).findProductById(anyString());
        verify(repository, times(1)).save(any(ShoppingCartEntity.class));
        verify(customerService, times(1)).findCustomerByEmail(anyString());
        verify(temporaryProductService, times(1)).createTemporaryProduct(any(TemporaryProductVO.class));

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(TestConstants.ID, product.getId());
        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE * TestConstants.PRODUCT_QUANTITY, product.getPrice());
        assertEquals(TestConstants.PRODUCT_QUANTITY, product.getQuantity());


    }

    @Test
    void addToShoppingCart_WhenShoppingCartIsCreatedAndProductWillBeAddForFirstTime_ShouldReturnAProductAdded(){

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        when(customerService.findCustomerByEmail(anyString())).thenReturn(customerVO);
        when(productService.findProductById(anyString())).thenReturn(productVO);
        when(repository.findShoppingCartByCustomerEmail(anyString())).thenReturn(Optional.of(shoppingCartEntity));
        when(temporaryProductService.findTemporaryProductById(TestConstants.ID)).thenReturn(temporaryProductVO);
        when(repository.save(any(ShoppingCartEntity.class))).thenReturn(shoppingCartEntity);

        ProductVO product = service.addToShoppingCart(shoppingCartRequest);

        verify(customerService, times(1)).findCustomerByEmail(anyString());
        verify(productService, times(1)).findProductById(anyString());
        verify(repository, times(1)).save(any(ShoppingCartEntity.class));
        verify(customerService, times(1)).findCustomerByEmail(anyString());

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(TestConstants.ID, product.getId());
        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE, product.getPrice());
        assertEquals(TestConstants.PRODUCT_QUANTITY + TestConstants.PRODUCT_QUANTITY, product.getQuantity());


    }

    @Test
    void addToShoppingCart_WhenShoppingCartIsCreatedAndProductQuantityIsHigher_ShouldReturnAProductAdded(){

        shoppingCartRequest.setQuantity(TestConstants.PRODUCT_QUANTITY + 1);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);

        when(customerService.findCustomerByEmail(anyString())).thenReturn(customerVO);
        when(productService.findProductById(anyString())).thenReturn(productVO);
        when(repository.findShoppingCartByCustomerEmail(anyString())).thenReturn(Optional.of(shoppingCartEntity));
        when(temporaryProductService.findTemporaryProductById(TestConstants.ID)).thenReturn(temporaryProductVO);
        when(repository.save(any(ShoppingCartEntity.class))).thenReturn(shoppingCartEntity);

        ProductVO product = service.addToShoppingCart(shoppingCartRequest);

        verify(customerService, times(1)).findCustomerByEmail(anyString());
        verify(productService, times(1)).findProductById(anyString());
        verify(repository, times(1)).save(any(ShoppingCartEntity.class));
        verify(customerService, times(1)).findCustomerByEmail(anyString());

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(TestConstants.ID, product.getId());
        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE * (TestConstants.PRODUCT_QUANTITY + 1), product.getPrice());
        assertEquals(TestConstants.PRODUCT_QUANTITY + 1, product.getQuantity());


    }

    @Test
    void testFindShoppingCartTemporaryProductById_WhenSuccessful_ShouldReturnTemporaryProductObject() {


        when(repository.findShoppingCartByCustomerEmail(anyString())).thenReturn(Optional.of(shoppingCartEntity));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(temporaryProductService.findTemporaryProductById(anyString())).thenReturn(temporaryProductVO);

        TemporaryProductVO product = service.findShoppingCartTemporaryProductById(TestConstants.ID);

        verify(repository, times(1)).findShoppingCartByCustomerEmail(anyString());

        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(TestConstants.ID, product.getId());
        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE , product.getPrice());
        assertEquals(TestConstants.PRODUCT_QUANTITY, product.getQuantity());

    }

    @Test
    void testFindShoppingCartTemporaryProductById_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.empty());


        ShoppingCartNotFoundException exception = assertThrows(
                ShoppingCartNotFoundException.class, () -> service.findShoppingCartTemporaryProductById(TestConstants.ID));

        assertNotNull(exception);
        assertEquals(ShoppingCartNotFoundException.ERROR.formatErrorMessage(SHOPPING_CART_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testFindShoppingCartTemporaryProductById_WhenProductIsNotAssociatedWithCustomer_ShouldThrowProductNotAssociatedWithTheCustomerException() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.of(shoppingCartEntity));


        ProductNotAssociatedWithTheCustomerException exception = assertThrows(
                ProductNotAssociatedWithTheCustomerException.class, () -> service.findShoppingCartTemporaryProductById(TestConstants.INVALID_ID));

        assertNotNull(exception);
        assertEquals(ProductNotAssociatedWithTheCustomerException.ERROR.formatErrorMessage(PRODUCT_NOT_ASSOCIATED_WITH_USER_MESSAGE), exception.getMessage());


    }

    @Test
    void testDeleteShoppingCartTemporaryProductById_WhenDeleteProduct_ShouldDoNothing() {


        when(repository.findShoppingCartByCustomerEmail(anyString())).thenReturn(Optional.of(shoppingCartEntity));
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(temporaryProductService.findTemporaryProductById(anyString())).thenReturn(temporaryProductVO);
        doNothing().when(temporaryProductService).deleteTemporaryProduct(TestConstants.ID);

        service.deleteShoppingCartTemporaryProductById(TestConstants.ID);

        verify(repository, times(1)).findShoppingCartByCustomerEmail(anyString());
        verify(temporaryProductService, times(1)).deleteTemporaryProduct(anyString());


    }

    @Test
    void testDeleteShoppingCartTemporaryProductById_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.empty());


        ShoppingCartNotFoundException exception = assertThrows(
                ShoppingCartNotFoundException.class, () -> service.deleteShoppingCartTemporaryProductById(TestConstants.ID));

        assertNotNull(exception);
        assertEquals(ShoppingCartNotFoundException.ERROR.formatErrorMessage(SHOPPING_CART_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testDeleteShoppingCartTemporaryProductById_WhenProductIsNotAssociatedWithCustomer_ShouldThrowProductNotAssociatedWithTheCustomerException() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.of(shoppingCartEntity));


        ProductNotAssociatedWithTheCustomerException exception = assertThrows(
                ProductNotAssociatedWithTheCustomerException.class, () -> service.deleteShoppingCartTemporaryProductById(TestConstants.INVALID_ID));

        assertNotNull(exception);
        assertEquals(ProductNotAssociatedWithTheCustomerException.ERROR.formatErrorMessage(PRODUCT_NOT_ASSOCIATED_WITH_USER_MESSAGE), exception.getMessage());


    }

    @Test
    void testFindShoppingCart_WhenShoppingCartWasFound_ShouldReturnShoppingCartObject() {

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.of(shoppingCartEntity));

        ShoppingCartResponse shoppingCart = service.findShoppingCart();
        verify(repository, times(1)).findShoppingCartByCustomerEmail(EMAIL);

        assertNotNull(shoppingCart);
        assertNotNull(shoppingCart.getId());
        assertEquals(TestConstants.ID, shoppingCart.getId());
        assertEquals(TestConstants.SHOPPING_CART_TOTAL_PRICE, shoppingCart.getTotalPrice());
        assertEquals(1, shoppingCart.getTemporaryProducts().size());


    }

    @Test
    void testFindShoppingCart_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.empty());


        ShoppingCartNotFoundException exception = assertThrows(
                ShoppingCartNotFoundException.class, () -> service.findShoppingCart());

        assertNotNull(exception);
        assertEquals(ShoppingCartNotFoundException.ERROR.formatErrorMessage(SHOPPING_CART_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testFindShoppingCartProducts_WhenProductsAreFound_ShouldReturnPagedProductList() {

        List<TemporaryProductEntity> temporaryProductEntityList = List.of(temporaryProductEntity);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.of(shoppingCartEntity));
        Pageable pageable = Pageable.ofSize(10);
        when(repository.findAllTemporaryProductsByShoppingCart(eq(TestConstants.ID), any(Pageable.class)))
                .thenReturn(new PageImpl<>(temporaryProductEntityList));

        Page<TemporaryProductVO> shoppingCartProducts = service.findShoppingCartProducts(pageable);
        verify(repository, times(1)).findShoppingCartByCustomerEmail(EMAIL);
        verify(repository, times(1)).findAllTemporaryProductsByShoppingCart(anyString(), any(Pageable.class));

        TemporaryProductVO product = shoppingCartProducts.getContent().getFirst();
        assertNotNull(shoppingCartProducts);
        assertNotNull(product);
        assertNotNull(product.getId());
        assertEquals(TestConstants.ID, product.getId());
        assertEquals(TestConstants.PRODUCT_NAME, product.getName());
        assertEquals(TestConstants.PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(TestConstants.PRODUCT_PRICE, product.getPrice());
        assertEquals(TestConstants.PRODUCT_QUANTITY, product.getQuantity());


    }

    @Test
    void testFindShoppingCartProducts_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.empty());


        ShoppingCartNotFoundException exception = assertThrows(
                ShoppingCartNotFoundException.class, () -> service.findShoppingCartProducts(Pageable.ofSize(10)));

        assertNotNull(exception);
        assertEquals(ShoppingCartNotFoundException.ERROR.formatErrorMessage(SHOPPING_CART_NOT_FOUND_MESSAGE), exception.getMessage());


    }

    @Test
    void testDeleteShoppingCart_WhenShoppingCartWasDeleted_ShouldDoNothing() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.of(shoppingCartEntity));
        doNothing().when(repository).delete(shoppingCartEntity);

        service.deleteShoppingCart();
        verify(repository, times(1)).findShoppingCartByCustomerEmail(EMAIL);
        verify(repository, times(1)).delete(any(ShoppingCartEntity.class));


    }

    @Test
    void testDeleteShoppingCart_WhenCustomerIsNotFound_ShouldThrowCustomerNotFoundException() {


        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(EMAIL);
        when(repository.findShoppingCartByCustomerEmail(EMAIL)).thenReturn(Optional.empty());


        ShoppingCartNotFoundException exception = assertThrows(
                ShoppingCartNotFoundException.class, () -> service.deleteShoppingCart());

        assertNotNull(exception);
        assertEquals(ShoppingCartNotFoundException.ERROR.formatErrorMessage(SHOPPING_CART_NOT_FOUND_MESSAGE), exception.getMessage());


    }


}
