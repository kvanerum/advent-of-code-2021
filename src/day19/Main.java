package day19;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) throws Exception {
        List<ScanReport> input = ScanReportReader.read("src/day19/input.txt");

        ScanReportMapper scanReportMapper = new ScanReportMapper();
        Set<Coordinate> mappedBeacons = scanReportMapper.mapReports(input);

        // part 1
        System.out.println(mappedBeacons.size());

        part2(input);
    }

    private static void part2(List<ScanReport> input) {
        // calculate all Manhattan distances
        Long biggestDistance = -1L;
        for (int i = 0; i < input.size(); ++i) {
            for (int j = i + 1; j < input.size(); ++j) {
                long d = Distance.manhattanDistance(input.get(i).getPosition(), input.get(j).getPosition());

                if (d > biggestDistance) {
                    biggestDistance = d;
                }
            }
        }

        System.out.println(biggestDistance);
    }
}