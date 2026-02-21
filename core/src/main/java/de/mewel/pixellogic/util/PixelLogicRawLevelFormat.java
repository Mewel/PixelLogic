package de.mewel.pixellogic.util;

import java.util.ArrayList;
import java.util.List;

public class PixelLogicRawLevelFormat {

    private final List<List<Integer>> rows;
    private final List<List<Integer>> columns;

    public PixelLogicRawLevelFormat(List<List<Integer>> rows, List<List<Integer>> columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public List<List<Integer>> getRows() {
        return rows;
    }

    public List<List<Integer>> getColumns() {
        return columns;
    }

    /**
     * Parses the given string in the *.non format of Steve Simpson's solver.
     *
     * @param data the level data
     * @return the pixel logic format
     */
    public static PixelLogicRawLevelFormat ofNON(String data) {
        String[] lines = data.split("\n");
        boolean parseRows = false;
        boolean parseColumns = false;
        List<List<Integer>> rows = new ArrayList<>();
        List<List<Integer>> columns = new ArrayList<>();
        for (String line : lines) {
            if (line.startsWith("rows")) {
                parseRows = true;
            } else if (line.startsWith("columns")) {
                parseRows = false;
                parseColumns = true;
            } else if (parseRows) {
                List<Integer> lineData = getLineData(line);
                if(!lineData.isEmpty()) {
                    rows.add(lineData);
                }
            } else if (parseColumns) {
                List<Integer> lineData = getLineData(line);
                if(!lineData.isEmpty()) {
                    columns.add(lineData);
                }
            }
        }
        return new PixelLogicRawLevelFormat(rows, columns);
    }

    private static List<Integer> getLineData(String line) {
        if(line.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] numbers = line.split(",");
        List<Integer> lineData = new ArrayList<>();
        for (String number : numbers) {
            lineData.add(Integer.valueOf(number));
        }
        return lineData;
    }


}
