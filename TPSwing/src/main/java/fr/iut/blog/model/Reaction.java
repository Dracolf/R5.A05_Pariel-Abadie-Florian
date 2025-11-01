package fr.iut.blog.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "reactions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","article_id"}))
public class Reaction {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserAccount user;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Article article;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type; // LIKE ou DISLIKE

    public enum Type { LIKE, DISLIKE }

    public UUID getId() { return id; }
    public UserAccount getUser() { return user; }
    public void setUser(UserAccount u) { this.user = u; }
    public Article getArticle() { return article; }
    public void setArticle(Article a) { this.article = a; }
    public Type getType() { return type; }
    public void setType(Type t) { this.type = t; }
}
