package de.mewel.pixellogic.util;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by mewel on 09.11.2017.
 */

public class PixelLogicBruteForceSolverTest {

    @Test
    public void bruteForceLine() {
        PixelLogicBruteForceSolver solver = new PixelLogicBruteForceSolver();

        // easy test's
        Boolean[] line = new Boolean[]{null, null};
        Boolean[] newLine = solver.bruteForceLine(line, Collections.singletonList(1), 0);
        assertFalse("there shouldn't be changes", PixelLogicUtil.differs(line, newLine));

        line = new Boolean[] {null, null, null};
        newLine = solver.bruteForceLine(line, Collections.singletonList(2), 0);
        assertTrue("there should be changes", PixelLogicUtil.differs(line, newLine));
    }

}