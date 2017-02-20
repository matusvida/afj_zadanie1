import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matus on 22.02.2016.
 */
public class Interpreter {

    private String sourceName;
    private String inputName;
    private String outputName;
    private byte[] arrayInput = new byte[100000];
    private byte[] arraySource;// = new byte[][(int) source.length()];
    private byte[] arrayOutput = new byte[100000];
    private List<String> listSource;

    public Interpreter(String sourceName, String inputName, String outputName){
        this.sourceName = sourceName;
        this.inputName = inputName;
        this.outputName = outputName;
        init(sourceName, inputName);
    }

    private void init(String sourceName, String inputName){
        Loader loader = new Loader();
        arrayInput = loader.load(inputName, 10000);
        arraySource = loader.load(sourceName);
        listSource = initListSource(arraySource);
    }

    public void doInterpreter(){
        int pointer = 0; //pointer ukazujuci na index pola do ktoreho ukladam
        int inputPointer = 0;
        int index = 0; //index, ktorym sa iteruje pole
        int beginLoopIndex = 0;
        int endLoopIndex = 0;
        boolean syntaxError = false;

        while (index < listSource.size() && !syntaxError) {
            switch (listSource.get(index)) {
                case Instructions.READ: {
                    read(arrayInput, arrayOutput, pointer, inputPointer);
                    inputPointer++;
                    index++;
                }break;

                case Instructions.WRITE: {
                    write(arrayOutput, pointer);
                    index++;
                }break;

                case Instructions.MOVE_LEFT: {
                    pointer = decPointer(pointer);
                    index++;
                }break;

                case Instructions.MOVE_RIGHT: {
                    pointer = incPointer(pointer);
                    index++;
                }break;

                case Instructions.NULLABLE: {
                    setToNull(arrayOutput, pointer);
                    index++;
                }break;

                case Instructions.INCREMENT: {
                    increment(arrayOutput, pointer);
                    index++;
                }break;

                case Instructions.DECREMENT: {
                    decrement(arrayOutput, pointer);
                    index++;
                }break;

                case Instructions.DEC_PLUS:{
                    decPlus(arrayOutput, index, pointer);
                    index++;
                }break;

                case Instructions.DEC_MINUS:{
                    decMinus(arrayOutput, index, pointer);
                    index++;
                }break;

                case Instructions.TO_START:{
                    pointer = toStart();
                    index++;
                }break;

                case Instructions.START_CYCLE: {
                    beginLoopIndex = index;
                    index++;
                }break;

                case Instructions.END_CYCLE: {
                    endLoopIndex = index;
                    index = stopLoop(arrayOutput, beginLoopIndex, endLoopIndex, pointer);
                }break;

                case Instructions.NOP:{
                    index++;
                }break;

                default: {
                    System.out.println("Nepodporovany znak " + listSource.get(index) + " na " + (index + 1) + ". mieste");
                    syntaxError = true;
                    index++;
                }break;
            }
        }

    }

    private static void read(byte[] arrayInput, byte[] arrayOutput, int pointer, int inputPointer){
        arrayOutput[pointer] = (byte)(arrayInput[inputPointer]);
    }

    private static void write(byte[] arrayOutput, int pointer){
        System.out.print((char)(arrayOutput[pointer]&0xff));
    }

    private static void increment(byte[] arrayOutput, int pointer){
        if(arrayOutput[pointer] == 255){
            arrayOutput[pointer] = (byte)0;
        }
        else
        arrayOutput[pointer]++;
    }

    private static void decrement(byte[] arrayOutput, int pointer){
        if( arrayOutput[pointer] == 0){
            arrayOutput[pointer] = (byte)255;
        }
        else
            arrayOutput[pointer]--;

    }

    private static void setToNull(byte[] arrayOutput, int pointer){
        arrayOutput[pointer] = 0x00;
    }

    private static int incPointer(int pointer){
        if(pointer == 99999){
            pointer = 0;
        }
        else{
            pointer++;
        }
        return pointer;
    }

    private static int decPointer(int pointer){
        if(pointer == 0){
            pointer = 99999;
        }
        else {
            pointer--;
        }
        return pointer;
    }

    private void decPlus(byte[] arrayOutput, int index, int pointer){
        int value = Integer.parseInt(listSource.get(index+1), 2);
        arrayOutput[pointer] = (byte) (arrayOutput[pointer] + value);
    }

    private void decMinus(byte[] arrayOutput, int index, int pointer){
        int value = Integer.parseInt(listSource.get(index+1), 2);
        arrayOutput[pointer] = (byte) (arrayOutput[pointer] - value);
    }

    private int toStart(){
        return 0;
    }

    private static void inverse(byte[] arrayOutput, int pointer){
        int temp = (int)arrayOutput[pointer];
        temp = ~temp;
        if (temp<0){
            temp = temp & 0xFF;
        }
        if (temp>127){
            arrayOutput[pointer] = (byte)(temp - 128);
        }
        else arrayOutput[pointer] = (byte)(temp);

    }

    private static int stopLoop(byte[] arrayOutput,int beginLoopIndex, int endLoopIndex, int pointer){
        if(arrayOutput[pointer] == 0x00){
            return endLoopIndex + 1;
        }
        return beginLoopIndex;
    }

    private List<String> initListSource(byte[] array){
        List<String> list = new ArrayList<String>();
         byte[] tmpArray= new byte[4];
        int index=0;
        int tmpI=0;
        while(index<array.length-2){
            for(int i=index;i<index+4;i++) {
                tmpArray[tmpI++] = array[i];

            }
            list.add(new String(tmpArray, StandardCharsets.UTF_8));
            tmpI=0;
            index+=4;
        }
        return list;
    }
}

