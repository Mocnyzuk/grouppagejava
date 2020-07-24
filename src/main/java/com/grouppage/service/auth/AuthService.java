package com.grouppage.service.auth;

import com.grouppage.domain.entity.User;
import com.grouppage.domain.notmapped.Token;
import com.grouppage.domain.repository.UserRepository;
import com.grouppage.domain.response.LoginRequest;
import com.grouppage.domain.response.RegisterRequest;
import com.grouppage.exception.UsernameAlreadyExists;
import com.grouppage.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity<Void> signIn(LoginRequest loginRequest) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpHeaders headers = this.generateCookieWithNewTokens();
        return ResponseEntity.accepted().headers(headers).build();
    }


    public void signUp(RegisterRequest registerRequest)throws UsernameAlreadyExists {
        if (!userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            User user = userRepository.save(registerRequest.toUser(this.passwordEncoder));
        }
        throw new UsernameAlreadyExists("This email is taken!");
    }

    public ResponseEntity<Void> signOut() {
        HttpHeaders removeCookiesHeader =
                this.jwtProvider.deleteJwtCookies();
        return ResponseEntity.noContent().headers(removeCookiesHeader).build();
    }

    /**
     * Class level logic
     */
    private User getUserFromContext()throws UsernameNotFoundException{
        Object objectPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;
        if(objectPrincipal instanceof UserDetails) {
            email = ((UserDetails) objectPrincipal).getUsername();
        }else {
            email = objectPrincipal.toString();
        }
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email "+ email+" doesnt exists!"));
    }
    private HttpHeaders generateCookieWithNewTokens(){
        HttpHeaders headers = new HttpHeaders();
        User user = this.getUserFromContext();
        Token access = jwtProvider.generateToken(user, true);
        Token refresh = jwtProvider.generateToken(user, false);
        headers.add(HttpHeaders.SET_COOKIE, jwtProvider.generateCookieFromToken(access).toString());
        headers.add(HttpHeaders.SET_COOKIE, jwtProvider.generateCookieFromToken(refresh).toString());
        return headers;
    }
}

