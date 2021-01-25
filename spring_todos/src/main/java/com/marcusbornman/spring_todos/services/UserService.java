package com.marcusbornman.spring_todos.services;

import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.exceptions.UserNotFoundException;
import com.marcusbornman.spring_todos.exceptions.UsernameExistsException;
import com.marcusbornman.spring_todos.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user with the given username could not be found."));
    }

    public User create(User user) throws UsernameExistsException {
        User existingUser = userRepository.findById(user.getUsername()).orElse(null);

        if (existingUser != null) throw new UsernameExistsException();

        return userRepository.save(user);
    }

    public User read(String username) throws UserNotFoundException {
        return userRepository.findById(username).orElseThrow(UserNotFoundException::new);
    }

    public void update(String username, User user) throws UserNotFoundException {
        User currentUser = read(username);

        user.setUsername(currentUser.getUsername());

        userRepository.save(user);
    }

    public void delete(String username) throws UserNotFoundException {
        User user = read(username);

        userRepository.delete(user);
    }
}
