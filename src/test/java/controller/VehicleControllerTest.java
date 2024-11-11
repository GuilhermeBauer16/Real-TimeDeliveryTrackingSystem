package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.RealTimeDeliveryTrackingSystemApplication;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.entity.values.VehicleVO;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.PaginatedResponse;
import config.TestConfigs;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import testContainers.AbstractionIntegrationTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = RealTimeDeliveryTrackingSystemApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
class VehicleControllerTest extends AbstractionIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static VehicleVO vehicleVO;

    private static final String URL_PREFIX = "/vehicle";
    private static final String HOST_PREFIX = "http://localhost:";

    private static final String ID = "d8e7df81-2cd4-41a2-a005-62e6d8079716";
    private static final String NAME = "Voyage";
    private static final String LICENSE_PLATE = "AQE1F34";
    private static final Type TYPE = Type.CAR;
    private static final Status STATUS = Status.AVAILABLE;

    private static final String UPDATED_NAME = "Gol";
    private static final String UPDATED_LICENSE_PLATE = "AXE1F34";

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        specification = new RequestSpecBuilder()
                .setBaseUri(HOST_PREFIX + TestConfigs.SERVER_PORT)
                .setBasePath(URL_PREFIX)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        vehicleVO = new VehicleVO(ID, NAME, LICENSE_PLATE, TYPE, STATUS);
    }

    @Test
    @Order(1)
    void givenVehicleObject_whenCreateVehicle_ShouldReturnVehicleObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(vehicleVO)
                .when()
                .post()
                .then()
                .statusCode(201)
                .extract()
                .body()
                .asString();

        VehicleVO createdVehicle = objectMapper.readValue(content, VehicleVO.class);

        Assertions.assertNotNull(createdVehicle);
        Assertions.assertNotNull(createdVehicle.getId());
        Assertions.assertTrue(createdVehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(NAME, createdVehicle.getName());
        assertEquals(LICENSE_PLATE, createdVehicle.getLicensePlate());
        assertEquals(TYPE, createdVehicle.getType());
        assertEquals(STATUS, createdVehicle.getStatus());

        vehicleVO.setId(createdVehicle.getId());
    }

    @Test
    @Order(2)
    void givenVehicleObject_whenFindVehicleById_ShouldReturnVehicleObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", vehicleVO.getId())
                .when()
                .get("/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        VehicleVO vehicle = objectMapper.readValue(content, VehicleVO.class);

        Assertions.assertNotNull(vehicle);
        Assertions.assertNotNull(vehicle.getId());
        Assertions.assertTrue(vehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TYPE, vehicle.getType());
        assertEquals(STATUS, vehicle.getStatus());

    }

    @Test
    @Order(3)
    void givenVehicleObject_whenFindVehicleByLicensePlate_ShouldReturnVehicleObject() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("licensePlate", vehicleVO.getLicensePlate())
                .when()
                .get("/findByLicensePlate/{licensePlate}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        VehicleVO vehicle = objectMapper.readValue(content, VehicleVO.class);

        Assertions.assertNotNull(vehicle);
        Assertions.assertNotNull(vehicle.getId());
        Assertions.assertTrue(vehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(NAME, vehicle.getName());
        assertEquals(LICENSE_PLATE, vehicle.getLicensePlate());
        assertEquals(TYPE, vehicle.getType());
        assertEquals(STATUS, vehicle.getStatus());

    }

    @Test
    @Order(4)
    void givenVehicleObject_whenFindAllVehicle_ShouldReturnVehicleObjectList() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PaginatedResponse<VehicleVO> paginatedResponse =
                objectMapper.readValue(content, new TypeReference<>() {
                });

        VehicleVO paginatedVehicle = paginatedResponse.getContent().getFirst();

        Assertions.assertNotNull(paginatedVehicle);
        Assertions.assertNotNull(paginatedVehicle.getId());
        Assertions.assertTrue(paginatedVehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(NAME, paginatedVehicle.getName());
        assertEquals(LICENSE_PLATE, paginatedVehicle.getLicensePlate());
        assertEquals(TYPE, paginatedVehicle.getType());
        assertEquals(STATUS, paginatedVehicle.getStatus());

    }

    @Test
    @Order(5)
    void givenVehicleObject_whenUpdateVehicle_ShouldReturnVehicleObject() throws JsonProcessingException {

        vehicleVO.setLicensePlate(UPDATED_LICENSE_PLATE);
        vehicleVO.setName(UPDATED_NAME);

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(vehicleVO)
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        VehicleVO createdVehicle = objectMapper.readValue(content, VehicleVO.class);

        Assertions.assertNotNull(createdVehicle);
        Assertions.assertNotNull(createdVehicle.getId());
        Assertions.assertTrue(createdVehicle.getId().matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));

        assertEquals(UPDATED_NAME, createdVehicle.getName());
        assertEquals(UPDATED_LICENSE_PLATE, createdVehicle.getLicensePlate());
        assertEquals(TYPE, createdVehicle.getType());
        assertEquals(STATUS, createdVehicle.getStatus());

    }

    @Order(6)
    @Test
    void givenVehicleObject_when_delete_ShouldReturnNoContent() {

        given().spec(specification)
                .pathParam("id", vehicleVO.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);

    }
}
