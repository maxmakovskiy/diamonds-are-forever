package ch.heigvd.dai.models;

import java.time.OffsetDateTime;

public class ColoredGemstone {
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
    public String gemColor;
    public String treatment;

    @Override
    public String toString() {
        return "Colored diamond: "
                + String.join(
                        ", ",
                        String.valueOf(weightCt),
                        shape,
                        String.valueOf(length),
                        String.valueOf(width),
                        String.valueOf(depth),
                        gemType,
                        gemColor,
                        treatment);
    }
}
