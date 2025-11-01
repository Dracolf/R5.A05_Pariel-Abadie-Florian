package fr.iut.blog;

import fr.iut.blog.model.UserAccount;
import fr.iut.blog.model.UserAccount.Role;
import fr.iut.blog.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Bootstrap implements CommandLineRunner {
    private final UserRepo users;
    public Bootstrap(UserRepo users) { this.users = users; }

    @Override public void run(String... args) {
        users.findByUsername("alice").orElseGet(() -> {
            var u = new UserAccount();
            u.setUsername("alice");
            u.setPassword("alice");
            u.setRole(Role.PUBLISHER);
            return users.save(u);
        });
    }
}
