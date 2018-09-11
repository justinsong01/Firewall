package illumio;

import java.io.*;
import java.util.*;

public class Firewall {
	
	// resultSet includes all the mapping to search 4 arguments for accept_packet method.
	private Map<String, HashMap<String, HashMap<Integer, HashSet<String>>>> resultSet;
	
	// the below filePath is not necessary, but just wrote it for this class for a future reference.  
	private String filePath;

	public Firewall(String filePath) {
		this.filePath = filePath;
		this.resultSet = new HashMap<String, HashMap<String, HashMap<Integer, HashSet<String>>>>();
		try {
			// Basically fills out all the necessary data for resultSet. 
			readAndStoreFiles(filePath);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void readAndStoreFiles(String filePath) throws NumberFormatException, IOException {
		File file = new File(filePath);
		FileInputStream fileIn = new FileInputStream(file);
		BufferedReader br = new BufferedReader(new InputStreamReader(fileIn));
		String line = null;

		while ((line = br.readLine()) != null) {
			String[] parsed = line.split(",");
				
			String direction = parsed[0];
			String protocol = parsed[1];
			String port = parsed[2];
			String ip_address = parsed[3];

			String[] portRange = null;
			if (!resultSet.containsKey(direction)){
				resultSet.put(direction, new HashMap<String, HashMap<Integer, HashSet<String>>>());
			}

			if (!resultSet.get(direction).containsKey(protocol)){
				resultSet.get(direction).put(protocol, new HashMap<Integer, HashSet<String>>());
			}

			if (port.contains("-")) {
				portRange = port.split("-");
				for (int i = Integer.valueOf(portRange[0]); i < Integer.valueOf(portRange[1]) + 1; i++){
					if (!resultSet.get(direction).get(protocol).containsKey(i)) {
						resultSet.get(direction).get(protocol).put(i, new HashSet<String>());
					}
					resultSet.get(direction).get(protocol).get(i).add(ip_address);
				}
			} else {
				if (!resultSet.get(direction).get(protocol).containsKey(Integer.valueOf(port))){
					resultSet.get(direction).get(protocol).put(Integer.valueOf(port), new HashSet<String>());
				}
				resultSet.get(direction).get(protocol).get(Integer.valueOf(port)).add(ip_address);
			}
		}
		br.close();
	}


	private boolean accept_packet(String direction, String protocol, 
		int port, String ip_address) {
		// Base case to handle null case and negative port number which can only happen
		// when restriction is violated
		if (direction == null || protocol == null || port < 1 || ip_address == null) {
			return false;
		}
		
		// find a direction from resultSet
		if (resultSet.containsKey(direction)){
			// find a protocol related to specified direction
			if (resultSet.get(direction).containsKey(protocol)) {
				// find a port that is related to specified direction and protocol.
				if (resultSet.get(direction).get(protocol).containsKey(port)){
					// compare the ip range to ensure whether ip_address exists or not. 
					return ipCompare(ip_address, resultSet.get(direction).get(protocol).get(port));
				}
			}
		}
		return false; 

	}

	private boolean ipCompare(String ip_address, HashSet<String> dataStore) {
		String[] range = null;
		String[] fromIp = null;
		String[] toIp = null;
		
		// Checking for base case
		if (ip_address == null || dataStore == null) {
			return false;
		}

		String[] ip_parsed = ip_address.split("\\.");
		// Base case to ensure that ip format we are searching for is followed. 
		if (ip_parsed.length != 4) {
			return false;
		}
		
		for (String name: dataStore) {
			if (name.contains("-")){
				range = name.split("-");
				fromIp = range[0].split("\\.");
				toIp = range[1].split("\\.");

				if (compareNum(fromIp, toIp, ip_parsed)) {
					return true;
				}	
			} else {
				if (name.equals(ip_address)) {
					return true;
				}
			}
		}
		return false; 
	}
	
	private boolean compareNum(String[] fromIp, String[] toIp, String[] ipAddr){
		// Because You may assume that the input file contains only	valid, well-formed entries, 
		// we are allowed to use a multiplication of 256 to compare the ip address with range provided. 
		
		long fromIpAddr = Long.valueOf(fromIp[0]) * Long.valueOf((long) Math.pow(256.0, 3.0))
				+ Long.valueOf(fromIp[1]) * Long.valueOf((long) Math.pow(256.0, 2.0))
				+ Long.valueOf(fromIp[2]) * 256 + Long.valueOf(fromIp[3]);
		
		long toIpAddr = Long.valueOf(toIp[0]) * Long.valueOf((long) Math.pow(256.0, 3.0))
				+ Long.valueOf(toIp[1]) * Long.valueOf((long) Math.pow(256.0, 2.0))
				+ Long.valueOf(toIp[2]) * 256 + Long.valueOf(toIp[3]);
		
		long parsedIpAddr = Long.valueOf(ipAddr[0]) * Long.valueOf((long) Math.pow(256.0, 3.0))
				+ Long.valueOf(ipAddr[1]) * Long.valueOf((long) Math.pow(256.0, 2.0))
				+ Long.valueOf(ipAddr[2]) * 256 + Long.valueOf(ipAddr[3]);

		int result1 = Long.compare(parsedIpAddr,fromIpAddr);
		int result2 = Long.compare(toIpAddr,parsedIpAddr);
		
		// When both comparison result satisfies below, this means the specified ipAddr is in range.
		if (result1 >= 0 && result2 >= 0) {
			return true;
		}
		return false;
	}

	public static void main(String[] args){
		// Base case to check the filePath is provided while running java program
		if (args.length != 1) {
			System.out.println("1 argument filePath is missing!");
			System.exit(0);
		}
		String path = args[0];
		Firewall illumio = new Firewall(path);
		// giving false
		System.out.println(illumio.accept_packet("inbound","udp",53,"192.168.2.6"));
		
		// giving true
		System.out.println(illumio.accept_packet("inbound","udp",53,"192.168.1.25"));
		
		// giving true
		System.out.println(illumio.accept_packet("inbound","udp",5001,"251.245.24.25"));
		
		// giving false
		System.out.println(illumio.accept_packet("inbound","udp",2999,"192.168.2.6"));
		
		// giving true
		System.out.println(illumio.accept_packet("outbound","tcp",10053,"192.168.10.11"));
	}

}
