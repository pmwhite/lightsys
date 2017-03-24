package lightsysnetwork;

import java.text.*;
import java.util.*;
import java.sql.*;

public class Packet {

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
    
    @Override
    public String toString(){
        int dirValue = in ? 1 : 0;
        return timestamp + "," + length + "," + dirValue + "," + name;
    }
    
    public static Packet parsePacket(String input) throws ParseException {
        Scanner scanner = new Scanner(input);
        
        // Grab the first portion of the date.
        String time = scanner.next() + " ";
        // Switch to comma separation
        scanner.useDelimiter(",");
        // Grab the last part of the date.
        time += scanner.next();
        // Remove double spaces.
        time = time.replaceAll("  ", " ");
        // Parse date string.
        Timestamp date = Timestamp.valueOf(time);
        // Convert to total microseconds.
        long timestamp = date.getTime() * 1000;
        long micros = (date.getNanos()/1000) % 1000;
        
        
        int len = Integer.parseInt(scanner.next());
        boolean isIn = scanner.next().equals("in");
        String n = scanner.next();
        return new Packet(timestamp+micros, len, isIn, n);
    }
}
