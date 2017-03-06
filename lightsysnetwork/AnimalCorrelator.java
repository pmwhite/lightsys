package lightsysnetwork;

import java.util.ArrayList;

/**
 *
 * @author stall
 */
public class AnimalCorrelator {
    public static double[][] Correlator (ArrayList<Packet> animalArray){
        DBSCANClusterer scanLuster;
        
        scanLuster = new DBSCANClusterer(1, 200);
        ArrayList<Cluster<Packet>> clusters = scanLuster.cluster(animalArray); 
        return null;        
    }
}
