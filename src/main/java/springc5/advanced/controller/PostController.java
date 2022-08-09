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

  @RequestMapping(value = "/api/auth/post", method = RequestMethod.POST)
  public ResponseDto<?> createPost(@RequestBody PostRequestDto requestDto,
                                   HttpServletRequest request
  ) {
    return postService.createPost(requestDto, request );
  }

  @RequestMapping(value = "/api/auth/post/upload", method = RequestMethod.POST)
  public ResponseDto<?> createPostUpload(@RequestPart PostRequestDto requestDto,
                                   HttpServletRequest request,
                                   @RequestPart MultipartFile file
                                  ) throws IllegalAccessException {
    return postService.createPostUpload(requestDto, request , file );
  }

  @RequestMapping(value = "/api/post/{id}", method = RequestMethod.GET)
  public ResponseDto<?> getPost(@PathVariable Long id) {
    return postService.getPost(id);
  }

  @RequestMapping(value = "/api/post", method = RequestMethod.GET)
  public ResponseDto<?> getAllPosts() {
    return postService.getAllPost();
  }

  @RequestMapping(value = "/api/auth/post/{id}", method = RequestMethod.PUT)
  public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
      HttpServletRequest request) {
    return postService.updatePost(id, postRequestDto, request);
  }

  @RequestMapping(value = "/api/auth/post/{id}", method = RequestMethod.DELETE)
  public ResponseDto<?> deletePost(@PathVariable Long id,
      HttpServletRequest request) {
    return postService.deletePost(id, request);
  }

  @PostMapping("/api/upload/image")
  public String uploadImage(@RequestPart MultipartFile file) throws IllegalAccessException {
    return fileUploadService.uploadImage( file );
  }

}
