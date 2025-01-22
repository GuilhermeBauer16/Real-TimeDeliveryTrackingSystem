package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.CodeGeneratorUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CodeGeneratorUtilsTest {


    @Test
    void testGenerateCode_WhenLengthIsValid_ShouldReturnGeneratedCode() {

        int length = 6;


        String generatedCode = CodeGeneratorUtils.generateCode(length);

        assertNotNull(generatedCode);
        assertEquals(length, generatedCode.length());

    }

}
