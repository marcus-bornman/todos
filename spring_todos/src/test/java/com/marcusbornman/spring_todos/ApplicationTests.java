package com.marcusbornman.spring_todos;

import com.marcusbornman.spring_todos.data.TodoListRepository;
import com.marcusbornman.spring_todos.data.TodoRepository;
import com.marcusbornman.spring_todos.data.UserRepository;
import com.marcusbornman.spring_todos.model.Todo;
import com.marcusbornman.spring_todos.model.TodoList;
import com.marcusbornman.spring_todos.model.User;
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
    public void givenNewUser_whenUserIsPosted_thenCreatedResponse() throws Exception {
        // Given
        String newUserJson = "{\"username\":\"John\",\"password\":\"password123\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson));

        // Then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void givenNewUser_whenUserIsPosted_thenUserIsCreated() throws Exception {
        // Given
        String newUserJson = "{\"username\":\"John\",\"password\":\"password123\"}";

        // When
        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson));

        // Then
        assertNotNull(userRepository.findByUsername("John").orElse(null));
    }

    @Test
    public void givenExistingUser_whenUserIsPostedByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));
        String newUserJson = "{\"username\":\"John\",\"password\":\"john123\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newUserJson));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenExistingUser_whenUserIsPostedByDifferentAuthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));
        userRepository.saveInternal(testUser("Jane", "password123"));
        String edited = "{\"username\":\"John\",\"password\":\"john123\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/users")
                .header("Authorization", "Basic Jane:password123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(edited));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    //endregion

    //region User - READ

    @Test
    public void givenExistingUsers_whenAllUsersIsReadByAuthorized_thenMethodNotAllowedResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));
        userRepository.saveInternal(testUser("Jane", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void givenExistingUser_whenUserIsReadByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    @Test
    public void givenExistingUser_whenUserIsReadByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));
        userRepository.saveInternal(testUser("Jane", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John")
                .with(httpBasic("Jane", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    public void givenExistingUser_whenUserIsReadByAuthorized_thenOkResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void givenExistingUser_whenUserIsReadByAuthorized_thenUserDetailsReturned() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/users/John")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.expiryDate").exists())
                .andExpect(jsonPath("$.locked").exists())
                .andExpect(jsonPath("$.credentialsExpiryDate").exists())
                .andExpect(jsonPath("$.disabled").exists());
    }

    //endregion

    //region User - UPDATE

    @Test
    public void givenExistingUser_whenUsersIsUpdatedByAuthorized_thenNoContentResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));
        String editedUserJson = "{\"username\":\"John\",\"password\":\"john123\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/users/John")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(editedUserJson));

        // Then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void givenExistingUser_whenUserIsUpdatedByAuthorized_thenUserIsUpdated() throws Exception {
        // Given
        User originalUser = userRepository.saveInternal(testUser("John", "password123"));
        String editedUserJson = "{\"username\":\"John\",\"password\":\"john123\"}";

        // When
        mvc.perform(put("/api/users/John")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(editedUserJson));

        // Then
        User updatedUser = userRepository.findByUsername("John").orElseThrow();
        assertNotEquals(originalUser.getPassword(), updatedUser.getPassword());
    }

    @Test
    public void givenExistingUser_whenUsersIsUpdatedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));
        userRepository.saveInternal(testUser("Jane", "password123"));
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
        userRepository.saveInternal(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John")
                .with(httpBasic("John", "password123")));

        // Then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void givenExistingUser_whenUserIsDeletedByAuthorized_thenUserIsDeleted() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));

        // When
        mvc.perform(delete("/api/users/John")
                .with(httpBasic("John", "password123")));

        // Then
        assertNull(userRepository.findByUsername("John").orElse(null));
    }

    @Test
    public void givenExistingUser_whenUsersIsDeletedByDifferentAuthorized_thenUnauthorizedResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));
        userRepository.saveInternal(testUser("Jane", "password123"));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/users/John")
                .with(httpBasic("Jane", "password123")));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region TodoList - CREATE

    @Test
    public void givenNewTodoList_whenTodoListIsPostedByAuthenticated_thenCreatedResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "john123"));
        String newTodoList = "{\"title\":\"University\",\"user\":\"/api/users/John\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/todoLists")
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoList));

        // Then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void givenNewTodoList_whenTodoListIsPostedByAuthenticated_thenTodoListCreated() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "john123"));
        String newTodoList = "{\"title\":\"University\",\"user\":\"/api/users/John\"}";

        // When
        mvc.perform(post("/api/todoLists")
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoList));

        // Then
        List<TodoList> todoLists = todoListRepository.findAll();
        assertEquals(1, todoLists.size());
        assertEquals("University", todoLists.get(0).getTitle());
        assertEquals("John", todoLists.get(0).getUser().getUsername());
    }

    @Test
    public void givenNewTodoList_whenTodoListIsPostedByUnauthenticated_thenBadRequestResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "john123"));
        String newTodoList = "{\"title\":\"University\",\"user\":\"/api/users/John\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/todoLists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoList));

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    //endregion

    //region TodoList - READ

    @Test
    public void givenNoTodoLists_whenAllListsIsReadByAuthorized_thenOkResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/todoLists")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void givenTodoLists_whenAllListsIsReadByAuthorized_thenRelevantListsReturned() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "password123"));
        User jane = userRepository.saveInternal(testUser("Jane", "password123"));
        todoListRepository.saveInternal(testTodoList("Personal", john));
        todoListRepository.saveInternal(testTodoList("University", john));
        todoListRepository.saveInternal(testTodoList("Family", jane));

        // When
        ResultActions resultActions = mvc.perform(get("/api/todoLists")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(jsonPath("$._embedded.todoLists.[?(@.title == \"Personal\")]").exists())
                .andExpect(jsonPath("$._embedded.todoLists.[?(@.title == \"University\")]").exists())
                .andExpect(jsonPath("$._embedded.todoLists.[?(@.title == \"Family\")]").doesNotExist());
    }

    @Test
    public void whenAllListsIsReadByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // When
        ResultActions resultActions = mvc.perform(get("/api/todoLists")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    //endregion

    //region TodoList - UPDATE

    @Test
    public void givenExistingList_whenListIsUpdatedByAuthorized_thenNoContentResponse() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList originalList = todoListRepository.saveInternal(testTodoList("University", john));
        String todoListJson = "{\"title\":\"School\",\"user\":\"/api/users/John\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/todoLists/" + originalList.getId())
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoListJson));

        // Then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void givenExistingList_whenListIsUpdatedByAuthorized_thenTodoListUpdated() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList originalList = todoListRepository.saveInternal(testTodoList("University", john));
        String todoListJson = "{\"title\":\"School\",\"user\":\"/api/users/John\"}";

        // When
        mvc.perform(put("/api/todoLists/" + originalList.getId())
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoListJson));

        // Then
        TodoList editedList = todoListRepository.findAll().get(0);
        assertEquals("School", editedList.getTitle());
    }

    @Test
    public void givenExistingList_whenListIsUpdatedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("Jane", "jane123"));
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList originalList = todoListRepository.saveInternal(testTodoList("University", john));
        String todoListJson = "{\"title\":\"School\",\"user\":\"/api/users/John\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/todoLists/" + originalList.getId())
                .with(httpBasic("Jane", "jane123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(todoListJson));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region TodoList - DELETE

    @Test
    public void givenExistingList_whenListIsDeletedByAuthorized_thenNoContentResponse() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList originalList = todoListRepository.saveInternal(testTodoList("University", john));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/todoLists/" + originalList.getId())
                .with(httpBasic("John", "john123")));

        // Then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void givenExistingList_whenListIsDeletedByAuthorized_thenTodoListDeleted() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList originalList = todoListRepository.saveInternal(testTodoList("University", john));

        // When
        mvc.perform(delete("/api/todoLists/" + originalList.getId())
                .with(httpBasic("John", "john123")));

        // Then
        assertFalse(todoListRepository.existsById(originalList.getId()));
    }

    @Test
    public void givenExistingList_whenListIsDeletedByDifferentAuthorized_thenDeletedResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("Jane", "jane123"));
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList originalList = todoListRepository.saveInternal(testTodoList("University", john));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/todoLists/" + originalList.getId())
                .with(httpBasic("Jane", "jane123")));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region Todo - CREATE

    @Test
    public void givenNewTodo_whenTodoIsPostedByAuthenticated_thenCreatedResponse() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("University", john));
        String newTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"todoList\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/todos")
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson));

        // Then
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void givenNewTodo_whenTodoIsPostedByAuthenticated_thenTodoCreated() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("University", john));
        String newTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"todoList\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        mvc.perform(post("/api/todos")
                .with(httpBasic("John", "john123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson));

        // Then
        List<Todo> todos = todoRepository.findAll();
        assertEquals("University", todos.get(0).getTodoList().getTitle());
        assertEquals("Register", todos.get(0).getTitle());
        assertEquals("Register Online", todos.get(0).getDescription());
    }

    @Test
    public void givenNewTodo_whenTodoIsPostedByUnauthenticated_thenBadRequestResponse() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "john123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("University", john));
        String newTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"todoList\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        ResultActions resultActions = mvc.perform(post("/api/todos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newTodoJson));

        // Then
        resultActions.andExpect(status().isBadRequest());
    }

    //endregion

    //region TodoList - READ

    @Test
    public void givenNoTodos_whenAllTodosIsReadByAuthorized_thenOkResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("John", "password123"));

        // When
        ResultActions resultActions = mvc.perform(get("/api/todos")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void givenTodos_whenAllTodosIsReadByAuthorized_thenRelevantListsReturned() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "password123"));
        User jane = userRepository.saveInternal(testUser("Jane", "password123"));
        TodoList johnList = todoListRepository.saveInternal(testTodoList("Personal", john));
        TodoList janeList = todoListRepository.saveInternal(testTodoList("Personal", jane));
        todoRepository.saveInternal(testTodo("John's Todo #1", "Get done first", johnList));
        todoRepository.saveInternal(testTodo("John's Todo #2", "Get done first", johnList));
        todoRepository.saveInternal(testTodo("Jane's Only Todo", "Get done ASAP", janeList));

        // When
        ResultActions resultActions = mvc.perform(get("/api/todos")
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(jsonPath("$._embedded.todos.[?(@.title == \"John's Todo #1\")]").exists())
                .andExpect(jsonPath("$._embedded.todos.[?(@.title == \"John's Todo #2\")]").exists())
                .andExpect(jsonPath("$._embedded.todos.[?(@.title == \"Jane's Only Todo\")]").doesNotExist());
    }

    @Test
    public void whenAllTodosIsReadByUnauthorized_thenUnauthorizedResponse() throws Exception {
        // When
        ResultActions resultActions = mvc.perform(get("/api/todos")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isUnauthorized());
    }

    //endregion

    //region TodoList - UPDATE

    @Test
    public void givenExistingTodo_whenTodoIsUpdatedByAuthorized_thenNoContentResponse() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "password123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.saveInternal(testTodo("John's Todo", "Get done ASAP", todoList));
        String updatedTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"user\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/todos/" + originalTodo.getId())
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTodoJson));

        // Then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void givenExistingTodo_whenTodoIsUpdatedByAuthorized_thenTodoUpdated() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "password123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.saveInternal(testTodo("John's Todo", "Get done ASAP", todoList));
        String updatedTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"user\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        mvc.perform(put("/api/todos/" + originalTodo.getId())
                .with(httpBasic("John", "password123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTodoJson));

        // Then
        Todo todo = todoRepository.findAll().get(0);
        assertEquals("Register", todo.getTitle());
        assertEquals("Register Online", todo.getDescription());
    }

    @Test
    public void givenExistingTodo_whenTodoIsUpdatedByDifferentAuthorized_thenForbiddenResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("Jane", "jane123"));
        User john = userRepository.saveInternal(testUser("John", "password123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.saveInternal(testTodo("John's Todo", "Get done ASAP", todoList));
        String updatedTodoJson = "{\"title\":\"Register\",\"description\":\"Register Online\",\"user\":\"/api/todoLists/" + todoList.getId() + "\"}";

        // When
        ResultActions resultActions = mvc.perform(put("/api/todos/" + originalTodo.getId())
                .with(httpBasic("Jane", "jane123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedTodoJson));

        // Then
        resultActions.andExpect(status().isForbidden());
    }

    //endregion

    //region TodoList - DELETE

    @Test
    public void givenExistingTodo_whenTodoIsDeletedByAuthorized_thenNoContentResponse() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "password123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.saveInternal(testTodo("John's Todo", "Get done ASAP", todoList));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/todos/" + originalTodo.getId())
                .with(httpBasic("John", "password123")));

        // Then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    public void givenExistingTodo_whenTodoIsDeletedByAuthorized_thenTodoDeleted() throws Exception {
        // Given
        User john = userRepository.saveInternal(testUser("John", "password123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.saveInternal(testTodo("John's Todo", "Get done ASAP", todoList));

        // When
        mvc.perform(delete("/api/todos/" + originalTodo.getId())
                .with(httpBasic("John", "password123")));

        // Then
        assertFalse(todoRepository.existsById(originalTodo.getId()));
    }

    @Test
    public void givenExistingTodo_whenTodoIsDeletedByDifferentAuthorized_thenDeletedResponse() throws Exception {
        // Given
        userRepository.saveInternal(testUser("Jane", "jane123"));
        User john = userRepository.saveInternal(testUser("John", "password123"));
        TodoList todoList = todoListRepository.saveInternal(testTodoList("Personal", john));
        Todo originalTodo = todoRepository.saveInternal(testTodo("John's Todo", "Get done ASAP", todoList));

        // When
        ResultActions resultActions = mvc.perform(delete("/api/todos/" + originalTodo.getId())
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
