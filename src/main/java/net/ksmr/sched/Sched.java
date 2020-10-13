package net.ksmr.sched;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.BitSet;

/**
 * Utils to set and get the CPU affinity mask of the current <code>Thread</code>.
 */
public class Sched {

    private static boolean supported = false;

    static {
        supported = checkSupport();
        if (supported) {
            try {
                loadNative("libsched-" + System.getProperty("os.arch") + ".so");
            } catch (Exception e) {
                supported = false;
            }
        }
    }

    private Sched() {
    }

    /**
     * Sets the CPU affinity mask of the current thread to <code>mask</code>.
     *
     * Only works on machines with at most 64 processors. For larger ranges of CPUs use <code>setAffinityBitSet</code>.
     *
     * @param mask The CPU affinity mask
     * @throws UnsupportedOperationException If the operating system is not supported by this library.
     * @throws IllegalArgumentException If the mask contains no processors that are currently physically on the system and permitted to the current thread.
     */
    public static void setAffinity(long mask) {
        if (!supported)
            throw new UnsupportedOperationException();

        linux_sched_setaffinity(mask);
    }

    /**
     * Sets the CPU affinity mask of the current thread to <code>mask</code>.
     *
     * @param mask The CPU affinity mask
     * @throws UnsupportedOperationException If the operating system is not supported by this library.
     * @throws IllegalArgumentException If the mask contains no processors that are currently physically on the system and permitted to the current thread.
     */
    public static void setAffinityBitSet(final BitSet mask) {
        if (!supported)
            throw new UnsupportedOperationException();

        linux_sched_setaffinity_dynamic(mask.toLongArray());
    }

    /**
     * Gets the CPU affinity mask of the current thread as a <code>long</code>.
     *
     * Only works on machines with at most 64 processors. For larger ranges of CPUs use <code>getAffinityBitSet</code>.
     *
     * @throws UnsupportedOperationException If the operating system is not supported by this library.
     */
    public static long getAffinity() {
        if (!supported)
            throw new UnsupportedOperationException();

        return linux_sched_getaffinity();
    }
    /**
     * Gets the CPU affinity mask of the current thread as a <code>long</code>.
     *
     * @throws UnsupportedOperationException If the operating system is not supported by this library.
     */
    public static BitSet getAffinityBitSet() {
        if (!supported)
            throw new UnsupportedOperationException();

        return BitSet.valueOf(linux_sched_getaffinity_dynamic());
    }

    private static native void linux_sched_setaffinity(long mask);

    private static native void linux_sched_setaffinity_dynamic(long[] mask);

    private static native long linux_sched_getaffinity();

    private static native long[] linux_sched_getaffinity_dynamic();

    private static boolean checkSupport() {
        return System.getProperty("os.name").contains("Linux");
    }

    private static void loadNative(final String name) throws IOException {
        InputStream in = Sched.class.getClassLoader().getResourceAsStream(name);

        if (in == null)
            throw new UnsupportedOperationException();

        int pos = name.lastIndexOf(".");
        File tmpFile = File.createTempFile(name.substring(0, pos), name.substring(pos));
        tmpFile.deleteOnExit();

        try {
            byte[] buffer = new byte[4096];
            OutputStream out = new FileOutputStream(tmpFile);

            try {
                while (in.available() > 0) {
                    int len = in.read(buffer);
                    if (len >= 0)
                        out.write(buffer, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }

        System.load(tmpFile.toString());
    }

}
