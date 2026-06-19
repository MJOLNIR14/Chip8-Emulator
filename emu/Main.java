package emu;

import chip.Chip;

public class Main extends Thread{
    private Chip chip8;
    private ChipFrame frame;

    public Main(){
        chip8 = new Chip();
        chip8.init();
        chip8.loadProgram("./prog2.c8");
        frame = new ChipFrame(chip8);
    }

    public void run(){
        //60hz, 60fps/updates per second
        while(true){
            chip8.run();
            if(chip8.needsRedraw()){
                frame.repaint();
                chip8.removeRedrawFlag();
            }
            try{
                Thread.sleep(16);
            }catch(InterruptedException e){
                //unthrown exception
            }
        }
    }
    public static void main(String[] args){
        Main main = new Main();
        main.start();
    }
}