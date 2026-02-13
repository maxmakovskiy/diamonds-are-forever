package ch.heigvd.dai.models;

public class Employee {
  public int employeeId;
  public int counterpartId;
  public String firstName;
  public String lastName;
  public String email;
  public boolean isTmpPassword;
  public byte[] password;
  public String role; // enum
  public boolean isActive;
}
