package objectcourseend;

import java.util.ArrayList;
import java.util.Arrays;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/** @author Henrik Valve */
public class SaxXML extends DefaultHandler{
    
    /* Elemenents to be parsed */
    final private String pins[];
    /* Output form class*/
    final private ArrayList<String> output=new ArrayList();
    /* Selected something to be read*/
    boolean selected=false;
    /* Buffer */
    String buff="";
   
    /**
     * Creates saxXML instance which looks for given pins.
     * @param elements Elements of which you are looking for
     * @author Henrik Valve
     *
     */
    public SaxXML(String elements[]){
        this.pins = elements;
    }
 
    @Override
    public void startElement(String uri,String localName,String qName,Attributes attributes) throws SAXException {
  	for(String i:pins){
            if(i.equals(qName)){
                this.selected=true;
                return;
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
  	if(this.selected){
            this.output.add(this.buff);
            this.buff="";
  	}
  	this.selected=false;
    }

    @Override
    public void characters(char[] ch,int start,int length) throws SAXException {
  	if(this.selected){
        	this.buff += new String(Arrays.copyOfRange(ch,start,length+start));
  	}
    }
    
    /**
     * Returns how many elements were buffered
     * @return Size of the buffer
     */
    public int getElemetsSize(){
  	return this.pins.length;
    }
    
    /**
     * Output from the parser. 
     * This cleans the buffer
     * @return Array of strings that are read 
     */
    public String[] getResult(){ 	 
  	String re[]=new String[this.output.size()];
  	this.output.toArray(re);
  	this.output.clear();
  	return re;
    }
}
