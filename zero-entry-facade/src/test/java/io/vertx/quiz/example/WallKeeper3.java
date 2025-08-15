package io.vertx.quiz.example;

import io.zerows.core.security.annotations.Wall;

@Wall(value = "key2", path = "/api/*")
public class WallKeeper3 {
}
