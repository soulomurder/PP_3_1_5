package ex.pp_3_1_5.service;

import ex.pp_3_1_5.model.Role;
import ex.pp_3_1_5.model.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User findOne(Long id);

    void save(User user);

    void update(Long id, User updatedUser);

    void delete(Long id);

    List<Role> getRoles();
}
