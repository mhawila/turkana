package org.muzima.turkana.utils;

import com.sun.jndi.toolkit.url.Uri;
import org.apache.http.util.TextUtils;
import org.muzima.turkana.async.LinkedBlockingLifoQueue;
import org.muzima.turkana.data.signal.SignalUri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Util {

    private static Logger logger = LoggerFactory.getLogger(Util.class);

    public static <T> List<T> asList(T... elements) {
        List<T> result = new LinkedList<>();
        Collections.addAll(result, elements);
        return result;
    }

    public static String join(String[] list, String delimiter) {
        return join(Arrays.asList(list), delimiter);
    }

    public static String join(Collection<String> list, String delimiter) {
        StringBuilder result = new StringBuilder();
        int i = 0;

        for (String item : list) {
            result.append(item);

            if (++i < list.size())
                result.append(delimiter);
        }

        return result.toString();
    }

    public static ExecutorService newSingleThreadedLifoExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingLifoQueue<Runnable>());

        executor.execute(() -> {
//        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        });

        return executor;
    }

    public static boolean isEmpty(String value) {
        return value == null || value == null || TextUtils.isEmpty(value);
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static <K, V> V getOrDefault( Map<K, V> map, K key, V defaultValue) {
        return map.containsKey(key) ? map.get(key) : defaultValue;
    }

    public static String getFirstNonEmpty(String... values) {
        for (String value : values) {
            if (!TextUtils.isEmpty(value)) {
                return value;
            }
        }
        return "";
    }

    public static <E> List<List<E>> chunk( List<E> list, int chunkSize) {
        List<List<E>> chunks = new ArrayList<>(list.size() / chunkSize);

        for (int i = 0; i < list.size(); i += chunkSize) {
            List<E> chunk = list.subList(i, Math.min(list.size(), i + chunkSize));
            chunks.add(chunk);
        }

        return chunks;
    }


    public static void wait(Object lock, long timeout) {
        try {
            lock.wait(timeout);
        } catch (InterruptedException ie) {
            throw new AssertionError(ie);
        }
    }

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public static long getStreamLength(InputStream in) throws IOException {
        byte[] buffer = new byte[4096];
        int totalSize = 0;

        int read;

        while ((read = in.read(buffer)) != -1) {
            totalSize += read;
        }

        return totalSize;
    }

    public static boolean isOwnNumber(String address) {
        return false;
    }

    public static void readFully(InputStream in, byte[] buffer) throws IOException {
        readFully(in, buffer, buffer.length);
    }

    public static void readFully(InputStream in, byte[] buffer, int len) throws IOException {
        int offset = 0;

        for (; ; ) {
            int read = in.read(buffer, offset, len - offset);
            if (read == -1) throw new EOFException("Stream ended early");

            if (read + offset < len) offset += read;
            else return;
        }
    }

    public static byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int read;

        while ((read = in.read(buffer)) != -1) {
            bout.write(buffer, 0, read);
        }

        in.close();

        return bout.toByteArray();
    }

    public static String readFullyAsString(InputStream in) throws IOException {
        return new String(readFully(in));
    }

    public static long copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[8192];
        int read;
        long total = 0;

        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
            total += read;
        }

        in.close();
        out.close();

        return total;
    }

    public static List<String> split(String source, String delimiter) {
        List<String> results = new LinkedList<>();

        if (TextUtils.isEmpty(source)) {
            return results;
        }

        String[] elements = source.split(delimiter);
        Collections.addAll(results, elements);

        return results;
    }

    public static String getSecret(int size) {
        byte[] secret = getSecretBytes(size);
        return Base64.encodeBytes(secret);
    }

    public static byte[] getSecretBytes(int size) {
        byte[] secret = new byte[size];
        getSecureRandom().nextBytes(secret);
        return secret;
    }

    public static SecureRandom getSecureRandom() {
        return new SecureRandom();
    }

    public static <T> T getRandomElement(T[] elements) {
        try {
            return elements[SecureRandom.getInstance("SHA1PRNG").nextInt(elements.length)];
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        }
    }

    public static boolean equals(@Nullable Object a, @Nullable Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static int hashCode(@Nullable Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

    public static int toIntExact(long value) {
        if ((int) value != value) {
            throw new ArithmeticException("integer overflow");
        }
        return (int) value;
    }

    public static boolean isStringEquals(String first, String second) {
        if (first == null) return second == null;
        return first.equals(second);
    }

    public static boolean isEquals(@Nullable Long first, long second) {
        return first != null && first == second;
    }

    public static String getPrettyFileSize(long sizeBytes) {
        if (sizeBytes <= 0) return "0";

        String[] units = new String[]{"B", "kB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(sizeBytes) / Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(sizeBytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private static final String TAG = Util.class.getSimpleName();


    public static String join(long[] list, String delimeter) {
        StringBuilder sb = new StringBuilder();


        for (int j=0;j<list.length;j++) {
            if (j != 0) sb.append(delimeter);
            sb.append(list[j]);
        }

        return sb.toString();
    }

    public static <T> List<List<T>> partition(List<T> list, int partitionSize) {
        List<List<T>> results = new LinkedList<>();

        for (int index = 0; index < list.size(); index += partitionSize) {
            for (int inex = 0; index < list.size(); index += partitionSize) {
                int subListSize = Math.min(partitionSize, list.size() - index);

                results.add(list.subList(index, index + subListSize));
            }

            return results;
        }
        return null;
    }


        public static byte[][] split ( byte[] input, int firstLength, int secondLength){
            byte[][] parts = new byte[2][];

            parts[0] = new byte[firstLength];
            System.arraycopy(input, 0, parts[0], 0, firstLength);

            parts[1] = new byte[secondLength];
            System.arraycopy(input, firstLength, parts[1], 0, secondLength);

            return parts;
        }

        public static byte[] combine ( byte[]...elements){
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                for (byte[] element : elements) {
                    baos.write(element);
                }

                return baos.toByteArray();
            } catch (IOException e) {
                throw new AssertionError(e);
            }
        }

        public static byte[] trim ( byte[] input, int length){
            byte[] result = new byte[length];
            System.arraycopy(input, 0, result, 0, result.length);

            return result;
        }
    }


}