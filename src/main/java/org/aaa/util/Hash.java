package org.aaa.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by alexandremasanes on 26/02/2017.
 */
public class Hash {

    public  final static int MD5     = 0x1,
                             SHA_1   = 0x2,
                             SHA_256 = 0x3;

    private final static String[] algorithms = {
            "MD5",
            "SHA-1",
            "SHA-256"
    };

    public static String encrypt(String str, int algo) throws NoSuchAlgorithmException {
        java.security.MessageDigest d;
        d = MessageDigest.getInstance(algorithms[algo-1]);
        d.reset();
        d.update(str.getBytes());


        byte[] bytes = d.digest();
        String result = "";
        for (int i=0; i < bytes.length; i++)
            result += Integer.toString( ( bytes[i] & 0xff ) + 0x100, 16).substring( 1 );
        return result;
    }

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub

        System.out.println(encrypt("toto", SHA_1));

    }

    private Hash(){}
}
