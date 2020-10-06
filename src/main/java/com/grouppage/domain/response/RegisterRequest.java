package com.grouppage.domain.response;

import com.grouppage.domain.entity.User;
import com.grouppage.exception.WrongCredentialsException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
        if (!this.password.equals(this.repassword)) {
            throw new WrongCredentialsException("Password is not the same");
        }
        User user = new User();
        user.setEmail(this.email);
        user.setResetPasswordToken(UUID.randomUUID().toString());
        user.setActivated(true);
        user.setAuthorities(Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")));
        user.setPhone(this.phone);
        user.setPassword(encoder.encode(this.password));
        user.setLastOnline(Instant.now());
        return user;
    }

}
