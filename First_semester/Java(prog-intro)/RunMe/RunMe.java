import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
// password Object catch short assert return Character
/**
 * Run this code with provided arguments.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@SuppressWarnings("MagicNumber")
public final class RunMe {
    private RunMe() {
        // Utility class
    }

    public static void main(final String[] args) {

        final byte[] password = parseArgs(args);

        key0(password);
        System.out.println("The first key was low-hanging fruit, can you find others?");
        System.out.println("Try to read, understand and modify code in keyX(...) functions");

        key1(password);
        key2(password);
        key3(password);
        key4(password);
        key5(password);
        key6(password);
        key7(password);
        key8(password);
        key9(password);
    //    key10(password);
        key11(password);
        key12(password);
        key13(password);
        key14(password);
        key15(password);
        key16(password);
        key17(password);
    }

    private static void key0(final byte[] password) {
        // The result of print(...) function depends only on explicit arguments
        print(0, 0, password);
    }


    private static void key1(final byte[] password) {

        print(1, 7580342570284508233L, password);
    }


    private static void key2(final byte[] password) {
        

        print(2, 3235749584985489343L, password);
    }

    private static void key3(final byte[] password) {


        print(3, 0, password);
    }


    private static void key4(final byte[] password) {
      /*  for (long i = 7832471307819155456L; i <= 7832471309966639104L   ; i++) {
            if ((i ^ (i >>> 32)) == 7832471308431238473L) {
                print(4, i, password);
            }
        } */
    }



    private static final long PRIME = 1073741783;

    private static void key5(final byte[] password) {
        BigInteger n = BigInteger.valueOf(999999999999999L + ByteBuffer.wrap(password).getInt());
        BigInteger result = BigInteger.ZERO;
        BigInteger PRIME1 = BigInteger.valueOf(1073741783);
        BigInteger k1 = (((n.divide(BigInteger.valueOf(2)).subtract(BigInteger.ONE)).multiply(n.divide(BigInteger.valueOf(2)))).divide(BigInteger.valueOf(2)).multiply(BigInteger.valueOf(2))).add((n.mod(BigInteger.valueOf(2)).add(BigInteger.ONE)).multiply(n.divide(BigInteger.valueOf(2))));
        BigInteger k2 = (((n.divide(BigInteger.valueOf(3)).subtract(BigInteger.ONE)).multiply(n.divide(BigInteger.valueOf(3)))).divide(BigInteger.valueOf(2)).multiply(BigInteger.valueOf(3))).add((n.mod(BigInteger.valueOf(3)).add(BigInteger.ONE)).multiply(n.divide(BigInteger.valueOf(3))));
        BigInteger k3 = (((n.divide(BigInteger.valueOf(5)).subtract(BigInteger.ONE)).multiply(n.divide(BigInteger.valueOf(5)))).divide(BigInteger.valueOf(2)).multiply(BigInteger.valueOf(5))).add((n.mod(BigInteger.valueOf(5)).add(BigInteger.ONE)).multiply(n.divide(BigInteger.valueOf(5))));
        BigInteger k4 = (((n.divide(BigInteger.valueOf(2023)).subtract(BigInteger.ONE)).multiply(n.divide(BigInteger.valueOf(2023)))).divide(BigInteger.valueOf(2)).multiply(BigInteger.valueOf(2023))).add((n.mod(BigInteger.valueOf(2023)).add(BigInteger.ONE)).multiply(n.divide(BigInteger.valueOf(2023))));
        result = (k1.add(k2).add(k3).add(k4)).mod(PRIME1);
        System.out.println(result);
        print(5, result.longValue(), password);
    } 


    private static void key6(final byte[] password) {
      /*  long result = 13420980954L + password[2];
      /*  long result = 13420980954L + password[2];
        print(6, result, password);
        for (int i = 0; i < password.length; i++) {
            System.out.println(password[i]);
        } */

    }

