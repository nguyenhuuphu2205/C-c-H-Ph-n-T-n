
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
        BigInteger p = new BigInteger("33478071698956898786044169848212690817704794983713768568912431388982883793878002287614711652531743087737814467999489");
        BigInteger q = new BigInteger("36746043666799590428244633799627952632279158164343087642676032283815739666511279233373417143396810270092798736308917");
        n = p.multiply(q);
        BigInteger m = (p.subtract(BigInteger.ONE)).multiply(q
                .subtract(BigInteger.ONE));
        e = new BigInteger("17");

        d = e.modInverse(m);
    }
    /*
        Hàm mã hóa
     */
    public BigInteger encrypt(BigInteger message) {
        return message.modPow(e, n);
    }
    /*
        Hàm mã hóa
     */
    public String encrypt1(String message) {
        BigInteger bigInteger = new BigInteger(message.getBytes());
        return bigInteger.modPow(e, n).toString();

    }
    /*
            Hàm giải mã
     */
    public String decrypt1(String message, int mamat) {
        if (mamat == 1996) {
            BigInteger bigInteger = new BigInteger(message);
            return new String(bigInteger.modPow(d, n).toByteArray());
        } else
            return "123456789";

    }
    /*
            Hàm giải mã
     */
    public BigInteger decrypt(BigInteger message, int mamat) {
        if (mamat == 1996) {
            return message.modPow(d, n);
        } else
            return new BigInteger("123456789");
    }
}
