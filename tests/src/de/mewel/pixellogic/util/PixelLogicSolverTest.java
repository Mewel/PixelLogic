package de.mewel.pixellogic.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;


import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicSolverTest {

    @Test
    public void solve() throws Exception {
        boolean[][] level = PixelLogicUtil.invalidSampleLevel();
        System.out.println(new PixelLogicLevel(level).toString());
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level);
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level);
        Boolean[][] solve = new PixelLogicSolver().solve(rowData, colData);
        System.out.println(PixelLogicSolver.print(solve));

    }

    @Test
    public void linePart() throws Exception {
        PixelLogicSolver.LinePart part = new PixelLogicSolver.LinePart();

        // test center pixel
        Boolean[] line = {null, null, null, null, null};
        part.start = 0;
        part.pixels = line.clone();
        assertTrue("there should be changes", part.apply(line, 3));
        assertArrayEquals(new Boolean[]{null, null, true, null, null}, line);

        // test fill between
        line = new Boolean[]{null, null, true, null, true, null, null, null};
        part.start = 0;
        part.pixels = line.clone();
        assertTrue("there should be changes", part.apply(line, 4));
        assertArrayEquals(new Boolean[]{null, null, true, true, true, null, null, null}, line);

        // test add right
        line = new Boolean[]{null, true, null, null, null, null, null, null};
        part.start = 0;
        part.pixels = line.clone();
        assertTrue("there should be changes", part.apply(line, 4));
        assertArrayEquals(new Boolean[]{null, true, true, true, null, null, null, null}, line);

        // test add left
        line = new Boolean[]{null, null, null, null, null, null, true, null};
        part.start = 0;
        part.pixels = line.clone();
        assertTrue("there should be changes", part.apply(line, 4));
        assertArrayEquals(new Boolean[]{null, null, null, null, true, true, true, null}, line);

        // test add false if line is full
        line = new Boolean[]{true, null, null, null, null};
        part.start = 0;
        part.pixels = line.clone();
        assertTrue("there should be changes", part.apply(line, 2));
        assertArrayEquals(new Boolean[]{true, true, false, null, null}, line);

        line = new Boolean[]{null, true, true, null, null};
        part.start = 0;
        part.pixels = line.clone();
        assertTrue("there should be changes", part.apply(line, 2));
        assertArrayEquals(new Boolean[]{false, true, true, false, null}, line);
    }

    @Test
    public void splitOnBlocked() {
        PixelLogicSolver solver = new PixelLogicSolver();

        // easy test
        Boolean[] line = new Boolean[]{false, null};
        List<PixelLogicSolver.LinePart> parts = solver.splitOnBlocked(line, 0, line.length);
        assertEquals(1, parts.size());
        assertEquals(1, parts.get(0).start);
        assertEquals(1, parts.get(0).pixels.length);

        // complex test
        line = new Boolean[]{null, null, false, true, null, null, true, false, null, false};
        parts = solver.splitOnBlocked(line, 0, line.length);
        assertEquals(3, parts.size());
        assertEquals(0, parts.get(0).start);
        assertArrayEquals(new Boolean[]{null, null}, parts.get(0).pixels);
        assertEquals(3, parts.get(1).start);
        assertArrayEquals(new Boolean[]{true, null, null, true}, parts.get(1).pixels);
        assertEquals(8, parts.get(2).start);
        assertArrayEquals(new Boolean[]{null}, parts.get(2).pixels);
    }

    @Test
    public void checkLine() {
        PixelLogicSolver solver = new PixelLogicSolver();

        // easy test
        Boolean[] line = new Boolean[]{false, null};
        assertTrue("there should be changes", solver.checkLine(line, Collections.singletonList(1)));
        assertArrayEquals(new Boolean[]{false, true}, line);

        // complex test
        line = new Boolean[]{null, null, false, true, null, null, true, false, null, false};
        assertTrue("there should be changes", solver.checkLine(line, Arrays.asList(1, 4)));
        assertArrayEquals(new Boolean[]{null, null, false, true, true, true, true, false, false, false}, line);

        // block test
        line = new Boolean[]{null, true, null, null, null, true, null};
        assertTrue("there should be changes", solver.checkLine(line, Arrays.asList(1, 2)));
        assertArrayEquals(new Boolean[]{false, true, false, false, null, true, null}, line);
    }

}
