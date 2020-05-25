package new_emt.demo.service;


import new_emt.demo.model.User;

public interface UserService {

    User findById(String username);

    User save(User user);

    void deleteById(String username);
}
