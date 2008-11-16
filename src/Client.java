//package fixmico;

/**
 * <p>Title: Client.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

import java.util.*;
import java.io.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class Client
{
  public static void main(String[] args)
  {
    BufferedReader infile=null;
    String ior = "";
    String ior_file = "";
    String xml_file = "";
    
    if (args.length == 0)
    {
    	xml_file = "account.xml";
    }
    else{
    	xml_file = args[0];
    }

    StringBuffer xml = new StringBuffer();
    try {
    	System.out.println("Lendo o arquivo "+xml_file);
        infile = new BufferedReader(new FileReader(xml_file));
        String str;
        while ((str = infile.readLine()) != null) {
            xml.append(str+"\n");
        }
        infile.close();
        //System.out.println("[client] conteudo do arquivo lido: \n"+xml.toString());
    } catch (IOException e) {
    	e.printStackTrace();
    }finally{
    	try {
			infile.close();
		} catch (IOException e) {
		}
    	System.out.println("leitura finalizada");
    }
    
    /*
    CodecXml codec = new CodecXml(xml);
    
    System.out.println("[cliente] object: "+codec.getField("object"));
    System.out.println("[cliente] host: "+codec.getField("host"));
    System.out.println("[cliente] port: "+codec.getField("port"));
    */
    
    XStream xstream = new XStream(new DomDriver());
    xstream.alias("reference", ObjectXmlReference.class);
    ObjectXmlReference acc_reference = (ObjectXmlReference) xstream.fromXML(xml.toString());
    
    ObjectReference ref = new ObjectReference (acc_reference.getObject(), acc_reference.getHost(), acc_reference.getPort());

    Account account = new AccountStub (ref);
    
    System.out.println("deposit");
    account.deposit (700);
    System.out.println("Client: balance is " + account.balance());
    
    
    System.out.println("withdraw");
    account.withdraw (50);
    System.out.println("Client: balance is " + account.balance());
    
    /*
    account.withdraw (200);
    System.out.println("Client: balance is " + account.balance());
	*/
    
  }
}
