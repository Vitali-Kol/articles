package com.example.vlogi.config;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.ArticleComment;
import com.example.vlogi.entity.ArticleFavorite;
import com.example.vlogi.entity.Tag;
import com.example.vlogi.entity.User;
import com.example.vlogi.entity.Roles;
import com.example.vlogi.repository.ArticleCommentRepository;
import com.example.vlogi.repository.ArticleFavoriteRepository;
import com.example.vlogi.repository.ArticleRepository;
import com.example.vlogi.repository.TagRepository;
import com.example.vlogi.repository.UserRepository;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class InsertData {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleFavoriteRepository articleFavoriteRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public InsertData(UserRepository userRepository,
                      TagRepository tagRepository,
                      ArticleRepository articleRepository,
                      ArticleCommentRepository articleCommentRepository,
                      ArticleFavoriteRepository articleFavoriteRepository) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.articleFavoriteRepository = articleFavoriteRepository;
    }

    @PostConstruct
    public void init() {
        Faker faker = new Faker(Locale.forLanguageTag("en"));

        // Проверка наличия администратора и устранение дубликатов
        List<User> admins = userRepository.findByEmailContainingIgnoreCase("admin@example.com");
        if (admins.size() > 1) {
            System.out.println("Найдено " + admins.size() + " записей c email admin@example.com. Удаляем дубли...");
            for (User dub : admins) {
                userRepository.delete(dub);
            }
            createAdmin();
        } else if (admins.size() == 1) {
            System.out.println("Админ с email admin@example.com уже есть, ничего не делаем");
        } else {
            createAdmin();
        }

        // Создаем 3 предопределенных пользователя
        createUser("user1", "user1@example.com", "user123", Roles.ROLE_USER);
        createUser("user2", "user2@example.com", "user123", Roles.ROLE_USER);
        createUser("user3", "user3@example.com", "user123", Roles.ROLE_USER);

        // Создаем теги с проверкой уникальности
        List<Tag> tags = new ArrayList<>();
        Set<String> tagNames = new HashSet<>();
        int desiredCount = 15;
        while (tags.size() < desiredCount) {
            String tagName = faker.book().genre();
            // Если имя слишком длинное, обрезаем его
            if (tagName.length() > 50) {
                tagName = tagName.substring(0, 50);
            }
            // Проверяем уникальность в базе и в текущем наборе тегов
            if (!tagRepository.existsByName(tagName) && tagNames.add(tagName)) {
                Tag tag = new Tag();
                tag.setCreatedAt(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                tag.setName(tagName);
                tags.add(tag);
            }
        }
        tags = tagRepository.saveAll(tags);

        // Создаем статьи
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Article article = new Article();
            article.setTitle(faker.book().title());
            article.setDescription(faker.lorem().sentence());
            article.setContent(faker.lorem().paragraph(3));
            article.setCreatedAt(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
            article.setUpdatedAt(LocalDateTime.now());

            // Генерация уникального slug
            String slug = faker.internet().slug();
            while (articleRepository.existsBySlug(slug)) {
                slug = faker.internet().slug();
            }
            article.setSlug(slug);

            // Привязываем случайного автора
            User randomUser = getRandomUser();
            article.setAuthor(randomUser);

            // Привязываем случайные теги
            Set<Tag> articleTags = new HashSet<>();
            int numTags = faker.number().numberBetween(1, 3);
            for (int j = 0; j < numTags; j++) {
                Tag randomTag = tags.get(faker.number().numberBetween(0, tags.size()));
                articleTags.add(randomTag);
            }
            article.setTags(articleTags);

            articles.add(article);
        }
        articleRepository.saveAll(articles);

        // Создаем комментарии и записи о фаворитах для статей
        createCommentsAndFavorites(articles);
    }

    private void createAdmin() {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@example.com");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole(Roles.ROLE_ADMIN);
        adminUser.setCreatedAt(LocalDateTime.now());
        adminUser.setBio("I'm the admin user");
        userRepository.save(adminUser);
        System.out.println("Администратор admin@example.com (admin123) создан");
    }

    private void createUser(String username, String email, String password, Roles role) {
        if (!userRepository.existsByEmail(email)) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setCreatedAt(LocalDateTime.now());
            user.setBio("I'm the " + username);
            userRepository.save(user);
            System.out.println(username + " created");
        }
    }

    private User getRandomUser() {
        List<User> users = userRepository.findAll();
        return users.get(new Random().nextInt(users.size()));
    }

    private void createCommentsAndFavorites(List<Article> articles) {
        Faker faker = new Faker(Locale.forLanguageTag("en"));
        List<ArticleComment> comments = new ArrayList<>();
        for (Article article : articles) {
            int commentCount = faker.number().numberBetween(1, 6);
            for (int i = 0; i < commentCount; i++) {
                ArticleComment comment = new ArticleComment();
                comment.setCreatedAt(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                comment.setUpdatedAt(LocalDateTime.now());
                comment.setBody(faker.lorem().sentence());
                comment.setArticle(article);

                // Выбираем случайного пользователя для комментария
                User randomUser = getRandomUser();
                comment.setUser(randomUser);

                comments.add(comment);
            }
        }
        articleCommentRepository.saveAll(comments);

        List<ArticleFavorite> favorites = new ArrayList<>();
        for (Article article : articles) {
            int favCount = faker.number().numberBetween(0, 4);
            for (int i = 0; i < favCount; i++) {
                ArticleFavorite favorite = new ArticleFavorite();
                favorite.setCreatedAt(LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                favorite.setUpdatedAt(LocalDateTime.now());
                favorite.setArticle(article);

                // Выбираем случайного пользователя для записи о фаворите
                User randomUser = getRandomUser();
                favorite.setUser(randomUser);

                favorites.add(favorite);
            }
        }
        articleFavoriteRepository.saveAll(favorites);
    }
}
