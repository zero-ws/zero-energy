package io.vertx.quiz.example;


import jakarta.ws.rs.Path;

@Path("")
public class Method4 {

    @Path("test////:name")
    public void test() {
    }

    @Path("///mock/:id")
    public void test1() {
    }
}
