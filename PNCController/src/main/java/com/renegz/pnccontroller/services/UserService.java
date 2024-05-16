package com.renegz.pnccontroller.services;

import com.renegz.pnccontroller.domain.dtos.UserRegisterDTO;
import com.renegz.pnccontroller.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User findByIdentifier(String identifier);
    User findByUsernameOrEmail(String username, String email);
    void register(UserRegisterDTO userRegisterDTO);
    boolean checkPassword(User user, String password);
}
