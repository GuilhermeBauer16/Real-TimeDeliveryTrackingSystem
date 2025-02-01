package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.ProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.TemporaryProductEntity;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ProductVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.ShoppingCartVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.driver.DuplicatedLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.InvalidLicensePlateException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.LicensePlateNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.vehicle.VehicleNotFoundException;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.request.ShoppingCartRequest;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.response.ShoppingCartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * VehicleControllerContract interface defines the API endpoints for managing vehicles
 * in the Real-Time Delivery Tracking System application.
 * It provides methods to create, update, retrieve, and delete vehicles.
 */

public interface ShoppingCartControllerContract {


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new Vehicle",
            description = "Creates a new vehicle and returns the created vehicle.",
            tags = "Driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = VehicleVO.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid License Plate or Duplicated License Plate",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Vehicle Not Found, Field Not Found, License Plate Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<ProductVO> addProductsToShoppingCart(@RequestBody ShoppingCartRequest shoppingCartRequest) throws InstantiationException, IllegalAccessException, NoSuchFieldException;

    @GetMapping
    ResponseEntity<Page<TemporaryProductEntity>> findShoppingCartProducts(Pageable pageable);

    @GetMapping(value= "/findShoppingCart", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ShoppingCartResponse> findShoppingCartById();

    @DeleteMapping
    ResponseEntity<Void> deleteShoppingCartProducts();



//    ShoppingCartVO updateShoppingCart(ShoppingCartVO shoppingCartVO);
//
//    ShoppingCartVO findShoppingCartById(ShoppingCartVO shoppingCartVO);
    /**
     * Updates an existing vehicle in the system.
     *
     * @param vehicleVO the vehicle data to update an existing vehicle
     * @return ResponseEntity containing the updated {@link VehicleVO}
     * @throws InvalidLicensePlateException
     * @throws DuplicatedLicensePlateException
     * @throws VehicleNotFoundException
     * @throws LicensePlateNotFoundException
     * @see VehicleVO
     */
//
//    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//    @Operation(summary = "Update a Vehicle",
//            description = "Update a vehicle and returns the updated vehicle.",
//            tags = "Driver")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful operation",
//                    content = @Content(schema = @Schema(implementation = VehicleVO.class))),
//            @ApiResponse(responseCode = "400", description = "Will throw Invalid License Plate or Duplicated License Plate",
//                    content = @Content),
//            @ApiResponse(responseCode = "404", description = "Will Throw Vehicle Not Found, Field Not Found, License Plate Not Found",
//                    content = @Content),
//            @ApiResponse(responseCode = "500", description = "Internal server error",
//                    content = @Content)
//    })
//    ResponseEntity<VehicleVO> update(@RequestBody VehicleVO vehicleVO) throws NoSuchFieldException, IllegalAccessException;
//
//    /**
//     * Finds a vehicle by its ID.
//     *
//     * @param id the unique identifier of the vehicle
//     * @return ResponseEntity containing the found {@link VehicleVO}
//     * @throws VehicleNotFoundException
//     * @see VehicleVO
//     */
//    @GetMapping(value = "/{id}")
//    @Operation(summary = "Find a single vehicle",
//            description = "Finds a vehicle by its ID and returns it.",
//            tags = "Driver")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful operation",
//                    content = @Content(schema = @Schema(implementation = VehicleVO.class))),
//            @ApiResponse(responseCode = "404", description = "Will Throw Vehicle Not Found or Field Not Found",
//                    content = @Content),
//            @ApiResponse(responseCode = "500", description = "Internal server error",
//                    content = @Content)
//    })
//    ResponseEntity<VehicleVO> findById(@PathVariable("id") String id);
//
//    /**
//     * Finds a vehicle by its license plate.
//     *
//     * @param licensePlate the license plate of the vehicle
//     * @return ResponseEntity containing the found {@link VehicleVO}
//     * @throws InvalidLicensePlateException
//     * @throws DuplicatedLicensePlateException
//     * @throws VehicleNotFoundException
//     * @throws LicensePlateNotFoundException
//     * @see VehicleVO
//     */
//
//    @GetMapping("/findByLicensePlate/{licensePlate}")
//    @Operation(summary = "Find a vehicle by its license plate",
//            description = "Find a vehicle by its license plate and returns it.",
//            tags = "Driver")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful operation",
//                    content = @Content(schema = @Schema(implementation = VehicleVO.class))),
//            @ApiResponse(responseCode = "400", description = "Will throw Invalid License Plate or Duplicated License Plate",
//                    content = @Content),
//            @ApiResponse(responseCode = "404", description = "Will Throw Vehicle Not Found, Field Not Found, License Plate Not Found",
//                    content = @Content),
//            @ApiResponse(responseCode = "500", description = "Internal server error",
//                    content = @Content)
//    })
//    ResponseEntity<VehicleVO> findByLicensePlate(@PathVariable("licensePlate") String licensePlate);
//
//    /**
//     * Finds all vehicles with pagination support.
//     *
//     * @param pageable the pagination information
//     * @return ResponseEntity containing a paginated list of VehicleVO
//     * @see Pageable
//     * @see VehicleVO
//     */
//    @GetMapping
//    @Operation(summary = "Find All vehicles",
//            description = "Finds all vehicles in the system and returns a paginated list.",
//            tags = "Driver")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successful operation",
//                    content = @Content(schema = @Schema(implementation = VehicleVO.class)))
//
//    })
//    ResponseEntity<Page<VehicleVO>> findAll(@PageableDefault(size = 20, page = 0) Pageable pageable);
//
//    /**
//     * Deletes a vehicle by its ID.
//     *
//     * @param id the unique identifier of the vehicle to be deleted
//     * @return ResponseEntity with no content if successful
//     */
//
//    @DeleteMapping(value = "/{id}")
//    @Operation(summary = "Delete a vehicle",
//            description = "Deletes a vehicle by its ID.",
//            tags = "Driver")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Successful operation, will return a not content",
//                    content = @Content),
//            @ApiResponse(responseCode = "404", description = "Will throw Vehicle Not Found",
//                    content = @Content),
//            @ApiResponse(responseCode = "500", description = "Internal server error",
//                    content = @Content)
//    })
//    ResponseEntity<Void> delete(@PathVariable("id") String id);
}


