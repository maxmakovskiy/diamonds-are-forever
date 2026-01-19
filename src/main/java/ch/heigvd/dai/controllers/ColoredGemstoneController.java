package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.ColoredGemstoneDao;
import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.models.ColoredGemstone;
import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;

public class ColoredGemstoneController {
    public void getOne(Context ctx) {
        Integer id = ctx.pathParamAsClass("id", Integer.class).get();

        ColoredGemstoneDao dao = Database.getInstance().jdbi.onDemand(ColoredGemstoneDao.class);
        ColoredGemstone cgs = dao.findByLotId(id);

        if (cgs == null) {
            throw new NotFoundResponse();
        }

        ctx.json(cgs);
        ctx.status(200);
    }
}
