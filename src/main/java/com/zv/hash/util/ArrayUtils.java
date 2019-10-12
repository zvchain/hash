package com.zv.hash.util;

public interface ArrayUtils {

    default void arrayCopy(byte[] src, int srcPos, byte[] dest, int destPos, int length) {
        for (int n = 0; n < length; n++) dest[n + destPos] = src[n + srcPos];
    }

    default void arrayCopy(int[] src, int srcPos, int[] dest, int destPos, int length) {
        for (int n = 0; n < length; n++) dest[n + destPos] = src[n + srcPos];
    }

    default void arrayCopy(long[] src, int srcPos, long[] dest, int destPos, int length) {
        for (int n = 0; n < length; n++) dest[n + destPos] = src[n + srcPos];
    }

    default void fill(byte[] a, byte val) {
        for (int i = 0, len = a.length; i < len; i++) a[i] = val;
    }

    default void fill(int[] a, int val) {
        for (int i = 0, len = a.length; i < len; i++) a[i] = val;
    }

    default void fill(long[] a, long val) {
        for (int i = 0, len = a.length; i < len; i++) a[i] = val;
    }
}
