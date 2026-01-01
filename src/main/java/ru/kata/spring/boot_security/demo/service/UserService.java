package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    public List<User> findAll();
    public User findById(Long id);
    public User save(User user);
    public void deleteById(Long id);
    public void saveUser(User user,
                         List<Long> roleIds,
                         String rawPassword);
}
