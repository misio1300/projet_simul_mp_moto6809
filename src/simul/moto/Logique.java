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
		if(m.startsWith("#$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F'))) && m.length() == 4)
			return "IMMEDIAT";
		else if(m.startsWith("#$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F'))) && m.length() == 6)
			return "IMMEDIAT3+";
		else if((m.startsWith("$") && (((m.charAt(1) >= '0' && m.charAt(1) <= '9') || (m.charAt(1) >= 'A' && m.charAt(1) <= 'F')) && ((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F'))) && m.length() == 5)
				|| (m.startsWith(">$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F'))) && m.length() == 6))
			return "ETENDU";
		else if((m.startsWith("[$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F'))) && m.endsWith("]") && m.length() == 7)
				|| (m.startsWith("[>$") && (((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F')) && ((m.charAt(6) >= '0' && m.charAt(6) <= '9') || (m.charAt(6) >= 'A' && m.charAt(6) <= 'F'))) && m.endsWith("]") && m.length() == 8))
			return "ETENDUINDIRECT";
		else if(m.startsWith("<$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F'))) && m.length() == 4)
			return "DIRECT";
		else if(m == null || m.isEmpty())
			return "INHERENT";
		else if((m.startsWith(",") && (m.charAt(1) == 'X' || m.charAt(1) == 'Y' || m.charAt(1) == 'U' || m.charAt(1) == 'S') && m.length() == 2)
				|| (m.startsWith(",") && m.charAt(1) == 'P' && m.charAt(2) == 'C' && m.length() == 3))
			return "INDEXEDEPNULL";
		else if(m.startsWith(",") && m.charAt(1) == '-' && (m.charAt(2) == 'X' || m.charAt(2) == 'Y' || m.charAt(2) == 'U' || m.charAt(2) == 'S') && m.length() == 3)
			return "INDEXEAUTODEC1";
		else if(m.startsWith(",") && m.charAt(1) == '-' && m.charAt(2) == '-' && (m.charAt(3) == 'X' || m.charAt(3) == 'Y' || m.charAt(3) == 'U' || m.charAt(3) == 'S') && m.length() == 4)
			return "INDEXEAUTODEC2";
		else if(m.startsWith(",") && (m.charAt(1) == 'X' || m.charAt(1) == 'Y' || m.charAt(1) == 'U' || m.charAt(1) == 'S') && m.charAt(2) == '+' && m.length() == 3)
			return "INDEXEAUTOINC1";
		else if(m.startsWith(",") && (m.charAt(1) == 'X' || m.charAt(1) == 'Y' || m.charAt(1) == 'U' || m.charAt(1) == 'S') && m.charAt(2) == '+' && m.charAt(3) == '+' && m.length() == 4)
			return "INDEXEAUTOINC2";
		else if(m.startsWith("A,") && (m.charAt(2) == 'X' || m.charAt(2) == 'Y' || m.charAt(2) == 'U' || m.charAt(2) == 'S') && m.length() == 3)
			return "INDEXEDEPA";
		else if(m.startsWith("B,") && (m.charAt(2) == 'X' || m.charAt(2) == 'Y' || m.charAt(2) == 'U' || m.charAt(2) == 'S') && m.length() == 3)
			return "INDEXEDEPB";
		else if(m.startsWith("D,") && (m.charAt(2) == 'X' || m.charAt(2) == 'Y' || m.charAt(2) == 'U' || m.charAt(2) == 'S') && m.length() == 3)
			return "INDEXEDEPD";
		else if(m.startsWith("$") && (((m.charAt(1) >= '0' && m.charAt(1) <= '9') || (m.charAt(1) >= 'A' && m.charAt(1) <= 'F')) && ((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F'))) && m.charAt(3) == ',' && (m.charAt(4) == 'X' || m.charAt(4) == 'Y' || m.charAt(4) == 'U' || m.charAt(4) == 'S') && m.length() == 5)
			return "INDEXEDEPCONS1OCT";
		else if(m.startsWith("$") && (((m.charAt(1) >= '0' && m.charAt(1) <= '9') || (m.charAt(1) >= 'A' && m.charAt(1) <= 'F')) && ((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F'))) && m.charAt(5) == ',' && (m.charAt(6) == 'X' || m.charAt(6) == 'Y' || m.charAt(6) == 'U' || m.charAt(6) == 'S') && m.length() == 7)
			return "INDEXEDEPCONS2OCT";
		else
			return "FALSE";
	}
	
	public static void execute(String inst, String mod)
	{
		Consumer<String> ins = instructions.get(inst);
	    if (ins != null) ins.accept(mod);
	}
	public void ecrireMemoire(int address, int val) {
	    Memoire.ecrire(address, val);
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
	        		if(i==0) 
        			{
	        			if("IMMEDIAT".equals(this.decode(mod)) || "DIRECT".equals(this.decode(mod))) this.ecrireMemoire(adr, opcode);
	        			else
	        			{
	        				String OPCODE = res.substring(0, 2);
	        				this.ecrireMemoire(adr, Integer.parseInt(OPCODE, 16));
	        			}
        			}
	        		else 
        			{
        				if("IMMEDIAT".equals(this.decode(mod)) || "DIRECT".equals(this.decode(mod))) this.ecrireMemoire(adr, Integer.parseInt(mod.substring(2), 16)); 
        				else
        				{
        					if("INDEXEDEPNULL".equals(this.decode(mod)) && mod.charAt(1) == 'X')
        					{
        						String po = res.substring(3, 7).replace("R", "00");
        						String p1 = String.format("%X", Integer.parseInt(po.substring(0, 4), 2));
        						String POSTOCTECT = p1 + po.charAt(4);
        						this.ecrireMemoire(adr, Integer.parseInt(POSTOCTECT, 16)); 
        					}
        					else if("INDEXEDEPNULL".equals(this.decode(mod)) && mod.charAt(1) == 'Y')
        					{
        						String po = res.substring(3, 7).replace("R", "01");
        						String p1 = String.format("%X", Integer.parseInt(po.substring(0, 4), 2));
        						String POSTOCTECT = p1 + po.charAt(4);
        						this.ecrireMemoire(adr, Integer.parseInt(POSTOCTECT, 16)); 
        					}
        					else if("INDEXEDEPNULL".equals(this.decode(mod)) && mod.charAt(1) == 'U')
        					{
        						String po = res.substring(3, 7).replace("R", "10");
        						String p1 = String.format("%X", Integer.parseInt(po.substring(0, 4), 2));
        						String POSTOCTECT = p1 + po.charAt(4);
        						this.ecrireMemoire(adr, Integer.parseInt(POSTOCTECT, 16)); 
        					}
        					else if("INDEXEDEPNULL".equals(this.decode(mod)) && mod.charAt(1) == 'S')
        					{
        						String po = res.substring(3, 7).replace("R", "11");
        						String p1 = String.format("%X", Integer.parseInt(po.substring(0, 4), 2));
        						String POSTOCTECT = p1 + po.charAt(4);
        						this.ecrireMemoire(adr, Integer.parseInt(POSTOCTECT, 16)); 
        					}
        					
        				}
        			}
	        		adr++;
	        	}
	        }
	        else if(nbOctect == 3)
	        {
	        	for(int i=0; i<3; i++)
	        	{
	        		if(i==0)
        			{
        				if("IMMEDIAT3+".equals(this.decode(mod)) || "ETENDU".equals(this.decode(mod))) this.ecrireMemoire(adr, opcode);
        				else if("DIRECT".equals(this.decode(mod)))
        				{
        					String OPCODE = res.substring(0, res.length()-2);
        					this.ecrireMemoire(adr, Integer.parseInt(OPCODE.substring(0, 2), 16));
        				}
        				else
        				{
        					String OPCODE = res.substring(0, 2);
	        				this.ecrireMemoire(adr, Integer.parseInt(OPCODE, 16));
        				}
        			}
	        		else if(i==1)
        			{
	        			if("IMMEDIAT3+".equals(this.decode(mod)) || "ETENDU".equals(this.decode(mod)))
	        			{
	        				if("IMMEDIAT3+".equals(this.decode(mod)) || ("ETENDU".equals(this.decode(mod)) && mod.charAt(0) == '>'))
	        				{
	        					this.ecrireMemoire(adr, Integer.parseInt(mod.substring(2, 4), 16));
	        				}
	        				else
	        				{
	        					this.ecrireMemoire(adr, Integer.parseInt(mod.substring(1, 3), 16));
	        				}
	        			}
	        			else if("DIRECT".equals(this.decode(mod)))
        				{
        					String OPCODE = res.substring(0, res.length()-2);
        					this.ecrireMemoire(adr, Integer.parseInt(OPCODE.substring(2, 4), 16));
        				}
	        			else
	        			{
	        				if(mod.charAt(4) == 'X')
        					{
        						String po = res.substring(3, 7).replace("R", "00");
        						String p1 = String.format("%X", Integer.parseInt(po.substring(0, 4), 2));
        						String POSTOCTECT = p1 + po.charAt(4);
        						this.ecrireMemoire(adr, Integer.parseInt(POSTOCTECT, 16)); 
        					}
	        			}
        			}
	        		else
	        		{
	        			if("IMMEDIAT3+".equals(this.decode(mod)) || "ETENDU".equals(this.decode(mod)))
	        			{
	        				if("IMMEDIAT3+".equals(this.decode(mod)) || ("ETENDU".equals(this.decode(mod)) && mod.charAt(0) == '>'))
	        				{
	        					this.ecrireMemoire(adr, Integer.parseInt(mod.substring(4, 6), 16));
	        				}
	        				else
	        				{
	        					this.ecrireMemoire(adr, Integer.parseInt(mod.substring(3, 5), 16));
	        				}
	        			}
	        			else if("DIRECT".equals(this.decode(mod)))
        				{
        					this.ecrireMemoire(adr, Integer.parseInt(mod.substring(2), 16));
        				}
	        			else
	        			{
	        				this.ecrireMemoire(adr, Integer.parseInt(mod.substring(1, 3), 16));
	        			}
	        		}
	        		adr++;
	        	}
	        }
	        else
	        {
	        	
	        }
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
			if(m.length() == 5)
			{
				int val = Memoire.lire(Integer.parseInt(m.substring(1), 16));
				String valHex = String.format("%02X", val);
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if(m.length() == 6)
			{
				int val = Memoire.lire(Integer.parseInt(m.substring(2), 16));
				String valHex = String.format("%02X", val);
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
		}
		else if("ETENDUINDIRECT".equals(is))
		{
			if(m.length() == 7) 
			{
				int adress = Integer.parseInt(m.substring(2, 6), 16);
				int v1 = Memoire.lire(adress); String V1 = String.format("%02X", v1);
				int v2 = Memoire.lire(adress + 1); String V2 = String.format("%02X", v2);
				String adressEFECTIVE = V1 + V2;
				int val = Memoire.lire(Integer.parseInt(adressEFECTIVE, 16));
				String valHex = String.format("%02X", val);
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else 
			{
				int adress = Integer.parseInt(m.substring(3, 7), 16);
				int v1 = Memoire.lire(adress); String V1 = String.format("%02X", v1);
				int v2 = Memoire.lire(adress + 1); String V2 = String.format("%02X", v2);
				String adressEFECTIVE = V1 + V2;
				int val = Memoire.lire(Integer.parseInt(adressEFECTIVE, 16));
				String valHex = String.format("%02X", val);
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}	
		}
		else if("DIRECT".equals(is))
		{
			String V1 = Controller.getRegDP();
			String V2 = m.substring(2);
			String adressEFECTIVE = V1 + V2;
			int val = Memoire.lire(Integer.parseInt(adressEFECTIVE, 16));
			String valHex = String.format("%02X", val);
			Controller.setAcumA(valHex);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("INDEXEDEPNULL".equals(is))
		{
			if("X".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegX();
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegY();
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegPU();
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegPS();
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTODEC1".equals(is))
		{
			if("X".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegX(), 16);
				int adressEFECTIVE = adress - 1;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 1;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 1;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 1;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTODEC2".equals(is))
		{
			if("X".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegX(), 16);
				int adressEFECTIVE = adress - 2;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 2;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 2;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 2;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTOINC1".equals(is))
		{
			if("X".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegX(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				int adress = adressEFECTIVE + 1;
				Controller.setRegX(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegY(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				int adress = adressEFECTIVE + 1;
				Controller.setRegY(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPU(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				int adress = adressEFECTIVE + 1;
				Controller.setRegPU(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPS(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				int adress = adressEFECTIVE + 1;
				Controller.setRegPS(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTOINC2".equals(is))
		{
			if("X".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegX(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				int adress = adressEFECTIVE + 2;
				Controller.setRegX(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegY(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				int adress = adressEFECTIVE + 2;
				Controller.setRegY(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPU(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				int adress = adressEFECTIVE + 2;
				Controller.setRegPU(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPS(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				int adress = adressEFECTIVE + 2;
				Controller.setRegPS(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPA".equals(is))
		{
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), Controller.getAcumA());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumA());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumA());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumA());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPB".equals(is))
		{
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), Controller.getAcumB());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumB());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumB());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumB());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPD".equals(is))
		{
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), Controller.getAcumD());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumD());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumD());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumD());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPCONS1OCT".equals(is))
		{
			if("X".equals(m.substring(4)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), m.substring(1, 3));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), m.substring(1, 3));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), m.substring(1, 3));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), m.substring(1, 3));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPCONS2OCT".equals(is))
		{
			if("X".equals(m.substring(6)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), m.substring(1, 5));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(6)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), m.substring(1, 5));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(6)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), m.substring(1, 5));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(6)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), m.substring(1, 5));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
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
		else if("ETENDU".equals(is))
		{
			if(m.length() == 5)
			{
				int val = Memoire.lire(Integer.parseInt(m.substring(1), 16));
				String valHex = String.format("%02X", val);
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if(m.length() == 6)
			{
				int val = Memoire.lire(Integer.parseInt(m.substring(2), 16));
				String valHex = String.format("%02X", val);
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
		}
		else if("ETENDUINDIRECT".equals(is))
		{
			if(m.length() == 7) 
			{
				int adress = Integer.parseInt(m.substring(2, 6), 16);
				int v1 = Memoire.lire(adress); String V1 = String.format("%02X", v1);
				int v2 = Memoire.lire(adress + 1); String V2 = String.format("%02X", v2);
				String adressEFECTIVE = V1 + V2;
				int val = Memoire.lire(Integer.parseInt(adressEFECTIVE, 16));
				String valHex = String.format("%02X", val);
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else 
			{
				int adress = Integer.parseInt(m.substring(3, 7), 16);
				int v1 = Memoire.lire(adress); String V1 = String.format("%02X", v1);
				int v2 = Memoire.lire(adress + 1); String V2 = String.format("%02X", v2);
				String adressEFECTIVE = V1 + V2;
				int val = Memoire.lire(Integer.parseInt(adressEFECTIVE, 16));
				String valHex = String.format("%02X", val);
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
		}
		else if("DIRECT".equals(is))
		{
			String V1 = Controller.getRegDP();
			String V2 = m.substring(2);
			String adressEFECTIVE = V1 + V2;
			int val = Memoire.lire(Integer.parseInt(adressEFECTIVE, 16));
			String valHex = String.format("%02X", val);
			Controller.setAcumB(valHex);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("INDEXEDEPNULL".equals(is))
		{
			if("X".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegX();
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegY();
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegPU();
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegPS();
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTODEC1".equals(is))
		{
			if("X".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegX(), 16);
				int adressEFECTIVE = adress - 1;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 1;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 1;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 1;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTODEC2".equals(is))
		{
			if("X".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegX(), 16);
				int adressEFECTIVE = adress - 2;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 2;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 2;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 2;
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTOINC1".equals(is))
		{
			if("X".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegX(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				int adress = adressEFECTIVE + 1;
				Controller.setRegX(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegY(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				int adress = adressEFECTIVE + 1;
				Controller.setRegY(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPU(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				int adress = adressEFECTIVE + 1;
				Controller.setRegPU(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPS(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				int adress = adressEFECTIVE + 1;
				Controller.setRegPS(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTOINC2".equals(is))
		{
			if("X".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegX(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				int adress = adressEFECTIVE + 2;
				Controller.setRegX(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegY(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				int adress = adressEFECTIVE + 2;
				Controller.setRegY(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPU(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				int adress = adressEFECTIVE + 2;
				Controller.setRegPU(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(1)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPS(), 16);
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				int adress = adressEFECTIVE + 2;
				Controller.setRegPS(String.format("%04X", adress));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPA".equals(is))
		{
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), Controller.getAcumA());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumA());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumA());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumA());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPB".equals(is))
		{
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), Controller.getAcumB());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumB());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumB());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumB());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPD".equals(is))
		{
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), Controller.getAcumD());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumD());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumD());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumD());
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(adressEFECTIVE, 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPCONS1OCT".equals(is))
		{
			if("X".equals(m.substring(4)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), m.substring(1, 3));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), m.substring(1, 3));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), m.substring(1, 3));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), m.substring(1, 3));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPCONS2OCT".equals(is))
		{
			if("X".equals(m.substring(6)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), m.substring(1, 5));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(6)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), m.substring(1, 5));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(6)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), m.substring(1, 5));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(6)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), m.substring(1, 5));
				String valHex = String.format("%02X", Memoire.lire(Integer.parseInt(String.format("%04X",adressEFECTIVE), 16)));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
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
		opcode.put("LDAETENDUINDIRECT", "B69F_4");
		opcode.put("LDADIRECT", "96_2");
		opcode.put("LDAINDEXEDEPNULL", "A6_1R04_2");
		opcode.put("LDAINDEXEAUTODEC1", "A6_1R02_2");
		opcode.put("LDAINDEXEAUTODEC2", "A6_1R03_2");
		opcode.put("LDAINDEXEAUTOINC1", "A6_1R00_2");
		opcode.put("LDAINDEXEAUTOINC2", "A6_1R01_2");
		opcode.put("LDAINDEXEDEPA", "A6_1R06_2");
		opcode.put("LDAINDEXEDEPB", "A6_1R05_2");
		opcode.put("LDAINDEXEDEPD", "A6_1R0B_2");
		opcode.put("LDAINDEXEDEPCONS1OCT", "A6_1R08_3");
		opcode.put("LDAINDEXEDEPCONS2OCT", "A6_1R09_4");
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
