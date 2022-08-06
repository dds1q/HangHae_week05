package springc5.advanced.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc5.advanced.domain.*;

import java.util.List;
import java.util.Optional;


public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByMemberAndPost(Member member , Post post);

    List<LikePost> findAllByPost( Post post );
}
