package com.zerobase.mentalgrowhdiary.controller;

import com.zerobase.mentalgrowhdiary.dto.Auth;
import com.zerobase.mentalgrowhdiary.security.TokenProvider;
import com.zerobase.mentalgrowhdiary.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> singup(@RequestBody Auth.SignUp request){

        var result = this.userService.register(request);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request){

        var user = this.userService.authenticate(request);
        var token = this.tokenProvider.generateToken(user.getUserId(),user.getUsername(),user.getRole());


        return ResponseEntity.ok(token);
    }
}
