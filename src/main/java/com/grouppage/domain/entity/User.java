package com.grouppage.domain.entity;

import com.grouppage.domain.converter.AuthoritiesConverter;
import com.sun.corba.se.spi.ior.IdentifiableFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractEntityDate{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    // TODO insert pattern for phone number
    //@Pattern(regexp = "")
    @Nullable
    private String phone;

    // TODO patter for secure pass
    @NotNull
    //@Pattern(regexp = "")
    private String password;

    @Column(columnDefinition = "boolean default false")
    private boolean isActivated;

    @Column(columnDefinition = "boolean default false")
    private boolean isDeleted;

    @Nullable
    private String resetPasswordToken;


    @Convert(converter = AuthoritiesConverter.class)
    private List<SimpleGrantedAuthority> authorities;

    @NotNull
    private Instant lastOnline;
}
