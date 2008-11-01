import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import xml.ObjectXmlReference;

import com.thoughtworks.xstream.XStream;


public class ChatServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BufferedWriter outfile = null;
	    String server_host = "localhost";
	    int server_port = 5555;

	    TCPAddress addr = new TCPAddress (server_host, server_port);
	    String ior = "";
	    String xml_file = "roomregistry.xml";
	    
	    ORB.instance().address(addr);
	    
	    /////////////////
	    //RoomRegistry //
	    /////////////////
	    RoomRegistry roomregistry = null;
	    roomregistry = new RoomRegistryImpl();
	    
	    ior = ORB.objectToString(roomregistry);
	    ObjectXmlReference roomreg_xml = new ObjectXmlReference(ior, server_host, String.valueOf(server_port));
		XStream xstream = new XStream();
		xstream.alias("reference", ObjectXmlReference.class);
		
		String ref_xml = xstream.toXML(roomreg_xml);
		echo("xml com a referencia: ");
		System.out.println(ref_xml);
		
	    
	    try {
	    	outfile = new BufferedWriter(new FileWriter(xml_file));
			outfile.write(ref_xml);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		    try {
				outfile.close();
			} catch (IOException e) {
			}
		}

	    
	    echo("obj reference do roomregistry -> "+ior);
	    echo("ORB Running on "+server_host+":"+server_port);
	    
	    ORB.instance().run();
	    
	}
	
	public static void echo(String msg) {
		System.out.println("[ChatServer] "+msg);
	}
		

}
