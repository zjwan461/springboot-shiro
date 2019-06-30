package com.itsu.springbootshiro.util;

import com.itsu.springbootshiro.shiro.bytesource.MySimpleByteSource;
import org.apache.shiro.util.ByteSource;

import java.io.File;
import java.io.InputStream;

/**
 * @author 苏犇
 * @date 2019/6/30 16:48
 */

public class ByteSourceUtil {

    public static ByteSource bytes(byte[] bytes) {
        return new MySimpleByteSource(bytes);
    }


    public static ByteSource bytes(char[] chars) {
        return new MySimpleByteSource(chars);
    }


    public static ByteSource bytes(String string) {
        return new MySimpleByteSource(string);
    }


    public static ByteSource bytes(ByteSource source) {
        return new MySimpleByteSource(source);
    }


    public static ByteSource bytes(File file) {
        return new MySimpleByteSource(file);
    }


    public static ByteSource bytes(InputStream stream) {
        return new MySimpleByteSource(stream);
    }


    public static boolean isCompatible(Object source) {
        return MySimpleByteSource.isCompatible(source);
    }


    public static ByteSource bytes(Object source) throws IllegalArgumentException {
        if (source == null) {
            return null;
        }
        if (!isCompatible(source)) {
            String msg = "Unable to heuristically acquire bytes for object of type [" +
                    source.getClass().getName() + "].  If this type is indeed a byte-backed data type, you might " +
                    "want to write your own ByteSource implementation to extract its bytes explicitly.";
            throw new IllegalArgumentException(msg);
        }
        if (source instanceof byte[]) {
            return bytes((byte[]) source);
        } else if (source instanceof ByteSource) {
            return (ByteSource) source;
        } else if (source instanceof char[]) {
            return bytes((char[]) source);
        } else if (source instanceof String) {
            return bytes((String) source);
        } else if (source instanceof File) {
            return bytes((File) source);
        } else if (source instanceof InputStream) {
            return bytes((InputStream) source);
        } else {
            throw new IllegalStateException("Encountered unexpected byte source.  This is a bug - please notify " +
                    "the Shiro developer list asap (the isCompatible implementation does not reflect this " +
                    "method's implementation).");
        }
    }
}
