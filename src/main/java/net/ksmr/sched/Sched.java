package net.ksmr.sched;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;

/**
 * Utils to set and get the CPU affinity mask of the current <code>Thread</code>.
 */
public class Sched {

    private static boolean supported = false;

    static {
        supported = checkLinux();
        if (supported) {
            try {
                loadNative("net_ksmr_sched_Sched.so");
            } catch (IOException e) {
                supported = false;
            }
        }
    }

    private Sched() {
    }

    /**
     * Sets the CPU affinity mask of the current thread to <code>mask</code>.
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
     * Gets the CPU affinity mask of the current thread as a <code>long</code>.
     *
     * @throws UnsupportedOperationException If the operating system is not supported by this library.
     */
    public static long getAffinity() throws IllegalArgumentException, UnsupportedOperationException {
        if (!supported)
            throw new UnsupportedOperationException();

        return linux_sched_getaffinity();
    }

    private static native void linux_sched_setaffinity(long mask);

    private static native long linux_sched_getaffinity();

    private static boolean checkLinux() {
        return System.getProperty("os.name").contains("Linux");
    }

    private static void loadNative(final String name) throws IOException {
        InputStream in = Sched.class.getClassLoader().getResourceAsStream(name);

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
