package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ColoredDiamondDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.models.ColoredDiamond;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

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
}
