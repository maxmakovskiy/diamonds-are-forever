package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.ItemDao;
import ch.heigvd.dai.database.WhiteDiamondDao;
import ch.heigvd.dai.models.WhiteDiamond;
import io.javalin.http.Context;

public class WhiteDiamondController {
    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();
        WhiteDiamondDao dao = Database.getInstance().jdbi.onDemand(WhiteDiamondDao.class);
        WhiteDiamond wd = dao.findByLotId(id);
        ctx.json(wd);
        ctx.status(200);
    }

    public void create(Context ctx) {
        WhiteDiamondDao wdDao = Database.getInstance().jdbi.onDemand(WhiteDiamondDao.class);
        ItemDao itemDao = Database.getInstance().jdbi.onDemand(ItemDao.class);

        WhiteDiamond item = ctx.bodyValidator(WhiteDiamond.class).get();
        item.lotId = itemDao.insertItem(item.stockName, item.purchaseDate, item.origin, item.type);
        wdDao.insertWhiteDiamond(item);
        ctx.status(201);
    }

    //        public void update(Context ctx) {
    //            ctx.status(200);
    //        }
    //
    //        public void delete(Context ctx) {
    //            ctx.status(200);
    //        }

}
