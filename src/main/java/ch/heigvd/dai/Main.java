package ch.heigvd.dai;

import static ch.heigvd.dai.Session.fileSessionHandler;
import static ch.heigvd.dai.controllers.AuthController.USER_ID;

import ch.heigvd.dai.controllers.*;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
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
                            config.jetty.modifyServletContextHandler(
                                    handler -> handler.setSessionHandler(fileSessionHandler()));
                        });

        app.beforeMatched(
                ctx -> {
                    if (ctx.routeRoles().contains(Role.AUTHENTICATED)) {
                        String id = ctx.sessionAttribute(USER_ID);
                        if (id == null) {
                            throw new UnauthorizedResponse();
                        }
                    }
                });

        AuthController authController = new AuthController();
        app.post("/sign-in", authController::login, Role.ANYONE);
        app.post("/sign-out", authController::logout, Role.AUTHENTICATED);

        ItemController itemController = new ItemController();
        app.get("/items", itemController::getAllItems, Role.ANYONE);

        WhiteDiamondController wd = new WhiteDiamondController();
        app.get("/white-diamonds/{id}", wd::getOne, Role.ANYONE);
        app.put("/white-diamonds/{id}", wd::update, Role.ANYONE);
        app.post("/white-diamonds", wd::create, Role.AUTHENTICATED);

        ColoredDiamondController cd = new ColoredDiamondController();
        app.get("/colored-diamonds/{id}", cd::getOne, Role.ANYONE);

        ActionController ac = new ActionController();
        app.get("/lifecycle/{id}", ac::getAllForItem, Role.ANYONE);

        app.start(PORT);
    }
}
