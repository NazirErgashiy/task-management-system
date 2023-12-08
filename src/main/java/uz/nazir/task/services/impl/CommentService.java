package uz.nazir.task.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.nazir.task.dto.request.CommentDtoRequest;
import uz.nazir.task.dto.response.CommentDtoResponse;
import uz.nazir.task.entities.Comment;
import uz.nazir.task.entities.User;
import uz.nazir.task.error.exceptions.CommentNotFoundException;
import uz.nazir.task.mappers.CommentMapper;
import uz.nazir.task.repositories.CommentRepository;
import uz.nazir.task.services.BaseService;

import java.util.Map;

@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class CommentService implements BaseService<CommentDtoRequest, CommentDtoResponse, Long> {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<CommentDtoResponse> readAll(Pageable pageable) {
        final Page<Comment> pagedResult = commentRepository.findAll(pageable);
        return pagedResult.map(commentMapper::entityToDto);
    }

    @Transactional(readOnly = true)
    @Override
    public CommentDtoResponse readById(Long id) {
        return commentRepository.findById(id)
                .map(commentMapper::entityToDto)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }

    @Transactional
    @Override
    public CommentDtoResponse create(CommentDtoRequest createRequest) {
        Map<String, Object> credentials = (Map<String, Object>) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        User currentUser = (User) credentials.get("user");
        createRequest.setAuthorId(currentUser.getId());

        Comment comment = commentMapper.dtoToEntity(createRequest);
        Comment createdComment = commentRepository.save(comment);
        return commentMapper.entityToDto(createdComment);
    }

    @Transactional
    @Override
    public void update(Long id, CommentDtoRequest updateRequest) {
        commentRepository.findById(id)
                .map(comment -> {
                    commentMapper.toEntity(updateRequest, comment);
                    Comment savedComment = commentRepository.save(comment);
                    return commentMapper.entityToDto(savedComment);
                }).orElseThrow(() -> new CommentNotFoundException(id));
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
        } else throw new CommentNotFoundException(id);
    }
}
