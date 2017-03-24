package lightsysnetwork;

import java.io.*;
import java.text.*;
import java.util.*;

public final class PacketFile {
    public static int numAnimals = 0;
    
    // The packets which are read from the file are partitioned by the direction
    // they were sent.
    ArrayList<Packet> packetsIn;
    ArrayList<Packet> packetsOut;

    // HashMaps for converting between the animal name number and the actual name.
    public static HashMap<String, Integer> animals = new HashMap();
    public static HashMap<Integer, String> backAnimals = new HashMap();
    
    // Reads a packet file into memory.
    public PacketFile(String filename) throws FileNotFoundException, ParseException {
        this(filename, new HashSet());
    }
    
    // Reads a packet file into memory, but ignores specified packet lengths.
    public PacketFile(String filename, HashSet<Integer> lengthBlacklist) throws FileNotFoundException, ParseException {
        // Initialize fields.
        packetsIn = new ArrayList<>();
        packetsOut = new ArrayList<>();

        System.out.println("Reading file: " + filename);
        Scanner scanner = new Scanner(new File(filename));
        
        // Keep track of what line we are at so that we can output some progress.
        int lineNumber = 0;
        while (scanner.hasNextLine()) {
            lineNumber++;
            if (lineNumber % 100000 == 0) {
                System.out.println("Scanned " + lineNumber + " lines.");
            }
            
            // Parse each line and store.
            String line = scanner.nextLine();
            Packet packet = Packet.parsePacket(line);
            if (!lengthBlacklist.contains(packet.length)) {
                addPacket(packet);
            }
        }
    }
    
    // Adds a packet to the data structure.
    public void addPacket(Packet packet) {
        if (!animals.containsKey(packet.name)) {
            animals.put(packet.name, numAnimals);
            backAnimals.put(numAnimals, packet.name);
            numAnimals++;
        }
        
        if(packet.in){
            packetsIn.add(packet);
        }
        else{
            packetsOut.add(packet);
        }
    }
    
    // Gets the index corresponding to the animal name of the packet.
    public static int packetNameIndex(Packet packet) {
        return animals.get(packet.name);
    }
    
    // Gets the animal name corresponding to the index.
    public static String packetName(int index) {
        return backAnimals.get(index);
    }
    
    // Gets the total number of animals encountered by all packet files.
    public static int animalCount() {
        return numAnimals;
    }
}
