package fr.iut.blog.repo;

import fr.iut.blog.model.Article;
import fr.iut.blog.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArticleRepo extends JpaRepository<Article, UUID> {
    List<Article> findByAuthor(UserAccount author);
}
