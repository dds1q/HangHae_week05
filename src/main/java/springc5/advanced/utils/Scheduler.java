package springc5.advanced.utils;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import springc5.advanced.domain.Comment;
import springc5.advanced.domain.Post;
import springc5.advanced.repository.CommentRepository;
import springc5.advanced.repository.PostRepository;
import springc5.advanced.service.PostService;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
public class Scheduler {

    private final Logger logger = LoggerFactory.getLogger( this.getClass() );
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    // 초, 분, 시, 일, 월, 주 순서
    @Scheduled(cron = "0 0 1 * * *")
    public void updatePrice() throws InterruptedException {
        System.out.println("가격 업데이트 실행");

        List<Post> postList = postRepository.findAll();
        for ( Post post : postList ) {
            List<Comment> comments = commentRepository.findAllByPost( post );
            if( comments.size() == 0 ){
                postRepository.delete( post );
                logger.info("게시물 <"+ post.getTitle() + ">이 삭제되었습니다.");
            }
        }
    }
}