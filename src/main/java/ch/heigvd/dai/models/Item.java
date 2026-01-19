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

    public Item() {}

    public Item(
            int lotId,
            String stockName,
            OffsetDateTime purchaseDate,
            String origin,
            String type,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.lotId = lotId;
        this.stockName = stockName;
        this.purchaseDate = purchaseDate;
        this.origin = origin;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
