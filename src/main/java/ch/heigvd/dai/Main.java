package ch.heigvd.dai;

import io.javalin.Javalin;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create();
        app.get("/", ctx -> ctx.result("Hello World"));
        app.start(PORT);
    }
}
