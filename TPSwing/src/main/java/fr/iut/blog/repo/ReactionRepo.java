package fr.iut.blog.repo;

import fr.iut.blog.model.Article;
import fr.iut.blog.model.Reaction;
import fr.iut.blog.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReactionRepo extends JpaRepository<Reaction, UUID> {
    long countByArticleAndType(Article article, Reaction.Type type);
    Optional<Reaction> findByUserAndArticle(UserAccount user, Article article);
}
