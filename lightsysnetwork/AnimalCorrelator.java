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
