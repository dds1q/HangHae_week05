package springc5.advanced.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import springc5.advanced.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCommentResponseDto {
    private Long id;
    private String author;
    private String content;
    private Long likes;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public SubCommentResponseDto (Comment subcomment , Long likes ){
        id = subcomment.getId();
        author = subcomment.getMember().getNickname();
        content = subcomment.getContent();
        createdAt = subcomment.getCreatedAt();
        modifiedAt = subcomment.getModifiedAt();
        this.likes = likes;
    }
}
