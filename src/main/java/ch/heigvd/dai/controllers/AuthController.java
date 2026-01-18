package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.EmployeeDao;
import ch.heigvd.dai.models.Employee;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.http.util.NaiveRateLimit;
import java.util.concurrent.TimeUnit;

public class AuthController {
    public static final String USER_ID = "USER_ID";
    public static final String USER_ROLE = "USER_ROLE";

    public void login(Context ctx) {
        NaiveRateLimit.requestPerTimeUnit(ctx, 20, TimeUnit.HOURS);

        Employee loginUser =
                ctx.bodyValidator(Employee.class)
                        .check(obj -> obj.email != null, "Missing email")
                        .get();

        System.out.println("User trying to login with email: " + loginUser.email);
        EmployeeDao dao = Database.getInstance().jdbi.onDemand(EmployeeDao.class);
        Employee user = dao.findByEmail(loginUser.email);

        if (user != null) {
            ctx.sessionAttribute(USER_ID, String.valueOf(user.employeeId));
            ctx.sessionAttribute(USER_ROLE, String.valueOf(user.role));
            ctx.status(HttpStatus.NO_CONTENT);
            return;
        }

        throw new UnauthorizedResponse();
    }

    public void logout(Context ctx) {
        ctx.req().getSession().invalidate();
        ctx.status(HttpStatus.NO_CONTENT);
    }

    //    public void profile(Context ctx) {
    //        String userIdCookie = ctx.cookie("user");
    //
    //        if (userIdCookie == null) {
    //            throw new UnauthorizedResponse();
    //        }
    //
    //        Integer userId = Integer.parseInt(userIdCookie);
    //
    //        Employee user = users.get(userId);
    //
    //        if (user == null) {
    //            throw new UnauthorizedResponse();
    //        }
    //
    //        ctx.json(user);
    //    }
}
