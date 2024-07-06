package instrumental.kiwi.security.user.service;

import instrumental.kiwi.security.user.enums.Role;
import instrumental.kiwi.security.user.model.User;
import instrumental.kiwi.security.user.repository.UserRepository;
import instrumental.kiwi.security.user.request.UserRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createSytemUser(UserRequest userRequest, Role role) {

        User user = User.buildForSystemSource(userRequest, role);
        return userRepository.save(user);
    }

    public User createOAuth2User(String email, Role role) {

        User user = User.buildForOAuth2Source(email, role);
        return userRepository.save(user);
    }

    public Optional<User> getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }
}