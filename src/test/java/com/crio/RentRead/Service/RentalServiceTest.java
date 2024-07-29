package com.crio.RentRead.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.crio.RentRead.Entity.Book;
import com.crio.RentRead.Entity.User;
import com.crio.RentRead.Entity.Stat.Status;
import com.crio.RentRead.Exceptions.BookAlreadyRentedException;
import com.crio.RentRead.Exceptions.BookNotRentedByUserException;
import com.crio.RentRead.Exceptions.RentedBooksOutOfBoundsException;
import com.crio.RentRead.Exceptions.ResourceNotFoundException;
import com.crio.RentRead.Repository.BookRepository;
import com.crio.RentRead.Repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentalServiceTest {

    @InjectMocks
    private RentalService rentalService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRentBookSuccess() {
        Long bookId = 1L;
        String userEmail = "user@example.com";

        User user = new User();
        user.setEmail(userEmail);
        user.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(bookId);
        book.setAvailabilityStatus(Status.AVAILABLE);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.save(user)).thenReturn(user);
        when(bookRepository.save(book)).thenReturn(book);

        Book result = rentalService.rentBook(bookId, userEmail);

        assertNotNull(result);
        assertEquals(Status.UNAVAILABLE, result.getAvailabilityStatus());
        assertEquals(user, result.getUser());
        assertTrue(user.getBooks().contains(result));
        verify(userRepository).save(user);
        verify(bookRepository).save(book);
    }

    @Test
    public void testRentBookAlreadyRented() {
        Long bookId = 1L;
        String userEmail = "user@example.com";

        User user = new User();
        user.setEmail(userEmail);
        user.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(bookId);
        book.setAvailabilityStatus(Status.UNAVAILABLE);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(BookAlreadyRentedException.class, () -> {
            rentalService.rentBook(bookId, userEmail);
        });

        assertEquals("The book is already rented and unavailable for rental.", exception.getMessage());
    }

    @Test
    public void testRentBookLimitExceeded() {
        Long bookId = 1L;
        String userEmail = "user@example.com";

        User user = new User();
        user.setEmail(userEmail);
        user.setBooks(new ArrayList<>());
        user.getBooks().add(new Book());
        user.getBooks().add(new Book());

        Book book = new Book();
        book.setId(bookId);
        book.setAvailabilityStatus(Status.AVAILABLE);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Exception exception = assertThrows(RentedBooksOutOfBoundsException.class, () -> {
            rentalService.rentBook(bookId, userEmail);
        });

        assertEquals("More than 2 books not allowed for rental", exception.getMessage());
    }

    @Test
    public void testRentBookBookNotFound() {
    Long bookId = 1L;
    String userEmail = "user@example.com";

    // Mock the user repository to return a valid user
    User user = new User();
    user.setEmail(userEmail);
    user.setBooks(new ArrayList<>());

    when(userRepository.findByEmail(userEmail)).thenReturn(user);
    when(bookRepository.findById(bookId)).thenReturn(Optional.empty()); // Book not found

    Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
        rentalService.rentBook(bookId, userEmail);
    });

    assertEquals("Book not found with the given id" + bookId, exception.getMessage());
   }


   @Test
public void testReturnBookSuccess() {
    Long bookId = 1L;
    String userEmail = "user@example.com";

    // Create user and book entities
    User user = new User();
    user.setEmail(userEmail);
    List<Book> rentedBooks = new ArrayList<>();
    Book book = new Book();
    book.setId(bookId);
    book.setAvailabilityStatus(Status.UNAVAILABLE);
    book.setUser(user); // Ensure book's user is set to the mock user
    rentedBooks.add(book);
    user.setBooks(rentedBooks);

    // Create mock behavior for repositories
    when(userRepository.findByEmail(userEmail)).thenReturn(user);
    when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
    when(userRepository.save(user)).thenReturn(user); // Save should update user
    when(bookRepository.save(book)).thenReturn(book); // Save should update book

    // Call the service method
    Book result = rentalService.returnBook(bookId, userEmail);

    // Assertions
    assertNotNull(result);
    assertEquals(Status.AVAILABLE, result.getAvailabilityStatus());
    assertNull(result.getUser()); // Book should no longer be associated with the user
    assertFalse(user.getBooks().contains(result)); // User's books should no longer contain the returned book

    // Verify repository interactions
    verify(userRepository).save(user); // Ensure user save was called
    verify(bookRepository).save(result); // Ensure book save was called
}


    @Test
    public void testReturnBookNotRentedByUser() {
        Long bookId = 1L;
        String userEmail = "user@example.com";

        User user = new User();
        user.setEmail(userEmail);
        user.setBooks(new ArrayList<>());

        Book bookToReturn = new Book();
        bookToReturn.setId(bookId);
        bookToReturn.setAvailabilityStatus(Status.UNAVAILABLE);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookToReturn));

        Exception exception = assertThrows(BookNotRentedByUserException.class, () -> {
            rentalService.returnBook(bookId, userEmail);
        });

        assertEquals("The book is not rented by the user: " + userEmail, exception.getMessage());
    }

    @Test
    public void testReturnBookNotFound() {
        Long bookId = 1L;
        String userEmail = "user@example.com";

        User user = new User();
        user.setEmail(userEmail);
        user.setBooks(new ArrayList<>());

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            rentalService.returnBook(bookId, userEmail);
        });

        assertEquals("Book not found with the given id" + bookId, exception.getMessage());
    }
}

