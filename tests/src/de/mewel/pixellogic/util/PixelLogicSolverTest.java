package de.mewel.pixellogic.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PixelLogicSolverTest {

    @Test
    public void solve() throws Exception {
        // test valid
        Boolean[][] level = PixelLogicUtil.sampleLevel();
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level);
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level);
        Boolean[][] solvedLevel = new PixelLogicSolver().solve(rowData, colData).getLevel();
        assertTrue("level should be valid", PixelLogicUtil.isValid(solvedLevel));
        assertArrayEquals("level should be equals", level, solvedLevel);

        // test invalid
        level = PixelLogicUtil.invalidSampleLevel();
        rowData = PixelLogicUtil.getRowData(level);
        colData = PixelLogicUtil.getColumnData(level);
        solvedLevel = new PixelLogicSolver().solve(rowData, colData).getLevel();
        assertFalse("level should be invalid", PixelLogicUtil.isValid(solvedLevel));
    }

    @Test
    public void solveLine() {
        PixelLogicSolver solver = new PixelLogicSolver();

        Boolean[] line;

        // easy test's
        line = new Boolean[]{null, null};
        assertFalse("there shouldn't be changes", solver.solveLine(line, Collections.singletonList(1)));
        assertArrayEquals(new Boolean[]{null, null}, line);

        line = new Boolean[]{null, null, null};
        assertTrue("there should be changes", solver.solveLine(line, Collections.singletonList(2)));
        assertArrayEquals(new Boolean[]{null, true, null}, line);

        // complex test
        line = new Boolean[]{null, null, false, true, null, null, true, false, null, false};
        assertTrue("there should be changes", solver.solveLine(line, Arrays.asList(1, 4)));
        assertArrayEquals(new Boolean[]{null, null, false, true, true, true, true, false, false, false}, line);

        // block test
        line = new Boolean[]{null, true, null, null, null, true, null};
        assertTrue("there should be changes", solver.solveLine(line, Arrays.asList(1, 2)));
        assertArrayEquals(new Boolean[]{false, true, false, false, null, true, null}, line);

        line = new Boolean[]{null, null, null, null, true, null, null, null, true, true, true};
        assertTrue("there should be changes", solver.solveLine(line, Arrays.asList(2, 3)));
        assertArrayEquals(new Boolean[]{false, false, false, null, true, null, false, false, true, true, true}, line);

        // test empty
        line = new Boolean[]{null, null, null};
        assertTrue("there should be changes", solver.solveLine(line, Arrays.asList(0)));
        assertArrayEquals(new Boolean[]{false, false, false}, line);
    }

}