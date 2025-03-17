/*
package com.example.vlogi.config;

import com.example.vlogi.entity.*;
import com.example.vlogi.repository.*;
import com.github.javafaker.Faker;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class InsertData {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;
    private final ArticleTagRepository articleTagRepository;
    private final ArticleFavoriteRepository articleFavoriteRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public InsertData(UserRepository userRepository,
                      TagRepository tagRepository,
                      ArticleRepository articleRepository,
                      ArticleTagRepository articleTagRepository,
                      ArticleFavoriteRepository articleFavoriteRepository,
                      ArticleCommentRepository articleCommentRepository) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.articleRepository = articleRepository;
        this.articleTagRepository = articleTagRepository;
        this.articleFavoriteRepository = articleFavoriteRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @PostConstruct
    public void init() {
        Faker faker = new Faker(Locale.ENGLISH);


        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            setField(user, "createdAt", LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
            setField(user, "email", faker.internet().emailAddress());
            setField(user, "username", faker.name().username());
            setField(user, "imageUrl", faker.internet().avatar());
            setField(user, "password", faker.internet().password());
            setField(user, "bio", faker.lorem().sentence());
            users.add(user);
        }
        users = userRepository.saveAll(users);


        Set<String> uniqueTagNames = new HashSet<>();
        List<Tag> tags = new ArrayList<>();
        while (uniqueTagNames.size() < 5) { // хотим 5 уникальных тегов
            String genre = faker.book().genre();
            // если genre успешно добавлено в Set (то есть такого ещё не было)
            if (uniqueTagNames.add(genre)) {
                Tag tag = new Tag();
                setField(tag, "createdAt", LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                setField(tag, "name", genre);
                tags.add(tag);
            }
        }
        tagRepository.saveAll(tags);







        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Article article = new Article();
            setField(article, "createdAt", LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
            setField(article, "updatedAt", LocalDateTime.now());

            String description = faker.lorem().sentence();
            setField(article, "description", description.substring(0, Math.min(50, description.length())));

            setField(article, "slug", faker.internet().slug());
            setField(article, "title", faker.book().title());
            setField(article, "content", faker.lorem().paragraph(3));
            setField(article, "author", users.get(faker.number().numberBetween(0, users.size())));
            articles.add(article);
        }
        articles = articleRepository.saveAll(articles);


        List<ArticleTag> articleTags = new ArrayList<>();
        for (Article article : articles) {
            int numTags = faker.number().numberBetween(1, 3);
            Set<Tag> articleTagSet = new HashSet<>();
            while (articleTagSet.size() < numTags) {
                articleTagSet.add(tags.get(faker.number().numberBetween(0, tags.size())));
            }
            for (Tag tag : articleTagSet) {
                ArticleTag articleTag = new ArticleTag();
                setField(articleTag, "article", article);
                setField(articleTag, "tag", tag);
                articleTags.add(articleTag);
            }
        }
        articleTagRepository.saveAll(articleTags);


        List<ArticleComment> comments = new ArrayList<>();
        for (Article article : articles) {
            int numComments = faker.number().numberBetween(1, 6);
            for (int i = 0; i < numComments; i++) {
                ArticleComment comment = new ArticleComment();
                setField(comment, "createdAt", LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                setField(comment, "article", article);
                setField(comment, "user", users.get(faker.number().numberBetween(0, users.size())));
                setField(comment, "content", faker.lorem().sentence());
                comments.add(comment);
            }
        }
        articleCommentRepository.saveAll(comments);


        List<ArticleFavorite> favorites = new ArrayList<>();
        for (Article article : articles) {
            int numFavorites = faker.number().numberBetween(0, 4);
            for (int i = 0; i < numFavorites; i++) {
                ArticleFavorite favorite = new ArticleFavorite();
                setField(favorite, "createdAt", LocalDateTime.now().minusDays(faker.number().numberBetween(1, 30)));
                setField(favorite, "article", article);
                setField(favorite, "user", users.get(faker.number().numberBetween(0, users.size())));
                favorites.add(favorite);
            }
        }
        articleFavoriteRepository.saveAll(favorites);

        System.out.println("Фейковые данные успешно загружены!");
    }


    private <T> void setField(T object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Ошибка при установке поля " + fieldName + " для " + object.getClass().getName(), e);
        }
    }
}
*/
