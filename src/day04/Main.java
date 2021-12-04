package day04;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Bingo input = readInput();

        List<Board> inGameBoards = input.boards;
        boolean haveFirstWinner = false;
        for (Integer n : input.drawnNumbers) {
            List<Board> nextBoards = new ArrayList<>(inGameBoards);
            for (Board board : inGameBoards) {
                boolean isWinner = board.processDrawnNumber(n);

                if (!isWinner) {
                    continue;
                }

                // print result of first and last board to win
                if (!haveFirstWinner) {
                    System.out.println(board.getUnmarkedSum() * n);
                    haveFirstWinner = true;
                } else if (inGameBoards.size() == 1) {
                    System.out.println(board.getUnmarkedSum() * n);
                    return;
                }

                // don't check this board anymore
                nextBoards.remove(board);
            }
            inGameBoards = nextBoards;
        }
    }

    private static Bingo readInput() throws IOException {
        Bingo result = new Bingo();
        List<String> lines = Files.lines(Paths.get("src/day04/input.txt")).collect(Collectors.toList());
        int boardSize = 5;

        // read drawn numbers
        result.drawnNumbers.addAll(Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).collect(Collectors.toList()));

        // read boards
        int currentLineNumber = 1;

        while (lines.size() > currentLineNumber + boardSize) {
            Board b = new Board();
            List<String> boardLines = lines.subList(currentLineNumber + 1, currentLineNumber + 1 + boardSize);

            boardLines.forEach(line -> b.addRow(Arrays.stream(line.trim().split("\\s+")).map(Integer::parseInt).collect(Collectors.toList())));
            result.boards.add(b);

            currentLineNumber += boardSize + 1;
        }

        return result;
    }
}