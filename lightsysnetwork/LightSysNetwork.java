package lightsysnetwork;
import java.io.*;
import java.text.*;
import java.util.*;

import lightsysnetwork.Packet;
import lightsysnetwork.AnimalCorrelator;
/**
 *
 * @author stall
 */
public class LightSysNetwork {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException {
	ArrayList<Packet> packets = Packet.parseFile(new File("test.csv"));
    	double[][] res = AnimalCorrelator.Correlator(packets);	
    }
    
}
