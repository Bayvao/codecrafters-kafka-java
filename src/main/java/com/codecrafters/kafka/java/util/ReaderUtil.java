package com.codecrafters.kafka.java.util;

public class ReaderUtil {
    /**
     * @param data (byte[4])
     * @return unsigned int (0 - 4,294,967,295)
     */
    public static long toUInt(final byte[] data) {
        if (data == null || data.length != 4)
            throw new IllegalArgumentException("!= 4 bytes");
        return (long) (
                (long) (data[3] & 0xffL) << 24 |
                        (long) (data[2] & 0xffL) << 16 |
                        (long) (data[1] & 0xffL) << 8 |
                        (long) (data[0] & 0xffL)
        );
    }

    /**
     * @param data (byte[2])
     * @return short (0 - 65,535)
     */
    public static int toUShort(final byte[] data) {
        if (data == null || data.length != 2)
            throw new IllegalArgumentException("!= 2 bytes");
        return (int)(
                (int)(data[1] & 0xff) << 8  |
                        (int)(data[0] & 0xff)
        );
    }

    /**
     * @param data (byte[1])
     * @return short (0-255)
     */
    public static short toUByte(final byte[] data) {
        if (data == null || data.length != 1)
            throw new IllegalArgumentException("!= 1 byte");
        return (short) (data[0] & 0xff);
    }


}
