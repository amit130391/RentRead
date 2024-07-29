package com.crio.RentRead.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crio.RentRead.Dto.BookDto;
import com.crio.RentRead.Entity.Book;
import com.crio.RentRead.Entity.Stat.Status;
import com.crio.RentRead.Service.BookService;

@RestController
public class BookController {
    
    @Autowired
    BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAvailableBooks(){
        List<Book> books = bookService.getAvailableBooks();
        if(books.isEmpty())
        return ResponseEntity.noContent().build();

        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/book")
    public ResponseEntity<Book> saveBook(@RequestBody BookDto bookDto){
        Book book = bookService.createBook(bookDto);
        return new ResponseEntity<Book>(book, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/book/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("id") Long id,@RequestBody BookDto bookDto,@RequestParam("status") Status status){
        Book updatedBook = bookService.updateBook(id, bookDto, status);
        return new ResponseEntity<Book>(updatedBook, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/book/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id){
        bookService.deleteBook(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
