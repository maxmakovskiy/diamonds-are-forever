package ch.heigvd.dai;

import ch.heigvd.dai.item.Item;
import ch.heigvd.dai.item.ItemDao;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import java.util.List;

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
                        });

        app.get(
                "/items",
                ctx -> {
                    ItemDao dao = Database.getInstance().jdbi.onDemand(ItemDao.class);
                    List<Item> items = dao.getAllItems();
                    ctx.json(items);
                    ctx.status(200);
                });
        app.post(
                "/items",
                ctx -> {
                    ItemDao dao = Database.getInstance().jdbi.onDemand(ItemDao.class);

                    Item item = ctx.bodyValidator(Item.class).get();

                    dao.insertItem(item.stockName, item.purchaseDate, item.origin, item.type);
                    ctx.status(201);
                });
        app.start(PORT);
    }
}
