package com.renegz.pnccontroller.controllers;

import com.renegz.pnccontroller.domain.dtos.ChangeRolesDTO;
import com.renegz.pnccontroller.domain.dtos.GeneralResponse;
import com.renegz.pnccontroller.domain.entities.User;
import com.renegz.pnccontroller.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/change-roles")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<GeneralResponse> changeRoles(@RequestBody @Valid ChangeRolesDTO info) {

        User user = userService.findUserByIdentifier(info.getIdentifier());

        if (user == null) {
            return GeneralResponse.getResponse(HttpStatus.NOT_FOUND, "User not found");
        }

        userService.changeRoles(user, info.getRoles());

        return GeneralResponse.getResponse(HttpStatus.OK, "Roles changed successfully");
    }
}
