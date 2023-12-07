package uz.nazir.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.nazir.task.dto.request.UserDtoRequest;
import uz.nazir.task.dto.response.UserDtoResponse;
import uz.nazir.task.entities.User;
import uz.nazir.task.error.exceptions.UserNotFoundException;
import uz.nazir.task.mappers.UserMapper;
import uz.nazir.task.repositories.UserRepository;
import uz.nazir.task.services.BaseService;

@Service
@RequiredArgsConstructor
public class UserService implements BaseService<UserDtoRequest, UserDtoResponse, Long> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<UserDtoResponse> readAll(Pageable pageable) {
        final Page<User> pagedResult = userRepository.findAll(pageable);
        return pagedResult.map(userMapper::entityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDtoResponse readById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::entityToDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    @Override
    public UserDtoResponse create(UserDtoRequest createRequest) {
        User user = userMapper.dtoToEntity(createRequest);
        User createdUser = userRepository.save(user);
        return userMapper.entityToDto(createdUser);
    }

    @Transactional
    @Override
    public void update(Long id, UserDtoRequest updateRequest) {
        userRepository.findById(id)
                .map(user -> {
                    userMapper.toEntity(updateRequest, user);
                    User savedUser = userRepository.save(user);
                    return userMapper.entityToDto(savedUser);
                }).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else throw new UserNotFoundException(id);
    }
}
