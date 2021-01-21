package com.marcusbornman.todos.data;

import com.marcusbornman.todos.model.Todo;
import com.marcusbornman.todos.model.TodoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RepositoryRestResource
public interface TodoRepository extends JpaRepository<Todo, Long> {
    @Override
    @PreAuthorize("isFullyAuthenticated()")
    @Query("select todo from Todo todo where todoList.user.username = ?#{principal.username}")
    Page<Todo> findAll(Pageable pageable);

    @Override
    @PreAuthorize("isFullyAuthenticated() and #s.todoList.user.username == principal.username")
    <S extends Todo> S save(S s);

    @Override
    @PreAuthorize("isFullyAuthenticated()")
    @PostAuthorize("!returnObject.empty and returnObject.get().todoList.user.username == principal.username")
    Optional<Todo> findById(Long aLong);

    @Override
    @PreAuthorize("isFullyAuthenticated() and #todo.todoList.user.username == principal.username")
    void delete(Todo todo);

    /**
     * Saves entity without security checks.
     */
    @RestResource(exported = false)
    @Transactional
    @Modifying
    default <S extends Todo> S saveInternal(final S entity) {
        return save(entity);
    }
}
