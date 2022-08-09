package springc5.advanced.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc5.advanced.controller.request.CommentRequestDto;
import springc5.advanced.controller.request.SubCommentRequestDto;
import springc5.advanced.controller.response.CommentResponseDto;
import springc5.advanced.controller.response.ResponseDto;
import springc5.advanced.controller.response.SubCommentResponseDto;
import springc5.advanced.domain.*;
import springc5.advanced.jwt.TokenProvider;
import springc5.advanced.repository.CommentRepository;
import springc5.advanced.repository.LikeCommentRepository;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final LikeCommentRepository likeCommentRepository;
  private final TokenProvider tokenProvider;
  private final PostService postService;

  @Transactional
  public ResponseDto<?> createComment( CommentRequestDto requestDto, HttpServletRequest request) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = postService.isPresentPost(requestDto.getPostId());
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    Comment comment = Comment.builder()
        .member(member)
        .post(post)
        .content(requestDto.getContent())
        .build();
    commentRepository.save(comment);

    return ResponseDto.success( new CommentResponseDto( comment , 0L , null ) );

  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getMyComments(HttpServletRequest request) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    List<Comment> commentList = commentRepository.findAllByMember(member);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
        List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );

        commentResponseDtoList.add( new CommentResponseDto( comment , (long) likeComments.size() , null ));
    }

    return ResponseDto.success(commentResponseDtoList);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getMyLikeComments(HttpServletRequest request) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    List<LikeComment> likecommentList = likeCommentRepository.findAllByMember(member);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (LikeComment likecomment : likecommentList) {
      Comment comment = likecomment.getComment();
      List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );

      commentResponseDtoList.add( new CommentResponseDto( comment , (long) likeComments.size() , null ) );
    }

    return ResponseDto.success(commentResponseDtoList);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllComments(Long postId) {
    Post post = postService.isPresentPost(postId);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }
    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

    for (Comment comment : commentList) {
      if( comment.getCid() == null ){
        List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );
        List<Comment> subComments = commentRepository.findAllByCid( comment.getId() );
        List<SubCommentResponseDto> subcommentResponseDtoList = new ArrayList<>();
        for( Comment subComment : subComments ){
          List<LikeComment> likesubComments = likeCommentRepository.findAllByComment( subComment );
          subcommentResponseDtoList.add( new SubCommentResponseDto ( subComment , (long) likesubComments.size()) );
        }
        commentResponseDtoList.add( new CommentResponseDto( comment , (long) likeComments.size() , subcommentResponseDtoList ) );
      }
    }
    return ResponseDto.success(commentResponseDtoList);
  }

  @Transactional
  public ResponseDto<?> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = postService.isPresentPost(requestDto.getPostId());
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    Comment comment = isPresentComment(id);
    if (null == comment) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
    }

    if (comment.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    comment.update(requestDto);
    List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );
    return ResponseDto.success( new CommentResponseDto( comment , (long) likeComments.size() , null ) );
  }

  @Transactional
  public ResponseDto<?> deleteComment(Long id, HttpServletRequest request) {

    Member member = validateMember( request );
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Comment comment = isPresentComment(id);
    if (null == comment) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
    }

    if ( comment.validateMember(member) ) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
    }

    List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );
    List<Comment> subComments = commentRepository.findAllByCid( comment.getId() );
    for( Comment subComment : subComments ){
      List<LikeComment> likeSubComments = likeCommentRepository.findAllByComment( subComment );
      likeCommentRepository.deleteAll( likeSubComments );
    }
    likeCommentRepository.deleteAll( likeComments );
    commentRepository.deleteAll( subComments );
    commentRepository.delete(comment);
    return ResponseDto.success("success");
  }

  @Transactional
  public ResponseDto<?> createSubComment(SubCommentRequestDto requestDto, HttpServletRequest request) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Comment comment = isPresentComment(requestDto.getCommentId());
    if (null == comment) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id입니다.");
    }

    Post post = comment.getPost();

    Comment subcomment = Comment.builder()
            .member(member)
            .post(post)
            .content(requestDto.getContent())
            .cid( comment.getId() )
            .build();
    commentRepository.save( subcomment );
    return ResponseDto.success(new SubCommentResponseDto( subcomment , 0L ) );
  }


  @Transactional
  public ResponseDto<?> updateSubComment(SubCommentRequestDto requestDto, HttpServletRequest request, Long id) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Comment comment = isPresentComment(id);
    if (null == comment) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
    }
    if( comment.getCid() == null ){
      return ResponseDto.fail("NOT_SUBCOMMENT", "대댓글이 아닙니다.");
    }

    if (comment.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    comment.update( requestDto );
    List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );
    return ResponseDto.success(new SubCommentResponseDto( comment , (long) likeComments.size() ) );

  }

  @Transactional
  public ResponseDto<?> deleteSubComment(Long id, HttpServletRequest request) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Comment comment = isPresentComment(id);
    if (null == comment) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
    }

    if (comment.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
    }

    List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );

    likeCommentRepository.deleteAll( likeComments );
    commentRepository.delete(comment);
    return ResponseDto.success("success");

  }


  @Transactional(readOnly = true)
  public Comment isPresentComment(Long id) {
    Optional<Comment> optionalComment = commentRepository.findById(id);
    return optionalComment.orElse(null);
  }

  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }


}
