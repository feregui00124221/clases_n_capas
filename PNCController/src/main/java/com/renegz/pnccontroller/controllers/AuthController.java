package com.renegz.pnccontroller.controllers;

import com.renegz.pnccontroller.domain.dtos.*;
import com.renegz.pnccontroller.domain.entities.Role;
import com.renegz.pnccontroller.domain.entities.Token;
import com.renegz.pnccontroller.domain.entities.User;
import com.renegz.pnccontroller.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> login(@RequestBody @Valid UserLoginDTO info) {

        User user = userService.findUserByIdentifier(info.getIdentifier());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        if (userService.checkPassword(user, info.getPassword())) {
            return GeneralResponse.getResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        Token token = userService.registerToken(user);

        return GeneralResponse.getResponse(HttpStatus.OK, "Login exitoso", new TokenDTO(token));
    }

    @GetMapping("/whoami")
    public ResponseEntity<GeneralResponse> whoami(@RequestBody @Valid UserLoginDTO info) {

        User user = userService.findUserByIdentifier(info.getIdentifier());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        if (userService.checkPassword(user, info.getPassword())) {
            return GeneralResponse.getResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        WhoamiDTO whoamiDTO = new WhoamiDTO();
        whoamiDTO.setUsername(user.getUsername());
        whoamiDTO.setEmail(user.getEmail());
        whoamiDTO.setRoles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toList()));

        return GeneralResponse.getResponse(HttpStatus.OK, "Login exitoso", whoamiDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> register(@RequestBody @Valid UserRegisterDTO info) {
        User user = userService.findByUsernameOrEmail(info.getUsername(), info.getEmail());

        if (user != null) {
            return GeneralResponse.getResponse(HttpStatus.CONFLICT, "User already exists");
        }

        userService.register(info);

        return GeneralResponse.getResponse(HttpStatus.CREATED, "User registered successfully");
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> getAllUsers() {
        List<UserResponseDTO> users = userService.findAll();

        if (users.isEmpty()) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "No users found");
        }

        return GeneralResponse.getResponse(HttpStatus.OK, userService.findAll());
    }

    @PostMapping("/update")
    public ResponseEntity<GeneralResponse> updateUser(@RequestBody @Valid ChangePasswordDTO info) {
        User user = userService.findUserByIdentifier(info.getIdentifier());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        if (userService.checkPassword(user, info.getOldPassword())) {
            return GeneralResponse.getResponse(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        userService.changePassword(info);

        return GeneralResponse.getResponse(HttpStatus.OK, "User updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<GeneralResponse> deleteUser(@PathVariable UUID id) {
        if (userService.findByUUID(id) == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        userService.deleteUser(id);

        return GeneralResponse.getResponse(HttpStatus.OK, "User deleted successfully");
    }
}
