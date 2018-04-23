package de.mewel.pixellogic.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PixelLogicUtilTest {

    public static void main(String[] args) throws Exception {
        new PixelLogicUtilTest().solveSpeedTest();
    }

    @Test
    public void isSolved() throws Exception {
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(1);
        assertFalse(PixelLogicUtil.isSolved(new Boolean[]{null, null}, numbers));
        assertTrue(PixelLogicUtil.isSolved(new Boolean[]{true, null}, numbers));
        assertTrue(PixelLogicUtil.isSolved(new Boolean[]{false, true}, numbers));
        numbers.add(2);
        assertTrue(PixelLogicUtil.isSolved(new Boolean[]{false, true, false, false, true, true}, numbers));
        assertTrue(PixelLogicUtil.isSolved(new Boolean[]{false, true, false, false, true, true, false}, numbers));
        assertFalse(PixelLogicUtil.isSolved(new Boolean[]{false, false, false, false, true, false, false}, numbers));
    }

    @Test
    public void createRandomLevelSpeedTest() throws Exception {
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            Boolean[][] randomLevel = PixelLogicUtil.createRandomLevel(9, 9, 4f, -1);

            List<List<Integer>> rowData = PixelLogicUtil.getRowData(randomLevel);
            List<List<Integer>> colData = PixelLogicUtil.getColumnData(randomLevel);

            System.out.println("new: " + new PixelLogicSolver().solve(rowData, colData).getComplexity());
            System.out.println("old: " + new PixelLogicSolverOld().solve(rowData, colData).getComplexity());
            System.out.println("------");
        }
        System.out.println(System.currentTimeMillis() - sTime + "ms");
    }

    @Test
    public void solveSpeedTest() throws Exception {
        PixelLogicRawLevelFormat pixelLogicRawLevelFormat = PixelLogicRawLevelFormat.ofNON(getPuzzle6());
        System.out.println("Puzzle successfully parsed. Start solving...");
/*
        long sTime = System.currentTimeMillis();
        PixelLogicSolverResult result = new PixelLogicSolver().solve(pixelLogicRawLevelFormat.getRows(), pixelLogicRawLevelFormat.getColumns());
        System.out.println("Puzzle solved. Check if puzzle is valid...");
        System.out.println("valid: " + PixelLogicUtil.isValid(result.getLevel()));
        System.out.println("took: " + (System.currentTimeMillis() - sTime) + "ms");
        System.out.println("complexity: " + result.getComplexity());

        System.out.println(PixelLogicUtil.toString(result.getLevel()));
*/
        long sTime2 = System.currentTimeMillis();
        System.out.println("Start second solver...");
        PixelLogicSolverResult result2 = new PixelLogicSolver().solve(pixelLogicRawLevelFormat.getRows(), pixelLogicRawLevelFormat.getColumns());
        System.out.println("Puzzle solved. Check if puzzle is valid...");
        System.out.println("valid: " + PixelLogicUtil.isValid(result2.getLevel()));
        System.out.println("took: " + (System.currentTimeMillis() - sTime2) + "ms");
        System.out.println("complexity: " + result2.getComplexity());

        System.out.println(PixelLogicUtil.toString(result2.getLevel()));
    }

    private String getPuzzle1() {
        return "catalogue \"webpbn.com #1\"\n" +
                "title \"Demo Puzzle from Front Page\"\n" +
                "by \"Jan Wolter\"\n" +
                "copyright \"&copy; Copyright 2004 by Jan Wolter\"\n" +
                "width 5\n" +
                "height 10\n" +
                "\n" +
                "rows\n" +
                "2\n" +
                "2,1\n" +
                "1,1\n" +
                "3\n" +
                "1,1\n" +
                "1,1\n" +
                "2\n" +
                "1,1\n" +
                "1,2\n" +
                "2\n" +
                "\n" +
                "columns\n" +
                "2,1\n" +
                "2,1,3\n" +
                "7\n" +
                "1,3\n" +
                "2,1";
    }

    private String getPuzzle6() {
        return "catalogue \"webpbn.com #6\"\n" +
                "title \"Scardy Cat\"\n" +
                "by \"Jan Wolter\"\n" +
                "copyright \"&copy; Copyright 2004 by Jan Wolter\"\n" +
                "width 20\n" +
                "height 20\n" +
                "\n" +
                "rows\n" +
                "2\n" +
                "2\n" +
                "1\n" +
                "1\n" +
                "1,3\n" +
                "2,5\n" +
                "1,7,1,1\n" +
                "1,8,2,2\n" +
                "1,9,5\n" +
                "2,16\n" +
                "1,17\n" +
                "7,11\n" +
                "5,5,3\n" +
                "5,4\n" +
                "3,3\n" +
                "2,2\n" +
                "2,1\n" +
                "1,1\n" +
                "2,2\n" +
                "2,2\n" +
                "\n" +
                "columns\n" +
                "5\n" +
                "5,3\n" +
                "2,3,4\n" +
                "1,7,2\n" +
                "8\n" +
                "9\n" +
                "9\n" +
                "8\n" +
                "7\n" +
                "8\n" +
                "9\n" +
                "10\n" +
                "13\n" +
                "6,2\n" +
                "4\n" +
                "6\n" +
                "6\n" +
                "5\n" +
                "6\n" +
                "6\n";
    }

    private String getPuzzle16() {
        return "catalogue \"webpbn.com #16\"\n" +
                "title \"Probably Not\"\n" +
                "by \"Jan Wolter\"\n" +
                "copyright \"&copy; Copyright 2004 by Jan Wolter\"\n" +
                "width 34\n" +
                "height 34\n" +
                "\n" +
                "rows\n" +
                "1,1\n" +
                "2,2\n" +
                "3,3\n" +
                "2,1,1,2\n" +
                "2,1,1,2\n" +
                "1,1,1,1\n" +
                "1,1,1,1\n" +
                "18\n" +
                "2,1,1,1,1,2\n" +
                "1,1,1,1,1,1\n" +
                "1,1,1,1,1,1\n" +
                "26\n" +
                "2,1,1,1,1,1,1,2\n" +
                "2,1,1,2,2,1,1,2\n" +
                "2,1,1,2,2,1,1,2\n" +
                "14,14\n" +
                "1,1,1,1\n" +
                "1,1,1,1\n" +
                "14,14\n" +
                "2,1,1,2,2,1,1,2\n" +
                "2,1,1,2,2,1,1,2\n" +
                "2,1,1,1,1,1,1,2\n" +
                "26\n" +
                "1,1,1,1,1,1\n" +
                "1,1,1,1,1,1\n" +
                "2,1,1,1,1,2\n" +
                "18\n" +
                "1,1,1,1\n" +
                "1,1,1,1\n" +
                "2,1,1,2\n" +
                "2,1,1,2\n" +
                "3,3\n" +
                "2,2\n" +
                "1,1\n" +
                "\n" +
                "columns\n" +
                "1,1\n" +
                "2,2\n" +
                "3,3\n" +
                "2,1,1,2\n" +
                "2,1,1,2\n" +
                "1,1,1,1\n" +
                "1,1,1,1\n" +
                "18\n" +
                "2,1,1,1,1,2\n" +
                "1,1,1,1,1,1\n" +
                "1,1,1,1,1,1\n" +
                "26\n" +
                "2,1,1,1,1,1,1,2\n" +
                "2,1,1,2,2,1,1,2\n" +
                "2,1,1,2,2,1,1,2\n" +
                "14,14\n" +
                "1,1,1,1\n" +
                "1,1,1,1\n" +
                "14,14\n" +
                "2,1,1,2,2,1,1,2\n" +
                "2,1,1,2,2,1,1,2\n" +
                "2,1,1,1,1,1,1,2\n" +
                "26\n" +
                "1,1,1,1,1,1\n" +
                "1,1,1,1,1,1\n" +
                "2,1,1,1,1,2\n" +
                "18\n" +
                "1,1,1,1\n" +
                "1,1,1,1\n" +
                "2,1,1,2\n" +
                "2,1,1,2\n" +
                "3,3\n" +
                "2,2\n" +
                "1,1";
    }

    private String getPuzzle65() {
        return "catalogue \"webpbn.com #65\"\n" +
                "title \"Mum's the Word [has only one solution]\"\n" +
                "by \"Jan Wolter\"\n" +
                "copyright \"&copy; Copyright 2004 by Jan Wolter\"\n" +
                "width 34\n" +
                "height 40\n" +
                "\n" +
                "rows\n" +
                "12\n" +
                "5,2,5\n" +
                "5,2,2,5\n" +
                "1,2,2,2,2,2,1\n" +
                "4,2,2,4,2,2,4\n" +
                "4,2,2,4,2,2,4\n" +
                "1,2,2,2,2,2,1\n" +
                "6,2,2,2,2,2,6\n" +
                "6,2,2,2,2,2,6\n" +
                "1,14,1\n" +
                "10,10\n" +
                "8,3,3,8\n" +
                "1,1,2,1,1,2,1,1\n" +
                "9,2,2,2,2,9\n" +
                "9,9\n" +
                "1,1,1,1,1,1\n" +
                "12,2,12\n" +
                "12,12\n" +
                "1,1,4,1,1\n" +
                "14,14\n" +
                "12,12\n" +
                "2,1,4,1,2\n" +
                "9,4,9\n" +
                "1,7,4,7,1\n" +
                "1,1,1,4,1,1,1\n" +
                "1,7,4,7,1\n" +
                "1,7,4,7,1\n" +
                "1,2,1,2,1,2,1\n" +
                "1,7,2,7,1\n" +
                "1,1,6,2,6,1,1\n" +
                "1,1,1,1,2,1,1,1,1\n" +
                "1,1,6,2,6,1,1\n" +
                "1,1,5,5,1,1\n" +
                "1,1,1,8,1,1,1\n" +
                "1,1,4,4,1,1\n" +
                "1,2,6,2,1\n" +
                "2,4,4,2\n" +
                "2,6,2\n" +
                "4,4\n" +
                "6\n" +
                "\n" +
                "columns\n" +
                "5\n" +
                "3,2,1\n" +
                "3,2,2,1\n" +
                "3,2,2,2,2\n" +
                "3,2,2,2,2,3\n" +
                "1,2,2,2,2,2,16\n" +
                "1,2,2,2,2,2,2,1,2\n" +
                "1,2,2,2,2,2,2,13,1\n" +
                "3,2,2,2,2,2,2,4,1,1\n" +
                "6,5,2,2,2,2,6,1,1\n" +
                "1,7,3,2,2,2,2,2,1,1,1\n" +
                "3,4,1,2,2,2,2,2,2,1,1,1\n" +
                "6,1,2,3,2,2,2,2,1,1,1\n" +
                "1,7,2,16,1,1\n" +
                "1,4,1,1,1,1,1,1,1,1,1\n" +
                "1,2,1,3,1,1,6,1,1,1,1\n" +
                "2,7,1,1,11,1,1,1,1\n" +
                "2,7,1,1,11,1,1,1,1\n" +
                "1,2,1,3,1,1,6,1,1,1,1\n" +
                "1,4,1,1,1,1,1,1,1,1,1\n" +
                "1,7,2,16,1,1\n" +
                "6,1,2,3,2,2,2,2,1,1,1\n" +
                "3,4,1,2,2,2,2,2,2,1,1,1\n" +
                "1,7,3,2,2,2,2,2,1,1,1\n" +
                "6,5,2,2,2,2,6,1,1\n" +
                "3,2,2,2,2,2,2,4,1,1\n" +
                "1,2,2,2,2,2,2,13,1\n" +
                "1,2,2,2,2,2,2,1,2\n" +
                "1,2,2,2,2,2,16\n" +
                "3,2,2,2,2,3\n" +
                "3,2,2,2,2\n" +
                "3,2,2,1\n" +
                "3,2,1\n" +
                "5";
    }

    private String getPuzzle2413() {
        return "catalogue \"webpbn.com #2413\"\n" +
                "title \"Where there's smoke...\"\n" +
                "by \"Marcus\"\n" +
                "copyright \"&copy; Copyright 2008 by Marcus\"\n" +
                "width 20\n" +
                "height 20\n" +
                "\n" +
                "rows\n" +
                "1,3,2,1\n" +
                "1,2,2\n" +
                "3,4\n" +
                "2,3,2\n" +
                "2,1,6\n" +
                "2,13,1\n" +
                "1,1,8\n" +
                "2,1,1,7\n" +
                "1,2,2,2,3\n" +
                "3,1,1,1,3\n" +
                "1,2,1,1,3\n" +
                "2,1,1,3\n" +
                "1,5,5\n" +
                "1,1,3\n" +
                "4,2\n" +
                "2,2,1,2,1\n" +
                "2,1,2,3,2\n" +
                "4,1,6,1\n" +
                "3,4,3,2\n" +
                "4,2\n" +
                "\n" +
                "columns\n" +
                "2,2,1\n" +
                "1,6,4,4\n" +
                "3,3,1,1,4\n" +
                "2,2\n" +
                "1,3,3,3\n" +
                "1,1,1,2,1,2\n" +
                "2,1,1,1,1\n" +
                "2,4,3,3\n" +
                "3,1,2,3,1\n" +
                "1,4,2,1\n" +
                "3,1,2\n" +
                "2,1,1\n" +
                "3,3\n" +
                "7,4\n" +
                "5,4\n" +
                "3,2,1,3\n" +
                "3,4,1\n" +
                "9,2\n" +
                "8,3\n" +
                "1,8,2\n";
    }

    private String getPuzzle2712() {
        return "catalogue \"webpbn.com #2712\"\n" +
                "title \"The Lion and the Stone\"\n" +
                "by \"Marto Benwa\"\n" +
                "copyright \"&copy; Copyright 2008 by Marto Benwa\"\n" +
                "width 47\n" +
                "height 47\n" +
                "\n" +
                "rows\n" +
                "10\n" +
                "4,5\n" +
                "3,3\n" +
                "2,11,2\n" +
                "3,17,2\n" +
                "2,19,1\n" +
                "1,13,7,2\n" +
                "2,10,2,9,2\n" +
                "2,9,2,1,9,2\n" +
                "2,9,1,1,8,1\n" +
                "1,8,2,10,1\n" +
                "2,7,2,9,2\n" +
                "2,6,2,8,2\n" +
                "1,8,3,1,8,1\n" +
                "1,9,3,2,9,1\n" +
                "1,14,1,8,1\n" +
                "1,14,1,2,8,1\n" +
                "1,13,2,1,8,1\n" +
                "2,13,2,2,8,1\n" +
                "1,15,2,8,2\n" +
                "1,15,1,3,7,1\n" +
                "1,14,1,2,7,1\n" +
                "1,4,2,1,1,2,6,1\n" +
                "1,3,1,1,2,2,4,1\n" +
                "1,2,1,2,3,1,7,1\n" +
                "1,2,1,1,3,4,6,1\n" +
                "1,11,1,4,5,6,2\n" +
                "1,4,4,1,3,3,2\n" +
                "2,2,3,1\n" +
                "1,2,1\n" +
                "1,1,1\n" +
                "1,2,1\n" +
                "1,2,1\n" +
                "1,3,2\n" +
                "2,2,1,1\n" +
                "2,2,3,4,1\n" +
                "1,2,11,1\n" +
                "1,2,11,1\n" +
                "2,1,11,1\n" +
                "2,5,12,1\n" +
                "2,6,11,1\n" +
                "2,1,10,2\n" +
                "2,8,2\n" +
                "3,6,2\n" +
                "3,4\n" +
                "14\n" +
                "8\n" +
                "\n" +
                "columns\n" +
                "11\n" +
                "4,4\n" +
                "3,3\n" +
                "2,12,2\n" +
                "3,14,2\n" +
                "2,11,2,2\n" +
                "2,12,3,2\n" +
                "2,12,1,2\n" +
                "1,13,1,1\n" +
                "2,14,1,1\n" +
                "1,15,1,2\n" +
                "2,15,1,2\n" +
                "1,6,9,2,1\n" +
                "1,5,8,2,1,1\n" +
                "1,6,8,1,2,1\n" +
                "2,6,12,2,2,1\n" +
                "1,6,4,3,1,2,2,2\n" +
                "1,5,4,2,1,4,2,1\n" +
                "2,5,1,4,3,2,1\n" +
                "1,5,1,2,4,8,2\n" +
                "1,4,1,4,2,5,2\n" +
                "1,4,1,2,1,2\n" +
                "1,4,1,2,1,2\n" +
                "1,4,1,1,1,3,2\n" +
                "1,4,1,1,3,1,5,2\n" +
                "1,3,1,3,2,5,2\n" +
                "1,3,1,1,1,2,6,2\n" +
                "2,3,1,3,10,1\n" +
                "1,5,2,2,8,1\n" +
                "1,5,3,2,8,2\n" +
                "1,5,1,3,1,7,1\n" +
                "2,8,3,1,6,1\n" +
                "1,7,2,6,1\n" +
                "1,7,1,1,6,1\n" +
                "1,10,1,6,1\n" +
                "2,11,5,1\n" +
                "1,13,4,2\n" +
                "1,14,1,2,1\n" +
                "1,14,3,1\n" +
                "2,13,3,1\n" +
                "2,15,1\n" +
                "2,15,1\n" +
                "2,12,2\n" +
                "2,10,2\n" +
                "3,3\n" +
                "5,5\n" +
                "9";
    }

}
