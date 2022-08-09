package springc5.advanced.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc5.advanced.domain.Comment;
import springc5.advanced.domain.LikeComment;
import springc5.advanced.repository.LikePostRepository;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDto {
  private Long id;
  private String author;
  private String content;
  private Long likes;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private List<SubCommentResponseDto> subComments;

  public CommentResponseDto( Comment comment , Long likes , List<SubCommentResponseDto>  subComments ){
    id = comment.getId();
    author = comment.getMember().getNickname();
    content = comment.getContent();
    createdAt = comment.getCreatedAt();
    modifiedAt = comment.getModifiedAt();
    this.likes = likes;
    this.subComments = subComments;
  }

}
