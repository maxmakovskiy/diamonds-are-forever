package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.Item;
import io.javalin.http.Context;
import java.util.List;

public class ItemController {
    public void getAllItems(Context ctx) {
        ItemDao dao = Database.getInstance().jdbi.onDemand(ItemDao.class);
        List<Item> items = dao.getAllItems();
        ctx.json(items);
        ctx.status(200);
    }

    public void createItem(Context ctx) {
        ItemDao dao = Database.getInstance().jdbi.onDemand(ItemDao.class);
        Item item = ctx.bodyValidator(Item.class).get();
        dao.insertItem(item.stockName, item.purchaseDate, item.origin, item.type);
        ctx.status(201);
    }
}
