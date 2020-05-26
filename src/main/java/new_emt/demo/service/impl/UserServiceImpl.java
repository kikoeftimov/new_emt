package new_emt.demo.service.impl;

import new_emt.demo.model.User;
import new_emt.demo.model.exceptions.UserNotFoundException;
import new_emt.demo.repository.UserRepository;
import new_emt.demo.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(String username) {
        return this.userRepository.findById(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteById(String username) {
        this.userRepository.deleteById(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.userRepository.findById(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));
    }
}
