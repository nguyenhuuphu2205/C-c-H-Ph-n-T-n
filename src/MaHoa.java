
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by nguyenhuuphu on 16-Mar-17.
 */
public class MaHoa {
    private BigInteger n, d, e;
    private int bitlen = 1024;
    public MaHoa(int bits) {
        bitlen = bits;
        SecureRandom r = new SecureRandom();
        BigInteger p = new BigInteger(bitlen / 2, 100, r);
        BigInteger q = new BigInteger(bitlen / 2, 100, r);
        n = p.multiply(q);
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q
                .subtract(BigInteger.ONE));
        e = new BigInteger("3");
        while (m.gcd(e).intValue() > 1) {
            e = e.add(new BigInteger("2"));
        }
        d = e.modInverse(m);
    }

    private   BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }


    private   BigInteger decrypt(BigInteger message,int mamat) {
        if(mamat==1996) {
            return message.modPow(d, n);
        }else
            return new BigInteger("123456789");
    }


    public static void main(String[] args) {
        MaHoa rsa = new MaHoa(1024);

        String text1 = "Nguyen Huu Phu";
        System.out.println("Plaintext: " + text1);
        BigInteger plaintext = new BigInteger(text1.getBytes());

        BigInteger ciphertext = rsa.encrypt(plaintext);
        System.out.println("Ciphertext: " + ciphertext);
        plaintext = rsa.decrypt(ciphertext,1996);

        String text2 = new String(plaintext.toByteArray());
        System.out.println("Plaintext: " + text2);
    }
}

