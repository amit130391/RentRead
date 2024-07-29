package com.crio.RentRead.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crio.RentRead.Entity.Book;

public interface BookRepository extends JpaRepository<Book,Long> {
    
}
