package com.renegz.pnccontroller.services;

import com.renegz.pnccontroller.domain.dtos.ChangePasswordDTO;
import com.renegz.pnccontroller.domain.dtos.UserRegisterDTO;
import com.renegz.pnccontroller.domain.dtos.UserResponseDTO;
import com.renegz.pnccontroller.domain.entities.Token;
import com.renegz.pnccontroller.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User findUserByIdentifier(String identifier);

    User findByUsernameOrEmail(String username, String email);

    User findByUUID(UUID uuid);

    List<UserResponseDTO> findAll();

    void register(UserRegisterDTO userRegisterDTO);

    void changePassword(ChangePasswordDTO info);

    void deleteUser(UUID uuid);

    boolean checkPassword(User user, String password);

    //Token management
    Token registerToken(User user);
    Boolean isTokenValid(User user, String token);
    void cleanTokens(User user);

    //Find User authenticated
    User findUserAuthenticated();
}
