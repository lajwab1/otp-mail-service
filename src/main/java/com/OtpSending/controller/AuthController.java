package com.OtpSending.controller;

import com.OtpSending.Dto.UserDTO;
import com.OtpSending.Service.UserService;
import com.OtpSending.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    UserService userService;
@PostMapping("/save")
    public ResponseEntity<User>registerUser(@RequestBody UserDTO userDTO){
        User user = userService.registerUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);

    }
@PostMapping("/otpVerified")
    public ResponseEntity<?>otpVerified(@RequestParam String email,@RequestParam int otp){
        String res = userService.otpVerified(email, otp);
     return new ResponseEntity<>(res,HttpStatus.OK);
    }


}
