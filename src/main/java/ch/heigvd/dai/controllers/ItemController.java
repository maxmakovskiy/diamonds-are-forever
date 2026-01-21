package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ActionDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.Item;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.NotModifiedResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import org.jdbi.v3.core.Handle;

public class ItemController {
    private final ConcurrentMap<Integer, LocalDateTime> itemsCache;
    private final ConcurrentMap<Integer, LocalDateTime> lifecyclesCache;
    public static final Integer RESERVED_ID_TO_ALL_ITEMS = -1;

    public ItemController(
            ConcurrentMap<Integer, LocalDateTime> itemsCache,
            ConcurrentMap<Integer, LocalDateTime> lifecyclesCache) {
        this.itemsCache = itemsCache;
        this.lifecyclesCache = lifecyclesCache;
    }

    public void getAllItems(Context ctx) {
        String isAvailable = ctx.queryParam("isAvailable");
        boolean filterAvailable = "true".equalsIgnoreCase(isAvailable);
        String type = ctx.queryParam("type");

        LocalDateTime lastKnownModification =
                ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);

        if (lastKnownModification != null
                && itemsCache.containsKey(RESERVED_ID_TO_ALL_ITEMS)
                && itemsCache.get(RESERVED_ID_TO_ALL_ITEMS).equals(lastKnownModification)) {
            throw new NotModifiedResponse();
        }

        ItemDao dao = Database.getInstance().jdbi.onDemand(ItemDao.class);

        List<Item> items;
        if (type != null) {
            items = dao.getItemsByType(type);
        } else if (filterAvailable) {
            items = dao.getAvailableItems();
        } else {
            items = dao.getAllItems();
        }

        LocalDateTime now;
        if (itemsCache.containsKey(RESERVED_ID_TO_ALL_ITEMS)) {
            now = itemsCache.get(RESERVED_ID_TO_ALL_ITEMS);
        } else {
            now = LocalDateTime.now();
            itemsCache.put(RESERVED_ID_TO_ALL_ITEMS, now);
        }
        ctx.header("Last-Modified", String.valueOf(now));

        ctx.json(items);
        ctx.status(200);
    }

    public void deleteItem(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        // NOTE:
        // I don't think it is worth to check cache here
        // since it is a bit contre-intuitive
        // if item has been deleted it should be 'Not Found'

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

            // If item gets deleted
            // we should remove it from item cache,
            // and we should remove all adjacent action from their own cache
            lifecyclesCache.remove(id);
            itemsCache.remove(id);
            itemsCache.remove(RESERVED_ID_TO_ALL_ITEMS);

            ctx.status(200);
        }
    }
}
