package de.mewel.pixellogic.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PixelLogicSolver {

    public PixelLogicSolverResult solve(List<List<Integer>> rowNumbers, List<List<Integer>> colNumbers) {
        int rows = rowNumbers.size();
        int cols = colNumbers.size();

        Map<String, Line> lines = new HashMap<String, Line>();
        for (int i = 0; i < rows; i++) {
            String id = "r" + i;
            lines.put(id, new Line(id, rowNumbers.get(i), cols));
        }
        for (int i = 0; i < cols; i++) {
            String id = "c" + i;
            lines.put(id, new Line(id, colNumbers.get(i), cols));
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Pixel pixel = new Pixel(row, col);
                lines.get("r" + row).pixels.add(pixel);
                lines.get("c" + col).pixels.add(pixel);
            }
        }

        ArrayMergingDeque<String, Line> solveQueue = new ArrayMergingDeque<String, Line>();
        for (Line line : lines.values()) {
            solveQueue.offer(line.id, line);
        }
        int complexity = solve(lines, solveQueue);
        Boolean[][] level = new Boolean[rows][cols];
        for (int row = 0; row < rows; row++) {
            level[row] = lines.get("r" + row).toBooleanLine();
        }
        return new PixelLogicSolverResult(level, complexity);
    }

    private int solve(Map<String, Line> lines, ArrayMergingDeque<String, Line> solveQueue) {
        int complexity = 0;
        while (!solveQueue.isEmpty()) {
            Line line = solveQueue.poll();
            boolean isLineARow = line.isRow();
            List<Pixel> dirtyPixels = solveLine(line);
            for (Pixel dirtyPixel : dirtyPixels) {
                String key = isLineARow ? "c" + dirtyPixel.col : "r" + dirtyPixel.row;
                Line dirtyLine = lines.get(key);
                if (!dirtyLine.solved) {
                    solveQueue.offer(key, dirtyLine);
                }
            }
            complexity++;
        }
        return complexity / (lines.size() / 2);
    }

    private List<Pixel> solveLine(Line line) {
        if (line.solved) {
            return new ArrayList<Pixel>();
        }
        if (line.isFullyFilled()) {
            line.solved = true;
            return line.blockPixel();
        }
        Boolean[] booleanLine = line.toBooleanLine();
        // brute force
        int filledPixel = PixelLogicUtil.countPixel(booleanLine);
        List<Boolean[]> lines = bruteForceLine(line, booleanLine, 0, filledPixel);
        Boolean[] newLine = mergeLines(lines);
        if (newLine != null && PixelLogicUtil.differs(booleanLine, newLine)) {
            return line.update(newLine);
        }
        return new ArrayList<Pixel>();
    }

    private List<Boolean[]> bruteForceLine(Line line, Boolean[] booleanLine, int startIndex, int filledPixel) {
        List<Boolean[]> lines = new ArrayList<Boolean[]>();
        if (filledPixel >= line.requiredPixels) {
            return lines;
        }
        for (int index = startIndex; index < booleanLine.length; index++) {
            Boolean[] newLine = copyLine(booleanLine);
            int newFilledPixel = filledPixel;
            if (newLine[index] == null) {
                newLine[index] = true;
                newFilledPixel++;
            }
            if (PixelLogicUtil.isSolved(newLine, line.numbers)) {
                lines.add(newLine);
            } else if (index + 1 < booleanLine.length) {
                lines.addAll(bruteForceLine(line, newLine, index + 1, newFilledPixel));
            }
        }
        return lines;
    }

    private Boolean[] mergeLines(List<Boolean[]> lines) {
        if (lines.isEmpty()) {
            return null;
        }
        if (lines.size() == 1) {
            return lines.get(0);
        }
        Boolean[] firstLine = lines.get(0);
        Boolean[] mergedLine = new Boolean[firstLine.length];
        boolean alwaysTrue;
        boolean alwaysNullOrFalse;
        for (int lineIndex = 0; lineIndex < firstLine.length; lineIndex++) {
            alwaysTrue = true;
            alwaysNullOrFalse = true;
            for (Boolean[] line : lines) {
                if (line[lineIndex] != null && line[lineIndex]) {
                    alwaysNullOrFalse = false;
                }
                if (line[lineIndex] == null || !line[lineIndex]) {
                    alwaysTrue = false;
                }
                if (!alwaysTrue && !alwaysNullOrFalse) {
                    break;
                }
            }
            if (alwaysTrue) {
                mergedLine[lineIndex] = true;
            } else if (alwaysNullOrFalse) {
                mergedLine[lineIndex] = false;
            }
        }
        return mergedLine;
    }

    private static Boolean[] copyLine(Boolean[] line) {
        Boolean[] copy = new Boolean[line.length];
        System.arraycopy(line, 0, copy, 0, line.length);
        return copy;
    }

    private static class Line {

        String id;

        List<Integer> numbers;

        int requiredPixels;

        List<Pixel> pixels;

        boolean solved;

        Line(String id, List<Integer> numbers, int pixels) {
            this.id = id;
            this.numbers = numbers;
            this.pixels = new ArrayList<Pixel>(pixels);
            this.requiredPixels = PixelLogicUtil.countPixel(numbers);
            this.solved = false;
        }

        boolean isFullyFilled() {
            return getAmountOfFilledPixels() >= PixelLogicUtil.countPixel(numbers);
        }

        int getAmountOfFilledPixels() {
            int amount = 0;
            for (Pixel pixel : pixels) {
                if (pixel.value == 2) {
                    amount++;
                }
            }
            return amount;
        }

        List<Pixel> blockPixel() {
            List<Pixel> dirtyPixels = new ArrayList<Pixel>();
            for (Pixel pixel : pixels) {
                if (pixel.value == 0) {
                    pixel.value = 1;
                    dirtyPixels.add(pixel);
                }
            }
            return dirtyPixels;
        }

        Boolean[] toBooleanLine() {
            Boolean[] line = new Boolean[pixels.size()];
            int i = 0;
            for (Pixel p : pixels) {
                line[i++] = p.value == 0 ? null : p.value != 1;
            }
            return line;
        }

        public List<Pixel> update(Boolean[] line) {
            List<Pixel> dirtyPixels = new ArrayList<Pixel>();
            for (int i = 0; i < line.length; i++) {
                Boolean b = line[i];
                if (b == null) {
                    continue;
                }
                Pixel p = pixels.get(i);
                if (p.value == 0) {
                    p.value = (byte) (b ? 2 : 1);
                    dirtyPixels.add(p);
                }
            }
            return dirtyPixels;
        }

        public boolean isRow() {
            return id.charAt(0) == 'r';
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof Line && id.equals(((Line) obj).id);
        }

    }

    private static class Pixel {

        int row, col;

        // 0 = unknown, 1 = false, 2 = true
        byte value;

        Pixel(int row, int col) {
            this.row = row;
            this.col = col;
            this.value = 0;
        }

        void set(byte value) {
            this.value = value;
        }

    }

}
