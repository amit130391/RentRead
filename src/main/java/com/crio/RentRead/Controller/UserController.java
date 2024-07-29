package com.crio.RentRead.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crio.RentRead.Entity.User;
import com.crio.RentRead.Service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/user/me")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetails userDetails){
        User user = userService.getUser(userDetails.getUsername());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    
}
