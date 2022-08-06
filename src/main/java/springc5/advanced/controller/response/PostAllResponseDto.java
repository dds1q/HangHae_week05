package springc5.advanced.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostAllResponseDto {
    private Long id;
    private String title;
    private String content;
    private String imgUrl;
    private String author;
    private Long likes;
    private Long commentsNum;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}

/**
 *  PostController 에서 getAllPosts 호출할때 리턴할 responseDto
 */
