package ch.heigvd.dai.models;

import java.time.OffsetDateTime;

public class WhiteDiamond {
    public int lotId;
    public String stockName;
    public OffsetDateTime purchaseDate;
    public String origin;
    public String type;
    public OffsetDateTime createdAt;
    public OffsetDateTime updatedAt;
    public double weightCt;
    public String shape;
    public double length;
    public double width;
    public double depth;
    public String gemType;
    public String whiteScale;
    public String clarity;

    @Override
    public String toString() {
        return "White diamond: "
                + String.join(
                        ", ",
                        String.valueOf(weightCt),
                        shape,
                        String.valueOf(length),
                        String.valueOf(width),
                        String.valueOf(depth),
                        whiteScale,
                        clarity);
    }
}
