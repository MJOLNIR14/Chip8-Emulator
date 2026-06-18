package chip;

public class Chip{
    private char[] memory;
    private char[] v;
    private char I;

    private char stack[];
    private int stackPointer;
    private char pc;

    private int delayTimer;
    private int soundTimer;

    private byte[] keys;
    public byte[] display;

    public void init(){
        memory = new char[4096]; //kB=1024B *4
        v = new char[16]; //16 registries
        
        stack = new char[16];
        stackPointer = 0;
        pc = 0x200; //program counter starts at 0x200 or box 512

        delayTimer = 0;
        soundTimer = 0;

        keys = new byte[16];

        display = new byte[64*32];
        display[0] = 1;
        display[130] = 1;
    }
    public void run(){
        //opcode
        //fetch
        char opcode = (char)((memory[pc]<<8)|memory[pc+1]);
        System.out.println(Integer.toHexString(opcode) + ": ");

        //decode
        switch(opcode & 0xF000){
        case 0x8000: //contains more data in the last nibble
            switch(opcode & 0x000F){
            case 0x0000: //8XY0: Sets VX to the value of VY.
                default: 
                    System.err.println("Unsupported opcode!");
                    System.exit(0);
                    break;
            }
            break;

            default:
                System.err.println("Unsupported opcode!");
                System.exit(0);
        }
        //execute
    }
    public byte[] getDisplay(){
        return display;
    }
}