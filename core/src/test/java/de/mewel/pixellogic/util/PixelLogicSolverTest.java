package de.mewel.pixellogic.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.util.PixelLogicSolver.Line;
import de.mewel.pixellogic.util.PixelLogicSolver.Pixel;

public class PixelLogicSolverTest {

    @Test
    public void solve() {
        // test valid
        Boolean[][] level = PixelLogicUtil.sampleLevel();
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level);
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level);

        Boolean[][] solvedLevel = new PixelLogicSolver().solve(rowData, colData).getLevel();

        assertTrue(PixelLogicUtil.isValid(solvedLevel), "level should be valid");
        assertArrayEquals(level, solvedLevel, "level should be equals");

        // test invalid
        level = PixelLogicUtil.invalidSampleLevel();
        rowData = PixelLogicUtil.getRowData(level);
        colData = PixelLogicUtil.getColumnData(level);

        solvedLevel = new PixelLogicSolver().solve(rowData, colData).getLevel();

        assertFalse(PixelLogicUtil.isValid(solvedLevel), "level should be invalid");
    }

    @Test
    public void testChromeLevel() {
        Boolean[][] level = new Boolean[][]{
                {false, true, false},
                {true, false, true},
                {false, true, false}
        };

        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level);
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level);
        Boolean[][] solvedLevel = new PixelLogicSolver().solve(rowData, colData).getLevel();

        System.out.println(PixelLogicUtil.toString(solvedLevel));

        assertTrue(PixelLogicUtil.isValid(solvedLevel), "chrome level should be valid");
        assertArrayEquals(level, solvedLevel, "level should be equals");
    }

    @Test
    public void solveLine() {
        PixelLogicSolver solver = new PixelLogicSolver();

        Line line;

        // easy tests
        line = buildLine(2, 1);
        solver.solveLine(line);
        assertEquals(0, line.getAmountOf((byte) 2), "there shouldn't be any changes");

        line = buildLine(3, 2);
        solver.solveLine(line);
        assertEquals(1, line.getAmountOf((byte) 2), "there shouldn't be any changes");
        assertArrayEquals(new Boolean[]{null, true, null}, line.toBooleanLine());

        line = buildLine(3, 1, 1);
        solver.solveLine(line);
        assertArrayEquals(new Boolean[]{true, false, true}, line.toBooleanLine());

        // complex test
        line = buildLine(new Boolean[]{null, null, false, true, null, null, true, false, null, false}, 1, 4);
        solver.solveLine(line);
        assertArrayEquals(
                new Boolean[]{null, null, false, true, true, true, true, false, false, false},
                line.toBooleanLine()
        );

        // block test
        line = buildLine(new Boolean[]{null, true, null, null, null, true, null}, 1, 2);
        solver.solveLine(line);
        assertArrayEquals(new Boolean[]{false, true, false, false, null, true, null}, line.toBooleanLine());

        line = buildLine(new Boolean[]{null, null, null, null, true, null, null, null, true, true, true}, 2, 3);
        solver.solveLine(line);
        assertArrayEquals(
                new Boolean[]{false, false, false, null, true, null, false, false, true, true, true},
                line.toBooleanLine()
        );

        // test empty
        line = buildLine(new Boolean[]{null, null, null}, 0);
        solver.solveLine(line);
        assertArrayEquals(new Boolean[]{false, false, false}, line.toBooleanLine());
    }

    private Line buildLine(int pixels, int... numbers) {
        List<Integer> numberList = new ArrayList<>();
        for (int i : numbers) numberList.add(i);

        Line line = new Line("r1", numberList, pixels);
        for (int i = 0; i < pixels; i++) {
            line.pixels.add(new Pixel(0, i));
        }
        return line;
    }

    private Line buildLine(Boolean[] bLine, int... numbers) {
        List<Integer> numberList = new ArrayList<>();
        for (int i : numbers) numberList.add(i);

        Line line = new Line("r1", numberList, bLine.length);
        for (int i = 0; i < bLine.length; i++) {
            Pixel pixel = new Pixel(0, i);
            if (bLine[i] != null) {
                pixel.value = (byte) (bLine[i] ? 2 : 1);
            }
            line.pixels.add(pixel);
        }
        return line;
    }
}