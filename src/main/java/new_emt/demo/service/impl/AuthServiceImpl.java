package new_emt.demo.service.impl;

import new_emt.demo.model.User;
import new_emt.demo.repository.UserRepository;
import new_emt.demo.service.AuthService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findUser() {
//        return this.userRepository.findById("current-user").
//                orElseGet(() -> {
//                    User user = new User();
//                    user.setUsername("current-user");
//                    return this.userRepository.save(user);
//                });

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUsername() {
        return this.findUser().getUsername();
    }
}
