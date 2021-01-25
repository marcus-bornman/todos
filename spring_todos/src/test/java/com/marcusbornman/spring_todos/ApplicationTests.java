package com.marcusbornman.spring_todos;

import com.marcusbornman.spring_todos.entities.Todo;
import com.marcusbornman.spring_todos.entities.TodoList;
import com.marcusbornman.spring_todos.entities.User;
import com.marcusbornman.spring_todos.repositories.TodoListRepository;
import com.marcusbornman.spring_todos.repositories.TodoRepository;
import com.marcusbornman.spring_todos.repositories.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureTestDatabase
public class ApplicationTests {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoListRepository todoListRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    //region User - CREATE

    @Test
    public void givenNewUser_whenValidUserIsPosted_thenUserIsCreated() throws Exception {
        // Given
        String newUserJson = "{\"username\":\"John\",\"password\":\"password123\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson));

        // Then
        resultActions.andExpect(status().isCreated());
        assertNotNull(userRepository.findById("John").orElse(null));
    }

    @Test
    public void givenNewUser_whenInvalidUserIsPosted_thenBadRequest() throws Exception {
        // Given
        String newUserJson = "{\"username\":\"\",\"password\":\"\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson));

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void givenExistingUser_whenUserIsPosted_thenConflictResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "password123"));
        String newUserJson = "{\"username\":\"John\",\"password\":\"john123\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson));

        // Then
        resultActions.andExpect(status().isConflict());
    }

    //endregion

    //region User - READ

    @Test
    public void givenExistingUser_whenUserIsReadByAuthorized_thenUserDetailsReturned() throws Exception {
        // Given
        userRepository.save(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.username").exists());
        resultActions.andExpect(jsonPath("$.password").doesNotExist());
        resultActions.andExpect(jsonPath("$.creationDate").exists());
        resultActions.andExpect(jsonPath("$.expiryDate").exists());
        resultActions.andExpect(jsonPath("$.locked").exists());
        resultActions.andExpect(jsonPath("$.credentialsExpiryDate").exists());
        resultActions.andExpect(jsonPath("$.disabled").exists());
    }

    @Test
    public void givenExistingUser_whenUserIsReadByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenExistingUser_whenUserIsReadByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "password123"));
        userRepository.save(testUser("Jane", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John")
                .with(httpBasic("Jane", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region User - UPDATE

    @Test
    public void givenExistingUser_whenUsersIsUpdatedByAuthorized_thenUserIsUpdated() throws Exception {
        // Given
        User originalUser = userRepository.save(testUser("John", "password123"));
        String editedUserJson = "{\"username\":\"John\",\"password\":\"john123\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/users/John")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(editedUserJson));

        // Then
        resultActions.andExpect(status().isNoContent());
        User updatedUser = userRepository.findById("John").orElseThrow();
        assertNotEquals(originalUser.getPassword(), updatedUser.getPassword());
    }

    @Test
    public void givenExistingUser_whenUsersIsUpdatedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "password123"));
        userRepository.save(testUser("Jane", "password123"));
        String editedUserJson = "{\"username\":\"John\",\"password\":\"john123\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/users/John")
                .with(httpBasic("Jane", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(editedUserJson));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region User - DELETE

    @Test
    public void givenExistingUser_whenUsersIsDeletedByAuthorized_thenNoContentResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John")
                .with(httpBasic("John", "password123")));

        // Then
        resultActions.andExpect(status().isNoContent());
        assertNull(userRepository.findById("John").orElse(null));
    }

    @Test
    public void givenExistingUser_whenUsersIsDeletedByUnAuthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John"));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenExistingUser_whenUsersIsDeletedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "password123"));
        userRepository.save(testUser("Jane", "password123"));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John")
                .with(httpBasic("Jane", "password123")));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region TodoList - CREATE

    @Test
    public void givenValidTodoList_whenTodoListIsPostedByAuthenticated_thenTodoListCreated() throws Exception {
        // Given
        userRepository.save(testUser("John", "john123"));
        String newTodoList = "{\"title\":\"University\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users/John/todoLists")
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoList));

        // Then
        resultActions.andExpect(status().isCreated());
        List<TodoList> todoLists = todoListRepository.findAll();
        assertEquals(1, todoLists.size());
        assertEquals("University", todoLists.get(0).getTitle());
        assertEquals("John", todoLists.get(0).getUser().getUsername());
    }

    @Test
    public void givenInalidTodoList_whenTodoListIsPostedByAuthenticated_thenBadRequestResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "john123"));
        String newTodoList = "{\"title\":\"\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users/John/todoLists")
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoList));

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void givenNewTodoList_whenTodoListIsPostedByUnauthenticated_thenUnauthorizedResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "john123"));
        String newTodoList = "{\"title\":\"University\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users/John/todoLists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoList));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenNewTodoList_whenTodoListIsPostedByDifferentUser_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("John", "john123"));
        userRepository.save(testUser("Jane", "jane123"));
        String newTodoList = "{\"title\":\"University\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users/John/todoLists")
                .with(httpBasic("Jane", "jane123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoList));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region TodoList - READ
    @Test
    public void givenTodoLists_whenAllListsIsReadByAuthorized_thenRelevantListsReturned() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        User jane = userRepository.save(testUser("Jane", "password123"));
        todoListRepository.save(testTodoList("Personal", john));
        todoListRepository.save(testTodoList("University", john));
        todoListRepository.save(testTodoList("Family", jane));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John/todoLists")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(jsonPath("$.[?(@.title == \"Personal\")]").exists())
                .andExpect(jsonPath("$.[?(@.title == \"University\")]").exists())
                .andExpect(jsonPath("$.[?(@.title == \"Family\")]").doesNotExist());
    }

    @Test
    public void givenTodoLists_whenAllListsIsReadByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        User jane = userRepository.save(testUser("Jane", "password123"));
        todoListRepository.save(testTodoList("Personal", john));
        todoListRepository.save(testTodoList("University", john));
        todoListRepository.save(testTodoList("Family", jane));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John/todoLists")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenTodoLists_whenAllListsIsReadByDifferentUser_thenForbiddenResponse() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        User jane = userRepository.save(testUser("Jane", "password123"));
        todoListRepository.save(testTodoList("Personal", john));
        todoListRepository.save(testTodoList("University", john));
        todoListRepository.save(testTodoList("Family", jane));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John/todoLists")
                .with(httpBasic("Jane", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region TodoList - UPDATE

    @Test
    public void givenExistingList_whenListIsUpdatedByAuthorized_thenTodoListUpdated() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "john123"));
        TodoList originalList = todoListRepository.save(testTodoList("University", john));
        String todoListJson = "{\"title\":\"School\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/users/John/todoLists/" + originalList.getId())
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoListJson));

        // Then
        resultActions.andExpect(status().isNoContent());
        TodoList editedList = todoListRepository.findAll().get(0);
        assertEquals("School", editedList.getTitle());
    }

    @Test
    public void givenExistingList_whenListIsUpdatedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("Jane", "jane123"));
        User john = userRepository.save(testUser("John", "john123"));
        TodoList originalList = todoListRepository.save(testTodoList("University", john));
        String todoListJson = "{\"title\":\"School\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/users/John/todoLists/" + originalList.getId())
                .with(httpBasic("Jane", "jane123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoListJson));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region TodoList - DELETE

    @Test
    public void givenExistingList_whenListIsDeletedByAuthorized_thenTodoListDeleted() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "john123"));
        TodoList originalList = todoListRepository.save(testTodoList("University", john));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John/todoLists/" + originalList.getId())
                .with(httpBasic("John", "john123")));

        // Then
        resultActions.andExpect(status().isNoContent());
        assertFalse(todoListRepository.existsById(originalList.getId()));
    }

    @Test
    public void givenExistingList_whenListIsDeletedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("Jane", "jane123"));
        User john = userRepository.save(testUser("John", "john123"));
        TodoList originalList = todoListRepository.save(testTodoList("University", john));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John/todoLists/" + originalList.getId())
                .with(httpBasic("Jane", "jane123")));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region Todo - CREATE

    @Test
    public void givenValidTodo_whenTodoIsPostedByAuthenticated_thenTodoCreated() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "john123"));
        TodoList todoList = todoListRepository.save(testTodoList("University", john));
        String newTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users/John/todoLists/" + todoList.getId() + "/todos")
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson));

        // Then
        resultActions.andExpect(status().isCreated());
        List<Todo> todos = todoRepository.findAll();
        assertEquals("University", todos.get(0).getTodoList().getTitle());
        assertEquals("Register", todos.get(0).getTitle());
        assertEquals("Register Online", todos.get(0).getDescription());
    }

    @Test
    public void givenInvalidTodo_whenTodoIsPostedByAuthenticated_thenBadRequestResponse() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "john123"));
        TodoList todoList = todoListRepository.save(testTodoList("University", john));
        String newTodoJson = "{\"title\":\"\",\"description\":\"Register Online\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users/John/todoLists/" + todoList.getId() + "/todos")
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson));

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void givenValidTodo_whenTodoIsPostedByUnauthenticated_thenUnauthorizedResponse() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "john123"));
        TodoList todoList = todoListRepository.save(testTodoList("University", john));
        String newTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users/John/todoLists/" + todoList.getId() + "/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenValidTodo_whenTodoIsPostedByDifferentUser_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("Jane", "jane123"));
        User john = userRepository.save(testUser("John", "john123"));
        TodoList todoList = todoListRepository.save(testTodoList("University", john));
        String newTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users/John/todoLists/" + todoList.getId() + "/todos")
                .with(httpBasic("Jane", "jane123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region Todo - READ

    @Test
    public void givenTodos_whenAllTodosIsReadByAuthorized_thenRelevantListReturned() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        User jane = userRepository.save(testUser("Jane", "password123"));
        TodoList johnList = todoListRepository.save(testTodoList("Personal", john));
        TodoList janeList = todoListRepository.save(testTodoList("Personal", jane));
        todoRepository.save(testTodo("John's Todo #1", "Get done first", johnList));
        todoRepository.save(testTodo("John's Todo #2", "Get done first", johnList));
        todoRepository.save(testTodo("Jane's Only Todo", "Get done ASAP", janeList));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John/todoLists/" + johnList.getId() + "/todos")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$.[?(@.title == \"John's Todo #1\")]").exists())
                .andExpect(jsonPath("$.[?(@.title == \"John's Todo #2\")]").exists())
                .andExpect(jsonPath("$.[?(@.title == \"Jane's Only Todo\")]").doesNotExist());
    }

    @Test
    public void givenTodos_whenAllTodosIsReadByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        User jane = userRepository.save(testUser("Jane", "password123"));
        TodoList johnList = todoListRepository.save(testTodoList("Personal", john));
        TodoList janeList = todoListRepository.save(testTodoList("Personal", jane));
        todoRepository.save(testTodo("John's Todo #1", "Get done first", johnList));
        todoRepository.save(testTodo("John's Todo #2", "Get done first", johnList));
        todoRepository.save(testTodo("Jane's Only Todo", "Get done ASAP", janeList));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John/todoLists/" + johnList.getId() + "/todos")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenTodos_whenAllTodosIsReadByDifferentUser_thenForbiddenResponse() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        User jane = userRepository.save(testUser("Jane", "password123"));
        TodoList johnList = todoListRepository.save(testTodoList("Personal", john));
        TodoList janeList = todoListRepository.save(testTodoList("Personal", jane));
        todoRepository.save(testTodo("John's Todo #1", "Get done first", johnList));
        todoRepository.save(testTodo("John's Todo #2", "Get done first", johnList));
        todoRepository.save(testTodo("Jane's Only Todo", "Get done ASAP", janeList));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John/todoLists/" + johnList.getId() + "/todos")
                .with(httpBasic("Jane", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region Todo - UPDATE

    @Test
    public void givenExistingTodo_whenTodoIsUpdatedByAuthorized_thenTodoUpdated() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        TodoList todoList = todoListRepository.save(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.save(testTodo("John's Todo", "Get done ASAP", todoList));
        String updatedTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"user\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/users/John/todoLists/" + todoList.getId() + "/todos/" + originalTodo.getId())
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTodoJson));

        // Then
        resultActions.andExpect(status().isNoContent());
        Todo todo = todoRepository.findAll().get(0);
        assertEquals("Register", todo.getTitle());
        assertEquals("Register Online", todo.getDescription());
    }

    @Test
    public void givenExistingTodo_whenTodoIsUpdatedByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        TodoList todoList = todoListRepository.save(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.save(testTodo("John's Todo", "Get done ASAP", todoList));
        String updatedTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"user\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/users/John/todoLists/" + todoList.getId() + "/todos/" + originalTodo.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTodoJson));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenExistingTodo_whenTodoIsUpdatedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("Jane", "jane123"));
        User john = userRepository.save(testUser("John", "password123"));
        TodoList todoList = todoListRepository.save(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.save(testTodo("John's Todo", "Get done ASAP", todoList));
        String updatedTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"user\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/users/John/todoLists/" + todoList.getId() + "/todos/" + originalTodo.getId())
                .with(httpBasic("Jane", "jane123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTodoJson));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region Todo - DELETE

    @Test
    public void givenExistingTodo_whenTodoIsDeletedByAuthorized_thenTodoDeleted() throws Exception {
        // Given
        User john = userRepository.save(testUser("John", "password123"));
        TodoList todoList = todoListRepository.save(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.save(testTodo("John's Todo", "Get done ASAP", todoList));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John/todoLists/" + todoList.getId() + "/todos/" + originalTodo.getId())
                .with(httpBasic("John", "password123")));

        // Then
        resultActions.andExpect(status().isNoContent());
        assertFalse(todoRepository.existsById(originalTodo.getId()));
    }

    @Test
    public void givenExistingTodo_whenTodoIsDeletedByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        userRepository.save(testUser("Jane", "jane123"));
        User john = userRepository.save(testUser("John", "password123"));
        TodoList todoList = todoListRepository.save(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.save(testTodo("John's Todo", "Get done ASAP", todoList));

        // When
        ResultActions resultActions = mvc.perform(
                delete("/api/users/John/todoLists/" + todoList.getId() + "/todos/" + originalTodo.getId()));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenExistingTodo_whenTodoIsDeletedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.save(testUser("Jane", "jane123"));
        User john = userRepository.save(testUser("John", "password123"));
        TodoList todoList = todoListRepository.save(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.save(testTodo("John's Todo", "Get done ASAP", todoList));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John/todoLists/" + todoList.getId() + "/todos/" + originalTodo.getId())
                .with(httpBasic("Jane", "jane123")));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region Helper Functions

    private User testUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }

    private TodoList testTodoList(String title, User user) {
        TodoList todoList = new TodoList();
        todoList.setTitle(title);
        todoList.setUser(user);
        return todoList;
    }

    private Todo testTodo(String title, String description, TodoList todoList) {
        Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setTodoList(todoList);
        return todo;
    }

    //endregion
}
