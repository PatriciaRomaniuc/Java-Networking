package persistance;

import domain.User;

import java.util.List;

public interface IRepositoryUsers {
    public void save(User user);
    public void delete(String username);
    public void update(User entity);
    public User findOne(String username);
    public List<User> findAll();
}
