package com.example.usermanagementservice.service;
 
import com.example.usermanagementservice.model.User;
import com.example.usermanagementservice.exception.EmailAlreadyExistsException;
import com.example.usermanagementservice.repository.UserRepository;
import com.example.usermanagementservice.service.impl.UserServiceImpl;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
 
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
public class UserServiceImplTest {
 
    // Logger to help debug test executions 
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImplTest.class);
 
    // Mock the UserRepository (we're not hitting the real DB)
    @Mock
    private UserRepository userRepository;
 
    // Inject mocks into the service we want to test
    @InjectMocks
    private UserServiceImpl userService;
 
    // Initialize mocks before each test
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
logger.info("Mocks initialized for UserServiceImplTest");
    }
 
    // Test successful user registration (no duplicate email or phone)
    @Test
    void testRegisterUser_success() {
logger.info("Running: testRegisterUser_success");
 
        // Given
        User user = new User();
        user.setEmail("test@travel.com");
        user.setContactNumber("1234567890");
 
        // Mock: email and phone do NOT exist
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByContactNumber(user.getContactNumber())).thenReturn(Optional.empty());
when(userRepository.save(user)).thenReturn(user);  // Save should return the user
 
        // When
        User result = userService.registerUser(user);
 
        // Then
        assertNotNull(result);
        assertEquals("test@travel.com", result.getEmail());
        verify(userRepository).save(user);  // Ensures save() was called
logger.info("User registered successfully in test");
    }
 
    // Test registration failure due to duplicate email
    @Test
    void testRegisterUser_emailAlreadyExists() {
logger.info("Running: testRegisterUser_emailAlreadyExists");
 
        // Given
        User user = new User();
        user.setEmail("exists@travel.com");
 
        // Mock: email already exists
        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(new User()));
 
        // Then + When
        assertThrows(EmailAlreadyExistsException.class, () -> userService.registerUser(user));
        logger.warn("EmailAlreadyExistsException was thrown as expected");
    }
 
    // Test that all users are returned from repository
    @Test
    void testGetAllUsers() {
logger.info("Running: testGetAllUsers");
 
        // Given
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(new User());
        mockUsers.add(new User());
 
        // Mock: findAll returns 2 users
        when(userRepository.findAll()).thenReturn(mockUsers);
 
        // When
        List<User> result = userService.getAllUsers();
 
        // Then
        assertEquals(2, result.size());
        verify(userRepository).findAll();  // Ensures method was called
logger.info("Retrieved {} users from getAllUsers", result.size());
    }
    
    
    @Test
    void testUpdateUser_success() {
    logger.info("Running: testUpdateUser_success");
     
        // Given
        Long userId = 1L;
        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setEmail("old@travel.com");
        existingUser.setName("Old Name");
     
        User updatedDetails = new User();
        updatedDetails.setName("New Name");
        updatedDetails.setEmail("old@travel.com");  // same email
        updatedDetails.setContactNumber("9999999999");
     
        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenReturn(existingUser);
     
        // When
        User result = userService.updateUserProfile(userId, updatedDetails);
     
        // Then
        assertEquals("New Name", result.getName());
        verify(userRepository).save(existingUser);
    logger.info("User updated successfully in test");
    }
    
    
    @Test
    void testDeleteUser_success() {
    logger.info("Running: testDeleteUser_success");
     
        // Given
        Long userId = 2L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);
     
        // When
        userService.deleteUser(userId);
     
        // Then
        verify(userRepository).deleteById(userId);
    logger.info("User deleted successfully");
    }
    
    @Test
    void testGetUserByEmail_success() {
    logger.info("Running: testGetUserByEmail_success");
     
        // Given
        String email = "test@travel.com";
        User user = new User();
        user.setEmail(email);
     
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
     
        // When
        User result = userService.getUserByEmail(email);
     
        // Then
        assertEquals(email, result.getEmail());
        verify(userRepository).findByEmail(email);
    logger.info("User retrieved successfully by email");
    }
    
    @Test
    void testGetUserByEmail_notFound() {
    logger.info("Running: testGetUserByEmail_notFound");
     
        // Given
        String email = "missing@travel.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
     
        // Then
        assertThrows(RuntimeException.class, () -> userService.getUserByEmail(email));
        logger.warn("User not found exception thrown as expected");
    }
}