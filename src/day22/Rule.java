package day22;

import java.util.Objects;

public class Rule {
    public final int xMin;
    public final int xMax;
    public final int yMin;
    public final int yMax;
    public final int zMin;
    public final int zMax;
    public final boolean isOn;

    public Rule(int xMin, int xMax, int yMin, int yMax, int zMin, int zMax, boolean isOn) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
        this.isOn = isOn;
    }

    public long totalCount() {
        return (long) (xMax - xMin + 1) * (yMax - yMin + 1) * (zMax - zMin + 1);
    }

    @Override
    public String toString() {
        return "Rule{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                ", zMin=" + zMin +
                ", zMax=" + zMax +
                ", isOn=" + isOn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Rule rule = (Rule) o;
        return xMin == rule.xMin && xMax == rule.xMax && yMin == rule.yMin && yMax == rule.yMax && zMin == rule.zMin && zMax == rule.zMax && isOn == rule.isOn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xMin, xMax, yMin, yMax, zMin, zMax, isOn);
    }
}
