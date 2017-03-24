package lightsysnetwork;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Correlation {

    public int animal1;
    public int animal2;
    public double amount;
    
    public Correlation(int a1, int a2, double c) {
        animal1 = a1;
        animal2 = a2;
        amount = c;
    }
    
    @Override
    public String toString() {
        String an1 = PacketFile.packetName(animal1);
        String an2 = PacketFile.packetName(animal2);
        return an1 + "," + an2 + "," + amount;
    }

    public static void writeCorrelations(ArrayList<Correlation> list, String filename) throws IOException {
        FileOutputStream file = new FileOutputStream(filename);
        for (Object str : list) {
            file.write((str.toString()+"\n").getBytes());
        }
    }
}
