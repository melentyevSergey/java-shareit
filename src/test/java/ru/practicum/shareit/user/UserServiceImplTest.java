package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utility.Utility;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Utility utility;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllTest() {
        UserDto expectedUser1 = new UserDto();
        UserDto expectedUser2 = new UserDto();

        List<UserDto> usersList = List.of(expectedUser1, expectedUser2);

        when(userRepository.findAll())
                .thenReturn(usersList.stream()
                        .map(UserMapper::toEntity)
                        .collect(Collectors.toList()));

        List<UserDto> actualUserList = userService.getAll();

        assertEquals(usersList, actualUserList);
    }

    @Test
    void saveUserTest() {
        UserDto userToSave = new UserDto();

        when(userRepository.save(UserMapper.toEntity(userToSave)))
                .thenReturn(UserMapper.toEntity(userToSave));

        UserDto actualUser = userService.save(userToSave);

        assertEquals(userToSave, actualUser);
    }

    @Test
    void updateTest() {
        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setName("Sergey");
        userToUpdate.setEmail("test@mail.ru");

        User userInDB = new User();
        userInDB.setId(1);
        userInDB.setName("ser");
        userInDB.setEmail("test2@mail.ru");

        when(userRepository.save(userToUpdate))
                .thenReturn(userToUpdate);

        UserDto actualUser = userService.update(userToUpdate.getId(), UserMapper.toDto(userToUpdate));

        assertEquals(UserMapper.toDto(userToUpdate), actualUser);
        verify(userRepository).save(userToUpdate);
    }

    @Test
    void updateWithNameOnlyTest() {
        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setName(null);
        userToUpdate.setEmail("test@mail.ru");

        User userInDB = new User();
        userInDB.setId(1);
        userInDB.setName("ser");
        userInDB.setEmail("test2@mail.ru");

        when(utility.checkUser(1))
                .thenReturn(userInDB);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserDto actualUser = userService.update(userToUpdate.getId(), UserMapper.toDto(userToUpdate));

        assertEquals(userInDB.getName(), actualUser.getName());
        assertEquals(userToUpdate.getEmail(), actualUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateWithEmailOnlyTest() {
        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setName("Sergey");
        userToUpdate.setEmail(null);

        User userInDB = new User();
        userInDB.setId(1);
        userInDB.setName("ser");
        userInDB.setEmail("test2@mail.ru");

        when(utility.checkUser(1))
                .thenReturn(userInDB);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserDto actualUser = userService.update(userToUpdate.getId(), UserMapper.toDto(userToUpdate));

        assertEquals(userToUpdate.getName(), actualUser.getName());
        assertEquals(userInDB.getEmail(), actualUser.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getByIdTest() {
        Integer userId = 1;
        User expectedUser = new User();
        when(utility.checkUser(userId)).thenReturn(expectedUser);

        UserDto actualUser = userService.getById(userId);

        assertEquals(UserMapper.toDto(expectedUser), actualUser);
    }

    @Test
    void removeByIdTest() {
        User userToDelete = new User();
        userToDelete.setId(1);

        when(utility.checkUser(1)).thenReturn(userToDelete);

        userService.removeById(userToDelete.getId());

        verify(userRepository).deleteById(1);
    }

    @Test
    void getByIdShouldThrowExceptionTest() {
        Integer userId = 1;
        when(utility.checkUser(userId)).thenThrow(new NotFoundException("error"));

        assertThrows(NotFoundException.class,
                () -> userService.getById(userId));
    }
}

