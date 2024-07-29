package com.crio.RentRead.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crio.RentRead.Entity.Book;
import com.crio.RentRead.Entity.User;
import com.crio.RentRead.Entity.Stat.Status;
import com.crio.RentRead.Exceptions.BookAlreadyRentedException;
import com.crio.RentRead.Exceptions.BookNotRentedByUserException;
import com.crio.RentRead.Exceptions.RentedBooksOutOfBoundsException;
import com.crio.RentRead.Exceptions.ResourceNotFoundException;
import com.crio.RentRead.Repository.BookRepository;
import com.crio.RentRead.Repository.UserRepository;

@Service
public class RentalService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @Transactional
    public Book rentBook(Long bookId,String useremail){
        User user = userRepository.findByEmail(useremail);
        Book book = bookRepository.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book not found with the given id"+bookId));
        if (book.getAvailabilityStatus() == Status.UNAVAILABLE) {
            throw new BookAlreadyRentedException("The book is already rented and unavailable for rental.");
        }
        if(user.getBooks().size()>=2){
            throw new RentedBooksOutOfBoundsException("More than 2 books not allowed for rental");
        }

        user.getBooks().add(book);
        book.setUser(user);
        book.setAvailabilityStatus(Status.UNAVAILABLE);

        userRepository.save(user);
        bookRepository.save(book);

        return book;
    }

    @Transactional
    public Book returnBook(Long bookId,String useremail){
        User user = userRepository.findByEmail(useremail);
        Book book = bookRepository.findById(bookId).orElseThrow(()->new ResourceNotFoundException("Book not found with the given id"+bookId));
        
        if (!user.getBooks().contains(book)) {
            throw new BookNotRentedByUserException("The book is not rented by the user: " + useremail);
        }

        user.getBooks().remove(book);
        book.setAvailabilityStatus(Status.AVAILABLE);

        book.setUser(null);

        userRepository.save(user);
        bookRepository.save(book);

        return book;
    }
}
