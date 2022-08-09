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

  @RequestMapping(value = "/api/auth/comment", method = RequestMethod.POST)
  public ResponseDto<?> createComment(@RequestBody CommentRequestDto requestDto,
                                      HttpServletRequest request) {
    return commentService.createComment(requestDto, request);
  }

  @RequestMapping(value = "/api/comment/{id}", method = RequestMethod.GET)
  public ResponseDto<?> getAllComments(@PathVariable Long id) {
    return commentService.getAllComments(id);
  }

  @RequestMapping(value = "/api/auth/comment", method = RequestMethod.GET)
  public ResponseDto<?> getMyComments( HttpServletRequest request ) {
    return commentService.getMyComments(request);
  }

  @RequestMapping(value = "/api/auth/like/comment", method = RequestMethod.GET)
  public ResponseDto<?> getMyLikeComments(HttpServletRequest request) {
    return commentService.getMyLikeComments(request);
  }

  @RequestMapping(value = "/api/auth/comment/{id}", method = RequestMethod.PUT)
  public ResponseDto<?> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
      HttpServletRequest request) {
    return commentService.updateComment(id, requestDto, request);
  }

  @RequestMapping(value = "/api/auth/comment/{id}", method = RequestMethod.DELETE)
  public ResponseDto<?> deleteComment(@PathVariable Long id,
      HttpServletRequest request) {
    return commentService.deleteComment(id, request);
  }

  @RequestMapping(value = "/api/auth/sub-comment", method = RequestMethod.POST)
  public ResponseDto<?> createSubComment(
          @RequestBody SubCommentRequestDto requestDto,
          HttpServletRequest request
  ) {
    return commentService.createSubComment(requestDto, request );
  }

  @RequestMapping(value = "/api/auth/sub-comment/{id}", method = RequestMethod.PUT)
  public ResponseDto<?> updateSubComment(
          @PathVariable Long id,
          @RequestBody SubCommentRequestDto requestDto,
          HttpServletRequest request
  ) {
    return commentService.updateSubComment(requestDto, request , id);
  }

  @RequestMapping(value = "/api/auth/sub-comment/{id}", method = RequestMethod.DELETE)
  public ResponseDto<?> deleteSubComment(@PathVariable Long id,
                                      HttpServletRequest request) {
    return commentService.deleteSubComment(id, request);
  }

//  @RequestMapping(value = "/api/sub-comment/{id}", method = RequestMethod.GET)
//  public ResponseDto<?> getSubComments( @PathVariable Long id ) {
//
//  }

}
