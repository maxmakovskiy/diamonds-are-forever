package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ActionDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.Item;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import java.util.List;
import org.jdbi.v3.core.Handle;

public class ItemController {
    public void getAllItems(Context ctx) {
        String isAvailable = ctx.queryParam("isAvailable");
        boolean filterAvailable = "true".equalsIgnoreCase(isAvailable);

        String type = ctx.queryParam("type");

        ItemDao dao = Database.getInstance().jdbi.onDemand(ItemDao.class);

        List<Item> items;
        if (type != null) {
            items = dao.getItemsByType(type);
        } else if (filterAvailable) {
            items = dao.getAvailableItems();
        } else {
            items = dao.getAllItems();
        }

        ctx.json(items);
        ctx.status(200);
    }

    public void deleteItem(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ItemDao itemDao = handle.attach(ItemDao.class);
            Item item = itemDao.getItemByLotId(id);

            if (item == null) {
                throw new NotFoundResponse();
            }

            // delete first all action (cascade) then the item
            ActionDao actionDao = handle.attach(ActionDao.class);
            actionDao.deleteActionsForItem(id);
            itemDao.deleteItem(id);
            ctx.status(200);
        }
    }
}
