import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by Matus on 15.02.2017.
 */
public class Loader {

    public byte[] load(String fileName){
        byte[] array=null;
        array = doLoad(fileName, array);
        return array;
    }

    public byte[] load(String fileName, int size){
        byte[] array = new byte[size];
        array = doLoad(fileName, array);
        return array;
    }

    private byte[] doLoad(String fileName, byte[] array){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try {
            array = new String(Files.readAllBytes(file.toPath())).getBytes();
        } catch (IOException e) {
            System.out.println("FILE WAS NOT FOUND :)");
            e.printStackTrace();
        }
        return array;
    }

}
