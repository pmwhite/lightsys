/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightsysnetwork;
import java.util.*;
import java.time.format.DateTimeFormatter;
import org.apache.commons.math3.ml.clustering.Clusterable;

/**
 *
 * @author Philip
 */
public class Packet implements Clusterable {
    public long timestamp;
    public int length;
    public boolean in;
    public String name;
    
    public Packet(long ts, int len, boolean isIn, String n) {
        timestamp = ts;
        length = len;
        in = isIn;
        name = n;
    }
    
    public static Packet parsePacket(String input) {
        Scanner scanner = new Scanner(input);
        scanner.useDelimiter(",");
        
        String date = scanner.next();
        int length = Integer.parseInt(scanner.next());
        boolean isIn = scanner.next() == "in";
        String name = scanner.next();
        
        
        DateTimeFormatter f = new DateTimeFormat.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        
       return null;
        
        
        
    }

    @Override
    public double[] getPoint() {
        double[] result = new double[] {
            timestamp, length, in
        };
        return result;
    }
}
