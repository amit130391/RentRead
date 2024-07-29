package com.crio.RentRead.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.RentRead.Entity.User;
import com.crio.RentRead.Service.UserService;

@RestController
public class AdminController {
    
    @Autowired
    UserService userService;
 
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getallUsers(){
        List<User> getallUsers = userService.getallUsers();
        if(getallUsers.isEmpty())
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        
        return new ResponseEntity<List<User>>(getallUsers, HttpStatus.OK);
    }

}
