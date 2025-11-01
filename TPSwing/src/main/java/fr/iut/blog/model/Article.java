package fr.iut.blog.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, updatable = false)
    private Instant publishedAt = Instant.now();

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserAccount author;

    @Lob
    @Column(nullable = false)
    private String content;

    // Optionnel: un titre
    private String title;

    public UUID getId() { return id; }
    public Instant getPublishedAt() { return publishedAt; }
    public UserAccount getAuthor() { return author; }
    public void setAuthor(UserAccount a) { this.author = a; }
    public String getContent() { return content; }
    public void setContent(String c) { this.content = c; }
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }
}
