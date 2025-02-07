package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.TemporaryProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.InvalidProductException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.product.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interface defining the contract for product-related services.
 * This contract provides methods for creating, updating, retrieving, and deleting products,
 * as well as retrieving products by name and all products with pagination support.
 */
public interface TemporaryProductServiceContract {

    /**
     * Creates a new product based on the provided product details.
     *
     * @param temporaryProductVO the product details to be saved
     * @return the created {@link ProductVO}
     * @throws InvalidProductException
     */
    TemporaryProductVO createTemporaryProduct(TemporaryProductVO temporaryProductVO);

    /**
     * Updates an existing product based on the provided product details.
     *
     * @param temporaryProductVO the product details to update
     * @return the updated {@link ProductVO}
     * @throws ProductNotFoundException if the product does not exist
     */
    TemporaryProductVO updateTemporaryProduct(TemporaryProductVO temporaryProductVO);

    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve
     * @return the found product
     * @throws ProductNotFoundException if no product with the given ID exists
     */
    TemporaryProductVO findTemporaryProductById(String id);

    /**
     * Retrieves a paginated list of all products.
     *
     * @param pageable the pagination information
     * @return a paginated list of all {@link ProductVO}
     */
    Page<ProductVO> findAllProducts(Pageable pageable);

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     * @throws ProductNotFoundException if no product with the given ID exists
     */
    void deleteTemporaryProduct(String id);

}
