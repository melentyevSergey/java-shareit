package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.utils.NotFoundException;
import ru.practicum.shareit.utils.validators.ValidateUser;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto getUser(Integer id) {
        return UserMapper.toUserDto(getUserIfExist(id));
    }

    @Override
    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {
        ValidateUser.validate(userDto);

        return UserMapper.toUserDto(userRepository.save((UserMapper.toUser(userDto))));
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer id, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(id);

        User userTemp = getUserIfExist(id);

        if (user.getName() == null) {
            user.setName(userTemp.getName());
        }

        if (user.getEmail() == null) {
            user.setEmail(userTemp.getEmail());
        }

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Integer id) {
        userRepository.deleteById(getUserIfExist(id).getId());
    }

    @Override
    public User getUserIfExist(Integer userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("Пользователь с идентификатором =%d не найден", userId)));
    }
}
