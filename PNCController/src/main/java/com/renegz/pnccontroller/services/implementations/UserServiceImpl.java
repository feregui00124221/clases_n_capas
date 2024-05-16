package com.renegz.pnccontroller.services.implementations;

import com.renegz.pnccontroller.domain.dtos.ChangePasswordDTO;
import com.renegz.pnccontroller.domain.dtos.UserRegisterDTO;
import com.renegz.pnccontroller.domain.dtos.UserResponseDTO;
import com.renegz.pnccontroller.domain.entities.User;
import com.renegz.pnccontroller.repositories.UserRepository;
import com.renegz.pnccontroller.services.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByIdentifier(String identifier) {
        return userRepository.findByUsernameOrEmail(identifier, identifier).orElse(null);
    }

    @Override
    public User findByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email).orElse(null);
    }

    @Override
    public User findByUUID(UUID uuid) {
        return userRepository.findById(uuid).orElse(null);
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponseDTO(user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void register(UserRegisterDTO UserInfo) {
        User user = new User();

        user.setUsername(UserInfo.getUsername());
        user.setEmail(UserInfo.getEmail());
        user.setPassword(UserInfo.getPassword());

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void changePassword(ChangePasswordDTO info) {
        User user = findByIdentifier(info.getIdentifier());

        if (user != null) {
            user.setPassword(info.getNewPassword());
            userRepository.save(user);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid);
    }

    @Override
    public boolean checkPassword(User user, String password) {
        return !user.getPassword().equals(password);
    }
}
