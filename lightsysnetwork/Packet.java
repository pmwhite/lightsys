/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lightsysnetwork;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
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
    
    public String toString(){
        return timestamp + "," + length + "," + in + "," + name;
    }
    
    public static Packet parsePacket(String input) throws ParseException {
        Scanner scanner = new Scanner(input);
        
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
        
        String time = scanner.next() + " ";
        scanner.next();
        scanner.useDelimiter(",");
        time += scanner.next();
        
        Date date = f.parse(time);
        long timestamp = date.getTime();
        
        int len = Integer.parseInt(scanner.next());
        boolean isIn = scanner.next().equals("in");
        String n = scanner.next();
        return new Packet(timestamp, len, isIn, n);
    }
    
    public static ArrayList<Packet> parseFile(File file) throws FileNotFoundException, ParseException {
        Scanner scanner = new Scanner(file);
        ArrayList<Packet> result = new ArrayList<>();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Packet packet = parsePacket(line);
            result.add(packet);
        }
        return result;
    }
    
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println("Parsing...");
        ArrayList<Packet> p = parseFile(new File("traffic_2017_03_06_a.csv"));
        System.out.println("Outputting...");
        File f = new File("output.out");
        FileOutputStream d = new FileOutputStream(f);
        for(Packet a : p){
            d.write(a.toString().getBytes());
        }
    }

    @Override
    public double[] getPoint() {
        double dirValue = in ? 1 : 0;
        double[] result = new double[] {
            timestamp, length, dirValue
        };
        return result;
    }
}
