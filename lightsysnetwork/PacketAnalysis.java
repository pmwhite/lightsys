package lightsysnetwork;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class PacketAnalysis {

    // Takes all the raw packets and combines those which are for the same animal, 
    // the same direction, and within a second of the previous one.
    public static ArrayList<Packet> combine(ArrayList<Packet> a) {

        ArrayList<Packet> n = new ArrayList();

        a.sort((Packet o1, Packet o2) -> {
            Packet p1 = (Packet) o1;
            Packet p2 = (Packet) o2;
            
            if (p1.name.equals(p2.name)) {
                return Long.compare(p1.timestamp, p2.timestamp);
            } else {
                return p1.name.compareTo(p2.name);            
            }
        });

        Packet prev = a.get(0);
        int sum = 0;
        boolean stop = false;
        for (Packet p : a) {
            if (p.name.equals(prev.name) && p.timestamp < prev.timestamp + 1000000) {
                sum += p.length;
            } else if (p.name.equals(prev.name) && p.timestamp < prev.timestamp + 1000000) {
                sum += p.length;
                stop = true;
            } else if (!p.name.equals(prev.name) || p.timestamp >= prev.timestamp + 1000000) {
                stop = true;
            }

            if (stop) {
                n.add(new Packet(p.timestamp, sum, true, p.name));
                sum = p.length;
                stop = false;
            }

            prev = p;

        }

        return n;
    }

    // Partitions packets into combined packets lengths, and removes those which
    // are used by too many animal or by just one animal.
    public static HashMap<Integer, ArrayList<Packet>> weed(ArrayList<Packet> a) {
        HashMap<Integer, ArrayList<Packet>> packetsBySize = new HashMap<>();

        for (int i = 0; i < a.size(); i++) {
            Packet currPacket = a.get(i);

            if (!packetsBySize.containsKey(currPacket.length)) {
                packetsBySize.put(currPacket.length, new ArrayList<>());
            }
            packetsBySize.get(currPacket.length).add(currPacket);
        }

        Iterator<Map.Entry<Integer, ArrayList<Packet>>> iter = packetsBySize.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry<Integer, ArrayList<Packet>> entry = iter.next();
            ArrayList<Packet> packets = entry.getValue();
            int animalCount = new HashSet<>(packets).size();
            if (animalCount < 2 || animalCount > 5) {
                iter.remove();
            }
            
        }

        return packetsBySize;
    }

    // Convert partitioned packets to a "multiplication table" of the correlations
    // between animals.
    public static double[][] intersect(HashMap<Integer, ArrayList<Packet>> h) {
        int animalCount = PacketFile.animalCount();

        double[][] freqs = new double[animalCount][animalCount];

        Set<Integer> packetSizes = h.keySet();
        for (int packetSize : packetSizes) {
            ArrayList<Packet> packets = h.get(packetSize);
            
            for(int i = 0; i < animalCount; i++) {
                String animal1, animal2;
                int a, b;
                
                animal1 = PacketFile.packetName(i);
                // TODO: use dynamic programming to scale better
                a = animalsOnPacketLength(animal1, packets);
                if(a == 0) continue;
                
                for(int j=i+1; j < animalCount; j++){
                    
                    animal2 = PacketFile.packetName(j);
                    b = animalsOnPacketLength(animal2, packets);
                    double addition = Math.pow(Math.min(a, b), 2);
                    freqs[i][j] += addition;
                    freqs[j][i] += addition;
                }
            }
        }
        
        return freqs;
    }
    
    // Convert correlation table to list of correlations for printing.
    public static ArrayList<Correlation> flatten(double[][] intersections) {
        ArrayList<Correlation> correlations = new ArrayList<>();
        for (int i = 0; i < PacketFile.animalCount(); i++) {
            for (int j = 0; j < PacketFile.animalCount(); j++) {
                correlations.add(new Correlation(i, j, intersections[i][j]));
            }
        }
        return correlations;
    }
    
    // Finds the number of animals have used a certain packet length.
    public static int animalsOnPacketLength(String str, ArrayList<Packet> packets) {
        int count = 0;
        
        for(Packet packet : packets){
            if(packet.name.equals(str)){
                count++;
            }
        }
        
        return count;
    }
    
    // Takes multiple packet files and sums the correlations into one large result.
    public static ArrayList<Correlation> sumCorrelations(String[] args) throws FileNotFoundException, ParseException, IOException {
        double[][] intersectionsTotal = new double[200][200];
        for (int k = 1; k < args.length; k++) {
            PacketFile pf = new PacketFile(args[k]);
            double[][] intersections = intersect(weed(combine(pf.packetsIn)));          
            int animalCount = PacketFile.animalCount();
            for (int i = 0; i < animalCount; i++) {
                for (int j = 0; j < animalCount; j++) {
                    intersectionsTotal[i][j] += intersections[i][j];
                }
            }
        }
        return flatten(intersectionsTotal);
    }
    
    // Correlates animals based on percentage in common with other animals.
    public static ArrayList<Correlation> relativeCorrelations(ArrayList<Packet> packets) { 
        int animalCount = PacketFile.animalCount();
        double[][] freqs =  intersect(weed(combine(packets)));
        
        for (int i = 0; i < animalCount; i++) {
            int total = 0;
            for (int j = 0; j < animalCount; j++) {
                total += freqs[i][j];
            }
            double divisor = Math.pow(total, 0.2);
            
            for (int j = 0; j < animalCount; j++) {
                if (total == 0) {
                    freqs[i][j] = 0;                    
                }
                else {
                    freqs[i][j] /= divisor;
                }
            }
        }
        
        return flatten(freqs);
    }
    
    // Simplest correlation of a single file.
    public static ArrayList<Correlation> basicCorrelations(ArrayList<Packet> packets) {
        return flatten(intersect(weed(combine(packets))));
    }

    // Scratch work.
    public static void main(String[] args) throws FileNotFoundException, ParseException, IOException {        
        String filename = args[0];
        PacketFile pf = new PacketFile(filename);

        ArrayList<Correlation> correlations = basicCorrelations(pf.packetsIn);//intersectionsToCorrelations(intersections);
        
        System.out.println(PacketFile.animals.toString());
        
        correlations.sort((Correlation o1, Correlation o2) -> Double.compare(o1.amount, o2.amount));
        
        Correlation.writeCorrelations(correlations, "correlate_" + filename);
    }
}
