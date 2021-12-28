package day22;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final Pattern rulePattern = Pattern.compile("^(on|off) x=(-?\\d+)\\.\\.(-?\\d+),y=(-?\\d+)\\.\\.(-?\\d+),z=(-?\\d+)\\.\\.(-?\\d+)$");

    public static void main(String[] args) throws Exception {
        List<Rule> rules = readInput();

        calculate(rules, true);
        calculate(rules, false);
    }

    private static void calculate(List<Rule> rules, boolean initialOnly) {
        List<Rule> onRegions = new ArrayList<>();
        for (Rule rule : rules.stream()
                .filter(r -> !initialOnly || (r.xMin >= -50 && r.xMax <= 50 && r.yMin >= -50 && r.yMax <= 50 && r.zMin >= -50 && r.zMax <= 50))
                .collect(Collectors.toList())) {
            if (rule.isOn) {
                addRegion(onRegions, rule);
            } else {
                removeRegion(onRegions, rule);
            }
        }

        System.out.println(onRegions.stream().map(Rule::totalCount).reduce(Long::sum).orElseThrow());
    }

    private static void addRegion(List<Rule> regions, Rule rule) {
        if (regions.stream().anyMatch(r -> isInside(rule, r))) {
            // completely inside an already on region, ignore this rule
            return;
        }

        if (regions.stream().allMatch(r -> haveNoOverlap(r, rule))) {
            // overlaps with nothing
            regions.add(rule);
        } else {
            // partly overlaps...
            List<Rule> overlappingRules = regions.stream().filter(r -> !isInside(rule, r) && !haveNoOverlap(rule, r)).collect(Collectors.toList());

            overlappingRules.stream().findFirst().ifPresent(overlappingRule -> {
                List<Rule> splitRule = removeFromRegion(rule, overlappingRule);
                splitRule.forEach(s -> addRegion(regions, s));
            });
        }
    }

    private static void removeRegion(List<Rule> regions, Rule rule) {
        List<Rule> overlappingRegions = regions.stream().filter(r -> !haveNoOverlap(r, rule)).collect(Collectors.toList());

        if (overlappingRegions.isEmpty()) {
            return;
        }

        overlappingRegions.forEach(overlappingRegion -> {
            if (isInside(overlappingRegion, rule)) {
                // on is completely inside off -> remove region
                regions.remove(overlappingRegion);
            } else {
                regions.remove(overlappingRegion);
                regions.addAll(removeFromRegion(overlappingRegion, rule));
            }
        });
    }

    private static boolean haveNoOverlap(Rule r1, Rule r2) {
        return r1.xMin > r2.xMax || r1.xMax < r2.xMin || r1.yMin > r2.yMax || r1.yMax < r2.yMin || r1.zMin > r2.zMax || r1.zMax < r2.zMin;
    }

    private static boolean isInside(Rule r1, Rule r2) {
        return r1.xMin >= r2.xMin && r1.xMax <= r2.xMax && r1.yMin >= r2.yMin && r1.yMax <= r2.yMax && r1.zMin >= r2.zMin && r1.zMax <= r2.zMax;
    }

    private static List<Rule> removeFromRegion(Rule original, Rule remove) {
        List<Rule> result = new ArrayList<>();

        List<int[]> xSectors = getSectors(original.xMin, original.xMax, remove.xMin, remove.xMax);
        List<int[]> ySectors = getSectors(original.yMin, original.yMax, remove.yMin, remove.yMax);
        List<int[]> zSectors = getSectors(original.zMin, original.zMax, remove.zMin, remove.zMax);

        for (int xI = 0; xI < xSectors.size(); ++xI) {
            if (xSectors.get(xI) == null) {
                continue;
            }
            for (int yI = 0; yI < ySectors.size(); ++yI) {
                if (ySectors.get(yI) == null) {
                    continue;
                }
                for (int zI = 0; zI < zSectors.size(); ++zI) {
                    if (zSectors.get(zI) == null) {
                        continue;
                    }

                    if (xI == 1 && yI == 1 && zI == 1) {
                        continue;
                    }

                    result.add(new Rule(
                            xSectors.get(xI)[0], xSectors.get(xI)[1],
                            ySectors.get(yI)[0], ySectors.get(yI)[1],
                            zSectors.get(zI)[0], zSectors.get(zI)[1],
                            true));
                }
            }
        }

        return result;
    }

    private static List<int[]> getSectors(int originalMin, int originalMax, int removeMin, int removeMax) {
        List<int[]> result = new ArrayList<>();

        // first
        if (removeMin <= originalMin) {
            result.add(null);
        } else {
            result.add(new int[] { originalMin, removeMin - 1 });
        }

        // second
        result.add(new int[] { Math.max(removeMin, originalMin), Math.min(removeMax, originalMax) });

        // last
        if (removeMax >= originalMax) {
            result.add(null);
        } else {
            result.add(new int[] { removeMax + 1, originalMax });
        }

        return result;
    }

    private static List<Rule> readInput() throws Exception {
        return Files.lines(Paths.get("src/day22/input.txt")).map(line -> {
            Matcher m = rulePattern.matcher(line);

            if (!m.matches()) {
                System.out.println("invalid rule " + line);
                return null;
            }

            return new Rule(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(5)),
                    Integer.parseInt(m.group(6)), Integer.parseInt(m.group(7)), m.group(1).equals("on"));
        }).collect(Collectors.toList());
    }
}