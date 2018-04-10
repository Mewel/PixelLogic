package de.mewel.pixellogic.util;

import org.junit.Test;

public class PixelLogicUtilTest {

    @Test
    public void createRandomLevelSpeedTest() throws Exception {
        long sTime = System.currentTimeMillis();
        for (int i = 0; i < 5000; i++) {
            PixelLogicUtil.createRandomLevel(5, 5, 5, 6);
        }
        System.out.println(System.currentTimeMillis() - sTime + "ms");
    }

}
