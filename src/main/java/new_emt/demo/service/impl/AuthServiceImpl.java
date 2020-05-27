package new_emt.demo.service.impl;

import new_emt.demo.model.Role;
import new_emt.demo.model.User;
import new_emt.demo.model.exceptions.PasswordDoesntMatchException;
import new_emt.demo.repository.RoleRepository;
import new_emt.demo.repository.UserRepository;
import new_emt.demo.service.AuthService;
import new_emt.demo.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserService userService;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userService = userService;
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

    @Override
    public User signUpUser(String username, String password, String repeatedPassword) {
        if(!password.equals(repeatedPassword)){
            throw new PasswordDoesntMatchException();
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        Role userRole = this.roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(userRole));
        return this.userService.registerUser(user);
    }

    @PostConstruct
    public void init(){
        if(!this.userRepository.existsById("admin")){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(this.roleRepository.findAll());
            this.userRepository.save(admin);
        }
    }
}
