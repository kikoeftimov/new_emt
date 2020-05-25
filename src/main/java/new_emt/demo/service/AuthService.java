package new_emt.demo.service;

import new_emt.demo.model.User;

public interface AuthService {

    User findUser();

    String getCurrentUsername();
}
