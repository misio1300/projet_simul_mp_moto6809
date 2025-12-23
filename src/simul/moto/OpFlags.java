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
	public static String substraction(String a, String b)
	{
		BigInteger hex1 = new BigInteger(a, 16);
		BigInteger hex2 = new BigInteger(b, 16);
		BigInteger sub = hex1.subtract(hex2);
		String res = sub.toString(16);
		return res;
	}
	public static String multiplication(String a, String b)
	{
		BigInteger hex1 = new BigInteger(a, 16);
		BigInteger hex2 = new BigInteger(b, 16);
		BigInteger mul = hex1.multiply(hex2);
		String res = mul.toString(16);
		return res;
	}
	public static String flags(int a, int b, int res)
	{
		int b7a = (a >> 7) & 1;
		int b7b = (b >> 7) & 1;
		int b7res = (res >> 7) & 1;
		
		boolean vsum = (b7a == b7b) && (b7a != b7res);
		boolean csum = (res & 0x100) != 0;
		boolean vsub = (b7a != b7b) && (b7a != b7res);
		boolean csub = (a & 0xFF) < (b & 0xFF);
		boolean cmul = (b7b & 1) != 0;
		boolean z = (res & 0xFF) == 0;
		boolean n = (b7res & 1) != 0
				;
		String flags = "";
		if(vsum) return flags += "vsum";
		else if(csum) return flags += "csum";
		else if(vsub) return flags += "vsub";
		else if(csub) return flags += "csub";
		else if(cmul) return flags += "cmul";
		else if(n) return flags += "n";
		else if(z) return flags += "z";
		
		else return "PASDE";
	}
}
