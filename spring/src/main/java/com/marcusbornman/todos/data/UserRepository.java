package com.marcusbornman.todos.data;

import com.marcusbornman.todos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, String> {
    @Override
    @RestResource(exported = false)
    List<User> findAll();

    @Override
    @RestResource(exported = false)
    List<User> findAll(Sort sort);

    @Override
    @RestResource(exported = false)
    Page<User> findAll(Pageable pageable);

    @Override
    @PreAuthorize("(isFullyAuthenticated() and #s.username == principal.username) " +
            "or #s.creationDate == null")
    <S extends User> S save(S s);

    @Override
    @PreAuthorize("isFullyAuthenticated() and #username == principal.username")
    Optional<User> findById(String username);

    @Override
    @PreAuthorize("isFullyAuthenticated() and #user.username == principal.username")
    void delete(User user);

    @RestResource(exported = false)
    Optional<User> findByUsername(String username);
}
