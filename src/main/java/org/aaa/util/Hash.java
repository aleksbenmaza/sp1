package org.aaa.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alexandremasanes on 26/02/2017.
 */
public final class Hash {

    public  final static int MD5     = 0x1,
                             SHA_1   = 0x2,
                             SHA_256 = 0x3;

    private final static String[] algorithms = {
            "MD5",
            "SHA-1",
            "SHA-256"
    };

    public static String encrypt(String str, int algo) throws NoSuchAlgorithmException {
        MessageDigest d;
        d = MessageDigest.getInstance(algorithms[algo-1]);
        d.reset();
        d.update(str.getBytes());


        byte[] bytes = d.digest();
        String result = "";
        for(byte b : bytes)
            result += Integer.toString( ( b & 0xff ) + 0x100, 16).substring( 1 );
        return result;
    }

    private Hash() {}
}
