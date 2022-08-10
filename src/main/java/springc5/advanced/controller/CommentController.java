package springc5.advanced.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springc5.advanced.controller.request.CommentRequestDto;
import springc5.advanced.controller.request.SubCommentRequestDto;
import springc5.advanced.controller.response.ResponseDto;
import springc5.advanced.service.CommentService;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/api/auth/comment")
  public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                      HttpServletRequest request) {
    return commentService.createComment(requestDto, request);
  }

  @GetMapping("/api/comment/{id}")
  public ResponseDto<?> getAllComments(@PathVariable Long id) {
    return commentService.getAllComments(id);
  }

  @GetMapping("/api/auth/comment")
  public ResponseDto<?> getMyComments( HttpServletRequest request ) {
    return commentService.getMyComments(request);
  }

  @GetMapping("/api/auth/like/comment")
  public ResponseDto<?> getMyLikeComments(HttpServletRequest request) {
    return commentService.getMyLikeComments(request);
  }

  @PutMapping("/api/auth/comment/{id}")
  public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
      HttpServletRequest request) {
    return commentService.updateComment(id, requestDto, request);
  }

  @DeleteMapping( "/api/auth/comment/{id}")
  public ResponseDto<?> deleteComment(@PathVariable Long id,
      HttpServletRequest request) {
    return commentService.deleteComment(id, request);
  }

  @PostMapping("/api/auth/sub-comment")
  public ResponseDto<?> createSubComment(
          @RequestBody SubCommentRequestDto requestDto,
          HttpServletRequest request
  ) {
    return commentService.createSubComment(requestDto, request );
  }

  @PutMapping("/api/auth/sub-comment/{id}")
  public ResponseDto<?> updateSubComment(
          @PathVariable Long id,
          @RequestBody SubCommentRequestDto requestDto,
          HttpServletRequest request
  ) {
    return commentService.updateSubComment(requestDto, request , id);
  }

  @DeleteMapping("/api/auth/sub-comment/{id}")
  public ResponseDto<?> deleteSubComment(@PathVariable Long id,
                                      HttpServletRequest request) {
    return commentService.deleteSubComment(id, request);
  }

}
