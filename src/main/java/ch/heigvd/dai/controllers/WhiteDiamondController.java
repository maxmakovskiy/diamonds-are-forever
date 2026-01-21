package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.database.WhiteDiamondDao;
import ch.heigvd.dai.models.Item;
import ch.heigvd.dai.models.WhiteDiamond;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.NotModifiedResponse;
import io.javalin.http.PreconditionFailedResponse;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentMap;
import org.jdbi.v3.core.Handle;

public class WhiteDiamondController {
    private final ConcurrentMap<Integer, LocalDateTime> itemsCache;

    public WhiteDiamondController(ConcurrentMap<Integer, LocalDateTime> itemsCache) {
        this.itemsCache = itemsCache;
    }

    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        LocalDateTime lastKnownModification =
                ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);

        if (lastKnownModification != null && itemsCache.get(id).equals(lastKnownModification)) {
            throw new NotModifiedResponse();
        }

        WhiteDiamondDao dao = Database.getInstance().jdbi.onDemand(WhiteDiamondDao.class);
        WhiteDiamond wd = dao.findByLotId(id);

        if (wd == null) {
            throw new NotFoundResponse();
        }

        LocalDateTime now;
        if (itemsCache.containsKey(wd.lotId)) {
            now = itemsCache.get(wd.lotId);
        } else {
            now = LocalDateTime.now();
            itemsCache.put(wd.lotId, now);
        }
        ctx.header("Last-Modified", String.valueOf(now));

        ctx.json(wd);
        ctx.status(200);
    }

    public void create(Context ctx) {
        WhiteDiamond wd =
                ctx.bodyValidator(WhiteDiamond.class)
                        .check(obj -> obj.stockName != null, "Missing stock name")
                        .check(obj -> obj.purchaseDate != null, "Missing purchase date")
                        .check(obj -> obj.origin != null, "Missing origin")
                        .check(obj -> obj.weightCt > 0, "Weight must be positive")
                        .check(obj -> obj.shape != null, "Missing shape")
                        .check(obj -> obj.length > 0, "Length must be positive")
                        .check(obj -> obj.width > 0, "Width must be positive")
                        .check(obj -> obj.depth > 0, "Depth must be positive")
                        .check(obj -> obj.whiteScale != null, "Missing white Scale")
                        .check(obj -> obj.clarity != null, "Missing clarity")
                        .get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            WhiteDiamondDao wdDao = handle.attach(WhiteDiamondDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);

            // WhiteDiamond item = ctx.bodyValidator(WhiteDiamond.class).get();
            int lotId =
                    itemDao.insertItem(wd.stockName, wd.purchaseDate, wd.origin, "white diamond");
            wd.lotId = lotId;

            wdDao.insertWhiteDiamond(wd);

            WhiteDiamond created = wdDao.findByLotId(wd.lotId);

            LocalDateTime now = LocalDateTime.now();
            itemsCache.put(created.lotId, now);
            itemsCache.remove(ItemController.RESERVED_ID_TO_ALL_ITEMS);
            ctx.header("Last-Modified", String.valueOf(now));

            ctx.json(created);
            ctx.status(201);
        }
    }

    public void update(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        LocalDateTime lastKnownModification =
                ctx.headerAsClass("If-Unmodified-Since", LocalDateTime.class).getOrDefault(null);

        if (lastKnownModification != null && !itemsCache.get(id).equals(lastKnownModification)) {
            throw new PreconditionFailedResponse();
        }

        WhiteDiamond wd =
                ctx.bodyValidator(WhiteDiamond.class)
                        .check(obj -> obj.stockName != null, "Missing stock name")
                        .check(obj -> obj.purchaseDate != null, "Missing purchase date")
                        .check(obj -> obj.origin != null, "Missing origin")
                        .check(obj -> obj.weightCt > 0, "Weight must be positive")
                        .check(obj -> obj.shape != null, "Missing shape")
                        .check(obj -> obj.length > 0, "Length must be positive")
                        .check(obj -> obj.width > 0, "Width must be positive")
                        .check(obj -> obj.depth > 0, "Depth must be positive")
                        .check(obj -> obj.clarity != null, "Missing clarity")
                        .get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            WhiteDiamondDao wdDao = handle.attach(WhiteDiamondDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);

            Item item = itemDao.getItemByLotId(id);
            if (item == null) {
                throw new NotFoundResponse();
            }

            Item updatedItem = new Item();
            updatedItem.lotId = id;
            updatedItem.stockName = wd.stockName != null ? wd.stockName : item.stockName;
            updatedItem.purchaseDate =
                    wd.purchaseDate != null ? wd.purchaseDate : item.purchaseDate;
            updatedItem.origin = wd.origin != null ? wd.origin : item.origin;
            itemDao.updateItem(updatedItem);

            wdDao.updateWhiteDiamond(
                    id,
                    wd.weightCt,
                    wd.shape,
                    wd.length,
                    wd.width,
                    wd.depth,
                    wd.whiteScale,
                    wd.clarity);

            WhiteDiamond updated = wdDao.findByLotId(id);

            LocalDateTime now = LocalDateTime.now();
            itemsCache.put(updated.lotId, now);
            itemsCache.remove(ItemController.RESERVED_ID_TO_ALL_ITEMS);
            ctx.header("Last-Modified", String.valueOf(now));

            ctx.json(updated);
            ctx.status(200);
        }
    }
}
