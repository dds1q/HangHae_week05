package springc5.advanced.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import springc5.advanced.controller.request.PostRequestDto;
import springc5.advanced.controller.response.*;
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
  private final FileUploadService fileUploadService;

  private final TokenProvider tokenProvider;


  @Transactional
  public ResponseDto<?> createPost(PostRequestDto requestDto, HttpServletRequest request ) {

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

    return ResponseDto.success( new PostResponseDto( post , 0L, null ) );
  }


  @Transactional
  public ResponseDto<?> createPostUpload(PostRequestDto requestDto, HttpServletRequest request , MultipartFile file) throws IllegalAccessException {


    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }

    String imgUrl = fileUploadService.uploadImage( file );

    Post post = Post.builder()
        .title(requestDto.getTitle())
        .content(requestDto.getContent())
        .member(member)
        .imgUrl(imgUrl)
        .commentsNum(0L)
        .build();
    postRepository.save(post);

    return ResponseDto.success(  new PostResponseDto( post , 0L , null ) );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getPost(Long id) {
    Post post = isPresentPost(id);
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
    List<LikePost> likePosts = likePostRepository.findAllByPost( post );

    return ResponseDto.success( new PostResponseDto( post , (long) likePosts.size(), commentResponseDtoList ) );
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getMyPosts(HttpServletRequest request ) {

    Member member = validateMember(request);
    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }
    List<Post> postList = postRepository.findAllByMemberOrderByModifiedAtDesc( member );
    List<PostAllResponseDto> posts = new ArrayList<>();
    for(Post post : postList){
      List<LikePost> likePosts = likePostRepository.findAllByPost( post );
      List<Comment> comments = commentRepository.findAllByPost( post );
      posts.add( new PostAllResponseDto( post , (long) likePosts.size(), (long) comments.size()) );
    }
    return ResponseDto.success(posts);
  }

  @Transactional(readOnly = true)
  public ResponseDto<?> getMyLikePosts( HttpServletRequest request ) {

    Member member = validateMember(request);

    if (null == member) {
      return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
    }
    List<LikePost> likepostList = likePostRepository.findAllByMember( member );
    List<PostAllResponseDto> posts = new ArrayList<>();
    for(LikePost likepost : likepostList){
      Post post = likepost.getPost();
      List<LikePost> likePosts = likePostRepository.findAllByPost( post );
      List<Comment> comments = commentRepository.findAllByPost( post );
      posts.add( new PostAllResponseDto( post , (long) likePosts.size(), (long) comments.size()) );

    }
    return ResponseDto.success(posts);
  }
  @Transactional(readOnly = true)
  public ResponseDto<?> getAllPosts() {
    List<Post> postList = postRepository.findAllByOrderByModifiedAtDesc();
    List<PostAllResponseDto> posts = new ArrayList<>();
    for(Post post : postList){
      List<LikePost> likePosts = likePostRepository.findAllByPost( post );
      List<Comment> comments = commentRepository.findAllByPost( post );
      posts.add( new PostAllResponseDto( post , (long) likePosts.size(), (long) comments.size()));
    }
    return ResponseDto.success(posts);
  }

  @Transactional
  public ResponseDto<?> updatePost(Long id, PostRequestDto requestDto, HttpServletRequest request) {

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

    post.update(requestDto);

    List<LikePost> likePosts = likePostRepository.findAllByPost( post );
    return ResponseDto.success( new PostResponseDto( post , (long) likePosts.size(), commentResponseDtoList ) );
  }

  @Transactional
  public ResponseDto<?> deletePost(Long id, HttpServletRequest request) {

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
