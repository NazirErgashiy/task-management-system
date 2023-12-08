package uz.nazir.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.nazir.task.dto.request.TaskDtoRequest;
import uz.nazir.task.dto.response.TaskDtoResponse;
import uz.nazir.task.entities.Task;
import uz.nazir.task.entities.User;
import uz.nazir.task.error.exceptions.TaskNotFoundException;
import uz.nazir.task.mappers.TaskMapper;
import uz.nazir.task.repositories.TaskRepository;
import uz.nazir.task.services.BaseService;

import java.util.Map;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class TaskService implements BaseService<TaskDtoRequest, TaskDtoResponse, Long> {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<TaskDtoResponse> readAll(Pageable pageable) {
        final Page<Task> pagedResult = taskRepository.findAll(pageable);
        return pagedResult.map(taskMapper::entityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public TaskDtoResponse readById(Long id) {
        return taskRepository.findById(id)
                .map(taskMapper::entityToDto)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    @Override
    public TaskDtoResponse create(TaskDtoRequest createRequest) {
        Map<String, Object> credentials = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User currentUser = (User) credentials.get("user");
        createRequest.setTaskAuthorId(currentUser.getId());

        Task task = taskMapper.dtoToEntity(createRequest);
        Task createdTask = taskRepository.save(task);
        return taskMapper.entityToDto(createdTask);
    }

    @Transactional
    @Override
    public void update(Long id, TaskDtoRequest updateRequest) {
        taskRepository.findById(id)
                .map(task -> {
                    taskMapper.toEntity(updateRequest, task);
                    Task savedTask = taskRepository.save(task);
                    return taskMapper.entityToDto(savedTask);
                }).orElseThrow(() -> new TaskNotFoundException(id));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
        } else throw new TaskNotFoundException(id);
    }
}
