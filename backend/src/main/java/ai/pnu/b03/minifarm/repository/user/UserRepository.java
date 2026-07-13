package ai.pnu.b03.minifarm.repository.user;

import ai.pnu.b03.minifarm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);
}
