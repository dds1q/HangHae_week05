package springc5.advanced.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc5.advanced.domain.Comment;
import springc5.advanced.domain.LikeComment;
import springc5.advanced.domain.Member;
import springc5.advanced.domain.Post;

import java.util.List;
import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByMemberAndComment(Member member, Comment comment);

    List<LikeComment> findAllByComment(Comment comment);

    List<LikeComment> findAllByMember(Member member);
}
