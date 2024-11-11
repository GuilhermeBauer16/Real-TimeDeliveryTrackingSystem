package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.controller.contract;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface VehicleControllerContract {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Register a new Vehicle",
            description = "Creates a new vehicle and returns the created vehicle.",
            tags = "Vehicles")
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
    ResponseEntity<VehicleVO> create(@RequestBody VehicleVO vehicleVO) throws InstantiationException, IllegalAccessException, NoSuchFieldException;


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a Vehicle",
            description = "Update a vehicle and returns the updated vehicle.",
            tags = "Vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = VehicleVO.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid License Plate or Duplicated License Plate",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Vehicle Not Found, Field Not Found, License Plate Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<VehicleVO> update(@RequestBody VehicleVO vehicleVO) throws NoSuchFieldException, IllegalAccessException;


    @GetMapping(value = "/{id}")
    @Operation(summary = "Find a single vehicle",
            description = "Finds a vehicle by its ID and returns it.",
            tags = "Vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = VehicleVO.class))),
            @ApiResponse(responseCode = "404", description = "Will Throw Vehicle Not Found or Field Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<VehicleVO> findById(@PathVariable("id") String id);


    @GetMapping("/findByLicensePlate/{licensePlate}")
    @Operation(summary = "Find a vehicle by its license plate",
            description = "Find a vehicle by its license plate and returns it.",
            tags = "Vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = VehicleVO.class))),
            @ApiResponse(responseCode = "400", description = "Will throw Invalid License Plate or Duplicated License Plate",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will Throw Vehicle Not Found, Field Not Found, License Plate Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<VehicleVO> findByLicensePlate(@PathVariable("licensePlate") String licensePlate);

    @GetMapping
    @Operation(summary = "Find All vehicles",
            description = "Finds all vehicles in the system and returns a paginated list.",
            tags = "Vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(schema = @Schema(implementation = VehicleVO.class)))

    })
    ResponseEntity<Page<VehicleVO>> findAll(@PageableDefault(size = 20, page = 0) Pageable pageable);


    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a vehicle",
            description = "Deletes a vehicle by its ID.",
            tags = "Vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successful operation, will return a not content",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Will throw Vehicle Not Found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    ResponseEntity<Void> delete(@PathVariable("id") String id);
}


