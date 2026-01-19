package ch.heigvd.dai.models;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Action {
    public int actionId;
    public int fromCounterpartId;
    public int toCounterpartId;
    public String terms;
    public String category;
    public String shipNum;
    public LocalDate shipDate;
    public int lotId;
    public int employeeId;
    public double price;
    public String currencyCode;
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;
}
