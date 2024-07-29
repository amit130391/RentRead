package com.crio.RentRead.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.RentRead.Entity.Book;
import com.crio.RentRead.Entity.User;
import com.crio.RentRead.Service.RentalService;

@RestController
@RequestMapping("user/me")
public class RentalController {

    @Autowired
    RentalService rentalService;

    @PostMapping("books/{bookId}/rent")
    public ResponseEntity<Book> rentBook(@AuthenticationPrincipal User user,@PathVariable("bookId") Long bookId){
        String email = user.getUsername();
        Book rentedBook = rentalService.rentBook(bookId, email);

        return new ResponseEntity<Book>(rentedBook, HttpStatus.OK);
    }

    @PostMapping("books/{bookId}/return")
    public ResponseEntity<Book> returnBook(@AuthenticationPrincipal User user,@PathVariable("bookId") Long bookId){
        String email=user.getUsername();
        Book rentedBook = rentalService.returnBook(bookId, email);

        return new ResponseEntity<Book>(rentedBook, HttpStatus.OK);
    }
}
