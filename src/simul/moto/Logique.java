package simul.moto;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Logique 
{
	private MOTOController Controller;
	public static Map<String, Consumer<String>> instructions = new HashMap<>();
	public static Map<String, String> opcode = new HashMap<>();

	public Logique(MOTOController Controller)
	{
		this.Controller = Controller;
	}
	public String fetch(String programme)
	{
		String[] lignes = programme.split("\\R");
		if(lignes.length == 0)
		{
			return "Pas d'instruction";
		}
		else
		{
			int numLigne = 1;

		    for (String ligne : lignes)
		    {
		        if (ligne.isBlank()) 
		        {
		            numLigne++;
		            continue;
		        }

		        if (ligne.equals("END")) 
		        {
		            return null;
		        }
		        String[] mot = ligne.split("\\s+");
		        String inst = mot[0].toUpperCase();
		        String mod  = (mot.length > 1) ? mot[1] : "";

		        String mode = decode(mod);

		        if ("FALSE".equals(mode)) {
		            return "Mode invalide à la ligne " + numLigne;
		        }

		        String key = inst + mode;
		        if (!instructions.containsKey(inst) || !opcode.containsKey(key)) {
		            return "Instruction invalide à la ligne " + numLigne + " : " + inst;
		        }

		        numLigne++;
		    }

		    return "END manquant à la fin du programme";
		}
	}
	
	public String decode(String m)
	{
		//char mod = m.charAt(0);
		if((m.startsWith("#$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F'))) && m.length() == 4)
		|| (m.startsWith("#$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F'))) && m.length() == 6))
			return "IMMEDIAT";
		else if(m.startsWith(">$") && m.length() == 5)
			return "ETENDU";
		else if(m.startsWith("<$") && m.length() == 4)
			return "DIRECT";
		else if(m.startsWith("[$") && m.endsWith("]") && m.length() == 7)
			return "ETENDU INDIRECT";
		else if(m == null || m.isEmpty())
			return "INHERENT";
		else if(m.startsWith(",") && m.length() == 2)
			return "INDEXEDEPNULL";
		else if(m.startsWith(",") && m.length() == 3)
			return "INDEXEAUTODEC1";
		else if(m.startsWith(",") && m.length() == 4)
			return "INDEXEAUTODEC2";
		else if(m.startsWith(",") && m.length() == 3)
			return "INDEXEAUTOINC1";
		else if(m.startsWith(",") && m.length() == 4)
			return "INDEXEAUTOINC2";
		else if(m.startsWith("A,") && m.length() == 3)
			return "INDEXEDEPABASE' '";
		else if(m.startsWith("B,") && m.length() == 3)
			return "INDEXEDEPBBASE' '";
		else if(m.startsWith("D,") && m.length() == 3)
			return "INDEXEDEPDBASE' '";
		else
			return "FALSE";
	}
	
	public static void execute(String inst, String mod)
	{
		Consumer<String> ins = instructions.get(inst);
	    if (ins != null) ins.accept(mod);
	}
	public void ecrireMemoire(int address, int val) {
	    Memoire.ecrireInt(address, val);
	    Controller.atualiserCaseMemoire(address);
	}
	public void progMemoire(String prog)
	{
		int adr = 0xFC00;
		String[] lignes = prog.split("\\R");
		Ex:
		for (String ligne : lignes) 
		{
			if (ligne.isBlank() || ligne.equals("END")) break Ex;
			String[] mot = ligne.split("\\s+");
			String instruction = mot[0];
	        String mod = (mot.length > 1) ? mot[1] : "";
	        String res = this.opcode(instruction, this.decode(mod));
	        int opcode = Integer.parseInt(res.substring(0, res.length()-2), 16);
	        int nbOctect = this.nbOct(instruction, this.decode(mod));
	        if(nbOctect == 1) this.ecrireMemoire(adr, opcode);
	        else if(nbOctect == 2)
	        {
	        	for(int i=0; i<2; i++)
	        	{
	        		if(i==0) this.ecrireMemoire(adr, opcode);
	        		else 
        			{
        				if("IMMEDIAT".equals(this.decode(mod))) 
        				this.ecrireMemoire(adr, Integer.parseInt(mod.substring(2), 16)); 
        			}
	        		adr++;
	        	}
	        }
	        else if(nbOctect == 3)
	        {
	        	for(int i=0; i<3; i++)
	        	{
	        		if(i==0) this.ecrireMemoire(adr, opcode);
	        		else if(i==1)
        			{
        				//if("IMMEDIAT".equals(this.decode(mod))) 
        				this.ecrireMemoire(adr, Integer.parseInt(mod.substring(2, 3), 16)); 
        			}
	        		else
	        		{
	        			//if("IMMEDIAT".equals(this.decode(mod))) 
	        				this.ecrireMemoire(adr, Integer.parseInt(mod.substring(4, 5), 16)); 
	        		}
	        		adr++;
	        	}
	        }
	        else
	        {
	        	
	        }
	        //adr = adr + nbOctect;
		}
		
	}
	public int nbOct(String ins, String op)
	{
		String cle = ins + op;
		String res = opcode.get(cle);
		if(res == null) throw new IllegalArgumentException();
		return Integer.parseInt(res.substring(res.length()-1));
		
	}
	public String opcode(String ins, String mode)
	{
		String c = ins + mode;
		return opcode.get(c);
	}
	public void abx(String m)
	{
		String B = Controller.getAcumB();
		String X = Controller.getRegX();
		String res = OpFlags.somme(B, X);
		res = String.format("%4s", res).replace(' ', '0');
		Controller.setRegX(res);
	}
	public void lda(String m)
	{
		String is = this.decode(m);
		if("IMMEDIAT".equals(is))
		{
			String val = m.substring(2);
			Controller.setAcumA(val);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("ETENDU".equals(m))
		{
			int val = Memoire.lireInt(Integer.parseInt(m.substring(1), 16));
			String valHex = String.format("%02X", val);
			Controller.setAcumA(valHex);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
	}
	public void ldb(String m)
	{
		String is = this.decode(m);
		if("IMMEDIAT".equals(is))
		{
			String val = m.substring(2);
			Controller.setAcumB(val);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("ETENDU".equals(m))
		{
			int val = Memoire.lireInt(Integer.parseInt(m.substring(1), 16));
			String valHex = String.format("%02X", val);
			Controller.setAcumB(valHex);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		
	}
	static void Inii(Logique logic)
	{
		//instructions = new HashMap<>();
		instructions.put("LDA", logic::lda);
		instructions.put("LDB", logic::ldb);
		instructions.put("ABX", logic::abx);
		opcode.put("LDAIMMEDIAT", "86_2");
		opcode.put("LDAETENDU", "B6_3");
		opcode.put("LDBIMMEDIAT", "C6_2");
		opcode.put("ABXINHERENT", "3A_1");
	}
	public void atualflags(String f)
	{
		Controller.setFlags(f);
	}
	//MOTOController Contr = new MOTOController();
			//Contr.setAcumB("A0");
			//Contr.setRegX("F");
}
