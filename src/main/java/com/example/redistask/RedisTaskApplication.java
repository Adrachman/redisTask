package com.example.redistask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.UUID;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@Slf4j
public class RedisTaskApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisTaskApplication.class, args);
    }
    //Redis хранит значения как строки
    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }

    @Bean
    public ReactiveRedisTemplate<String, Language> reactiveRedisTemplateJSON(
            ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        RedisSerializationContext<String, Language> serializationContext = RedisSerializationContext
                .<String, Language>newSerializationContext(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .hashValue(new Jackson2JsonRedisSerializer<>(Language.class))
                .build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, serializationContext);
    }

    @Bean
    public RouterFunction<ServerResponse> routes(LanguageHandler languageHandler) {
        return route(GET("/"), languageHandler::all)
                .andRoute(GET("/{id}"), languageHandler::get);
    }

    @Bean
    public CommandLineRunner commandLineRunner(LanRepo lanRepo) {
        return args -> {
            log.info("start fill database");
            Language language1 = new Language(UUID.randomUUID().toString(), "scala", "odersky");
            Language language2 = new Language(UUID.randomUUID().toString(), "java", "gosling");
            Language language3 = new Language(UUID.randomUUID().toString(), "elixir", "valim");

            lanRepo.deleteAll()
                    .then(lanRepo.save(language1))
                    .then(lanRepo.save(language2))
                    .then(lanRepo.save(language3))
                    .subscribe();

            log.info("finish fill database");
        };

    }

}
