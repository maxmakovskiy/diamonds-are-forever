package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.database.WhiteDiamondDao;
import ch.heigvd.dai.models.Item;
import ch.heigvd.dai.models.WhiteDiamond;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jdbi.v3.core.Handle;

public class WhiteDiamondController {
    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        WhiteDiamondDao dao = Database.getInstance().jdbi.onDemand(WhiteDiamondDao.class);
        WhiteDiamond wd = dao.findByLotId(id);

        if (wd == null) {
            throw new NotFoundResponse();
        }

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
            ctx.json(created);
            ctx.status(201);
        }
    }

    public void update(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

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
            // WhiteDiamond wd = ctx.bodyValidator(WhiteDiamond.class).get();

            if (wd.stockName != null || wd.purchaseDate != null || wd.origin != null) {
                Item updatedItem = new Item();
                updatedItem.lotId = id;
                updatedItem.stockName = wd.stockName != null ? wd.stockName : item.stockName;
                updatedItem.purchaseDate =
                        wd.purchaseDate != null ? wd.purchaseDate : item.purchaseDate;
                updatedItem.origin = wd.origin != null ? wd.origin : item.origin;
                itemDao.updateItem(updatedItem);
            }

            wdDao.updateWhiteDiamond(
                    id,
                    wd.weightCt,
                    wd.shape,
                    wd.length,
                    wd.width,
                    wd.depth,
                    wd.whiteScale,
                    wd.clarity);

            // TODO:
            // Return updated WhiteDiamond
            // example ActionController::update
            WhiteDiamond created = wdDao.findByLotId(id);
            ctx.json(created);
            ctx.status(200);
        }
    }
}
