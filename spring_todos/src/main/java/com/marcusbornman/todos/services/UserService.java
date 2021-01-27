package com.marcusbornman.todos.services;

import com.marcusbornman.todos.entities.User;
import com.marcusbornman.todos.exceptions.UserNotFoundException;
import com.marcusbornman.todos.exceptions.UsernameExistsException;
import com.marcusbornman.todos.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public User loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository.findById(username)
        .orElseThrow(() -> new UsernameNotFoundException(
            "The user with the given username could not be found."));
  }

  /**
   * Save a new User.
   *
   * @param user the user entity object to create.
   * @return the user entity that has been created.
   * @throws UsernameExistsException if there is already a user that exists with the username of the
   *                                 new user entity.
   */
  public User create(User user) throws UsernameExistsException {
    User existingUser = userRepository.findById(user.getUsername()).orElse(null);

    if (existingUser != null) {
      throw new UsernameExistsException();
    }

    return userRepository.save(user);
  }

  /**
   * Get a saved USer.
   *
   * @param username the username of the user to be read.
   * @return the created user entity with the given username.
   * @throws UserNotFoundException if no user could be found with the given username.
   */
  public User read(String username) throws UserNotFoundException {
    return userRepository.findById(username).orElseThrow(UserNotFoundException::new);
  }

  /**
   * Update a saved user.
   *
   * @param username the username of the user to update.
   * @param user the user entity with the updated details.
   * @throws UserNotFoundException if no user could be found with the given username.
   */
  public void update(String username, User user) throws UserNotFoundException {
    User currentUser = read(username);

    user.setUsername(currentUser.getUsername());

    userRepository.save(user);
  }

  /**
   * Delete a saved user.
   *
   * @param username the username of the user to delete.
   * @throws UserNotFoundException if no user could be found with the given username.
   */
  public void delete(String username) throws UserNotFoundException {
    User user = read(username);

    userRepository.delete(user);
  }
}
