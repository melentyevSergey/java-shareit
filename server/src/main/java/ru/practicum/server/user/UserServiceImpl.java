package ru.practicum.server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.server.user.dto.UserDto;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.server.utility.Utility;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final Utility utility;

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(userDto)));
    }

    @Override
    @Transactional
    public UserDto update(Integer userId, UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setId(userId);
        User userTemp = utility.checkUser(userId);
        if (user.getName() == null) {
            user.setName(userTemp.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userTemp.getEmail());
        }
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Integer id) {
        return UserMapper.toDto(utility.checkUser(id));
    }

    @Override
    @Transactional
    public void removeById(Integer id) {
        userRepository.deleteById(utility.checkUser(id).getId());
    }
}
