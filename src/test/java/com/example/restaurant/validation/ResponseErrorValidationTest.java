package com.example.restaurant.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResponseErrorValidationTest {

    private ResponseErrorValidation responseErrorValidation;

    @BeforeEach
    void setUp() {
        responseErrorValidation = new ResponseErrorValidation();
    }

    @Test
    void mapValidationService_WithErrors_ReturnsResponseEntityWithErrorMessageMap() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        List<ObjectError> objectErrors = new ArrayList<>();
        List<FieldError> fieldErrors = new ArrayList<>();

        // Add some global errors
        objectErrors.add(new ObjectError("error1", "Global error 1"));
        objectErrors.add(new ObjectError("error2", "Global error 2"));

        // Add some field errors
        fieldErrors.add(new FieldError("field1", "field1", "Field error 1"));
        fieldErrors.add(new FieldError("field2", "field2", "Field error 2"));

        // Mock the BindingResult behavior
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(objectErrors);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

        // Act
        ResponseEntity<Object> responseEntity = responseErrorValidation.mapValidationService(bindingResult);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        Map<String, String> errorMap = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorMap);
        assertFalse(CollectionUtils.isEmpty(errorMap));
        assertEquals("Global error 1", errorMap.get("error1"));
        assertEquals("Global error 2", errorMap.get("error2"));
        assertEquals("Field error 1", errorMap.get("field1"));
        assertEquals("Field error 2", errorMap.get("field2"));

        // Verify the mock interactions
        verify(bindingResult, times(1)).hasErrors();
        verify(bindingResult, times(1)).getAllErrors();
        verify(bindingResult, times(1)).getFieldErrors();
    }

    @Test
    void mapValidationService_WithoutErrors_ReturnsNull() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);

        // Mock the BindingResult behavior
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        ResponseEntity<Object> responseEntity = responseErrorValidation.mapValidationService(bindingResult);

        // Assert
        assertNull(responseEntity);

        // Verify the mock interaction
        verify(bindingResult, times(1)).hasErrors();
        verifyNoMoreInteractions(bindingResult);
    }
}
