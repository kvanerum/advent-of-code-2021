package day16;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private static int bitIndex = 0;

    public static void main(String[] args) throws Exception {
        String input = readInput();
        String bits = toBits(input);
        Packet packet = parsePacket(bits);

        System.out.println(sumVersionNumbers(packet));
        System.out.println(evaluatePacket(packet));
    }

    private static int sumVersionNumbers(Packet packet) {
        return packet.version +
               packet.subPackets.stream().map(Main::sumVersionNumbers).reduce(Integer::sum).orElse(0);
    }

    private static BigInteger evaluatePacket(Packet packet) {
        Stream<BigInteger> subPackets = packet.subPackets.stream().map(Main::evaluatePacket);
        switch (packet.type) {
            case 0:
                return subPackets.reduce(BigInteger::add).orElseThrow();
            case 1:
                return subPackets.reduce(BigInteger.ONE, BigInteger::multiply);
            case 2:
                return subPackets.min(BigInteger::compareTo).orElseThrow();
            case 3:
                return subPackets.max(BigInteger::compareTo).orElseThrow();
            case 4:
                return packet.value;
            case 5:
                List<BigInteger> t = subPackets.collect(Collectors.toList());
                return t.get(0).compareTo(t.get(1)) > 0 ? BigInteger.ONE : BigInteger.ZERO;
            case 6:
                List<BigInteger> t2 = subPackets.collect(Collectors.toList());
                return t2.get(0).compareTo(t2.get(1)) < 0 ? BigInteger.ONE : BigInteger.ZERO;
            case 7:
                List<BigInteger> t3 = subPackets.collect(Collectors.toList());
                return t3.get(0).compareTo(t3.get(1)) == 0 ? BigInteger.ONE : BigInteger.ZERO;
            default:
                System.out.println("unknown packet type " + packet.type);
                return BigInteger.ZERO;
        }
    }

    private static Packet parsePacket(String bits) {
        Packet result = new Packet();

        result.version = Integer.parseInt(bits.substring(bitIndex, bitIndex + 3), 2);
        bitIndex += 3;
        result.type = Integer.parseInt(bits.substring(bitIndex, bitIndex + 3), 2);
        bitIndex += 3;

        if (result.type == 4) {
            result.value = parseLiteral(bits);
        } else {
            result.subPackets.addAll(parseSubPackets(bits));
        }

        return result;
    }

    private static List<Packet> parseSubPackets(String bits) {
        char typeId = bits.charAt(bitIndex);
        bitIndex += 1;
        if (typeId == '0') {
            return parseLengthType0(bits);
        } else {
            return parseLengthType1(bits);
        }
    }

    private static List<Packet> parseLengthType0(String bits) {
        List<Packet> result = new ArrayList<>();

        int length = Integer.parseInt(bits.substring(bitIndex, bitIndex + 15), 2);
        bitIndex += 15;
        int stopAt = bitIndex + length;

        while (bitIndex < stopAt) {
            result.add(parsePacket(bits));
        }

        return result;
    }

    private static List<Packet> parseLengthType1(String bits) {
        List<Packet> result = new ArrayList<>();

        int subPackets = Integer.parseInt(bits.substring(bitIndex, bitIndex + 11), 2);
        bitIndex += 11;

        while (result.size() < subPackets) {
            result.add(parsePacket(bits));
        }

        return result;
    }

    private static BigInteger parseLiteral(String bits) {
        long result = 0;

        while (bits.charAt(bitIndex) == '1') {
            bitIndex++;
            result += Integer.parseInt(bits.substring(bitIndex, bitIndex + 4), 2);
            bitIndex += 4;
            result <<= 4;
        }

        result += Integer.parseInt(bits.substring(bitIndex + 1, bitIndex + 5), 2);
        bitIndex += 5;

        return BigInteger.valueOf(result);
    }

    private static String toBits(String input) {
        StringBuilder sb = new StringBuilder();

        for (char b : input.toCharArray()) {
            int i = Integer.parseInt(String.valueOf(b), 16);
            String s = Integer.toString(i, 2);
            sb.append("0".repeat(Math.max(0, 4 - s.length())));
            sb.append(s);
        }

        return sb.toString();
    }

    private static String readInput() throws Exception {
        return Files.lines(Paths.get("src/day16/input.txt")).findFirst().orElseThrow();
    }
}