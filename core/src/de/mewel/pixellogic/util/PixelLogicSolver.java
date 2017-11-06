package de.mewel.pixellogic.util;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.sound.sampled.Line;

/**
 * Solves a pixel logic game.
 */
public class PixelLogicSolver {

    public Boolean[][] solve(List<List<Integer>> rowNumbers, List<List<Integer>> colNumbers) {
        int rows = rowNumbers.size();
        int cols = colNumbers.size();

        Boolean[][] level = new Boolean[rows][cols];

        boolean changed;
        do {
            changed = false;
            for (int col = 0; col < cols; col++) {
                Boolean[] line = columnToLine(level, col);
                if (checkLine(line, colNumbers.get(col))) {
                    for (int row = 0; row < level.length; row++) {
                        level[row][col] = line[row];
                    }
                    changed = true;
                }
            }
            for (int row = 0; row < rows; row++) {
                if (checkLine(level[row], rowNumbers.get(row))) {
                    changed = true;
                }
            }
        } while (changed);
        return level;
    }

    boolean checkLine(Boolean[] line, List<Integer> numbers) {
        boolean changed = false;

        // fill pixel
        for (int numberIndex = 0; numberIndex < numbers.size(); numberIndex++) {
            List<Integer> left = new ArrayList<Integer>(numbers.subList(0, numberIndex));
            List<Integer> right = new ArrayList<Integer>(numbers.subList(numberIndex + 1, numbers.size()));

            int pixelsToAdd = numbers.get(numberIndex);
            int start = getNumberLength(left) + left.size();
            int end = line.length - (getNumberLength(right) + right.size());

            List<LinePart> lineParts = splitOnBlocked(line, start, end);

            int fitIndex = -1;
            boolean multipleFit = false;
            for (int linePartIndex = 0; linePartIndex < lineParts.size(); linePartIndex++) {
                LinePart part = lineParts.get(linePartIndex);
                if (part.fit(pixelsToAdd)) {
                    if (fitIndex != -1) {
                        multipleFit = true;
                        break;
                    }
                    fitIndex = linePartIndex;
                }
            }
            if (fitIndex != -1 && !multipleFit) {
                LinePart fittingPart = lineParts.get(fitIndex);
                changed = fittingPart.apply(line, pixelsToAdd);
            }
        }

        // block pixel when line is filled
        int filledPixel = getFilledPixels(line);
        if (filledPixel == getNumberLength(numbers)) {
            for (int i = 0; i < line.length; i++) {
                if (line[i] == null) {
                    line[i] = false;
                    changed = true;
                }
            }
            return changed;
        }
        // block left & right
        /*List<LinePart> connectedParts = splitOnNotConnected(line);
        if (blockLeft(line, connectedParts, numbers)) {
            changed = true;
        }*/
        /*if (blockRight(line, numbers.get(numbers.size() - 1))) {
            changed = true;
        }*/
        if (blockLine(line, numbers)) {
            changed = true;
        }

        return changed;
    }

