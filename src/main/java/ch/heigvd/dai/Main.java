package ch.heigvd.dai;

import ch.heigvd.dai.item.Item;
import ch.heigvd.dai.item.ItemDao;
import io.javalin.Javalin;
import java.util.List;

public class Main {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        Javalin app = Javalin.create();
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
