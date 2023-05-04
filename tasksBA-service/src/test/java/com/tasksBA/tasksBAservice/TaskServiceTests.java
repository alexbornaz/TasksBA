package com.tasksBA.tasksBAservice;

import com.tasksBA.tasksBAservice.dto.requests.SearchReq;
import com.tasksBA.tasksBAservice.dto.requests.TaskDTO;
import com.tasksBA.tasksBAservice.exceptions.UserNotFoundException;
import com.tasksBA.tasksBAservice.model.Status;
import com.tasksBA.tasksBAservice.model.Task;
import com.tasksBA.tasksBAservice.model.User;
import com.tasksBA.tasksBAservice.repository.TaskRepository;
import com.tasksBA.tasksBAservice.service.task.TaskServiceImpl;
import com.tasksBA.tasksBAservice.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {
    private static final String username = "testuser";
    private Task task1;
    private Task task2;
    private List<Task> tasks;

    @Mock
    TaskRepository taskRepository;
    @Mock
    UserService userService;
    @InjectMocks
    TaskServiceImpl taskService;

    @BeforeEach
    public void init() {
        tasks = new ArrayList<>();
        task1 = new Task("test1", LocalDate.now(), new User());
        task2 = new Task("test2", LocalDate.now().plusMonths(1), new User());
        tasks.add(task1);
        tasks.add(task2);
    }

    @Test
    public void getAllTasks_test() {
        when(taskRepository.findAll((Sort) any())).thenReturn(tasks);

        List<Task> actualTasks = taskService.getAll();

        assertEquals(tasks, actualTasks);
        verify(taskRepository).findAll((Sort) any());
    }

    @Test
    public void getTaskById_test() {
        Long id = 1L;
        task1.setId(id);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task1));

        Optional<Task> result = taskService.getTask(id);

        assertTrue(result.isPresent());
        assertEquals(Optional.of(task1), result);
        verify(taskRepository).findById(id);
    }

    @Test
    public void getAssignedTasks_UserExists() throws UserNotFoundException {
        task1.getAssignedTo().setUsername(username);
        task2.getAssignedTo().setUsername(username);
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(task1.getAssignedTo()));
        when(taskRepository.findAllByAssignedToOrderByDueDateDesc(task1.getAssignedTo())).thenReturn(tasks);

        List<Task> result = taskService.getAssignedTasks(username);

        assertEquals(tasks, result);
        verify(taskRepository).findAllByAssignedToOrderByDueDateDesc(task1.getAssignedTo());
    }

    @Test
    public void getAssignedTasks_UserDoesNotExists() {
        when(userService.getUserByUsername(username)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> taskService.getAssignedTasks(username));
    }

    @Test
    public void createTask_test() throws UserNotFoundException {
        User user = new User();
        user.setUsername(username);
        TaskDTO taskDTO = new TaskDTO("testtask", LocalDate.now(), user.getUsername(), Status.NEW);
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));

        taskService.createTask(taskDTO);

        assertEquals(1, user.getAssignedTasks().size());
        verify(userService, times(1)).saveUser(user);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void deleteTask_testO() {
        User user = new User();
        user.setUsername(username);
        task1.setId(1l);
        task1.setAssignedTo(user);
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));

        taskService.deleteTask(task1);

        assertEquals(0, user.getAssignedTasks().size());
        verify(userService, times(1)).saveUser(user);
        verify(taskRepository, times(1)).delete(task1);
    }

    @Test
    public void editTask_test() {
        User user = new User();
        user.setUsername(username);
        TaskDTO taskDTO = new TaskDTO("editedTask1", LocalDate.now().plusDays(1), user.getUsername(), Status.NEW);
        taskDTO.setId(1l);
        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));
        when(taskRepository.findById(1l)).thenReturn(Optional.of(task1));

        taskService.editTask(taskDTO);

        verify(userService, times(1)).getUserByUsername(username);
        verify(taskRepository, times(1)).findById(1L);
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());
        Task savedTask = captor.getValue();

        assertEquals("editedTask1", savedTask.getSubject());
        assertEquals(LocalDate.now().plusDays(1), savedTask.getDueDate());
        assertEquals(Status.NEW, savedTask.getStatus());
        assertEquals(user, savedTask.getAssignedTo());
    }

    @Test
    public void testSearchTasks() {
        SearchReq searchReq = new SearchReq("test", LocalDate.now(), Status.NEW, "Task 1");
        when(taskRepository.findTasksBySearchReq(searchReq.getAssignedTo(), searchReq.getSubject(), searchReq.getStatus(), searchReq.getDueDate())).thenReturn(tasks);

        List<Task> foundTasks = taskService.searchTasks(searchReq);

        assertEquals(tasks, foundTasks);
        verify(taskRepository, times(1)).findTasksBySearchReq(searchReq.getAssignedTo(), searchReq.getSubject(), searchReq.getStatus(), searchReq.getDueDate());
    }

}
