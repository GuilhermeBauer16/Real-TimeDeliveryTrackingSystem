package utils;

import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils.CodeGeneratorUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CodeGeneratorUtilsTest {

    private static final String CODE_LENGTH_GREATER_ZERO = "Code length must be greater than zero";

    @Test
    void testGenerateCode_WhenLengthIsValid_ShouldReturnGeneratedCode() {

        int length = 6;


        String generatedCode = CodeGeneratorUtils.generateCode(length);

        assertNotNull(generatedCode);
        assertEquals(length, generatedCode.length());

    }

}
