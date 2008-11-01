package xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String xml_file = "account.xml";
		BufferedReader infile=null;
	    StringBuffer xml = new StringBuffer();
	    try {
	    	System.out.println("Lendo o arquivo "+xml_file);
	        infile = new BufferedReader(new FileReader(xml_file));
	        String str;
	        while ((str = infile.readLine()) != null) {
	            xml.append(str+"\n");
	        }
	        infile.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }finally{
	    	try {
				infile.close();
			} catch (IOException e) {
			}
	    	System.out.println("leitura finalizada");
	    }
	    
		
		System.out.println("xml lido: \n"+xml.toString());
		
		System.out.println("");
		/*
		 * Teste de Validacao do xml
		 */
	    // parse an XML document into a DOM tree
	    DocumentBuilder parser = null;
		try {
			parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//Document
	    Document document = null;
		try {
			document = parser.parse(new File(xml_file));
		} catch (SAXException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	    // create a SchemaFactory capable of understanding WXS schemas
	    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

	    // load a WXS schema, represented by a Schema instance
	    Source schemaFile = new StreamSource(new File("/Users/gustavosoares/workspace/morb_modificado/protocol.xsd"));
	    Schema schema = null;
		try {
			schema = factory.newSchema(schemaFile);
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	    // create a Validator instance, which can be used to validate an instance document
	    Validator validator = schema.newValidator();

	    // validate the DOM tree
	    try {
	    	System.out.println("validating xml "+xml_file);
	        validator.validate(new DOMSource(document));
	    } catch (SAXException e) {
	        // instance document is invalid!
	    	System.out.println("DOCUMENTO INVALIDO");
	    } catch (IOException e) {
			e.printStackTrace();
		}
		
	    System.out.println("");
		String field = "object";
	    String patternStr = "<"+field+">(.*?)</"+field+">";
	    
	    System.out.println("pattern: "+patternStr);
	    
	    // Compile and use regular expression
	    Pattern pattern = Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(xml.toString());
	    boolean matchFound = matcher.find();
	    
	    if (matchFound) {
	        // Get all groups for this match
	    	String field_value = matcher.group(matcher.groupCount());
	    	System.out.println("field value: "+field_value);
	    }else{
	    	System.out.println("object tag not found!");
	    }
	}

}
