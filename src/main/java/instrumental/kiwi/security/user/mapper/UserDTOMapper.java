package instrumental.kiwi.security.user.mapper;

import instrumental.kiwi.security.user.dto.UserDTO;
import instrumental.kiwi.security.user.model.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class UserDTOMapper implements Function<User, UserDTO> {

    @Override
    public UserDTO apply(User user) {

        return new UserDTO(
                user.getUserId(),
                user.getEmail(),
                user.getRole()
        );
    }
}