package com.dan.shoe.shoe.services;

import com.dan.shoe.shoe.dtos.requests.UpdateProfile;
import com.dan.shoe.shoe.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    User save(User user);
    User saveByAdmin(User user);
    Page<User> getAllUser(Pageable pageable);
    Page<User> searchUserByKeyword(String keyword, Pageable pageable);
    ResponseMessage changePassword(String username, ChangePasswordForm changePasswordForm);
    User updateUser(User user, String username);
    boolean isEnableUser(String username);
    boolean verify(String verificationCode);
    ResponseMessage updateProfile(UpdateProfile updateProfile, String username);
    void updateVerificationCode(String username, String verificationCode);
}
