package lightsysnetwork;
import java.io.*;
import java.text.*;
import java.util.*;

/**
 *
 * @author stall
 */
public class LightSysNetwork {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
	ArrayList<Packet> packets = Packet.parseFile(new File("traffic_2017_03_06_a.csv"));
    	double[][] res = AnimalCorrelator.Correlator(packets);
    }
    
}
