package ch.heigvd.dai.database;

import ch.heigvd.dai.models.Employee;
import org.jdbi.v3.sqlobject.config.RegisterFieldMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

@RegisterFieldMapper(Employee.class)
public interface EmployeeDao {
    @SqlQuery(
            """
            SELECT * FROM diamonds_are_forever.employee
            WHERE email = :email
            """)
    Employee findByEmail(@Bind("email") String email);
}
