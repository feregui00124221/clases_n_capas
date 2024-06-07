package com.renegz.pnccontroller.services.implementations;

import com.renegz.pnccontroller.domain.dtos.ChangePasswordDTO;
import com.renegz.pnccontroller.domain.dtos.UserRegisterDTO;
import com.renegz.pnccontroller.domain.dtos.UserResponseDTO;
import com.renegz.pnccontroller.domain.entities.Role;
import com.renegz.pnccontroller.domain.entities.Token;
import com.renegz.pnccontroller.domain.entities.User;
import com.renegz.pnccontroller.repositories.RoleRepository;
import com.renegz.pnccontroller.repositories.TokenRepository;
import com.renegz.pnccontroller.repositories.UserRepository;
import com.renegz.pnccontroller.services.UserService;
import com.renegz.pnccontroller.security.JWTTools;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final JWTTools jwtTools;

    private final TokenRepository tokenRepository;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, JWTTools jwtTools, TokenRepository tokenRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtTools = jwtTools;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User findUserByIdentifier(String identifier) {
        return this.findByUsernameOrEmail(identifier, identifier);
    }

    @Override
    public User findByUsernameOrEmail(String username, String email) {
        return userRepository
                .findByActiveIsTrueAndUsernameOrEmail(username, email)
                .orElse(null);
    }

    @Override
    public User findByUUID(UUID uuid) {
        return userRepository
                .findByUserIdAndActiveIsTrue(uuid).orElse(null);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDTO(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void register(UserRegisterDTO UserInfo) {
        User user = new User();

        List<Role> roles = findRolesByIdentifiers(List.of("USER"));
        user.setUsername(UserInfo.getUsername());
        user.setEmail(UserInfo.getEmail());
        user.setPassword(UserInfo.getPassword());
        user.setActive(true);
        user.setRoles(roles);


        userRepository.save(user);
    }

    private Role findRoleByIdentifier(String identifier) {
        return roleRepository.findById(identifier).orElse(null);
    }

    private List<Role> findRolesByIdentifiers(List<String> identifiers) {
        return roleRepository.findAllById(identifiers
                .stream()
                .map(String::toUpperCase)
                .collect(Collectors.toList()));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void changePassword(ChangePasswordDTO info) {
        User user = findUserByIdentifier(info.getIdentifier());

        if (user != null) {
            user.setPassword(info.getNewPassword());
            userRepository.save(user);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    @Secured({"ROLE_ADMIN", "ROLE_SUPER_ADMIN"})
    public void deleteUser(UUID uuid) {
        User user = findByUUID(uuid);

        if (user != null) {
            user.setActive(false);
            userRepository.save(user);
        }
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return !user.getPassword().equals(password);
    }

    //  TOKEN SERVICES

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Token registerToken(User user) {
        cleanTokens(user);

        String tokenString = jwtTools.generateToken(user);
        Token token = new Token(tokenString, user);

        tokenRepository.save(token);

        return token;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Boolean isTokenValid(User user, String token) {

        cleanTokens(user);
        List<Token> tokens = tokenRepository.findByUserAndActive(user, true);

        tokens.stream()
                .filter(tk -> tk.getContent().equals(token))
                .findAny()
                .ifPresent(tk -> {
                    tk.setActive(false);
                    tokenRepository.save(tk);
                });

        return tokens.stream().anyMatch(tk -> tk.getContent().equals(token));
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void cleanTokens(User user) {
        List<Token> tokens = tokenRepository.findByUserAndActive(user, true);

        tokens.forEach(token -> {
            if (!jwtTools.verifyToken(token.getContent())) {
                token.setActive(false);
                tokenRepository.save(token);
            }
        });
    }

    @Override
    public User findUserAuthenticated() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByActiveIsTrueAndUsernameOrEmail(username, username).orElse(null);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void changeRoles(User user, List<String> roles) {
        List<Role> rolesList = findRolesByIdentifiers(roles);

        user.setRoles(rolesList);
        userRepository.save(user);
    }
}
