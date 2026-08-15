package com.example.usermanagement.service;

import com.example.usermanagement.exception.BadRequestException;
import com.example.usermanagement.exception.ResourceNotFoundException;
import com.example.usermanagement.model.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(0);

    public Collection<User> findAll() {
        return users.values();
    }

    public User findById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return user;
    }

    public User create(User user) {
        if (user.getId() != null) {
            throw new BadRequestException("New user must not have an id");
        }
        user.setId(idGenerator.incrementAndGet());
        users.put(user.getId(), user);
        return user;
    }

    public User update(Long id, User updatedUser) {
        User existing = findById(id);

        existing.setName(updatedUser.getName());
        existing.setUsername(updatedUser.getUsername());
        existing.setEmail(updatedUser.getEmail());
        existing.setAddress(updatedUser.getAddress());
        existing.setPhone(updatedUser.getPhone());
        existing.setWebsite(updatedUser.getWebsite());
        existing.setCompany(updatedUser.getCompany());
        
        return existing;
    }

    public void delete(Long id) {
        User existing = findById(id);
        users.remove(existing.getId());
    }
}
