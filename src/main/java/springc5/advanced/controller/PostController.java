package springc5.advanced.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springc5.advanced.controller.request.PostRequestDto;
import springc5.advanced.controller.response.ResponseDto;
import springc5.advanced.service.FileUploadService;
import springc5.advanced.service.PostService;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@RestController
public class PostController {

  private final PostService postService;
  private final FileUploadService fileUploadService;

  @PostMapping("/api/auth/post")
  public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
                                   HttpServletRequest request
  ) {
    return postService.createPost(requestDto, request );
  }

  @PostMapping("/api/auth/post/upload")
  public ResponseDto<?> createPostUpload(@RequestPart PostRequestDto requestDto,
                                   HttpServletRequest request,
                                   @RequestPart MultipartFile file
                                  ) throws IllegalAccessException {
    return postService.createPostUpload(requestDto, request , file );
  }

  @GetMapping("/api/post/{id}")
  public ResponseDto<?> getPost(@PathVariable Long id) {
    return postService.getPost(id);
  }

  @GetMapping("/api/post")
  public ResponseDto<?> getAllPosts() {
    return postService.getAllPosts();
  }

  @GetMapping("/api/auth/post")
  public ResponseDto<?> getMyPosts( HttpServletRequest request ) {
    return postService.getMyPosts( request );
  }

  @GetMapping("/api/auth/like/post")
  public ResponseDto<?> getMyLikePosts( HttpServletRequest request ) {
    return postService.getMyLikePosts( request );
  }


  @PutMapping("/api/auth/post/{id}")
  public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
      HttpServletRequest request) {
    return postService.updatePost(id, postRequestDto, request);
  }

  @DeleteMapping("/api/auth/post/{id}")
  public ResponseDto<?> deletePost(@PathVariable Long id,
      HttpServletRequest request) {
    return postService.deletePost(id, request);
  }

  @PostMapping("/api/upload/image")
  public String uploadImage(@RequestPart MultipartFile file) throws IllegalAccessException {
    return fileUploadService.uploadImage( file );
  }

}
