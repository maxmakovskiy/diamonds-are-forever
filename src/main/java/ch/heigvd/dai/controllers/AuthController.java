package ch.heigvd.dai.controllers;

import ch.heigvd.dai.database.Database;
import ch.heigvd.dai.database.EmployeeDao;
import ch.heigvd.dai.models.Employee;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.http.util.NaiveRateLimit;
import jakarta.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

public class AuthController {
    public static final String USER_ID = "USER_ID";

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
            ctx.status(HttpStatus.NO_CONTENT);
            return;
        }

        throw new UnauthorizedResponse();
    }

    public void logout(Context ctx) {
        HttpSession s = ctx.req().getSession(false);
        if (s != null) {
            s.invalidate();
        }
        ctx.status(HttpStatus.NO_CONTENT);
    }

}
