package com.nightguy.spark.comment;

import com.nightguy.spark.post.Post;
import com.nightguy.spark.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class CommentService {
  private final CommentMapper commentMapper;
  private final CommentRepository commentRepository;
  @PersistenceContext private EntityManager em;

  public CommentResponseDTO createComment(
      User user, @Valid CommentRequestDTO newCommentDto, Long postId) {
    Comment newComment = commentMapper.toEntity(newCommentDto);
    newComment.setAuthor(user);
    newComment.setPost(em.getReference(Post.class, postId));

    return commentMapper.toDto(commentRepository.save(newComment));
  }

  public Page<CommentResponseDTO> getAllCommentsForPost(Long postId, int pageNumber) {
    if (pageNumber < 0) throw new IllegalArgumentException("Invalid request param page");
    Pageable pageable = PageRequest.of(pageNumber, 10);
    return commentRepository.findAllByPost_Id(postId, pageable).map(commentMapper::toDto);
  }

  public CommentResponseDTO updateComment(
      User user, @Valid CommentRequestDTO newCommentDto, Long commentId) {
    // find comment
    Comment oldComment =
        commentRepository
            .findById(commentId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    // check if user owns comment
    if (!oldComment.getAuthor().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    // update comment
    Comment newComment = commentMapper.updateEntityFromDto(oldComment, newCommentDto);
    return commentMapper.toDto(commentRepository.save(newComment));
  }

  public void deleteComment(User user, Long commentId) {
    // find comment
    Comment comment =
        commentRepository
            .findById(commentId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found"));
    // check if user owns comment
    if (!comment.getAuthor().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    // delete comment
    commentRepository.delete(comment);
  }
}
