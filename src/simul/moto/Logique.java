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
			return "Pas d'instruction.";
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
		            return "Mode invalide à la ligne " + numLigne + ".";
		        }

		        String key = inst + mode;
		        if (!instructions.containsKey(inst) || !opcode.containsKey(key)) {
		            return "Instruction invalide à la ligne " + numLigne + " : " + inst + ".";
		        }

		        numLigne++;
		    }

		    return "END manquant à la fin du programme.";
		}
	}
	
	public String decode(String m)
	{
		if(m == null || m.isEmpty())
			return "INHERENT";
		if(m.length() == 4 && m.startsWith("#$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F'))))
			return "IMMEDIAT";
		else if(m.length() == 6 && m.startsWith("#$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F'))))
			return "IMMEDIAT3+";
		else if((m.length() == 5 && m.startsWith("$") && (((m.charAt(1) >= '0' && m.charAt(1) <= '9') || (m.charAt(1) >= 'A' && m.charAt(1) <= 'F')) && ((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F'))))
				|| (m.length() == 6 && m.startsWith(">$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F')))))
			return "ETENDU";
		else if((m.length() == 7 && m.startsWith("[$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F'))) && m.endsWith("]"))
				|| (m.length() == 8 && m.startsWith("[>$") && (((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F')) && ((m.charAt(5) >= '0' && m.charAt(5) <= '9') || (m.charAt(5) >= 'A' && m.charAt(5) <= 'F')) && ((m.charAt(6) >= '0' && m.charAt(6) <= '9') || (m.charAt(6) >= 'A' && m.charAt(6) <= 'F'))) && m.endsWith("]")))
			return "ETENDUINDIRECT";
		else if(m.length() == 4 && m.startsWith("<$") && (((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F'))))
			return "DIRECT";
		else if((m.length() == 2 && m.startsWith(",") && (m.charAt(1) == 'X' || m.charAt(1) == 'Y' || m.charAt(1) == 'U' || m.charAt(1) == 'S'))
				|| (m.length() == 3 && m.startsWith(",") && m.charAt(1) == 'P' && m.charAt(2) == 'C'))
			return "INDEXEDEPNULL";
		else if(m.length() == 3 && m.startsWith(",") && m.charAt(1) == '-' && (m.charAt(2) == 'X' || m.charAt(2) == 'Y' || m.charAt(2) == 'U' || m.charAt(2) == 'S'))
			return "INDEXEAUTODEC1";
		else if(m.length() == 4 && m.startsWith(",") && m.charAt(1) == '-' && m.charAt(2) == '-' && (m.charAt(3) == 'X' || m.charAt(3) == 'Y' || m.charAt(3) == 'U' || m.charAt(3) == 'S'))
			return "INDEXEAUTODEC2";
		else if(m.length() == 3 && m.startsWith(",") && (m.charAt(1) == 'X' || m.charAt(1) == 'Y' || m.charAt(1) == 'U' || m.charAt(1) == 'S') && m.charAt(2) == '+')
			return "INDEXEAUTOINC1";
		else if(m.length() == 4 && m.startsWith(",") && (m.charAt(1) == 'X' || m.charAt(1) == 'Y' || m.charAt(1) == 'U' || m.charAt(1) == 'S') && m.charAt(2) == '+' && m.charAt(3) == '+')
			return "INDEXEAUTOINC2";
		else if(m.length() == 3 && m.startsWith("A,") && (m.charAt(2) == 'X' || m.charAt(2) == 'Y' || m.charAt(2) == 'U' || m.charAt(2) == 'S'))
			return "INDEXEDEPA";
		else if(m.length() == 3 && m.startsWith("B,") && (m.charAt(2) == 'X' || m.charAt(2) == 'Y' || m.charAt(2) == 'U' || m.charAt(2) == 'S'))
			return "INDEXEDEPB";
		else if(m.length() == 3 && m.startsWith("D,") && (m.charAt(2) == 'X' || m.charAt(2) == 'Y' || m.charAt(2) == 'U' || m.charAt(2) == 'S'))
			return "INDEXEDEPD";
		else if(m.length() == 5 && m.startsWith("$") && (((m.charAt(1) >= '0' && m.charAt(1) <= '9') || (m.charAt(1) >= 'A' && m.charAt(1) <= 'F')) && ((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F'))) && m.charAt(3) == ',' && (m.charAt(4) == 'X' || m.charAt(4) == 'Y' || m.charAt(4) == 'U' || m.charAt(4) == 'S'))
			return "INDEXEDEPCONS1OCT";
		else if(m.length() == 7 && m.startsWith("$") && (((m.charAt(1) >= '0' && m.charAt(1) <= '9') || (m.charAt(1) >= 'A' && m.charAt(1) <= 'F')) && ((m.charAt(2) >= '0' && m.charAt(2) <= '9') || (m.charAt(2) >= 'A' && m.charAt(2) <= 'F')) && ((m.charAt(3) >= '0' && m.charAt(3) <= '9') || (m.charAt(3) >= 'A' && m.charAt(3) <= 'F')) && ((m.charAt(4) >= '0' && m.charAt(4) <= '9') || (m.charAt(4) >= 'A' && m.charAt(4) <= 'F'))) && m.charAt(5) == ',' && (m.charAt(6) == 'X' || m.charAt(6) == 'Y' || m.charAt(6) == 'U' || m.charAt(6) == 'S'))
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
	        //int opcode = Integer.parseInt(res.substring(0, res.length()-2));
	        int nbOctect = this.nbOct(instruction, this.decode(mod));
	        if(nbOctect == 1) this.ecrireMemoire(adr, Integer.parseInt(res.substring(0, res.length()-2), 16));
	        else if(nbOctect == 2)
	        {
	        	for(int i=0; i<2; i++)
	        	{
	        		if(i==0) 
        			{
	        			if("IMMEDIAT".equals(this.decode(mod)) || "DIRECT".equals(this.decode(mod))) this.ecrireMemoire(adr, Integer.parseInt(res.substring(0, res.length()-2), 16));
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
        				if("IMMEDIAT3+".equals(this.decode(mod)) || "ETENDU".equals(this.decode(mod))) this.ecrireMemoire(adr, Integer.parseInt(res.substring(0, res.length()-2), 16));
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
		this.atualflags("0 0 0 0 0 0 0 0");
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
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
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
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumA(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
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
		else
		{
			return;
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
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
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
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				String valHex = String.format("%02X", Memoire.lire(adressEFECTIVE));
				Controller.setAcumB(valHex);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(3)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
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
		else
		{
			return;
		}
		
	}
	public void sta(String m)
	{
		String is = this.decode(m);
		if("ETENDU".equals(is))
		{
			int adressEFFECTIVE;
			int val = Integer.parseInt(Controller.getAcumA(), 16);
			if(m.startsWith(">$")) { adressEFFECTIVE = Integer.parseInt(m.substring(2), 16); }
			else { adressEFFECTIVE = Integer.parseInt(m.substring(1), 16); }
			this.ecrireMemoire(adressEFFECTIVE, val);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("ETENDUINDIRECT".equals(is))
		{
			int adress;
			int val = Integer.parseInt(Controller.getAcumA(), 16);
			if(m.startsWith("[>$")) { adress = Integer.parseInt(m.substring(3), 16); }
			else { adress = Integer.parseInt(m.substring(2), 16); }
			int v1 = Memoire.lire(adress); String V1 = String.format("%02X", v1);
			int v2 = Memoire.lire(adress + 1); String V2 = String.format("%02X", v2);
			int adressEFFECTIVE = Integer.parseInt((V1 + V2), 16);
			this.ecrireMemoire(adressEFFECTIVE, val);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("DIRECT".equals(is))
		{
			int val = Integer.parseInt(Controller.getAcumA(), 16);
			String V1 = Controller.getRegDP();
			String V2 = m.substring(2);
			int adressEFFECTIVE = Integer.parseInt((V1 + V2), 16);
			this.ecrireMemoire(adressEFFECTIVE, val);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("INDEXEDEPNULL".equals(is))
		{
			if("X".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegX();
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegY();
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegPU();
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegPS();
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val);
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
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTODEC2".equals(is)) 
		{
			if("X".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegX(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTOINC1".equals(is)) 
		{
			if("X".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegX(), 16);
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 1;
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegY(), 16);
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 1;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPU(), 16);
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 1;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPS(), 16);
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 1;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTOINC2".equals(is)) 
		{
			if("X".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegX(), 16);
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 2;
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegY(), 16);
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 2;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPU(), 16);
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 2;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPS(), 16);
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 2;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
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
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumA());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumA());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumA());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
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
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumB());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumB());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumB());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
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
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumD());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumD());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumD());
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPCONS1OCT".equals(is)) 
		{
			
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), m.substring(1, 3)); 
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), m.substring(1, 3));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), m.substring(1, 3));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), m.substring(1, 3));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPCONS2OCT".equals(is)) 
		{
			
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), m.substring(1, 5)); 
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), m.substring(1, 5));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), m.substring(1, 5));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), m.substring(1, 5));
				int val = Integer.parseInt(Controller.getAcumA(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else
		{
			return;
		}
	}
	public void stb(String m)
	{
		String is = this.decode(m);
		if("ETENDU".equals(is))
		{
			int adressEFFECTIVE;
			int val = Integer.parseInt(Controller.getAcumB(), 16);
			if(m.startsWith(">$")) { adressEFFECTIVE = Integer.parseInt(m.substring(2), 16); }
			else { adressEFFECTIVE = Integer.parseInt(m.substring(1), 16); }
			this.ecrireMemoire(adressEFFECTIVE, val);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("ETENDUINDIRECT".equals(is))
		{
			int adress;
			int val = Integer.parseInt(Controller.getAcumB(), 16);
			if(m.startsWith("[>$")) { adress = Integer.parseInt(m.substring(3), 16); }
			else { adress = Integer.parseInt(m.substring(2), 16); }
			int v1 = Memoire.lire(adress); String V1 = String.format("%02X", v1);
			int v2 = Memoire.lire(adress + 1); String V2 = String.format("%02X", v2);
			int adressEFFECTIVE = Integer.parseInt((V1 + V2), 16);
			this.ecrireMemoire(adressEFFECTIVE, val);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("STADIRECT".equals(is))
		{
			int val = Integer.parseInt(Controller.getAcumB(), 16);
			String V1 = Controller.getRegDP();
			String V2 = m.substring(2);
			int adressEFFECTIVE = Integer.parseInt((V1 + V2), 16);
			this.ecrireMemoire(adressEFFECTIVE, val);
			this.atualflags("0 0 0 0 0 0 0 0");
		}
		else if("INDEXEDEPNULL".equals(is))
		{
			if("X".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegX();
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegY();
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegPU();
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(1)))
			{
				String adressEFECTIVE = Controller.getRegPS();
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val);
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
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 1;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTODEC2".equals(is)) 
		{
			if("X".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegX(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegY(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPU(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adress = Integer.parseInt(Controller.getRegPS(), 16);
				int adressEFECTIVE = adress - 2;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTOINC1".equals(is)) 
		{
			if("X".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegX(), 16);
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 1;
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegY(), 16);
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 1;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPU(), 16);
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 1;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPS(), 16);
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 1;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEAUTOINC2".equals(is)) 
		{
			if("X".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegX(), 16);
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 2;
				Controller.setRegX(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("Y".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegY(), 16);
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 2;
				Controller.setRegY(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("U".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPU(), 16);
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 2;
				Controller.setRegPU(String.format("%04X", adressEFECTIVE));
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else if("S".equals(m.substring(2)))
			{
				int adressEFECTIVE = Integer.parseInt(Controller.getRegPS(), 16);
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(adressEFECTIVE, val);
				adressEFECTIVE = adressEFECTIVE + 2;
				Controller.setRegPS(String.format("%04X", adressEFECTIVE));
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
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumA());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumA());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumA());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
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
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumB());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumB());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumB());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
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
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), Controller.getAcumD());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), Controller.getAcumD());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), Controller.getAcumD());
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPCONS1OCT".equals(is)) 
		{
			
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), m.substring(1, 3)); 
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), m.substring(1, 3));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), m.substring(1, 3));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), m.substring(1, 3));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else if("INDEXEDEPCONS2OCT".equals(is)) 
		{
			
			if("X".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegX(), m.substring(1, 5)); 
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("Y".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegY(), m.substring(1, 5));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("U".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPU(), m.substring(1, 5));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			if("S".equals(m.substring(2)))
			{
				String adressEFECTIVE = OpFlags.somme(Controller.getRegPS(), m.substring(1, 5));
				int val = Integer.parseInt(Controller.getAcumB(), 16);
				Memoire.ecrire(Integer.parseInt(adressEFECTIVE, 16), val); 
				this.atualflags("0 0 0 0 0 0 0 0");
			}
			else
			{
				if("END".equals(Controller.getValPc())) { return; }
			}
		}
		else
		{
			return;
		}
	}
	public void mul(String m)
	{
		String val = OpFlags.multiplication(Controller.getAcumA(), Controller.getAcumB());
		int valint = Integer.parseInt(val, 16);
		int Aint = Integer.parseInt(Controller.getAcumA(), 16);
		int Bint = Integer.parseInt(Controller.getAcumB(), 16);
		Controller.setAcumD(val);
		if(valint == 0 && "cmul".equals(OpFlags.flags(Aint, Bint, valint))) 
			this.atualflags("0 0 0 0 0 1 0 1");
		else if(valint == 0)
			this.atualflags("0 0 0 0 0 1 0 0");
		else if("cmul".equals(OpFlags.flags(Aint, Bint, valint)))
			this.atualflags("0 0 0 0 0 0 0 1");
		else
			this.atualflags("0 0 0 0 0 0 0 0");
		
	}
	public void cmpa(String m)
	{
		String is = this.decode(m);
		if("IMMEDIAT".equals(is))
		{
			String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumA()), Integer.parseInt(m.substring(2)), Integer.parseInt(OpFlags.substraction(Controller.getAcumA(), m.substring(2))));
			if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
			else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
			else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
			else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
			else this.atualflags("0 0 0 0 1 0 1 1");//(-)
		}
		if("ETENDU".equals(is))
		{
			if(m.startsWith(">$"))
			{
				String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumA()), Memoire.lire(Integer.parseInt(m.substring(2), 16)), Integer.parseInt(OpFlags.substraction(Controller.getAcumA(), String.format("%02X", Memoire.lire(Integer.parseInt(m.substring(1), 16))))));
				if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
				else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
				else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
				else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
				else this.atualflags("0 0 0 0 1 0 1 1");//(-)
			}
			else
			{
				String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumA()), Memoire.lire(Integer.parseInt(m.substring(1), 16)), Integer.parseInt(OpFlags.substraction(Controller.getAcumA(), String.format("%02X", Memoire.lire(Integer.parseInt(m.substring(1), 16)))))); 
				if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
				else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
				else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
				else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
				else this.atualflags("0 0 0 0 1 0 1 1");//(-)
			}
		}
		else if("ETENDUINDIRECT".equals(is))
		{
			if(m.startsWith("[>$"))
			{
				int adress = Integer.parseInt(m.substring(3), 16);
				String V1 = String.format("%02X", Memoire.lire(adress));
				String V2 = String.format("%02X", Memoire.lire(adress + 1));
				int adressEFF = Integer.parseInt((V1 + V2), 16);
				String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumA()), Memoire.lire(adressEFF), Integer.parseInt(OpFlags.substraction(Controller.getAcumA(), String.format("%02X", Memoire.lire(adressEFF)))));
				if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
				else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
				else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
				else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
				else this.atualflags("0 0 0 0 1 0 1 1");//(-)
			}
			else
			{
				int adress = Integer.parseInt(m.substring(2), 16);
				String V1 = String.format("%02X", Memoire.lire(adress));
				String V2 = String.format("%02X", Memoire.lire(adress + 1));
				int adressEFF = Integer.parseInt((V1 + V2), 16);
				String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumA()), Memoire.lire(adressEFF), Integer.parseInt(OpFlags.substraction(Controller.getAcumA(), String.format("%02X", Memoire.lire(adressEFF)))));
				if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
				else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
				else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
				else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
				else this.atualflags("0 0 0 0 1 0 1 1");//(-)
			}
		}
		
	}
	public void cmpb(String m)
	{
		String is = this.decode(m);
		if("IMMEDIAT".equals(is))
		{
			String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumB()), Integer.parseInt(m.substring(2)), Integer.parseInt(OpFlags.substraction(Controller.getAcumB(), m.substring(2))));
			if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
			else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
			else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
			else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
			else this.atualflags("0 0 0 0 1 0 1 1");//(-)
		}
		if("ETENDU".equals(is))
		{
			if(m.startsWith(">$"))
			{
				String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumB()), Memoire.lire(Integer.parseInt(m.substring(2), 16)), Integer.parseInt(OpFlags.substraction(Controller.getAcumB(), String.format("%02X", Memoire.lire(Integer.parseInt(m.substring(1), 16))))));
				if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
				else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
				else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
				else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
				else this.atualflags("0 0 0 0 1 0 1 1");//(-)
			}
			else
			{
				String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumB()), Memoire.lire(Integer.parseInt(m.substring(1), 16)), Integer.parseInt(OpFlags.substraction(Controller.getAcumB(), String.format("%02X", Memoire.lire(Integer.parseInt(m.substring(1), 16)))))); 
				if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
				else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
				else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
				else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
				else this.atualflags("0 0 0 0 1 0 1 1");//(-)
			}
		}
		else if("ETENDUINDIRECT".equals(is))
		{
			if(m.startsWith("[>$"))
			{
				int adress = Integer.parseInt(m.substring(3), 16);
				String V1 = String.format("%02X", Memoire.lire(adress));
				String V2 = String.format("%02X", Memoire.lire(adress + 1));
				int adressEFF = Integer.parseInt((V1 + V2), 16);
				String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumB()), Memoire.lire(adressEFF), Integer.parseInt(OpFlags.substraction(Controller.getAcumB(), String.format("%02X", Memoire.lire(adressEFF)))));
				if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
				else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
				else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
				else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
				else this.atualflags("0 0 0 0 1 0 1 1");//(-)
			}
			else
			{
				int adress = Integer.parseInt(m.substring(2), 16);
				String V1 = String.format("%02X", Memoire.lire(adress));
				String V2 = String.format("%02X", Memoire.lire(adress + 1));
				int adressEFF = Integer.parseInt((V1 + V2), 16);
				String flags = OpFlags.flags(Integer.parseInt(Controller.getAcumB()), Memoire.lire(adressEFF), Integer.parseInt(OpFlags.substraction(Controller.getAcumB(), String.format("%02X", Memoire.lire(adressEFF)))));
				if("PASDE".equals(flags)) this.atualflags("0 0 0 0 0 0 0 0"); //A > sub
				else if("z".equals(flags)) this.atualflags("0 0 0 0 0 1 0 0");//A = sub
				else if("csubn".equals(flags)) this.atualflags("0 0 0 0 1 0 0 1");//A < sub
				else if("vsub".equals(flags)) this.atualflags("0 0 0 0 0 0 1 0"); //(+)
				else this.atualflags("0 0 0 0 1 0 1 1");//(-)
			}
		}
	}
	static void Inii(Logique logic)
	{
		//instructions = new HashMap<>();
		instructions.put("LDA", logic::lda);
		instructions.put("STA", logic::sta);
		instructions.put("LDB", logic::ldb);
		instructions.put("STB", logic::stb);
		instructions.put("ABX", logic::abx);
		instructions.put("MUL", logic::mul);
		instructions.put("CMPA", logic::cmpa);
		instructions.put("CMPB", logic::cmpb);
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
		opcode.put("LDBETENDU", "B6_3");
		opcode.put("LDBETENDUINDIRECT", "B69F_4");
		opcode.put("LDBDIRECT", "96_2");
		opcode.put("LDBINDEXEDEPNULL", "A6_1R04_2");
		opcode.put("LDBINDEXEAUTODEC1", "A6_1R02_2");
		opcode.put("LDBINDEXEAUTODEC2", "A6_1R03_2");
		opcode.put("LDBINDEXEAUTOINC1", "A6_1R00_2");
		opcode.put("LDBINDEXEAUTOINC2", "A6_1R01_2");
		opcode.put("LDBINDEXEDEPA", "A6_1R06_2");
		opcode.put("LDBINDEXEDEPB", "A6_1R05_2");
		opcode.put("LDBINDEXEDEPD", "A6_1R0B_2");
		opcode.put("LDBINDEXEDEPCONS1OCT", "A6_1R08_3");
		opcode.put("LDBINDEXEDEPCONS2OCT", "A6_1R09_4");
		opcode.put("STAETENDU", "B7_3");
		opcode.put("STAETENDUINDIRECT", "B79F_4");
		opcode.put("STADIRECT", "97_2");
		opcode.put("STAINDEXEDEPNULL", "A7_1R04_2");
		opcode.put("STAINDEXEAUTODEC1", "A7_1R02_2");
		opcode.put("STAINDEXEAUTODEC2", "A7_1R03_2");
		opcode.put("STAINDEXEAUTOINC1", "A7_1R00_2");
		opcode.put("STAINDEXEAUTOINC2", "A7_1R01_2");
		opcode.put("STAINDEXEDEPA", "A7_1R06_2");
		opcode.put("STAINDEXEDEPB", "A7_1R05_2");
		opcode.put("STAINDEXEDEPD", "A7_1R0B_2");
		opcode.put("STAINDEXEDEPCONS1OCT", "A7_1R08_3");
		opcode.put("STAINDEXEDEPCONS2OCT", "A7_1R09_4");
		opcode.put("STBETENDU", "F7_3");
		opcode.put("STBETENDUINDIRECT", "F79F_4");
		opcode.put("STBDIRECT", "D7_2");
		opcode.put("STBINDEXEDEPNULL", "E7_1R04_2");
		opcode.put("STBINDEXEAUTODEC1", "E7_1R02_2");
		opcode.put("STBINDEXEAUTODEC2", "E7_1R03_2");
		opcode.put("STBINDEXEAUTOINC1", "E7_1R00_2");
		opcode.put("STBINDEXEAUTOINC2", "E7_1R01_2");
		opcode.put("STBINDEXEDEPA", "E7_1R06_2");
		opcode.put("STBINDEXEDEPB", "E7_1R05_2");
		opcode.put("STBINDEXEDEPD", "E7_1R0B_2");
		opcode.put("STBINDEXEDEPCONS1OCT", "E7_1R08_3");
		opcode.put("STBINDEXEDEPCONS2OCT", "E7_1R09_4");
		opcode.put("CMPAIMMEDIAT", "81_2");
		opcode.put("CMPAETENDU", "B1_3");
		opcode.put("CMPAETENDUINDIRECT", "B19F_4");
		opcode.put("CMPADIRECT", "91_2");
		opcode.put("CMPAINDEXEDEPNULL", "A1_1R04_2");
		opcode.put("CMPAINDEXEAUTODEC1", "A1_1R02_2");
		opcode.put("CMPAINDEXEAUTODEC2", "A1_1R03_2");
		opcode.put("CMPAINDEXEAUTOINC1", "A1_1R00_2");
		opcode.put("CMPAINDEXEAUTOINC2", "A1_1R01_2");
		opcode.put("CMPAINDEXEDEPA", "A1_1R06_2");
		opcode.put("CMPAINDEXEDEPB", "A1_1R05_2");
		opcode.put("CMPAINDEXEDEPD", "A1_1R0B_2");
		opcode.put("CMPAINDEXEDEPCONS1OCT", "A1_1R08_3");
		opcode.put("CMPAINDEXEDEPCONS2OCT", "A1_1R09_4");
		opcode.put("CMPBIMMEDIAT", "C1_2");
		opcode.put("CMPBETENDU", "F1_3");
		opcode.put("CMPBETENDUINDIRECT", "F19F_4");
		opcode.put("CMPBDIRECT", "D1_2");
		opcode.put("CMPBINDEXEDEPNULL", "E1_1R04_2");
		opcode.put("CMPBINDEXEAUTODEC1", "E1_1R02_2");
		opcode.put("CMPBINDEXEAUTODEC2", "E1_1R03_2");
		opcode.put("CMPBINDEXEAUTOINC1", "E1_1R00_2");
		opcode.put("CMPBINDEXEAUTOINC2", "E1_1R01_2");
		opcode.put("CMPBINDEXEDEPA", "E1_1R06_2");
		opcode.put("CMPBINDEXEDEPB", "E1_1R05_2");
		opcode.put("CMPBINDEXEDEPD", "E1_1R0B_2");
		opcode.put("CMPBINDEXEDEPCONS1OCT", "E1_1R08_3");
		opcode.put("CMPBINDEXEDEPCONS2OCT", "E1_1R09_4");
		opcode.put("ABXINHERENT", "3A_1");
		opcode.put("MULINHERENT", "3D_1");
	}
	public void atualflags(String f)
	{
		Controller.setFlags(f);
	}
	//MOTOController Contr = new MOTOController();
			//Contr.setAcumB("A0");
			//Contr.setRegX("F");
}
