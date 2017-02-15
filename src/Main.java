/**
 * Created by Matus on 15.02.2017.
 */
public class Main {
    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter("source.txt", "input.txt", "output.bin");
        interpreter.doInterpreter();
    }
}
