import org.junit.jupiter.api.Test;

import java.io.*;

public class SerializeTest {
    @Test
    public void testLoadImage(){
        T t =new T();
        File f = new File("");
        try {
            FileOutputStream fos = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class T implements Serializable{
    int m = 4;
    int n = 8;
}
