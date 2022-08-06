package springc5.advanced.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import springc5.advanced.domain.Member;
import springc5.advanced.domain.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByMember(Member member);
}
