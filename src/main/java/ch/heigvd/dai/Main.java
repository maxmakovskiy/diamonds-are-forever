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
    private static final int PORT = Integer.parseInt(System.getenv("DIAMONDS_APP_PORT"));

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

        app.get(
                "/",
                ctx -> {
                    ctx.redirect("/items");
                },
                Role.ANYONE);

        AuthController authController = new AuthController();
        app.post("/sign-in", authController::login, Role.ANYONE);
        app.post("/sign-out", authController::logout, Role.AUTHENTICATED);

        ItemController itemController = new ItemController();
        app.get("/items", itemController::getAllItems, Role.ANYONE);

        WhiteDiamondController whiteDiamondController = new WhiteDiamondController();
        app.get("/white-diamonds/{id}", whiteDiamondController::getOne, Role.ANYONE);
        app.put("/white-diamonds/{id}", whiteDiamondController::update, Role.ANYONE);
        app.post("/white-diamonds", whiteDiamondController::create, Role.AUTHENTICATED);

        ColoredDiamondController coloredDiamondController = new ColoredDiamondController();
        app.get("/colored-diamonds/{id}", coloredDiamondController::getOne, Role.ANYONE);

        ColoredGemstoneController coloredGemstoneController = new ColoredGemstoneController();
        app.get("/colored-gemstones/{id}", coloredGemstoneController::getOne, Role.ANYONE);

        ActionController actionController = new ActionController();
        app.get("/items/lifecycle/{id}", actionController::getAllForItem, Role.ANYONE);
        app.delete("/actions/{id}", actionController::delete, Role.AUTHENTICATED);
        app.post("/actions", actionController::create, Role.AUTHENTICATED);

        app.start(PORT);
    }
}
