package com.example.restaurant.service;

import com.example.restaurant.entity.Dish;
import com.example.restaurant.entity.ImageModel;
import com.example.restaurant.entity.User;
import com.example.restaurant.exception.ImageNotFoundException;
import com.example.restaurant.repository.ImageRepository;
import com.example.restaurant.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImageUploadServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DishService dishService;

    @InjectMocks
    private ImageUploadService imageUploadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadImageToUser() throws IOException {
        User user = new User();
        user.setId(1L);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".getBytes());

        when(imageRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(imageRepository.save(any(ImageModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ImageModel uploadedImage = imageUploadService.uploadImageToUser(file, principal);

        assertNotNull(uploadedImage);
        assertEquals(user.getId(), uploadedImage.getUserId());
        assertArrayEquals(file.getBytes(), uploadedImage.getImageBytes());
        assertEquals(file.getOriginalFilename(), uploadedImage.getName());

        verify(userRepository, times(1)).findUserByUsername("testUser");
        verify(imageRepository, times(1)).findByUserId(user.getId());
        verify(imageRepository, times(1)).save(any(ImageModel.class));
    }

    @Test
    void testUploadImageToDish() throws IOException {
        Dish dish = new Dish();
        dish.setId(1L);

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image".getBytes());

        when(dishService.getDishById(dish.getId())).thenReturn(dish);
        when(imageRepository.save(any(ImageModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ImageModel uploadedImage = imageUploadService.uploadImageToDish(file, null, dish.getId());

        assertNotNull(uploadedImage);
        assertEquals(dish.getId(), uploadedImage.getDishId());
        assertArrayEquals(file.getBytes(), uploadedImage.getImageBytes());
        assertEquals(file.getOriginalFilename(), uploadedImage.getName());

        verify(dishService, times(1)).getDishById(dish.getId());
        verify(imageRepository, times(1)).save(any(ImageModel.class));
    }

    @Test
    void testGetImageToUser() {
        User user = new User();
        user.setId(1L);

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("testUser");
        when(userRepository.findUserByUsername("testUser")).thenReturn(Optional.of(user));

        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes("test image".getBytes());

        when(imageRepository.findByUserId(user.getId())).thenReturn(Optional.of(imageModel));

        ImageModel retrievedImage = imageUploadService.getImageToUser(principal);

        assertNotNull(retrievedImage);
        assertEquals(user.getId(), retrievedImage.getUserId());
        assertArrayEquals("test image".getBytes(), retrievedImage.getImageBytes());

        verify(userRepository, times(1)).findUserByUsername("testUser");
        verify(imageRepository, times(1)).findByUserId(user.getId());
    }

    @Test
    void testGetImageToDish() {
        Long dishId = 1L;

        ImageModel imageModel = new ImageModel();
        imageModel.setDishId(dishId);
        imageModel.setImageBytes("test image".getBytes());

        when(imageRepository.findByDishId(dishId)).thenReturn(Optional.of(imageModel));

        ImageModel retrievedImage = imageUploadService.getImageToDish(dishId);

        assertNotNull(retrievedImage);
        assertEquals(dishId, retrievedImage.getDishId());
        assertArrayEquals("test image".getBytes(), retrievedImage.getImageBytes());

        verify(imageRepository, times(1)).findByDishId(dishId);
    }

    @Test
    void testGetImageToDish_ImageNotFoundException() {
        Long dishId = 1L;

        when(imageRepository.findByDishId(dishId)).thenReturn(Optional.empty());

        assertThrows(ImageNotFoundException.class, () -> imageUploadService.getImageToDish(dishId));

        verify(imageRepository, times(1)).findByDishId(dishId);
    }
}
