package day19;

import java.util.*;
import java.util.stream.Collectors;

public class ScanReportMapper {
    private final Map<Coordinate, Map<Coordinate, Double>> distanceMap = new HashMap<>();

    public Set<Coordinate> mapReports(List<ScanReport> input) throws Exception {
        distanceMap.clear();

        // link scan reports to each other
        linkScanReports(input);

        Set<Coordinate> allMappedBeacons = new HashSet<>();
        Set<ScanReport> mappedScanReports = new HashSet<>();
        Set<ScanReport> unmappedScanReports = new HashSet<>(input);

        // scanner 0 is at 0,0,0 and is oriented as +x,+y,+z
        input.get(0).getBeacons().forEach(beacon -> addBeacon(beacon, allMappedBeacons));
        input.get(0).setPosition(new Coordinate(0, 0, 0));
        mappedScanReports.add(input.get(0));
        unmappedScanReports.remove(input.get(0));

        while (!unmappedScanReports.isEmpty()) {
            mapOne(mappedScanReports, unmappedScanReports, allMappedBeacons);
        }

        return allMappedBeacons;
    }

    private void mapOne(Set<ScanReport> mappedScanReports, Set<ScanReport> unmappedScanReports, Set<Coordinate> allMappedBeacons) throws Exception {
        // find an unmapped scan report that is linked to an already mapped scan report
        for (ScanReport unmapped : unmappedScanReports) {
            for (ScanReport overlapping : unmapped.getOverlapsWith()) {
                if (mappedScanReports.contains(overlapping)) {
                    mapScanReports(unmapped, overlapping, allMappedBeacons, mappedScanReports, unmappedScanReports);
                    return;
                }
            }
        }

        throw new Exception("could not find a new mapping");
    }

    private void linkScanReports(List<ScanReport> input) {
        for (int i = 0; i < input.size(); ++i) {
            for (int j = i + 1; j < input.size(); ++j) {
                int finalJ = j;
                long count = input.get(i).getAllDistances().stream().filter(d -> input.get(finalJ).getAllDistances().stream().anyMatch(d2 -> doubleEquals(d, d2))).count();

                if (count >= 66) {
                    input.get(i).getOverlapsWith().add(input.get(j));
                    input.get(j).getOverlapsWith().add(input.get(i));
                }
            }
        }
    }

    private void mapScanReports(ScanReport unmapped, ScanReport mapped, Set<Coordinate> allBeacons, Set<ScanReport> mappedScanReports, Set<ScanReport> unmappedScanReports) throws Exception {
        // loop through each beacon...
        for (Coordinate b1 : unmapped.getBeacons()) {
            // loop through each already mapped beacon
            for (Coordinate b2 : mapped.getBeacons()) {
                Map<Coordinate, Coordinate> correspondingBeacons = new HashMap<>();
                correspondingBeacons.put(b1, b2);

                // map beacons around b1 to beacons around b2
                for (Map.Entry<Coordinate, Double> e : unmapped.getDistanceMap().get(b1).entrySet()) {
                    Coordinate b1b = e.getKey();
                    Double d = e.getValue();
                    Optional<Map.Entry<Coordinate, Double>> match = mapped.getDistanceMap().get(b2).entrySet().stream().filter(entry -> doubleEquals(d, entry.getValue())).findFirst();
                    match.ifPresent(beaconDoubleEntry -> correspondingBeacons.put(b1b, beaconDoubleEntry.getKey()));
                }

                if (correspondingBeacons.size() >= 12) {
                    positionAndOrientateScanner(unmapped, correspondingBeacons, allBeacons);
                    mappedScanReports.add(unmapped);
                    unmappedScanReports.remove(unmapped);
                    return;
                }
            }
        }

        throw new Exception("Could not map reports");
    }

    private void positionAndOrientateScanner(ScanReport scanReport, Map<Coordinate, Coordinate> correspondingBeacons, Set<Coordinate> mappedBeacons) throws Exception {
        Map.Entry<Coordinate, Coordinate> first = correspondingBeacons.entrySet().stream().findFirst().orElseThrow();
        for (Transformations.Transformation transformation : Transformations.Transformations) {// transform first beacon without position arguments to determine position
            Coordinate transformed = transformation.transform(first.getKey(), 0, 0, 0);

            int dX = first.getValue().getX() - transformed.getX();
            int dY = first.getValue().getY() - transformed.getY();
            int dZ = first.getValue().getZ() - transformed.getZ();

            // check if all beacon positions match
            boolean allMatch = correspondingBeacons.entrySet().stream().allMatch(entry -> transformation.transform(entry.getKey(), dX, dY, dZ).equals(entry.getValue()));

            if (allMatch) {
                // set position of this scanner
                scanReport.setPosition(new Coordinate(dX, dY, dZ));

                // map coordinates of beacons relative to scanner 0 and add them to the list of mapped beacons
                Set<Coordinate> updatedBeacons = scanReport.getBeacons().stream().map(beacon -> transformation.transform(beacon, dX, dY, dZ)).collect(Collectors.toSet());
                updatedBeacons.forEach(beacon -> addBeacon(beacon, mappedBeacons));
                scanReport.updateBeacons(updatedBeacons);

                return;
            }
        }

        throw new Exception("could not position scanner");
    }

    private void addBeacon(Coordinate beacon, Set<Coordinate> mappedBeacons) {
        if (mappedBeacons.contains(beacon)) {
            return;
        }

        distanceMap.put(beacon, new HashMap<>());
        mappedBeacons.forEach(b -> {
            double distance = Distance.calculateDistance(beacon, b);
            distanceMap.get(b).put(beacon, distance);
            distanceMap.get(beacon).put(b, distance);
        });

        mappedBeacons.add(beacon);
    }

    private boolean doubleEquals(Double d1, Double d2) {
        return Math.abs(d1 - d2) < 0.000001;
    }
}
