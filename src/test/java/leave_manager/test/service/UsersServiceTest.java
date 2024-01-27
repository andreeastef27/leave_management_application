package leave_manager.test.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import com.example.leave_manager.dto.UserDto;
import com.example.leave_manager.model.User;
import com.example.leave_manager.repository.UsersRepo;
import com.example.leave_manager.service.UsersService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {

    @Mock
    private UsersRepo usersRepo;
    @InjectMocks
    private UsersService usersService;

    @Test
    void canGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(getUser());
        when(usersRepo.findAll()).thenReturn(users);
        List<UserDto> savedUsersDto = usersService.getAllUsers();
        verify(usersRepo).findAll();
        assertThat(savedUsersDto.size()).isEqualTo(1);
    }

    @Test
    void canGetUserById() {
        when(usersRepo.findById(getUser().getUserId())).thenReturn(
                Optional.ofNullable(getUser()));
        List<UserDto> savedUserDto = usersService.getUserById(getUser().getUserId());
        verify(usersRepo).findById(getUser().getUserId());
        assertThat(savedUserDto.get(0).getName()).isEqualTo(getUser().getName());
    }

    @Test
    void canCreateUser() {
        when(usersRepo.save(Mockito.any(User.class))).thenReturn(getUser());
        usersService.createUser(getUserDto());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(usersRepo).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        verify(usersRepo).save(Mockito.any(User.class));
        assertThat(capturedUser.getName()).isEqualTo("Albert");
    }

    @Test
    void canModifyUser() {
        when(usersRepo.findById(getUserDto().getUserId())).thenReturn(
                Optional.of(getUser()));
        usersService.modifyUser(getUserDto().getUserId(), getUserDto());
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(usersRepo, times(1)).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getUserId()).isEqualTo(1);
    }

    @Test
    void canDeleteUserById() {
        assertAll(() -> usersService.deleteUserById(getUser().getUserId()));
    }

    @Test
    void canDeleteAllUsers() {
        usersService.deleteAllUsers();
        verify(usersRepo).deleteAll();
    }

    @Test
    void cannotGetUserById() {
        when(usersRepo.findById(getUser().getUserId())).thenReturn(Optional.empty());
        List<UserDto> savedUserDto = usersService.getUserById(getUser().getUserId());
        assertThat(savedUserDto).isEmpty();
    }

    @Test
    void cannotModifyUser() {
        when(usersRepo.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            usersService.modifyUser(getUserDto().getUserId(), Mockito.mock(UserDto.class));
        });
    }

    private User getUser() {
        return User.builder()
                .userId(1)
                .departmentId(2)
                .name("Albert")
                .surname("Smith")
                .marca("4444")
                .role("ADMIN")
                .build();
    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .userId(1)
                .departmentId(2)
                .name("Albert")
                .surname("Smith")
                .marca("4444")
                .role("ADMIN")
                .build();
    }
}