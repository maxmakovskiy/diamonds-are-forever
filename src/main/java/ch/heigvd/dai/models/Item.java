package ch.heigvd.dai.models;

import java.time.OffsetDateTime;

public class Item {
    public int lotId;
    public String stockName;
    public OffsetDateTime purchaseDate;
    public String origin;
    public String type;
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;
}
