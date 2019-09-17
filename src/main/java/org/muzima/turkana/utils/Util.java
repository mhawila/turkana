package org.muzima.turkana.utils;

import javax.annotation.Nullable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class Util {
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

        for (int index=0;index<list.size();index+=partitionSize) {
            int subListSize = Math.min(partitionSize, list.size() - index);

            results.add(list.subList(index, index + subListSize));
        }

        return results;
    }


    public static byte[][] split(byte[] input, int firstLength, int secondLength) {
        byte[][] parts = new byte[2][];

        parts[0] = new byte[firstLength];
        System.arraycopy(input, 0, parts[0], 0, firstLength);

        parts[1] = new byte[secondLength];
        System.arraycopy(input, firstLength, parts[1], 0, secondLength);

        return parts;
    }

    public static byte[] combine(byte[]... elements) {
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

    public static byte[] trim(byte[] input, int length) {
        byte[] result = new byte[length];
        System.arraycopy(input, 0, result, 0, result.length);

        return result;
    }


}