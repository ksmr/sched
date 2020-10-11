package net.ksmr.sched;


import java.util.BitSet;
import org.junit.Test;
import static org.junit.Assert.*;

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

    @Test
    public void pinBitSet() {
        long fullmask = Sched.getAffinity();

        BitSet mask = new BitSet();
        mask.set(0);
        mask.set(600);
        Sched.setAffinityBitSet(mask);

        long pinnedMask = Sched.getAffinity();

        Sched.setAffinity(Long.MAX_VALUE);

        long mask2 = Sched.getAffinity();

        assertEquals(1l, pinnedMask);
        assertEquals(fullmask, mask2);
    }

    @Test
    public void getAffinityBitSet() {
        Sched.setAffinity(1);

        BitSet set = Sched.getAffinityBitSet();

        assertEquals(1, set.cardinality());
        assertEquals(true, set.get(0));
    }

}
