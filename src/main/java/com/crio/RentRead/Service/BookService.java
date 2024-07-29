package com.crio.RentRead.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.crio.RentRead.Dto.BookDto;
import com.crio.RentRead.Entity.Book;
import com.crio.RentRead.Entity.Stat.Status;
import com.crio.RentRead.Exceptions.BookPresentInDatabaseException;
import com.crio.RentRead.Exceptions.ResourceNotFoundException;
import com.crio.RentRead.Repository.BookRepository;

@Service
public class BookService {
    
    @Autowired
    BookRepository bookRepository;

    public List<Book> getAvailableBooks(){
        List<Book> availableBooks = bookRepository.findAll().stream().filter(book->book.getAvailabilityStatus().equals(Status.AVAILABLE)).collect(Collectors.toList());
        return availableBooks;
    }

    public Book createBook(BookDto bookDto){
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthorName(bookDto.getAuthorName());
        book.setGenre(bookDto.getGenre());
        book.setAvailabilityStatus(Status.AVAILABLE);
        try {
            bookRepository.save(book);
        } catch (DataIntegrityViolationException e) {
            throw new BookPresentInDatabaseException("Book with the same name is already present in the database");
        }
        return book;
    }

    public Book updateBook(Long id,BookDto updatedBook,Status status){
        Book book = bookRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Book not found for the given id:"+id));
        book.setTitle(updatedBook.getTitle());
        book.setAuthorName(updatedBook.getAuthorName());
        book.setGenre(updatedBook.getGenre());
        book.setAvailabilityStatus(status);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

}
