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
        try (Handle handle = Database.getInstance().jdbi.open()) {
            WhiteDiamondDao wdDao = handle.attach(WhiteDiamondDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);

            WhiteDiamond item = ctx.bodyValidator(WhiteDiamond.class).get();
            item.lotId =
                    itemDao.insertItem(item.stockName, item.purchaseDate, item.origin, item.type);
            wdDao.insertWhiteDiamond(item);

            ctx.status(201);
        }
    }

    public void update(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        try (Handle handle = Database.getInstance().jdbi.open()) {
            WhiteDiamondDao wdDao = handle.attach(WhiteDiamondDao.class);
            ItemDao itemDao = handle.attach(ItemDao.class);

            if (wdDao.findByLotId(id) == null) {
                throw new NotFoundResponse();
            }

            WhiteDiamond wd = ctx.bodyValidator(WhiteDiamond.class).get();
            itemDao.updateItem(
                    new Item(
                            id,
                            wd.stockName,
                            wd.purchaseDate,
                            wd.origin,
                            wd.type,
                            wd.createdAt,
                            wd.updatedAt));
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
            ctx.status(200);
        }
    }
}
