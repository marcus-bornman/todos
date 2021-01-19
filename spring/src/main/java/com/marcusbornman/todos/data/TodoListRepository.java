package com.marcusbornman.todos.data;

import com.marcusbornman.todos.model.TodoList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@RepositoryRestResource
public interface TodoListRepository extends JpaRepository<TodoList, Long> {
    @Override
    @PreAuthorize("isFullyAuthenticated()")
    @Query("select todoList from TodoList todoList where user.username = ?#{principal.username}")
    Page<TodoList> findAll(Pageable pageable);

    @Override
    @PreAuthorize("isFullyAuthenticated() and #s.user.username == principal.username")
    <S extends TodoList> S save(S s);

    @Override
    @PreAuthorize("isFullyAuthenticated()")
    @PostAuthorize("!returnObject.empty and returnObject.get().user.username == principal.username")
    Optional<TodoList> findById(Long aLong);

    @Override
    @PreAuthorize("isFullyAuthenticated() and #todoList.user.username == principal.username")
    void delete(TodoList todoList);
}
