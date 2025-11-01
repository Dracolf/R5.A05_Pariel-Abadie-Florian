package fr.iut.blog.controller;

import fr.iut.blog.model.*;
import fr.iut.blog.repo.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepo articleRepo;
    private final UserRepo userRepo;
    private final ReactionRepo reactionRepo;

    public ArticleController(ArticleRepo a, UserRepo u, ReactionRepo r) {
        this.articleRepo = a; this.userRepo = u; this.reactionRepo = r;
    }

    // ---- CRUD Article (sans auth pour TP2)
    @GetMapping
    public List<Map<String,Object>> list() {
        // retourne auteur, date, contenu, nb likes/dislikes
        List<Map<String,Object>> out = new ArrayList<>();
        for (Article ar : articleRepo.findAll()) {
            Map<String,Object> m = new LinkedHashMap<>();
            m.put("id", ar.getId());
            m.put("author", ar.getAuthor().getUsername());
            m.put("publishedAt", ar.getPublishedAt());
            m.put("title", ar.getTitle());
            m.put("content", ar.getContent());
            m.put("likes", reactionRepo.countByArticleAndType(ar, Reaction.Type.LIKE));
            m.put("dislikes", reactionRepo.countByArticleAndType(ar, Reaction.Type.DISLIKE));
            out.add(m);
        }
        return out;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable UUID id) {
        return articleRepo.findById(id)
                .map(ar -> {
                    Map<String,Object> m = new LinkedHashMap<>();
                    m.put("id", ar.getId());
                    m.put("author", ar.getAuthor().getUsername());
                    m.put("publishedAt", ar.getPublishedAt());
                    m.put("title", ar.getTitle());
                    m.put("content", ar.getContent());
                    m.put("likes", reactionRepo.countByArticleAndType(ar, Reaction.Type.LIKE));
                    m.put("dislikes", reactionRepo.countByArticleAndType(ar, Reaction.Type.DISLIKE));
                    return ResponseEntity.ok(m);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DTO très simple pour créer/MAJ
    public record ArticleIn(String authorUsername, String title, String content) {}

    @PostMapping
    public ResponseEntity<?> create(@RequestBody ArticleIn in) {
        var author = userRepo.findByUsername(in.authorUsername())
                .orElseThrow(() -> new IllegalArgumentException("Unknown author"));
        var a = new Article();
        a.setAuthor(author);
        a.setTitle(in.title());
        a.setContent(in.content());
        return ResponseEntity.ok(articleRepo.save(a));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody ArticleIn in) {
        return articleRepo.findById(id).map(a -> {
            a.setTitle(in.title());
            a.setContent(in.content());
            return ResponseEntity.ok(articleRepo.save(a));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        if (!articleRepo.existsById(id)) return ResponseEntity.notFound().build();
        articleRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Like / Dislike (un seul par user/article, overwrite si différent)
    @PostMapping("/{id}/like")
    public ResponseEntity<?> like(@PathVariable UUID id, @RequestParam String username) {
        return react(id, username, Reaction.Type.LIKE);
    }

    @PostMapping("/{id}/dislike")
    public ResponseEntity<?> dislike(@PathVariable UUID id, @RequestParam String username) {
        return react(id, username, Reaction.Type.DISLIKE);
    }

    @DeleteMapping("/{id}/reaction")
    public ResponseEntity<?> clearReaction(@PathVariable UUID id, @RequestParam String username) {
        var ar = articleRepo.findById(id).orElse(null);
        var u = userRepo.findByUsername(username).orElse(null);
        if (ar == null || u == null) return ResponseEntity.notFound().build();
        reactionRepo.findByUserAndArticle(u, ar).ifPresent(reactionRepo::delete);
        return ResponseEntity.noContent().build();
    }

    private ResponseEntity<?> react(UUID articleId, String username, Reaction.Type type) {
        var ar = articleRepo.findById(articleId).orElse(null);
        var u = userRepo.findByUsername(username).orElse(null);
        if (ar == null || u == null) return ResponseEntity.notFound().build();

        var r = reactionRepo.findByUserAndArticle(u, ar).orElseGet(() -> {
            var nr = new Reaction();
            nr.setUser(u); nr.setArticle(ar);
            return nr;
        });
        r.setType(type);
        reactionRepo.save(r);
        return ResponseEntity.ok().build();
    }
}
