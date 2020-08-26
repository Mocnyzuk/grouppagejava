package com.grouppage.service.auth;

import com.grouppage.domain.entity.User;
import com.grouppage.domain.notmapped.Layout;
import com.grouppage.domain.notmapped.Token;
import com.grouppage.domain.repository.UserRepository;
import com.grouppage.domain.response.LoginRequest;
import com.grouppage.domain.response.RegisterRequest;
import com.grouppage.exception.UsernameAlreadyExists;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.security.jwt.JwtProvider;
import com.grouppage.service.ExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final ExecService execService;
    private final ApplicationEventPublisher eventPublisher;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       ExecService execService, ApplicationEventPublisher eventPublisher, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.execService = execService;
        this.eventPublisher = eventPublisher;
        this.jwtProvider = jwtProvider;
    }

    public ResponseEntity<User> signIn(LoginRequest loginRequest) throws ExecutionException, InterruptedException {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                loginRequest.getEmail(),
                                loginRequest.getPassword())
                );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        HttpHeaders headers = this.generateCookieWithNewTokens();
        return ResponseEntity.status(HttpStatus.ACCEPTED).headers(headers).body(
                this.userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(
                        () -> new UsernameNotFoundException("User with email: "+ loginRequest.getEmail()+ " doesnt exists!")
                )
        );
    }


    public void signUp(RegisterRequest registerRequest)throws UsernameAlreadyExists {
        if (!userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            User user = userRepository.save(registerRequest.toUser(this.passwordEncoder));
            //this.eventPublisher.publishEvent(new RegistrationEvent(user));
            return;
        }
        throw new UsernameAlreadyExists("This email is taken!");
    }

    public ResponseEntity<Void> signOut() {
        HttpHeaders removeCookiesHeader =
                this.jwtProvider.deleteJwtCookies();
        return ResponseEntity.noContent().headers(removeCookiesHeader).build();
    }

    public void resetPassword(String email){

    }

    /**
     * Class level logic
     */
    public User getUserFromContext()throws UsernameNotFoundException{
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
    private HttpHeaders generateCookieWithNewTokens() throws ExecutionException, InterruptedException {
        HttpHeaders headers = new HttpHeaders();
        User user = this.getUserFromContext();
        Future<Token> futureAcces = execService.executeCallable(
                () -> jwtProvider.generateToken(user, true)
        );
        Future<Token> futureRefresh = execService.executeCallable(
                () -> jwtProvider.generateToken(user, false)
        );
        headers.add(HttpHeaders.SET_COOKIE, jwtProvider.generateCookieFromToken(futureAcces.get()).toString());
        headers.add(HttpHeaders.SET_COOKIE, jwtProvider.generateCookieFromToken(futureRefresh.get()).toString());
        return headers;
    }

    public void activateAccount(String uuid) throws WrongDataPostedException{
        Optional<User> optional = this.userRepository.findByResetPasswordToken(uuid);
        if(optional.isPresent()){
            optional.get().setActivated(true);
            execService.executeRunnable(
                    () -> this.userRepository.save(optional.get())
            );
        }else{
            throw new WrongDataPostedException("Invalid activation token");
        }
    }

    public List<Layout> getLayouts() {
        return this.getUserFromContext().getLayout();
    }

    public void saveLayout(Layout layout) {
        User user = this.getUserFromContext();
        List<Layout> layouts = user.getLayout();
        if(layout != null){
            layouts.add(layout);
        }else{
            user.setLayout(new ArrayList<Layout>(){{add(layout);}});
        }
        this.userRepository.save(user);
    }

    public void saveLayouts(List<Layout> layouts) {
        User user = this.getUserFromContext();
        List<Layout> old = user.getLayout();
        if(old != null){
            old.addAll(layouts);
        }else{
            user.setLayout(layouts);
        }
        this.userRepository.save(user);
    }

    public void deleteLayout(String name) {
        User user = this.getUserFromContext();
        user.setLayout(user.getLayout().stream().filter(l -> !l.getName().equals(name)).collect(Collectors.toList()));
        this.userRepository.save(user);
    }
}

