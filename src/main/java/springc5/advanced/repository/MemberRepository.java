package springc5.advanced.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc5.advanced.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findById(Long id);
  Optional<Member> findByNickname(String nickname);
}
