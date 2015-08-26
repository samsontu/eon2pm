package gov.va.test.opioidtesttool;

// lotsa stuff for XML 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.stream.StreamResult; 
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  
import java.io.File;
import java.io.IOException;
import org.w3c.dom.*;
import java.util.*;

/* 
 * XML Format
 *
 * <patientdata>
 *    <patient name="John Doe">
 *       ...  (see PatientDataStore)
 *    </patient>
 *
 *     ....
 *
 *    <patient name="Jane Doe">
 *        ...
 *    </patient>
 * </patientdata>
 */

public class XMLInterface {
    public static void read() {
	int x;
	Document doc = null;
	DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
	System.out.println("Reading in XML patient data");
        try {
           DocumentBuilder builder = factory.newDocumentBuilder();
           doc = builder.parse( new File(GlobalVars.XMLDataFile) ); 

	   Element root = doc.getDocumentElement();
	
	   NodeList nl = root.getElementsByTagName("patient");
	   for (x=0; x < nl.getLength(); x++) {
	       Element e =  (Element) nl.item(x);

	       PatientDataStore p = new PatientDataStore(e);
	       GlobalVars.pds.add(p);	       
	   }
        } catch (Exception e) {
	    System.err.println("Error READING xml patient data file");
	    System.err.println("Going to default to no patients read");
	    e.printStackTrace();
        } 
    }

    public static void write() {
	DocumentBuilderFactory factory =
	    DocumentBuilderFactory.newInstance();
	Document doc;

	System.out.println("Writing patient data file\n");

        try {
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    doc = builder.newDocument();
	    
	    Element root = doc.createElement("patientdata");
	    root.appendChild(XMLInterface.nl(doc));

	    // get first PatientData from DataStore
	    Iterator<PatientDataStore> pdsIt = GlobalVars.pds.iterator();
	    while ( pdsIt.hasNext() ) {
		PatientDataStore pds = pdsIt.next(); 
		Node n = pds.createXMLNode(doc);

		XMLInterface.addNode(doc, root, n, 0); 
	    }

	    root.normalize();
	    // create junk to write it out (see java xml tutorial)
            TransformerFactory tFactory =
                TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
	    DOMSource src = new DOMSource(root) ;
	    StreamResult result = new StreamResult(new File(GlobalVars.XMLDataFile));
            transformer.transform(src, result);
	} catch (Exception e) {
	    System.err.println("Error WRITING xml patient data file");
	    e.printStackTrace();
	}
    }

    /*
     * Functions to make sure XML is indented properly
     */
    
    public static void addNode(Document doc, Node parent, Node child, int level) {
	parent.appendChild(indent(doc, level));
	parent.appendChild(child);
	parent.appendChild(nl(doc));
    }

    public static Node indent(Document doc, int level) {
	StringBuffer sb = new StringBuffer();
	final String basicIndent = "    ";
	int x;
	for (x=0; x < level; x++) {
	    sb.append(basicIndent);
	}
	return doc.createTextNode(sb.toString());
    }

    public static Node nl(Document doc) {
	return doc.createTextNode("\n");
    }
}
