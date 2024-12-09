package com.dan.shoe.shoe.dtos.requests;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdatetionByAdmin {
    String name;
    String username;
    String email;
    String phoneNumber;
    String password;
    String rePassword;
    String role;
    Set<String> roles;
    MultipartFile avatar;
    boolean enabled;
}
