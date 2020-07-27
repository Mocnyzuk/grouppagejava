package com.grouppage.web.auth;

import com.grouppage.domain.response.LoginRequest;
import com.grouppage.domain.response.RegisterRequest;
import com.grouppage.exception.UsernameAlreadyExists;
import com.grouppage.exception.WrongCredentialsException;
import com.grouppage.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest loginRequest
    )throws WrongCredentialsException  {
        return authService.signIn(loginRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody RegisterRequest registerRequest
    )throws WrongCredentialsException, UsernameAlreadyExists {
        authService.signUp(registerRequest);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/logout")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        return authService.signOut();

    }

    @GetMapping("/api/actiate")
    public ResponseEntity<Void> activateAccount(
            @RequestParam("id") String uuid
    ){
        this.authService.activateAccount(uuid);
        return ResponseEntity.accepted().build();
    }
}
