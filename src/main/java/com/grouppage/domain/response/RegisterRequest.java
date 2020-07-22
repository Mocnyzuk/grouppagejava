package com.grouppage.domain.response;

import com.grouppage.domain.entity.User;
import com.grouppage.exception.WrongCredentialsException;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;

@Data
public class RegisterRequest {

    @Email
    private String email;
    @Size(min = 9)
    private String phone;
    @Size(min = 8, max = 16)
    private String password;
    @Size(min = 8, max = 16)
    private String repassword;

    public User toUser(PasswordEncoder encoder)throws WrongCredentialsException {
        if (this.password != this.repassword) {
            throw new WrongCredentialsException("Password is not the same");
        }
        User user = new User();
        user.setEmail(this.email);
        user.setActivated(true);
        user.setAuthorities(Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")));
        user.setPhone(this.phone);
        user.setPassword(encoder.encode(this.password));
        user.setLastOnline(Instant.now());
        return user;
    }

}
