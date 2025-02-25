package com.netzwerk.ecommerce.service.user;

import com.netzwerk.ecommerce.dto.UserDto;
import com.netzwerk.ecommerce.entity.UserEntity;
import com.netzwerk.ecommerce.repository.UserRepository;
import com.netzwerk.ecommerce.util.ServiceUtil;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    public UserServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String register(UserDto userDto) {
        if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            return "User already exists";
        }
        userRepository.save(Objects.requireNonNull(ServiceUtil.convertUserDtoToEntity(userDto)));
        return "Registration successful";
    }

    @Override
    public String login(UserDto userDto) {
        Optional<UserEntity> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            return "User does not exist";
        }
        if (!user.get().getPassword().equals(userDto.getPassword())) {
            return "Email or password incorrect";
        }
        String loggedInUsername = user.get().getUsername();
        return "Welcome " + loggedInUsername;
    }

    @Override
    public String getLoggedInUsername(UserDto userDto) {
        return userRepository.findByEmail(userDto.getEmail()).map(UserEntity::getUsername).orElse(null);
    }

    @Override
    public UserDto getUserDetails(String userEmail) {
      Optional<UserEntity>  entity = userRepository.findByEmail(userEmail);
        return entity.map(ServiceUtil::convertUserEntityToDto).orElse(null);
    }
}
