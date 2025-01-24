package constants;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Status;
import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.enums.Type;

import java.time.LocalDateTime;

public final class TestConstants {

    // ID
    public static final String ID = "5f68880e-7356-4c86-a4a9-f8cc16e2ec87";
    public static final String INVALID_ID = "5f68880";

    //URL
    public static final String URL_HOST_PREFIX = "http://localhost:";
    public static final String URL_BEARER_PREFIX = "Bearer ";

    // Address
    public static final String ADDRESS_STREET = "123 Main St";
    public static final String ADDRESS_CITY = "Sample City";
    public static final String ADDRESS_STATE = "Sample State";
    public static final String ADDRESS_POSTAL_CODE = "12345";
    public static final String ADDRESS_COUNTRY = "Sample Country";
    public static final String ADDRESS_UPDATED_COUNTRY = "Brazil";
    public static final String ADDRESS_UPDATED_STATE = "Rio de Janeiro";


    // Vehicle

    public static final String VEHICLE_NAME = "Voyage";
    public static final Type VEHICLE_TYPE = Type.CAR;
    public static final Status VEHICLE_STATUS = Status.AVAILABLE;
    public static final String VEHICLE_UPDATED_NAME = "Gol";

    // User

    public static final String USER_USERNAME = "user";
    public static final String USER_PASSWORD = "password";
    public static final boolean USER_AUTHENTICATED = true;
    public static final LocalDateTime USER_CODE_EXPIRATION = LocalDateTime.now().plusDays(5);
    public static final String USER_VERIFY_CODE = "574077";
    public static final String USER_UPDATED_VERIFY_CODE = "574037";
    public static final LocalDateTime USER_EXPIRED_CODE_EXPIRATION = LocalDateTime.now().minusDays(1);

    //Product

    public static final String PRODUCT_NAME = "Shoes";
    public static final String PRODUCT_DESCRIPTION = "That is the new version of the Shoes";
    public static final Double PRODUCT_PRICE = 100D;
    public static final Integer PRODUCT_QUANTITY = 12;
    public static final Double PRODUCT_UPDATED_PRICE = 100D;
    public static final String PRODUCT_UPDATED_NAME = "AirMax";
}
