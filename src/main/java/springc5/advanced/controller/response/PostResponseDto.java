package springc5.advanced.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc5.advanced.domain.Comment;
import springc5.advanced.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {
  private Long id;
  private String title;
  private String content;
  private String imgUrl;
  private String author;
  private Long likes;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private List<CommentResponseDto> comments;

  public PostResponseDto(Post post , Long likes , List<CommentResponseDto> commentResponseDtoList ){
    id = post.getId();
    title = post.getTitle();
    content = post.getContent();
    imgUrl = post.getImgUrl();
    author = post.getMember().getNickname();
    this.likes = likes;
    createdAt = post.getCreatedAt();
    modifiedAt = post.getModifiedAt();
    comments = commentResponseDtoList;
  }
}
