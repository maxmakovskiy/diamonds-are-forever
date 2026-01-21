package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ColoredGemstoneDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.ColoredGemstone;
import ch.heigvd.dai.models.Item;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.http.NotModifiedResponse;
import io.javalin.http.PreconditionFailedResponse;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentMap;
import org.jdbi.v3.core.Handle;

public class ColoredGemstoneController {
    private final ConcurrentMap<Integer, LocalDateTime> itemsCache;

    public ColoredGemstoneController(ConcurrentMap<Integer, LocalDateTime> itemsCache) {
        this.itemsCache = itemsCache;
    }

    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        LocalDateTime lastKnownModification =
                ctx.headerAsClass("If-Modified-Since", LocalDateTime.class).getOrDefault(null);

        if (lastKnownModification != null && itemsCache.get(id).equals(lastKnownModification)) {
            throw new NotModifiedResponse();
        }

        ColoredGemstoneDao dao = Database.getInstance().jdbi.onDemand(ColoredGemstoneDao.class);
        ColoredGemstone cgs = dao.findByLotId(id);

        if (cgs == null) {
            throw new NotFoundResponse();
        }

        LocalDateTime now;
        if (itemsCache.containsKey(cgs.lotId)) {
            now = itemsCache.get(cgs.lotId);
        } else {
            now = LocalDateTime.now();
            itemsCache.put(cgs.lotId, now);
        }
        ctx.header("Last-Modified", String.valueOf(now));

        ctx.json(cgs);
        ctx.status(200);
    }

    public void create(Context ctx) {
        ColoredGemstone cgs =
                ctx.bodyValidator(ColoredGemstone.class)
                        .check(obj -> obj.stockName != null, "Missing stock name")
                        .check(obj -> obj.purchaseDate != null, "Missing purchase date")
                        .check(obj -> obj.origin != null, "Missing origin")
                        .check(obj -> obj.weightCt > 0, "Weight must be positive")
                        .check(obj -> obj.shape != null, "Missing shape")
                        .check(obj -> obj.length > 0, "Length must be positive")
                        .check(obj -> obj.width > 0, "Width must be positive")
                        .check(obj -> obj.depth > 0, "Depth must be positive")
                        .check(obj -> obj.gemType != null, "Missing gem type")
                        .check(obj -> obj.gemColor != null, "Missing gem color")
                        .check(obj -> obj.treatment != null, "Missing treatment")
                        .get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            ColoredGemstoneDao cgsd = handle.attach(ColoredGemstoneDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);

            int lotId =
                    itemDao.insertItem(
                            cgs.stockName, cgs.purchaseDate, cgs.origin, "colored gemstone");
            cgs.lotId = lotId;

            cgsd.insertColoredGemstone(cgs);

            ColoredGemstone created = cgsd.findByLotId(lotId);

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

        ColoredGemstone cgs =
                ctx.bodyValidator(ColoredGemstone.class)
                        .check(obj -> obj.stockName != null, "Missing stock name")
                        .check(obj -> obj.purchaseDate != null, "Missing purchase date")
                        .check(obj -> obj.origin != null, "Missing origin")
                        .check(obj -> obj.weightCt > 0, "Weight must be positive")
                        .check(obj -> obj.shape != null, "Missing shape")
                        .check(obj -> obj.length > 0, "Length must be positive")
                        .check(obj -> obj.width > 0, "Width must be positive")
                        .check(obj -> obj.depth > 0, "Depth must be positive")
                        .check(obj -> obj.gemType != null, "Missing gem type")
                        .check(obj -> obj.gemColor != null, "Missing gem color")
                        .check(obj -> obj.treatment != null, "Missing treatment")
                        .get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            // WhiteDiamondDao wdDao = handle.attach(WhiteDiamondDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);
            Item item = itemDao.getItemByLotId(id);
            if (item == null) {
                throw new NotFoundResponse();
            }
            ColoredGemstoneDao cgsd = handle.attach(ColoredGemstoneDao.class);
            // WhiteDiamond wd = ctx.bodyValidator(WhiteDiamond.class).get();

            // update item first then colored gems specific field

            Item updatedItem = new Item();
            updatedItem.lotId = id;
            updatedItem.stockName = cgs.stockName != null ? cgs.stockName : item.stockName;
            updatedItem.purchaseDate =
                    cgs.purchaseDate != null ? cgs.purchaseDate : item.purchaseDate;
            updatedItem.origin = cgs.origin != null ? cgs.origin : item.origin;
            itemDao.updateItem(updatedItem);

            cgsd.updateColoredGemstone(
                    id,
                    cgs.weightCt,
                    cgs.shape,
                    cgs.length,
                    cgs.width,
                    cgs.depth,
                    cgs.gemType,
                    cgs.gemColor,
                    cgs.treatment);

            ColoredGemstone updated = cgsd.findByLotId(id);

            LocalDateTime now = LocalDateTime.now();
            itemsCache.put(updated.lotId, now);
            itemsCache.remove(ItemController.RESERVED_ID_TO_ALL_ITEMS);
            ctx.header("Last-Modified", String.valueOf(now));

            ctx.json(updated);
            ctx.status(200);
        }
    }
}
