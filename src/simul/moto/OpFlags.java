package simul.moto;
import java.math.BigInteger;

public class OpFlags 
{
	
	public static String somme(String a, String b)
	{
		BigInteger hex1 = new BigInteger(a, 16);
		BigInteger hex2 = new BigInteger(b, 16);
		BigInteger sum = hex1.add(hex2);
		String res = sum.toString(16);
		return res;
	}
	
}
