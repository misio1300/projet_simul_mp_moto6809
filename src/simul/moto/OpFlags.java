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
	public static String flags(int a, int b, int res)
	{
		int b7a = (a >> 7) & 1;
		int b7b = (b >> 7) & 1;
		int b7res = (res >> 7) & 1;
		
		boolean v = (b7a == b7b) && (b7a != b7res);
		boolean c = (res & 0x100) != 0;
		
		if(v && c) return "2";
		else if(v) return "DÉBORDEMENT";
		else if(c) return "RETENUE";
		else return "PASDE";
	}
}
