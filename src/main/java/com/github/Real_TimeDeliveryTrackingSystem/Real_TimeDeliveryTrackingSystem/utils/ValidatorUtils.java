package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.utils;




import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.exception.ValidationUtilsException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ValidatorUtils {


    public static <T>  void checkFieldNotNullAndNotEmptyOrThrowException(T target, String errorMessage, Class<? extends RuntimeException> customException) {

        if (target == null) {
            throwException(errorMessage, customException);
        }

        Class<?> targetClass = target.getClass();


        Field[] sourcesFields = targetClass.getDeclaredFields();

        for (Field sourceField : sourcesFields) {

            try {
                sourceField.setAccessible(true);

                Object value = sourceField.get(target);

                if (value == null) {
                    throwException(sourceField.getName(), customException);
                }

                if (value instanceof String && ((String) value).trim().isEmpty()) {
                    throwException(sourceField.getName(), customException);
                }

            } catch (IllegalAccessException e) {
                throwException(errorMessage, customException);
            }
        }


    }

    public static  <T>  void checkObjectIsNullOrThrowException(T target, String errorMessage, Class<? extends RuntimeException> customException) {

        if (target == null) {
            throwException(errorMessage, customException);
        }

    }

    public static <T, S> T  updateFieldIfNotNull(T target, S source, String errorMessage, Class<? extends RuntimeException> customException) {

        Class<?> targetClass = target.getClass();
        Class<?> sourceClass = source.getClass();
        Field[] sourcesFields = sourceClass.getDeclaredFields();

        for (Field sourceField : sourcesFields) {
            try {

                sourceField.setAccessible(true);
                Object value = sourceField.get(source);

                if (value != null && !sourceField.getName().equals("serialVersionUID")) {
                    Field targetField = targetClass.getDeclaredField(sourceField.getName());
                    targetField.setAccessible(true);
                    targetField.set(target, value);
                }

            } catch (IllegalAccessException | NoSuchFieldException e) {
                throwException(errorMessage, customException);

            }
        }

        return target;

    }


    public static void throwException(String message, Class<? extends RuntimeException> runtimeExceptionClass) {
        try {
            Constructor<? extends RuntimeException> exceptionConstruction = runtimeExceptionClass.getConstructor(String.class);
            throw exceptionConstruction.newInstance(message);
        } catch (ReflectiveOperationException e) {

            throw new ValidationUtilsException(runtimeExceptionClass.getSimpleName(), e);


        }
    }
}
