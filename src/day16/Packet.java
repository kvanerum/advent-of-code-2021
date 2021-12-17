package day16;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Packet {
    public int version;
    public int type;
    public BigInteger value;
    public List<Packet> subPackets = new ArrayList<>();

    @Override
    public String toString() {
        return "Packet{" +
               "version=" + version +
               ", type=" + type +
               ", value=" + value +
               ", subPackets=\n\t" + subPackets +
               '}';
    }
}
