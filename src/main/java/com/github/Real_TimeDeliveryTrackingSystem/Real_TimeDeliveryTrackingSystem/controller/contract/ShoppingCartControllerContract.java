package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * This interface defines the contract for the Shopping Cart Controller. It exposes endpoints
 * for managing the shopping cart, including adding products, retrieving items, and clearing the cart.
 */

public interface ShoppingCartControllerContract {

    /**
     * Adds a product to the shopping cart.
     *
     * @param shoppingCartRequest The request body containing the product details.
     * @return A {@link ResponseEntity} containing the added {@link ProductVO} and a 200 OK status,
     *         or an appropriate error response.
     * @throws InstantiationException If an error occurs during object instantiation.
     * @throws IllegalAccessException If access to a field is denied.
     * @throws NoSuchFieldException If a specified field cannot be found.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ProductVO> addProductsToShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest) throws InstantiationException, IllegalAccessException, NoSuchFieldException;

    /**
     * Retrieves a specific temporary product from the shopping cart by its ID.
     *
     * @param id The ID of the temporary product to retrieve.
     * @return A {@link ResponseEntity} containing the {@link TemporaryProductVO} and a 200 OK status,
     *         or a 404 Not Found status if the product is not found.
     */
    @GetMapping(value="/findProduct/{id}")
    ResponseEntity<TemporaryProductVO> findShoppingCartTemporaryProductById(@PathVariable("id") String id);

    /**
     * Retrieves all temporary products currently in the shopping cart, paginated.
     *
     * @param pageable The pagination information.
     * @return A {@link ResponseEntity} containing a {@link Page} of {@link TemporaryProductEntity} objects
     *         and a 200 OK status.
     */
    @GetMapping
    ResponseEntity<Page<TemporaryProductVO>> findShoppingCartTemporaryProducts(Pageable pageable);

    /**
     * Deletes a specific temporary product from the shopping cart by its ID.
     *
     * @param id The ID of the temporary product to delete.
     * @return A {@link ResponseEntity} with a 204 No Content status on successful deletion,
     *         or an appropriate error response.
     */
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteShoppingCartTemporaryProductById(@PathVariable("id") String id);

    /**
     * Retrieves the complete shopping cart contents.
     *
     * @return A {@link ResponseEntity} containing the {@link ShoppingCartResponse} and a 200 OK status.
     */
    @GetMapping(value= "/findShoppingCart", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ShoppingCartResponse> findShoppingCart();

    /**
     * Clears the entire shopping cart, removing all items.
     *
     * @return A {@link ResponseEntity} with a 204 No Content status on successful deletion,
     *         or an appropriate error response.
     */
    @DeleteMapping
    ResponseEntity<Void> deleteShoppingCart();
}