package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ColoredDiamondDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.ColoredDiamond;
import ch.heigvd.dai.models.Item;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.NotModifiedResponse;
import io.javalin.http.PreconditionFailedResponse;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentMap;
import org.jdbi.v3.core.Handle;

public class ColoredDiamondController {
    private final ConcurrentMap<Integer, LocalDateTime> itemsCache;

    public ColoredDiamondController(ConcurrentMap<Integer, LocalDateTime> itemsCache) {
        this.itemsCache = itemsCache;
    }

    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        LocalDateTime lastKnownModification =
                ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);

        if (lastKnownModification != null && itemsCache.get(id).equals(lastKnownModification)) {
            throw new NotModifiedResponse();
        }

        ColoredDiamondDao dao = Database.getInstance().jdbi.onDemand(ColoredDiamondDao.class);
        ColoredDiamond cd = dao.findByLotId(id);

        if (cd == null) {
            throw new NotFoundResponse();
        }

        LocalDateTime now;
        if (itemsCache.containsKey(cd.lotId)) {
            now = itemsCache.get(cd.lotId);
        } else {
            now = LocalDateTime.now();
            itemsCache.put(cd.lotId, now);
        }
        ctx.header("Last-Modified", String.valueOf(now));

        ctx.json(cd);
        ctx.status(200);
    }

    public void create(Context ctx) {
        ColoredDiamond cd =
                ctx.bodyValidator(ColoredDiamond.class)
                        .check(obj -> obj.stockName != null, "Missing stock name")
                        .check(obj -> obj.purchaseDate != null, "Missing purchase date")
                        .check(obj -> obj.origin != null, "Missing origin")
                        .check(obj -> obj.weightCt > 0, "Weight must be positive")
                        .check(obj -> obj.shape != null, "Missing shape")
                        .check(obj -> obj.length > 0, "Length must be positive")
                        .check(obj -> obj.width > 0, "Width must be positive")
                        .check(obj -> obj.depth > 0, "Depth must be positive")
                        .check(obj -> obj.gemType != null, "Missing gem type")
                        .check(obj -> obj.fancyIntensity != null, "Missing fancy intensity")
                        .check(obj -> obj.fancyOvertone != null, "Missing fancy overtone")
                        .check(obj -> obj.fancyColor != null, "Missing fancy color")
                        .check(obj -> obj.clarity != null, "Missing clarity")
                        .get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ColoredDiamondDao cdd = handle.attach(ColoredDiamondDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);

            // WhiteDiamond item = ctx.bodyValidator(WhiteDiamond.class).get(); -> should add some
            // erorr msg ?
            int lotId =
                    itemDao.insertItem(cd.stockName, cd.purchaseDate, cd.origin, "colored diamond");
            cd.lotId = lotId;

            cdd.insertColoredDiamond(cd);

            ColoredDiamond created = cdd.findByLotId(lotId);

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

        ColoredDiamond cd =
                ctx.bodyValidator(ColoredDiamond.class)
                        .check(obj -> obj.stockName != null, "Missing stock name")
                        .check(obj -> obj.purchaseDate != null, "Missing purchase date")
                        .check(obj -> obj.origin != null, "Missing origin")
                        .check(obj -> obj.weightCt > 0, "Weight must be positive")
                        .check(obj -> obj.shape != null, "Missing shape")
                        .check(obj -> obj.length > 0, "Length must be positive")
                        .check(obj -> obj.width > 0, "Width must be positive")
                        .check(obj -> obj.depth > 0, "Depth must be positive")
                        .check(obj -> obj.gemType != null, "Missing gem type")
                        .check(obj -> obj.fancyIntensity != null, "Missing fancy intensity")
                        .check(obj -> obj.fancyOvertone != null, "Missing fancy overtone")
                        .check(obj -> obj.fancyColor != null, "Missing fancy color")
                        .check(obj -> obj.clarity != null, "Missing clarity")
                        .get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ItemDao itemDao = handle.attach(ItemDao.class);
            Item item = itemDao.getItemByLotId(id);
            if (item == null) {
                throw new NotFoundResponse();
            }
            ColoredDiamondDao cdd = handle.attach(ColoredDiamondDao.class);

            Item updatedItem = new Item();
            updatedItem.lotId = id;
            updatedItem.stockName = cd.stockName != null ? cd.stockName : item.stockName;
            updatedItem.purchaseDate =
                    cd.purchaseDate != null ? cd.purchaseDate : item.purchaseDate;
            updatedItem.origin = cd.origin != null ? cd.origin : item.origin;
            itemDao.updateItem(updatedItem);

            cdd.updateColoredDiamond(
                    id,
                    cd.weightCt,
                    cd.shape,
                    cd.length,
                    cd.width,
                    cd.depth,
                    cd.gemType,
                    cd.fancyIntensity,
                    cd.fancyOvertone,
                    cd.fancyColor,
                    cd.clarity);

            ColoredDiamond updated = cdd.findByLotId(id);

            LocalDateTime now = LocalDateTime.now();
            itemsCache.put(updated.lotId, now);
            itemsCache.remove(ItemController.RESERVED_ID_TO_ALL_ITEMS);
            ctx.header("Last-Modified", String.valueOf(now));

            ctx.json(updated);
            ctx.status(200);
        }
    }
}