    boolean blockLine(Boolean[] line, List<Integer> numbers) {
        List<NumberPart> numberParts = new ArrayList<NumberPart>(numbers.size());
        for (int numberIndex = 0; numberIndex < numbers.size(); numberIndex++) {
            List<Integer> left = new ArrayList<Integer>(numbers.subList(0, numberIndex));
            List<Integer> right = new ArrayList<Integer>(numbers.subList(numberIndex + 1, numbers.size()));

            int pixelsToAdd = numbers.get(numberIndex);
            int start = getNumberLength(left) + left.size();
            int end = line.length - (getNumberLength(right) + right.size());
            numberParts.add(new NumberPart(line, start, end, pixelsToAdd, numberIndex));
        }

        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < numberParts.size(); i++) {
                NumberPart part = numberParts.get(i);
                if (part.update(numberParts)) {
                    changed = true;
                }
            }
        } while (changed);

        return NumberPart.block(line, numberParts);
    }

    /**
     * Returns a list of all line parts for the given range. The line part's consists of pixels which
     * are connected and are NOT blocked. This methods splits (create a new line part) when reaching
     * a blocked pixel.
     *
     * @param line  the line to analyse
     * @param start inclusive where to start inclusive
     * @param end   where to stop exclusive
     * @return list of line parts
     */
    static List<LinePart> splitOnBlocked(Boolean[] line, int start, int end) {
        List<LinePart> parts = new ArrayList<LinePart>();
        LinePart part = null;
        for (int lineIndex = start; lineIndex < end; lineIndex++) {
            boolean blocked = line[lineIndex] != null && !line[lineIndex];
            part = getPart(line, parts, part, lineIndex, !blocked);
        }
        if (part != null) {
            addPart(line, parts, part, end);
        }
        return parts;
    }

    static List<LinePart> splitOnNotConnected(Boolean[] line, int start, int end) {
        List<LinePart> parts = new ArrayList<LinePart>();
        LinePart part = null;
        for (int lineIndex = start; lineIndex < end; lineIndex++) {
            boolean filled = line[lineIndex] != null && line[lineIndex];
            part = getPart(line, parts, part, lineIndex, filled);
        }
        if (part != null) {
            addPart(line, parts, part, line.length);
        }
        return parts;
    }

    private static LinePart getPart(Boolean[] line, List<LinePart> parts, LinePart part, int lineIndex, boolean pixelType) {
        if (part == null && pixelType) {
            part = new LinePart();
            part.start = lineIndex;
        } else if (part != null) {
            if (!pixelType) {
                addPart(line, parts, part, lineIndex);
                part = null;
            }
        }
        return part;
    }

    private static void addPart(Boolean[] line, List<LinePart> parts, LinePart part, int lineIndex) {
        int size = lineIndex - part.start;
        part.pixels = new Boolean[size];
        System.arraycopy(line, part.start, part.pixels, 0, size);
        parts.add(part);
    }

    private int getNumberLength(List<Integer> numbers) {
        int amount = 0;
        for (Integer number : numbers) {
            amount += number;
        }
        return amount;
    }

    private Boolean[] columnToLine(Boolean[][] level, int col) {
        Boolean[] column = new Boolean[level.length];
        for (int row = 0; row < level.length; row++) {
            column[row] = level[row][col];
        }
        return column;
    }

