package day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ScanReportReader {
    public static List<ScanReport> read(String input) throws IOException {
        List<String> lines = Files.lines(Paths.get(input)).toList();

        List<ScanReport> result = new ArrayList<>();
        List<Coordinate> beaconBuffer = new ArrayList<>();

        int id = 0;

        for (String line : lines) {
            if (line.startsWith("---")) {
                if (!beaconBuffer.isEmpty()) {
                    // create scan report
                    result.add(new ScanReport(id, beaconBuffer));
                    beaconBuffer.clear();
                    id++;
                }
            } else if (!line.isBlank()) {
                String[] split = line.split(",");
                beaconBuffer.add(new Coordinate(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2])));
            }
        }

        if (!beaconBuffer.isEmpty()) {
            // create scan report
            result.add(new ScanReport(id, beaconBuffer));
            beaconBuffer.clear();
        }

        return result;
    }
}
