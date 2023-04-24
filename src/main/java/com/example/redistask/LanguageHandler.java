package com.example.redistask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class LanguageHandler {
    private LanRepo lanRepo;

    @Autowired
    public LanguageHandler(LanRepo lanRepo) {
        this.lanRepo = lanRepo;
    }

    public <T extends ServerResponse> Mono<ServerResponse> all(ServerRequest serverRequest) {
        return ServerResponse.ok().body(lanRepo.findAll(), Language.class);
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest) {
        return lanRepo.findById(serverRequest.pathVariable("id"))
                .flatMap(language -> ServerResponse
                        .ok()
                        .body(Mono.just(language),Language.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
