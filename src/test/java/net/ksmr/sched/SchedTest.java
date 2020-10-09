package net.ksmr.sched;

import static org.junit.Assert.*;

import org.junit.Test;

public class SchedTest {

    @Test
    public void pin() {
        Sched.setAffinity(1);

        long mask = Sched.getAffinity();

        assertEquals(1l, mask);
    }

    @Test
    public void pinAndReset() {
        long fullmask = Sched.getAffinity();

        Sched.setAffinity(1);

        long pinnedMask = Sched.getAffinity();

        Sched.setAffinity(Long.MAX_VALUE);

        long mask = Sched.getAffinity();

        assertEquals(1l, pinnedMask);
        assertEquals(fullmask, mask);
    }

}
