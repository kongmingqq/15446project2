package edu.cmu.partytracer.serverThread;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException; 

public class XMLParser{
	private List<Client> clientList;
	private Destination destination;
	
    public List<Client> getClientList() {
		return clientList;
	}
	public Destination getDestination() {
		return destination;
	}
	
	public static void main (String args []){
		XMLParser p = new XMLParser(args[0]);
		System.out.println(p.getDestination());
		System.out.println(p.getClientList());
	}
	
	public XMLParser(String filename){
		try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(filename));

            // normalize text representation
            doc.getDocumentElement().normalize();

    		clientList = new ArrayList<Client>();
    		destination = new Destination(((Element)(doc.getElementsByTagName("destination").item(0))).getAttribute("name"),
    				Integer.parseInt(((Element)(doc.getElementsByTagName("destination").item(0))).getAttribute("latitude")),
    				Integer.parseInt(((Element)(doc.getElementsByTagName("destination").item(0))).getAttribute("longitude")));
    		
            NodeList clients = doc.getElementsByTagName("client");
            int totalClients = clients.getLength();
            System.out.println("Total no of clients: " + totalClients);

            for(int i=0; i<clients.getLength(); i++){
                Node client = clients.item(i);
                if(client.getNodeType() == Node.ELEMENT_NODE){
                    Element clientElement = (Element)client;

                    //-------
                    String id = clientElement.getElementsByTagName("id").item(0).getChildNodes().item(0).getNodeValue().trim();
                    int latO = Integer.parseInt(clientElement.getElementsByTagName("latoffset").item(0).getChildNodes().item(0).getNodeValue().trim());
                    int lngO = Integer.parseInt(clientElement.getElementsByTagName("lngoffset").item(0).getChildNodes().item(0).getNodeValue().trim());
                    int step = Integer.parseInt(clientElement.getElementsByTagName("step").item(0).getChildNodes().item(0).getNodeValue().trim());
                    int r = Integer.parseInt(clientElement.getElementsByTagName("randomdelay").item(0).getChildNodes().item(0).getNodeValue().trim());
                    clientList.add(new Client(id,latO,lngO,step,r));
                }//end of if clause
            }//end of for loop with s var


        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
    }
    
    public class Destination {
    	private String name;
    	private int latitude;
    	private int longitude;
    	
    	public Destination(String n, int lat, int lng) {
    		name = n;
    		latitude = lat;
    		longitude = lng;
    	}

		public String getName() {
			return name;
		}
		
		public int getLatitude() {
			return latitude;
		}

		public int getLongitude() {
			return longitude;
		}
		
		public String toString(){
			return "Name: "+name+" Lat: "+latitude+" Lng: "+longitude;
		}
    }
    public class Client {
    	private String id;
    	private int latOffset;
    	private int lngOffset;
    	private int step;
    	private int randomDelay;
    	
    	public Client(String id, int latO, int lngO, int step, int r) {
    		this.id = id;
    		this.latOffset = latO;
    		this.lngOffset = lngO;
    		this.step = step;
    		this.randomDelay = r;
    	}
    	
		public String getId() {
			return id;
		}
		public int getLatOffset() {
			return latOffset;
		}
		public int getLngOffset() {
			return lngOffset;
		}
		public int getStep() {
			return step;
		}
		public int getRandomDelay() {
			return randomDelay;
		}
		public String toString(){
			return "ID: "+id+" LatO: "+latOffset+" LngO: "+lngOffset+" Step: "+step+" Random: "+randomDelay;
		}
    }
}
