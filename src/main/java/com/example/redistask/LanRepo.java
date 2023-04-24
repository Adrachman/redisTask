package com.example.redistask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//Нет отдельного реактивного репозитория? работаем через темплейт
@Component
public class LanRepo  {
    private ReactiveRedisOperations<String, Language> template;

    @Autowired
    public LanRepo(ReactiveRedisOperations<String, Language> template) {
        this.template = template;
    }

    public Flux<Language> findAll(){
        return template.<String,Language>opsForHash().values("language");
    }

    public Mono<Language> findById(String id){
        return template.<String, Language>opsForHash().get("language", id);
    }

    public Mono<Language> save(Language language){
        return template.<String, Language>opsForHash().put("language", language.getId(),language)
                .map(aBoolean -> language);
    }

    public Mono<Boolean> deleteAll() {
        return template.<String, Language>opsForHash().delete("language");
    }
}
