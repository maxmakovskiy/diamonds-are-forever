package ch.heigvd.dai;

import static ch.heigvd.dai.controllers.AuthController.USER_ID;

import ch.heigvd.dai.controllers.AuthController;
import ch.heigvd.dai.controllers.ItemController;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.json.JavalinJackson;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Javalin app =
                Javalin.create(
                        config -> {
                            config.jsonMapper(
                                    new JavalinJackson()
                                            .updateMapper(
                                                    mapper ->
                                                            mapper.registerModule(
                                                                            new JavaTimeModule())
                                                                    .disable(
                                                                            SerializationFeature
                                                                                    .WRITE_DATES_AS_TIMESTAMPS)));
                            //                            config.jetty.modifyServletContextHandler(
                            //                                    handler ->
                            // handler.setSessionHandler(fileSessionHandler()));
                        });

        app.beforeMatched(
                "/items",
                ctx -> {
                    if (ctx.method() == HandlerType.POST) {
                        System.out.println("Checking user session...");
                        String id = ctx.sessionAttribute(USER_ID);
                        System.out.println("Found : " + String.valueOf(id));
                        if (id == null) {
                            throw new UnauthorizedResponse();
                        }
                    }
                });

        ItemController itemController = new ItemController();
        AuthController authController = new AuthController();

        app.get("/items", itemController::getAllItems);
        app.post("/items", itemController::createItem);
        app.post("/sign-in", authController::login);
        app.post("/sign-out", authController::logout);

        app.start(PORT);
    }
}