// 80
    private static void key7(final byte[] password) {
        // Count the number of occurrences of the most frequent noun at the following page:
        // https://docs.oracle.com/javase/specs/jls/se20/html/jls-4.html

        // The singular form of the most frequent noun
        final String singular = "type";
        // The plural form of the most frequent noun
        final String plural = "types";
        // The total number of occurrences
        final int total = 751;
        if (total != 0) {
            print(7, (singular + ":" + plural + ":" + total).hashCode(), password);
        }
    }


    private static void key8(final byte[] password) {
        // Count the number of red (#EC2025) pixes of this image:
        // https://1000logos.net/wp-content/uploads/2020/09/Java-Logo-1024x640.png

        final int number = 33508;
        if (number != 0) {
            print(8, number, password);
        }
    }


    private static final String PATTERN = "Reading the documentation can be surprisingly helpful!";
    private static final int SMALL_REPEAT_COUNT = 10_000_000;

    private static void key9(final byte[] password) {
        StringBuffer repeated = new StringBuffer("");
        for (int i = 0; i < SMALL_REPEAT_COUNT; i++) {
            repeated.append(PATTERN);
        }
        System.out.println(repeated.toString().hashCode());
        System.out.println("fdsaffdgmkm".hashCode());
        print(9, repeated.toString().hashCode(), password); 
    }


    private static final int LARGE_REPEAT_SHIFT = 28;
    private static final int LARGE_REPEAT_COUNT = 1 << LARGE_REPEAT_SHIFT;

    private static void key10(final byte[] password) {
        long n = LARGE_REPEAT_COUNT * PATTERN.length();
        int hashCode = 0; // Начальное значение хэш-кода
        for (int k = 0; k < LARGE_REPEAT_COUNT; k++) {
            for (int i = 0; i < PATTERN.length(); i++) {
                char ch = PATTERN.charAt(i); // Получаем символ из строки
                hashCode = 31 * hashCode + (int) ch; // Вычисляем хэш-код символа и обновляем хэш-код
            }
        }

    /*    //268435456
        System.out.println("Reading the documentation can be surprisingly helpful!".length());
        StringBuilder buffer = new StringBuilder("");
        String repeated = "";
        System.out.println(LARGE_REPEAT_COUNT);
        for (int i = 0; i < LARGE_REPEAT_COUNT; i++) {
            buffer.append(PATTERN);
            if (buffer.length() > 1_000_000_00) {
            //    System.out.println(repeated);
                repeated += buffer.toString();
                buffer = new StringBuilder("");
            }
        }
        repeated.concat(buffer.toString());
        System.out.println(repeated.length());
      //  String repeated = PATTERN.repeat(LARGE_REPEAT_COUNT); */
        print(10, hashCode, password);
    }


    private static void key11(final byte[] password) {
        print(11, 3498732498723948739L, password);
    }

    private static void key12(final byte[] password) {
        final BigInteger year = BigInteger.valueOf(-2023);
        final BigInteger prime = BigInteger.valueOf(PRIME + 4);

        final long result = Stream.iterate(BigInteger.ZERO, BigInteger.ONE::add)
                .limit((PRIME + 5) / 2023 + 100)
                .filter(i -> year.multiply(i).add(prime).multiply(i).compareTo(BigInteger.ZERO) > 0)
                .mapToLong(i -> i.longValue() * password[i.intValue() % password.length])
                .sum();

        print(12, result, password);
    }





    private static final long MAX_DEPTH = 100_000_000L;

    private static void key13(final byte[] password) {
        try {
            key13(password, 0, 0);
        } catch (final StackOverflowError e) {
            System.err.println("Stack overflow :((");
        }
    }

    private static void key13(final byte[] password, long depth, long result) {
        long k = 0;
        for (long i = 0; i < MAX_DEPTH; i++) {
            k = (k ^ 475934873) + (k << 2) + i * 17;
        }
        print(13, k, password);
    }

  /*  private static void key13(final byte[] password, final long depth, final long result) {
        if (depth < MAX_DEPTH) {
            key13(password, depth + 1, (result ^ 475934873) + (result << 2) + depth * 17);
        } else {
            print(13, result, password);
        }
    } */


    private static void key14(final byte[] password) {
        final Instant today = Instant.parse("2023-09-04T12:00:00Z");
        final BigInteger hours = BigInteger.valueOf(Duration.between(Instant.EPOCH, today).toHours());
        System.out.println(hours);
      /*  final long result = Stream.iterate(BigInteger.ZERO, BigInteger.ONE::add)
                .map(hours::multiply)
                .reduce(BigInteger.ZERO, BigInteger::add)
                .longValue(); */        

        print(14, hours.longValue(), password);
       }


    private static void key15(final byte[] password) {
        print(15, 2387498237498232333L + password[2], password);
    }   

    private static void key16(final byte[] password) {
        byte a1 = (byte) (password[0] + password[1]);
        byte a2 = (byte) (password[2] + password[3]);
        byte a3 = (byte) (password[4] + password[5]);
        System.out.println("0" + Integer.toBinaryString(a1));
        System.out.println("0" + Integer.toBinaryString(a2));
        System.out.println(Integer.toBinaryString(a3));
        byte a1_first = a1;
        byte a2_first = a2;
        byte a3_first = a3;
        for (long i = (1_000_000_000_000_000L + ByteBuffer.wrap(password).getInt()) % 10080; i >= 0; i--) {
            a1 ^= a2;
            a2 += a3 | a1;
            a3 -= a1;
            if (a1 == a1_first && a2 == a2_first && a3 == a3_first) {
                System.out.println(i);
            }
        /*    System.out.println("----------");
            System.out.println(Integer.toBinaryString(a1));
            System.out.println(Integer.toBinaryString(a2));
            System.out.println(Integer.toBinaryString(a3));   */
        }

        key16(password, a1, a2, a3);
    }

    private static void key16(final byte[] password, final byte a1, final byte a2, final byte a3) {
        final String result = a1 + " " + a2 + " " + a3;
        print(16, result.hashCode(), password);
    }

    private static void key17(final byte[] password) {
      /*  String s = "";
        for (   int i = 0; i < password.length; i++) {
            s += Integer.toString((int) password[i]);
        }
        System.out.println(s);
        System.out.println(Arrays.toString(password).hashCode() % 2022);
        System.out.println("hashCode(): " + Arrays.toString(password).hashCode());
        System.out.println("Arrays.toString(): " + Arrays.toString(password)); */
        print(17, calc17(Math.abs(Arrays.toString(password).hashCode() % 2022)), password);
    }

    /**
         * Write me
         * <pre>
             *    0: iconst_0                 //Push int constant
             *    1: istore_1                 //Store int into local variable
             *    2: iload_1                  //
             *    3: bipush        23
             *    5: idiv
             *    6: iload_0
         *    7: isub
         *    8: ifge          17
         *   11: iinc          1, 1
         *   14: goto          2
         *   17: iload_1
         *   18: ireturn
         * </pre>
     */
    private static int calc17(int n) {
        int i = 0;
        while (true) {
            if (i / 23 - n >= 0) {
                return i;
            } else {
                i++;
            }
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // You may ignore all code below this line.
    // It is not required to get any of the keys.
    // ---------------------------------------------------------------------------------------------------------------

    private static void print(final int no, long result, final byte[] password) {
        final byte[] key = password.clone();
        for (int i = 0; i < 6; i++) {
            key[i] ^= result;
            result >>>= 8;
        }

        System.out.format("Key %d: https://www.kgeorgiy.info/courses/prog-intro/hw1/%s%n", no, key(SALT, key));
    }

    private static String key(final byte[] salt, final byte[] data) {
        DIGEST.update(salt);
        DIGEST.update(data);
        DIGEST.update(salt);
        final byte[] digest = DIGEST.digest();

        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (i != 0) {
                sb.append("-");
            }
            sb.append(KEYWORDS.get(digest[i] & 63));
        }
        return sb.toString();
    }

    private static byte[] parseArgs(final String[] args) {
        if (args.length != 6) {
            throw error("Expected 6 command line arguments, found: %d", args.length);
        }

        final byte[] bytes = new byte[args.length];
        for (int i = 0; i < args.length; i++) {
            final Byte value = VALUES.get(args[i].toLowerCase(Locale.US));
            if (value == null) {
                throw error("Expected keyword, found: %s", args[i]);
            }
            bytes[i] = value;
        }
        return bytes;
    }

    private static AssertionError error(final String format, final Object... args) {
        System.err.format(format, args);
        System.err.println();
        System.exit(1);
        throw new AssertionError();
    }

    private static final MessageDigest DIGEST;
    static {
        try {
            DIGEST = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException e) {
            throw new AssertionError("Cannot create SHA-256 digest", e);
        }
    }

    private static final byte[] SALT = "jig6`wriusoonBaspaf9TuRutabyikUch/Bleir3".getBytes(StandardCharsets.UTF_8);

    private static final List<String> KEYWORDS = List.of(
            "abstract",
            "assert",
            "boolean",
            "break",
            "byte",
            "case",
            "catch",
            "char",
            "class",
            "const",
            "continue",
            "default",
            "do",
            "double",
            "else",
            "enum",
            "extends",
            "false",
            "final",
            "finally",
            "float",
            "for",
            "goto",
            "if",
            "implements",
            "import",
            "instanceof",
            "int",
            "interface",
            "long",
            "native",
            "new",
            "null",
            "package",
            "private",
            "protected",
            "public",
            "return",
            "short",
            "static",
            "strictfp",
            "super",
            "switch",
            "synchronized",
            "this",
            "throw",
            "throws",
            "transient",
            "true",
            "try",
            "var",
            "void",
            "volatile",
            "while",
            "Exception",
            "Error",
            "Object",
            "Number",
            "Integer",
            "Character",
            "String",
            "Math",
            "Runtime",
            "Throwable"
    );

    private static final Map<String, Byte> VALUES = IntStream.range(0, KEYWORDS.size())
            .boxed()
            .collect(Collectors.toMap(index -> KEYWORDS.get(index).toLowerCase(Locale.US), Integer::byteValue));
}
