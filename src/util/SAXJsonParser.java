package util;

import net.sf.json.*;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.List;

public class SAXJsonParser extends DefaultHandler {

    static final String TEXTKEY = "_text";

    JSONObject result;
    List<JSONObject> stack;

    public SAXJsonParser(){}
    public JSONObject getJson(){return result;}
    public String attributeName(String name){return "@"+name;}

    public void startDocument () throws SAXException {
        stack = new ArrayList<JSONObject>();
        stack.add(0,new JSONObject());
    }
    public void endDocument () throws SAXException {result = stack.remove(0);}
    public void startElement (String uri, String localName,String qName, Attributes attributes) throws SAXException {
        JSONObject work = new JSONObject();
        for (int ix=0;ix<attributes.getLength();ix++)
            work.put( attributeName( attributes.getLocalName(ix) ), attributes.getValue(ix) );
        stack.add(0,work);
    }
    public void endElement (String uri, String localName, String qName) throws SAXException {
        JSONObject pop = stack.remove(0);       // examine stack
        Object stashable = pop;
        if (pop.containsKey(TEXTKEY)) {
            String value = pop.getString(TEXTKEY).trim();
            if (pop.keySet().size()==1) stashable = value; // single value
            else if (StringUtils.isBlank(value)) pop.remove(TEXTKEY);
        }
        JSONObject parent = stack.get(0);
        if (!parent.containsKey(localName)) {   // add new object
            parent.put( localName, stashable );
        }
        else {                                  // aggregate into arrays
            Object work = parent.get(localName);
            if (work instanceof JSONArray) {
                ((JSONArray)work).add(stashable);
            }
            else {
                parent.put(localName,new JSONArray());
                parent.getJSONArray(localName).add(work);
                parent.getJSONArray(localName).add(stashable);
            }
        }
    }
    public void characters (char ch[], int start, int length) throws SAXException {
        JSONObject work = stack.get(0);            // aggregate characters
        String value = (work.containsKey(TEXTKEY) ? work.getString(TEXTKEY) : "" );
        work.put(TEXTKEY, value+new String(ch,start,length) );
    }
    public void warning (SAXParseException e) throws SAXException {
        System.out.println("warning  e=" + e.getMessage());
    }
    public void error (SAXParseException e) throws SAXException {
        System.err.println("error  e=" + e.getMessage());
    }
    public void fatalError (SAXParseException e) throws SAXException {
        System.err.println("fatalError  e=" + e.getMessage());
        throw e;
    }
}