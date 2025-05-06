package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * This interface defines the contract for managing a shopping cart.  It provides methods
 * for adding, retrieving, deleting, and managing items within the shopping cart.
 */
public interface ShoppingCartServiceContract {

    /**
     * Adds a product to the shopping cart.
     *
     * @param shoppingCartRequest The request object containing the product details to add.
     * @return A {@link ProductVO} representing the added product.
     */
    ProductVO addToShoppingCart(ShoppingCartRequest shoppingCartRequest);

    /**
     * Retrieves a temporary product from the shopping cart by its ID.
     *
     * @param id The ID of the temporary product to retrieve.
     * @return A {@link TemporaryProductVO} representing the retrieved temporary product, or null if not found.
     */
    TemporaryProductVO findShoppingCartTemporaryProductById(String id);

    /**
     * Deletes a temporary product from the shopping cart by its ID.
     *
     * @param id The ID of the temporary product to delete.
     */
    void deleteShoppingCartTemporaryProductById(String id);

    /**
     * Retrieves the contents of the shopping cart.
     *
     * @return A {@link ShoppingCartResponse} containing the current items in the shopping cart.
     */
    ShoppingCartResponse findShoppingCart();

    /**
     * Retrieves a paginated list of temporary products in the shopping cart.
     *
     * @param pageable The pagination information.
     * @return A {@link Page} of {@link TemporaryProductEntity} objects representing the products in the shopping cart.
     */
    Page<TemporaryProductVO> findShoppingCartProducts(Pageable pageable);

    /**
     * Clears the entire shopping cart, removing all items.
     */
    void deleteShoppingCart(String email);
}