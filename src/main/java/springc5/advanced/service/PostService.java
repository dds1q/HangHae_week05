package springc5.advanced.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springc5.advanced.controller.request.PostRequestDto;
import springc5.advanced.controller.response.CommentResponseDto;
import springc5.advanced.controller.response.PostAllResponseDto;
import springc5.advanced.controller.response.PostResponseDto;
import springc5.advanced.controller.response.ResponseDto;
import springc5.advanced.domain.*;
import springc5.advanced.jwt.TokenProvider;
import springc5.advanced.repository.CommentRepository;
import springc5.advanced.repository.LikeCommentRepository;
import springc5.advanced.repository.LikePostRepository;
import springc5.advanced.repository.PostRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

  private final LikePostRepository likePostRepository;
  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final LikeCommentRepository likeCommentRepository;

  private final TokenProvider tokenProvider;

  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }


    Post post = Post.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .member(member)
        .commentsNum(0L)
        .build();
    postRepository.save(post);

    return ResponseDto.success(
        PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .imgUrl(post.getImgUrl())
            .author(post.getMember().getNickname())
            .likes( 0L )
            .comments(null)
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getPost(Long id) {
    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post );
    List<CommentResponseDto> comments = new ArrayList<>();

    for (Comment comment : commentList) {
      if( comment.getCid() == null ){
        List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );
        comments.add(
                CommentResponseDto.builder()
                        .id(comment.getId())
                        .author(comment.getMember().getNickname())
                        .content(comment.getContent())
                        .likes((long) likeComments.size() )
                        .createdAt(comment.getCreatedAt())
                        .modifiedAt(comment.getModifiedAt())
                        .build()
        );
      }

    }
    List<LikePost> likePosts = likePostRepository.findAllByPost( post );

    return ResponseDto.success(
        PostResponseDto.builder()
            .id(post.getId())
            .title(post.getTitle())
            .content(post.getContent())
            .imgUrl(post.getImgUrl())
            .author(post.getMember().getNickname())
            .likes((long) likePosts.size())
            .comments(comments)
            .createdAt(post.getCreatedAt())
            .modifiedAt(post.getModifiedAt())
            .build()
    );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPost() {
    List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
    List<PostAllResponseDto> posts = new ArrayList<>();
    for(Post post : postList){
      List<LikePost> likePosts = likePostRepository.findAllByPost( post );
      posts.add(
              PostAllResponseDto.builder()
                      .id(post.getId())
                      .title(post.getTitle())
                      .content(post.getContent())
                      .imgUrl(post.getImgUrl())
                      .author(post.getMember().getNickname())
                      .likes((long) likePosts.size())
                      .commentsNum(post.getCommentsNum())
                      .createdAt(post.getCreatedAt())
                      .modifiedAt(post.getModifiedAt())
                      .build()
      );
    }
    return ResponseDto.success(posts);
  }

  @Transactional
  public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
    }

    List<Comment> commentList = commentRepository.findAllByPost(post);
    List<CommentResponseDto> comments = new ArrayList<>();

    for (Comment comment : commentList) {
      List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );
      comments.add(
              CommentResponseDto.builder()
                      .id(comment.getId())
                      .author(comment.getMember().getNickname())
                      .content(comment.getContent())
                      .likes((long) likeComments.size() )
                      .createdAt(comment.getCreatedAt())
                      .modifiedAt(comment.getModifiedAt())
                      .build()
      );
    }

    post.update(requestDto);

    List<LikePost> likePosts = likePostRepository.findAllByPost( post );
    return ResponseDto.success(
            PostResponseDto.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .imgUrl(post.getImgUrl())
                    .author(post.getMember().getNickname())
                    .likes((long) likePosts.size())
                    .comments(comments)
                    .createdAt(post.getCreatedAt())
                    .modifiedAt(post.getModifiedAt())
                    .build()
    );
  }

  @Transactional
  public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {
    if (null == request.getHeader("Refresh-Token")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    if (null == request.getHeader("Authorization")) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "로그인이 필요합니다.");
    }

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    Post post = isPresentPost(id);
    if (null == post) {
      return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
    }

    if (post.validateMember(member)) {
      return ResponseDto.fail("BAD_REQUEST", "작성자만 삭제할 수 있습니다.");
    }
    List<LikePost> likePosts = likePostRepository.findAllByPost( post );
    List<Comment> comments = commentRepository.findAllByPost( post );
    for( Comment comment : comments ){
      List<LikeComment> likeComments = likeCommentRepository.findAllByComment( comment );
      likeCommentRepository.deleteAll( likeComments );
    }
    commentRepository.deleteAll( comments );
    likePostRepository.deleteAll( likePosts );
    postRepository.delete(post);
    return ResponseDto.success("delete success");
  }

  @Transactional(readOnly = true)
  public Post isPresentPost(Long id) {
    Optional<Post> optionalPost = postRepository.findById(id);
    return optionalPost.orElse(null);
  }

  @Transactional
  public Member validateMember(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return null;
    }
    return tokenProvider.getMemberFromAuthentication();
  }

}
