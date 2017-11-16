package oose.p.c6.imd.service;

import javax.ejb.Singleton;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

@Singleton
class RandomTokenString {
    /*
     * Modified from https://stackoverflow.com/questions/41107/how-to-generate-a-random-alpha-numeric-string
     */


    /**
     * Generate a random string.
     */
    String nextString() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        String s1, s2, s3;
        String s = new String(buf);
        s1 = s.substring(0,4);
        s2 = s.substring(4,8);
        s3 = s.substring(8,12);
        return(s1 + "-" + s2 + "-" + s3);
    }

    private static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String lower = upper.toLowerCase(Locale.ROOT);

    private static final String digits = "0123456789";

    private static final String alphanum = upper + lower + digits;

    private final Random random;

    private final char[] symbols;

    private final char[] buf;

    private RandomTokenString(int length, Random random, String symbols) {
        if (length < 1) throw new IllegalArgumentException();
        if (symbols.length() < 2) throw new IllegalArgumentException();
        this.random = Objects.requireNonNull(random);
        this.symbols = symbols.toCharArray();
        this.buf = new char[length];
    }

    /**
     * Create an alphanumeric string generator.
     */
    public RandomTokenString(int length, Random random) {
        this(length, random, alphanum);
    }

    /**
     * Create an alphanumeric strings from a secure generator.
     */
    public RandomTokenString(int length) {
        this(length, new SecureRandom());
    }

    /**
     * Create session identifiers.
     */
    public RandomTokenString() {
        this(12);
    }

}
