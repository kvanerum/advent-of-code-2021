package day19;

import java.util.*;

public class ScanReport {
    private final int id;
    private final List<Coordinate> beacons;
    private final Set<Double> allDistances;
    private final Map<Coordinate, Map<Coordinate, Double>> distanceMap;
    private Coordinate position;
    private final Set<ScanReport> overlapsWith = new HashSet<>();

    public ScanReport(final int id, final List<Coordinate> beacons) {
        this.id = id;
        this.beacons = new ArrayList<>(beacons);

        this.distanceMap = new HashMap<>();
        this.allDistances = new HashSet<>();
        this.calculateDistances();
    }

    private void calculateDistances() {
        for (int i = 0; i < this.beacons.size(); ++i) {
            Coordinate b1 = this.beacons.get(i);
            for (int j = i + 1; j < this.beacons.size(); ++j) {
                Coordinate b2 = this.beacons.get(j);
                double d = Distance.calculateDistance(b1, b2);
                allDistances.add(d);
                this.distanceMap.computeIfAbsent(b1, (k) -> new HashMap<>()).put(b2, d);
                this.distanceMap.computeIfAbsent(b2, (k) -> new HashMap<>()).put(b1, d);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScanReport report = (ScanReport) o;
        return id == report.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public List<Coordinate> getBeacons() {
        return this.beacons;
    }

    public void updateBeacons(Collection<Coordinate> newBeacons) {
        this.beacons.clear();
        this.beacons.addAll(newBeacons);
        this.calculateDistances();
    }

    public Map<Coordinate, Map<Coordinate, Double>> getDistanceMap() {
        return distanceMap;
    }

    public Set<Double> getAllDistances() {
        return allDistances;
    }

    public void setPosition(Coordinate position) {
        this.position = position;
    }

    public Coordinate getPosition() {
        return position;
    }

    public Set<ScanReport> getOverlapsWith() {
        return overlapsWith;
    }
}
