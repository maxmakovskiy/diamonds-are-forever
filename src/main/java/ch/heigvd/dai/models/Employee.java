package ch.heigvd.dai.models;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class Employee {
  public int employeeId;

  public int counterpartId;

  @ColumnName("name")
  public String counterpartName;

  public String firstName;
  public String lastName;
  public String email;
  public boolean isTmpPassword;
  public byte[] password;
  public String role; // enum
  public boolean isActive;
}
