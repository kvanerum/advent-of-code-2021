package day12;

public class Path {
    final public String from;
    final public String to;

    Path(String from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Path{" +
               "from='" + from + '\'' +
               ", to='" + to + '\'' +
               '}';
    }
}
