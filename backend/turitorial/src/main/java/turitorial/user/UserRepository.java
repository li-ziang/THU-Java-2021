package turitorial.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import turitorial.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}