package chip;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Chip{
    private char[] memory;
    private char[] V;
    private char I;

    private char stack[];
    private int stackPointer;
    private char pc;

    private int delayTimer;
    private int soundTimer;

    private byte[] keys;
    public byte[] display;

    private boolean needRedraw;

    public void init(){
        memory = new char[4096]; //kB=1024B *4
        V = new char[16]; //16 registries
        
        stack = new char[16];
        stackPointer = 0;
        pc = 0x200; //program counter starts at 0x200 or box 512

        delayTimer = 0;
        soundTimer = 0;

        keys = new byte[16];

        display = new byte[64*32];

        needRedraw = false;
        loadFontset();
        
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

        case 0x1000:
            break;

        case 0x2000: //call subroutine at nnn
            stack[stackPointer] = pc;
            stackPointer++;
            pc = (char)(opcode & 0x0FFF);
            pc += 2;
            break;

        case 0x3000: //skip next instruction if Vx == kk
            break;
        
        case 0x6000: //set Vx = kk
            byte x = (byte)((opcode & 0x0F00) >> 8);
            System.out.println("x: " + x);
            V[x] = (char)(opcode & 0x00FF);
            pc += 2;
            break;

        case 0x7000: //set Vx = Vx + kk
            int _x = (opcode & 0x0F00) >> 8;
            int nn = opcode & 0x00FF;
            V[_x] = (char)((V[_x] + nn) & 0xFF);
            pc += 2;
            break;
            
        case 0x8000: //contains more data in the last nibble
            switch(opcode & 0x000F){
            case 0x0000: //8XY0: Sets VX to the value of VY.
                default: 
                    System.err.println("Unsupported opcode!");
                    System.exit(0);
                    break;
            }
            break;

        case 0xA000: //set I = nnn
            I = (char)(opcode & 0x0FFF);
            pc += 2;
            break;

        case 0xD000: //draw a sprite
            pc+=2;
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

    public boolean needsRedraw(){
        return needRedraw;
    }

    public void removeRedrawFlag(){
        needRedraw = false;
    }
    public void loadProgram(String file){
        DataInputStream input = null;
        try{
            input = new DataInputStream(new FileInputStream(file));

            int offset = 0;
            while(input.available() > 0){

                memory[0x200 + offset] = (char)(input.readByte() & 0xFF);

                offset++;
            }

        } catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        } finally{
            if(input != null){
                try{
                    input.close();
                } catch(IOException ex){

                }
            }
        }
        public void loadFontset(){
            for(int i=0; i<ChipData.fontset.length; i++){
                memory[0x50 + i] = (char)(ChipData.fontset[i] & 0xF00);
            }
        }
    }
}