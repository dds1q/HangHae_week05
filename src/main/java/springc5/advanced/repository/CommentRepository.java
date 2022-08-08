package springc5.advanced.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc5.advanced.domain.Comment;
import springc5.advanced.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);

  List<Comment> findAllByCid(Long id);
}
