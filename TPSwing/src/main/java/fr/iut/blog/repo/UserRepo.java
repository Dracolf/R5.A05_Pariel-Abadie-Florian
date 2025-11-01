package fr.iut.blog.repo;

import fr.iut.blog.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByUsername(String username);
}