/*
    boolean blockLeft(Boolean[] line, List<LinePart> connectedParts, List<Integer> numbers) {
        if (numbers.isEmpty()) {
            return false;
        }
        int number = numbers.get(0);
        // no connected parts -> cannot block anything
        if (connectedParts.isEmpty()) {
            return false;
        }
        LinePart firstPart = connectedParts.get(0);
        int alreadyFilled = firstPart.pixels.length;
        // the "current" first part is not the actual first part and we cannot block anything
        // of position 0 and 1.
        if (alreadyFilled > number || firstPart.start <= 1) {
            return false;
        }

        // the first part
        boolean canFitBefore = getMaxFreeConnectedPixel(line, 0, firstPart.start - 1) >= number;

        // if there are enough free pixels to fit the actual first number and
        if (canFitBefore && firstPart.pixels.length > numbers.get(1)) {
            return false;
        }

        //boolean numberFitsInLeftPart = false;
        //int firstFilled = firstPart.pixels.length;

        // all connected parts are on their respective position -> we know that the firstPart
        // is really the first part
        if(numbers.size() != connectedParts.size()) {

        }


        boolean changed = false;
        int toFill = number - alreadyFilled;
        int blockTo = firstPart.start - toFill;
        for(int lineIndex = 0; lineIndex < blockTo; lineIndex++) {
            if(line[lineIndex] == null) {
                line[lineIndex] = false;
                changed = true;
            }
        }
        return changed;
    }
*/


    /**
     * Returns the maximum amount of free pixel in the given range.
     *
     * @param line the line to check
     * @param from the start inclusive
     * @param to   the end exclusive
     * @return number of free connected pixels in this range
     */
    private int getMaxFreeConnectedPixel(Boolean line[], int from, int to) {
        int freePixel = 0;
        int maxFreePixel = 0;
        for (int lineIndex = from; lineIndex < to; lineIndex++) {
            if (line[lineIndex] == null) {
                freePixel++;
            } else {
                maxFreePixel = freePixel > maxFreePixel ? freePixel : maxFreePixel;
                freePixel = 0;
            }
        }
        return freePixel > maxFreePixel ? freePixel : maxFreePixel;
    }

    private int getFilledPixels(Boolean[] line) {
        int filledPixel = 0;
        for (Boolean pixel : line) {
            filledPixel += (pixel != null && pixel) ? 1 : 0;
        }
        return filledPixel;
    }

    static class LinePart {

        public int start;
        public Boolean[] pixels;

        @Override
        public String toString() {
            return Arrays.asList(pixels).toString();
        }

        public boolean fit(int amount) {
            return amount <= pixels.length;
        }

        public boolean apply(Boolean[] line, int amount) {
            boolean addedCenterPixel = addCenterPixel(line, amount);
            int[] firstLast = getFirstAndLastFilled(line);
            if (firstLast != null) {
                boolean filledBetween = fillBetween(line, firstLast[0], firstLast[1]);
                boolean addedLeftRightPixel = addLeftRightPixel(line, amount);
                boolean addedSeparator = addSeparator(line, amount);
                return addedCenterPixel || filledBetween || addedLeftRightPixel || addedSeparator;
            }
            return addedCenterPixel;
        }

        private boolean addCenterPixel(Boolean[] line, int amount) {
            int possiblePixels = pixels.length;
            int ignorePixels = possiblePixels - amount;
            int fillPixels = possiblePixels - (ignorePixels * 2);
            boolean changed = false;
            for (int i = ignorePixels; i < (ignorePixels + fillPixels); i++) {
                int lineIndex = start + i;
                if (line[lineIndex] != null) {
                    continue;
                }
                line[start + i] = true;
                changed = true;
            }
            return changed;
        }

        private boolean addLeftRightPixel(Boolean[] line, int amount) {
            int[] firstLast = getFirstAndLastFilled(line);
            int first = firstLast[0];
            int last = firstLast[1];
            int alreadyFilledPixels = last - first + 1;
            int missing = amount - alreadyFilledPixels;
            int freeLeft = first - start;
            int freeRight = (pixels.length - 1) - (last - start);
            boolean changed = false;

            int addRight = -(freeLeft - missing);
            if (addRight > 0) {
                for (int i = first + 1; i <= (first + addRight); i++) {
                    if (line[i] == null) {
                        line[i] = true;
                        changed = true;
                    }
                }
            }
            int addLeft = -(freeRight - missing);
            if (addLeft > 0) {
                for (int i = last - 1; i >= (last - addLeft); i--) {
                    if (line[i] == null) {
                        line[i] = true;
                        changed = true;
                    }
                }
            }
            return changed;
        }

        private boolean fillBetween(Boolean[] line, int first, int last) {
            boolean changed = false;
            for (int i = first; i <= last; i++) {
                if (line[i] == null) {
                    line[i] = true;
                    changed = true;
                }
            }
            return changed;
        }

        private boolean addSeparator(Boolean[] line, int amount) {
            int[] firstLast = getFirstAndLastFilled(line);
            int first = firstLast[0];
            int last = firstLast[1];
            if ((last - first + 1) != amount) {
                return false;
            }
            boolean changed = false;
            if (first - 1 >= 0 && line[first - 1] == null) {
                line[first - 1] = false;
                changed = true;
            }
            if (last + 1 < line.length && line[last + 1] == null) {
                line[last + 1] = false;
                changed = true;
            }
            return changed;
        }

        private int[] getFirstAndLastFilled(Boolean[] line) {
            int first = Integer.MAX_VALUE;
            int last = Integer.MIN_VALUE;
            for (int i = start; i < (start + pixels.length); i++) {
                if (line[i] != null && line[i]) {
                    first = first > i ? i : first;
                    last = last < i ? i : last;
                }
            }
            if (first > last) {
                return null;
            }
            return new int[]{first, last};
        }
    }

    static class NumberPart {

        Boolean[] line;

        int start, end, amount, index;

        /**
         * Creates a new number part.
         *
         * @param line   the line
         * @param start  start inclusive
         * @param end    end exclusive
         * @param amount amount of pixel to fill
         */
        public NumberPart(Boolean[] line, int start, int end, int amount, int index) {
            this.line = line;
            this.start = start;
            this.end = end;
            this.amount = amount;
            this.index = index;
        }

        public boolean isComplete() {
            return end - start == amount;
        }

        public boolean update(List<NumberPart> numberParts) {
            NumberPart leftPart = index > 0 ? numberParts.get(index - 1) : null;
            NumberPart rightPart = index < (numberParts.size() - 1) ? numberParts.get(index + 1) : null;
            boolean changed = false;
            if (this.updateFitting()) {
                changed = true;
            }
            if (this.updateLeft(leftPart)) {
                changed = true;
            }
            if (this.updateRight(rightPart)) {
                changed = true;
            }
            if (this.updateFitting()) {
                changed = true;
            }
            LinePart part = getUniqueFilledPart();
            if (part != null) {
                boolean hasPart = false;
                for (int i = 0; i < numberParts.size(); i++) {
                    if (i == index) {
                        continue;
                    }
                    NumberPart otherPart = numberParts.get(i);
                    if (part.start + part.pixels.length > otherPart.start || otherPart.end > part.start) {
                        hasPart = true;
                        break;
                    }
                }
                if (!hasPart) {
                    int toFill = this.amount - part.pixels.length;
                    updateStartAndEnd(part.start - toFill, part.start + part.pixels.length + toFill);
                }
            }
            return changed;
        }

        public boolean updateLeft(NumberPart leftPart) {
            if (leftPart == null) {
                return false;
            }
            return updateStartAndEnd(leftPart.start + leftPart.amount + 1, this.end);
        }

        public boolean updateRight(NumberPart rightPart) {
            if (rightPart == null) {
                return false;
            }
            return updateStartAndEnd(this.start, rightPart.end - (rightPart.amount + 1));
        }

        public boolean updateFitting() {
            int newStart = fitFirst(line, this.start, this.end, this.amount);
            int newEnd = line.length - fitFirst(PixelLogicUtil.invert(line),
                    line.length - end, line.length - start, this.amount);
            return updateStartAndEnd(newStart, newEnd);
        }

        /**
         * Updates the start and the end value if the new values are greater or lower than the
         * current ones.
         *
         * @param newStart the new start, has to be greated than the current start to be accepted
         * @param newEnd   the new end, has to be lower than the current end to be accepted
         * @return true if one of start or end has changed
         */
        private boolean updateStartAndEnd(int newStart, int newEnd) {
            boolean changed = false;
            if (this.start < newStart) {
                this.start = newStart;
                changed = true;
            }
            if (this.end > newEnd) {
                this.end = newEnd;
                changed = true;
            }
            return changed;
        }

        int fitFirst(Boolean[] line, int start, int end, int pixel) {
            for (int lineIndex = start; lineIndex < (end - pixel); lineIndex++) {
                boolean fitSubLine = true;
                for (int subLineIndex = lineIndex; subLineIndex < (lineIndex + pixel); subLineIndex++) {
                    if (line[subLineIndex] != null && !line[subLineIndex]) {
                        fitSubLine = false;
                        break;
                    }
                }
                if (fitSubLine) {
                    return lineIndex;
                }
            }
            throw new IllegalArgumentException("Does not fit at all. This should never happen!");
        }

        private LinePart getUniqueFilledPart() {
            List<LinePart> thisFilledParts = splitOnNotConnected(this.line, this.start, this.end);
            for(LinePart part : new ArrayList<LinePart>(thisFilledParts)) {
                if(part.pixels.length > this.amount) {
                    thisFilledParts.remove(part);
                }
            }
            //List<LinePart> allFilledParts = splitOnNotConnected(this.line, 0, this.line.length);


            return thisFilledParts.size() == 1 ? thisFilledParts.get(0) : null;
        }

        @Override
        public String toString() {
            return "s: " + start + " e: " + end + " a: " + amount;
        }

        public static boolean block(Boolean[] line, List<NumberPart> numberParts) {
            Set<Integer> dontBlock = new HashSet<Integer>();
            for (NumberPart part : numberParts) {
                for (int i = part.start; i < part.end; i++) {
                    dontBlock.add(i);
                }
            }

            boolean changed = false;
            for (int lineIndex = 0; lineIndex < line.length; lineIndex++) {
                if (dontBlock.contains(lineIndex)) {
                    continue;
                }
                if (line[lineIndex] == null) {
                    line[lineIndex] = false;
                    changed = true;
                }
            }
            return changed;
        }

    }

    /*
    for (int row = 0; row < rows; row++) {
        for (int col = 0; col < cols; col++) {
            Boolean pixel = level[row][col];
            if (pixel == null) {
                pixel = checkPixel(col, level[row], rowNumbers.get(row), cols);
                if (pixel == null) {
                    pixel = checkPixel(row, columnToLine(level, col), colNumbers.get(col), rows);
                }
                if (pixel != null) {
                    changed = true;
                    level[row][col] = pixel;
                }
            }
        }
    }

    private Boolean checkPixel(int pos, Boolean[] line, List<Integer> numbers, int lineLength) {
        // 0 line
        if (numbers.get(0) == 0) {
            return false;
        }
        // line is full
        if (numbers.get(0) == lineLength) {
            return true;
        }
        // line is completable
        int numberLength = getNumberLength(numbers);
        if ((numberLength + numbers.size() - 1) == lineLength) {
            int internalPos = 0;
            for (Integer number : numbers) {
                for (int numberIndex = 0; numberIndex < number; numberIndex++) {
                    if (internalPos == pos) {
                        return true;
                    }
                    internalPos++;
                }
                internalPos++;
            }
            return false;
        }
        // line numbers are used -> each other pos is false
        if (isLineMatchNumbers(line, numbers)) {
            return false;
        }
        // line has just some pixels missing
        if (getFreePixels(line) == (numberLength - getFullPixels(line))) {
            return true;
        }
        return null;
    }

        private int getFreePixels(Boolean[] line) {
        int freePixels = 0;
        for (Boolean pixel : line) {
            freePixels += pixel == null ? 1 : 0;
        }
        return freePixels;
    }

    private int getFullPixels(Boolean[] line) {
        int fullPixels = 0;
        for (Boolean pixel : line) {
            fullPixels += (pixel != null && pixel) ? 1 : 0;
        }
        return fullPixels;
    }

    private boolean isLineMatchNumbers(Boolean[] line, List<Integer> numbers) {
        List<Integer> fullLines = new ArrayList<Integer>();
        int consecutive = 0;
        for (Boolean pixel : line) {
            if (pixel != null && pixel) {
                consecutive++;
                continue;
            }
            if (consecutive == 0) {
                continue;
            }
            fullLines.add(consecutive);
            consecutive = 0;
        }
        if (consecutive != 0) {
            fullLines.add(consecutive);
        }
        if (fullLines.isEmpty()) {
            fullLines.add(0);
        }
        return PixelLogicUtil.compareNumberLists(fullLines, numbers);
    }
*/

/*
        List<Line> lines = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            lines.add(new Line(rowData.get(row), cols));
        }
        for (int col = 0; col < cols; col++) {
            lines.add(new Line(colData.get(col), rows));
        }

        boolean solved = true;
        do {
            for (Line line : lines) {
                if (line.isSolved()) {
                    continue;
                }
                line.solve();
            }
        } while (!solved);
    }

    private static class Line {

        private List<Integer> numbers;

        private Pixel[] data;

        public Line(List<Integer> numbers, int length) {
            this.numbers = numbers;
            this.data = new Pixel[length];
        }

        public boolean isSolved() {
            for (Pixel pixel : data) {
                if (pixel == null) {
                    return false;
                }
            }
            return true;
        }

        public void solve() {
            for(int i = 0; i < data.length; i++) {

            }
        }

    }*/

    public static String print(Boolean[][] level) {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < level.length; row++) {
            for (int col = 0; col < level[0].length; col++) {
                Boolean pixel = level[row][col];
                builder.append(pixel == null ? "?" : (pixel ? "x" : "o"));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
