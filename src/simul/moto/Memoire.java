package simul.moto;

public class Memoire 
{
	//public static final byte[] RAM = new byte[0xFC00];
    //public static final byte[] ROM = new byte[0x10000 - 0xFC00];
    
    public static final int RAM_SIZE = 0xFC00;
    public static final int ROM_START = 0xFC00;
    public static final int MEM_SIZE = 0x10000;

    // Memória total (RAM + ROM)
    private static final byte[] mem = new byte[MEM_SIZE];

    // Escrever inteiro de 0–255
    public static void ecrire(int address, int value) {
        mem[address] = (byte)(value & 0xFF);
    }

    // Retornar valor sem sinal (0–255)
    public static int lire(int address) {
        return mem[address] & 0xFF;
    }

    // Inicializar RAM e ROM com zeros
    public static void reset() {
        for (int i = 0; i < MEM_SIZE; i++) {
            mem[i] = 0 ;
        }
    }
}
