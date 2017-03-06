package lightsysnetwork;

import java.util.*;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

/**
 *
 * @author stall
 */
public class AnimalCorrelator {
    public static double[][] Correlator (ArrayList<Packet> animalArray){
        DBSCANClusterer scanLuster;
        
        scanLuster = new DBSCANClusterer(1, 200);
        List<Cluster<Packet>> clusters = scanLuster.cluster(animalArray); 
        int i = 0;

	for(Cluster c : clusters){
		System.out.println("CLUSTER #" + ++i);
		System.out.println("----------------");
		HashSet<String> m = new HashSet();
		
		List<Packet> l = c.getPoints();
		for(Packet p : l){
			m.add(p.name);
		}

		for(String s : m) System.out.println(s);

		System.out.println();
	}
	return null;        
    }
}
