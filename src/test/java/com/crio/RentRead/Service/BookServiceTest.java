package com.crio.RentRead.Service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.crio.RentRead.Dto.BookDto;
import com.crio.RentRead.Entity.Book;
import com.crio.RentRead.Entity.Stat.Status;
import com.crio.RentRead.Exceptions.BookPresentInDatabaseException;
import com.crio.RentRead.Exceptions.ResourceNotFoundException;
import com.crio.RentRead.Repository.BookRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAvailableBooks() {
        Book book1 = new Book();
        book1.setAvailabilityStatus(Status.AVAILABLE);

        Book book2 = new Book();
        book2.setAvailabilityStatus(Status.UNAVAILABLE);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAvailableBooks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Status.AVAILABLE, result.get(0).getAvailabilityStatus());
        verify(bookRepository).findAll();
    }

    @Test
    public void testCreateBookSuccess() {
        BookDto bookDto = new BookDto("Title", "Author", "Genre");
        Book book = new Book();
        book.setTitle("Title");
        book.setAuthorName("Author");
        book.setGenre("Genre");
        book.setAvailabilityStatus(Status.AVAILABLE);

        when(bookRepository.save(book)).thenReturn(book);

        Book result = bookService.createBook(bookDto);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Author", result.getAuthorName());
        assertEquals("Genre", result.getGenre());
        assertEquals(Status.AVAILABLE, result.getAvailabilityStatus());
        verify(bookRepository).save(book);
    }

    @Test
    public void testCreateBookAlreadyPresent() {
        BookDto bookDto = new BookDto("Title", "Author", "Genre");
        Book book = new Book();
        book.setTitle("Title");
        book.setAuthorName("Author");
        book.setGenre("Genre");
        book.setAvailabilityStatus(Status.AVAILABLE);

        when(bookRepository.save(book)).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        Exception exception = assertThrows(BookPresentInDatabaseException.class, () -> {
            bookService.createBook(bookDto);
        });

        assertEquals("Book with the same name is already present in the database", exception.getMessage());
    }

    @Test
    public void testUpdateBookSuccess() {
        Long id = 1L;
        BookDto updatedBookDto = new BookDto("Updated Title", "Updated Author", "Updated Genre");
        Book existingBook = new Book();
        existingBook.setId(id);
        existingBook.setTitle("Old Title");
        existingBook.setAuthorName("Old Author");
        existingBook.setGenre("Old Genre");
        existingBook.setAvailabilityStatus(Status.UNAVAILABLE);

        Book updatedBook = new Book();
        updatedBook.setId(id);
        updatedBook.setTitle("Updated Title");
        updatedBook.setAuthorName("Updated Author");
        updatedBook.setGenre("Updated Genre");
        updatedBook.setAvailabilityStatus(Status.AVAILABLE);

        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);

        Book result = bookService.updateBook(id, updatedBookDto, Status.AVAILABLE);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthorName());
        assertEquals("Updated Genre", result.getGenre());
        assertEquals(Status.AVAILABLE, result.getAvailabilityStatus());
        verify(bookRepository).findById(id);
        verify(bookRepository).save(updatedBook);
    }

    @Test
    public void testUpdateBookNotFound() {
        Long id = 1L;
        BookDto updatedBookDto = new BookDto("Updated Title", "Updated Author", "Updated Genre");

        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            bookService.updateBook(id, updatedBookDto, Status.AVAILABLE);
        });

        assertEquals("Book not found for the given id:" + id, exception.getMessage());
    }

    @Test
    public void testDeleteBook() {
        Long id = 1L;

        doNothing().when(bookRepository).deleteById(id);

        assertDoesNotThrow(() -> bookService.deleteBook(id));

        verify(bookRepository).deleteById(id);
    }
}

