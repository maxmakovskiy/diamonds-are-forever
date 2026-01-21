package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ColoredDiamondDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.models.ColoredDiamond;
import ch.heigvd.dai.models.Item;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import org.jdbi.v3.core.Handle;

public class ColoredDiamondController {
    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        ColoredDiamondDao dao = Database.getInstance().jdbi.onDemand(ColoredDiamondDao.class);
        ColoredDiamond cd = dao.findByLotId(id);

        if (cd == null) {
            throw new NotFoundResponse();
        }

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
            ctx.json(created);
            ctx.status(201);
        }
    }

    public void update(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

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
            // WhiteDiamondDao wdDao = handle.attach(WhiteDiamondDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);
            Item item = itemDao.getItemByLotId(id);
            if (item == null) {
                throw new NotFoundResponse();
            }
            ColoredDiamondDao cdd = handle.attach(ColoredDiamondDao.class);
            // WhiteDiamond wd = ctx.bodyValidator(WhiteDiamond.class).get();

            // update item first then colored diamonds specific field

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

            // TODO:
            // Return updated WhiteDiamond
            // example ActionController::update
            ColoredDiamond updated = cdd.findByLotId(id);
            ctx.json(updated);
            ctx.status(200);
        }
    }
}
