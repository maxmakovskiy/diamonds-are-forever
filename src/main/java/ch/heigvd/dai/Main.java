package ch.heigvd.dai;

import static ch.heigvd.dai.Session.fileSessionHandler;
import static ch.heigvd.dai.controllers.AuthController.USER_ID;

import ch.heigvd.dai.controllers.*;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.json.JavalinJackson;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

                            config.validation.register(LocalDateTime.class, LocalDateTime::parse);
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

        ConcurrentMap<Integer, LocalDateTime> itemsCache = new ConcurrentHashMap<>();
        ConcurrentMap<Integer, LocalDateTime> lifecyclesCache = new ConcurrentHashMap<>();

        AuthController authController = new AuthController();
        app.post("/sign-in", authController::login, Role.ANYONE);
        app.post("/sign-out", authController::logout, Role.AUTHENTICATED);
        app.get("/profile", authController::getProfile, Role.AUTHENTICATED);

        ItemController itemController = new ItemController(itemsCache, lifecyclesCache);
        app.get("/items", itemController::getAllItems, Role.ANYONE);
        app.delete("/items/{id}", itemController::deleteItem, Role.AUTHENTICATED);

        WhiteDiamondController whiteDiamondController = new WhiteDiamondController(itemsCache);
        app.get("/white-diamonds/{id}", whiteDiamondController::getOne, Role.ANYONE);
        app.put("/white-diamonds/{id}", whiteDiamondController::update, Role.AUTHENTICATED);
        app.post("/white-diamonds", whiteDiamondController::create, Role.AUTHENTICATED);

        ColoredDiamondController coloredDiamondController =
                new ColoredDiamondController(itemsCache);
        app.get("/colored-diamonds/{id}", coloredDiamondController::getOne, Role.ANYONE);
        app.put("/colored-diamonds/{id}", coloredDiamondController::update, Role.AUTHENTICATED);
        app.post("/colored-diamonds", coloredDiamondController::create, Role.AUTHENTICATED);

        ColoredGemstoneController coloredGemstoneController =
                new ColoredGemstoneController(itemsCache);
        app.get("/colored-gemstones/{id}", coloredGemstoneController::getOne, Role.ANYONE);
        app.put("/colored-gemstones/{id}", coloredGemstoneController::update, Role.AUTHENTICATED);
        app.post("/colored-gemstones", coloredGemstoneController::create, Role.AUTHENTICATED);

        ActionController actionController = new ActionController(lifecyclesCache);
        app.get("/items/lifecycle/{id}", actionController::getAllForItem, Role.ANYONE);
        app.delete("/actions/{id}", actionController::delete, Role.AUTHENTICATED);
        app.post("/actions", actionController::create, Role.AUTHENTICATED);

        app.start(PORT);
    }
}
