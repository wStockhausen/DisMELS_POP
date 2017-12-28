/*
 * AbstractPOPAttributes.java
 */

package wts.models.DisMELS.IBMs.POP;

import java.util.*;
import java.util.logging.Logger;
import wts.models.DisMELS.framework.AbstractLHSAttributes;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttribute;
import wts.models.DisMELS.framework.LifeStageAttributesInterface;

/**
 * Abstract DisMELS class encapsulating attributes for several POP life stage classes 
 * (i.e., adults, benthic juveniles and settlers).
 * <p>
 * The complete list of attributes and keys for this abstract class is (in order):
 * <ul>
 *  <li> typeName - "Life stage type name"
 *  <li> id - "ID"
 *  <li> parentID - "Parent ID"
 *  <li> origID - "Original ID"
 *  <li> startTime - "Start tie (s)"
 *  <li> time - "Time (s)"
 *  <li> horizType - "Horiz. position type"
 *  <li> vertType - "Vert. position type"
 *  <li> horizPos1 - "Horiz. position 1"
 *  <li> horizPos2 - "Horiz. position 2"
 *  <li> vertPos - "Vert. position"
 *  <li> gridCellID - "Grid Cell ID"
 *  <li> track - "track"
 *  <li> active - "Active status"
 *  <li> alive - "Alive status"
 *  <li> age - "Age (d)"
 *  <li> ageInStage - "Age in stage (d)"
 *  <li> number - "Number of individuals"
 *  <li> attached - "attached"
 *  <li> sie - "size (cm)"
 *  <li> weight - "weight (kg)"
 *  <li> temperature - "temperature deg C"
 *  <li> salinity - "salinity"
 *  <li> bathymetry - "bathymetry"
 *  <li> romsvar1 - "romsvar1"
 *  <li> romsvar2 - "romsvar2"
 *  <li> romsvar3 - "romsvar3"
 *  <li> romsvar4 - "romsvar4"
 *  <li> romsvar5 - "romsvar5"
 * </ul>
 */
public abstract class AbstractPOPAttributes extends AbstractLHSAttributes {
    
    /** total number of attributes for this class */
    protected static final int totNumAttributes = AbstractLHSAttributes.numAttributes+NewAttributes.numNewAttributes;
        
    /** set of all keys to attributes */
    protected static final Set<String> allKeys = new LinkedHashSet<>((int)(2*(totNumAttributes)));
    /** map of all keys to attributes */
    protected static final Map<String,IBMAttribute> mapAllAttributes = new HashMap<>((int)(2*(totNumAttributes)));
    /** set of keys for all attributes other than typeName */
    protected static final String[] aKeys      = new String[totNumAttributes-1];//does not include typeName
    /** array of classes associated with the attributes */
    protected static final Class[]  classes    = new Class[totNumAttributes];
    /** array of short names associated with the attributes */
    protected static final String[] shortNames = new String[totNumAttributes];
   
    /** the class logger for debugging info */
    private static final Logger logger = Logger.getLogger(AbstractPOPAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    protected AbstractPOPAttributes(){
        super("NULL");
        finishInstantiation();
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    protected AbstractPOPAttributes(String typeName) {
        super(typeName);
        finishInstantiation();
    }

    private void finishInstantiation(){
        if (mapAllAttributes.isEmpty()){
            //set static field information
            mapAllAttributes.putAll(AbstractLHSAttributes.mapAttributes);//add from AbstractLHSAttributes
            mapAllAttributes.putAll(NewAttributes.getNewMapAttributes());//add from NewAttributes
            allKeys.addAll(AbstractLHSAttributes.keys);//add from AbstractLHSAttributes
            allKeys.addAll(NewAttributes.getNewKeys());//add from NewAttributes
            Iterator<String> it = allKeys.iterator();
            int j = 0; it.next();//skip typeName
            while (it.hasNext()) aKeys[j++] = it.next();
        }
        //set instance information with dummy values (already done for superclass)
        mapValues.putAll(NewAttributes.getNewMapValues());
    }
    
    /**
     *  This method should be overridden by extending classes.
     */
    @Override
    public abstract LifeStageAttributesInterface createInstance(final String[] strv);

    /**
     *  This method should be overridden by extending classes.
     */
    @Override
    public abstract Object clone() throws CloneNotSupportedException;

    /**
     * Returns the attribute values as an ArrayList (including typeName).
     * 
     * @return 
     */
    @Override
    public ArrayList getArrayList() {
        ArrayList a = super.getArrayList();
        for (String key: NewAttributes.getNewKeys()) a.add(getValue(key));
        return a;
    }

    /**
     * Returns the attributes values (not including typeName) as an Object[].
     * 
     * @return 
     */
    @Override
    public Object[] getAttributes() {
        Object[] atts = new Object[totNumAttributes-1];
        int j = 0;
        Iterator<String> it = allKeys.iterator();
        it.next();//skip PROP_typeName
        while (it.hasNext()) atts[j++] = getValue(it.next()); 
        return atts;
    }
    
    /**
     * Returns a CSV string representation of the attribute values.
     * 
     *@return - CSV string attribute values
     */
    @Override
    public String getCSV() {
        String str = super.getCSV();
        Iterator<String> it = NewAttributes.getNewKeys().iterator();
        while (it.hasNext()) str = str+cc+getValueAsString(it.next());
        return str;
    }
                
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     * Use getCSV() to get the string of actual attribute values.
     *
     *@return - String of CSV header names
     */
    @Override
    public String getCSVHeader() {
        Iterator<String> it = allKeys.iterator();
        String str = it.next();//typeName
        while (it.hasNext()) str = str+cc+it.next();
        return str;
    }
                
    /**
     * Returns the comma-delimited string corresponding to the attributes
     * to be used as a header for a csv file.  
     *
     *@return - String of CSV header names (short style)
     */
    @Override
    public String getCSVHeaderShortNames() {
        Iterator<String> it = allKeys.iterator();
        String str = mapAllAttributes.get(it.next()).shortName;//this is "typeName"
        while (it.hasNext()) str = str+cc+mapAllAttributes.get(it.next()).shortName;
        return str;
    }

    /**
     * Returns Class types for all attributes (including typeName) as a Class[]
     * in the order the allKeys are defined.
     * 
     * @return 
     */
    @Override
    public Class[] getClasses() {
        if (classes[0]==null){
            int j = 0;
            for (String key: allKeys){
                classes[j++] = mapAllAttributes.get(key).getValueClass();
            }
        }
        return classes;
    }

    /**
     * Returns keys for all attributes excluding typeName as a String[]
     * in the order the keys are defined.
     * 
     * @return 
     */
    @Override
    public String[] getKeys() {        
        return aKeys;
    }

    /**
     * Returns short names for all attributes (including typeName) as a String[]
     * in the order the allKeys are defined.
     * 
     * @return 
     */
    @Override
    public String[] getShortNames() {
        if (shortNames[0]==null){
            int j = 0;
            for (String key: allKeys){
                shortNames[j++] = mapAllAttributes.get(key).shortName;
            }
        }
        return shortNames;
    }
    
    /**
     * Sets attribute values to those of input String[].
     * @param strv - String[] of attribute values.
     */
    @Override
    public void setValues(final String[] strv) {
        super.setValues(strv);//set the standard attribute values
        //set the values of the new attributes
        int j = AbstractLHSAttributes.numAttributes;
        try {
            for (String key: NewAttributes.getNewKeys()) setValueFromString(key,strv[j++]);
        } catch (java.lang.IndexOutOfBoundsException ex) {
            //@TODO: should throw an exception here that identifies the problem
            String[] aKeys = new String[allKeys.size()];
            aKeys = allKeys.toArray(aKeys);
                String str = "Missing attribute value for "+aKeys[j-1]+".\n"+
                             "Prior values are ";
                for (int i=0;i<(j);i++) str = str+strv[i]+" ";
                javax.swing.JOptionPane.showMessageDialog(
                        null,
                        str,
                        "Error setting attribute values:",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                throw ex;
        } catch (java.lang.NumberFormatException ex) {
            String[] aKeys = new String[allKeys.size()];
            aKeys = allKeys.toArray(aKeys);
            String str = "Bad attribute value for "+aKeys[j-2]+".\n"+
                         "Value was '"+strv[j-1]+"'.\n"+
                         "Entry was '";
            try {
                for (int i=0;i<(strv.length-1);i++) {
                    if ((strv[i]!=null)&&(!strv[i].isEmpty())) {
                        str = str+strv[i]+", ";
                    } else {
                        str = str+"<missing_value>, ";
                    }
                }
                if ((strv[strv.length-1]!=null)&&(!strv[strv.length-1].isEmpty())) {
                    str = str+strv[strv.length-1]+"'.";
                } else {
                    str = str+"<missing_value>'.";
                }
            }  catch (java.lang.IndexOutOfBoundsException ex1) {
                //do nothing
            }
            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    str,
                    "Error setting attribute values:",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            throw ex;
        }
    }
    
    @Override
    public String getValueAsString(String key){
        Object val = getValue(key);
        IBMAttribute att = mapAllAttributes.get(key);
        att.setValue(val);
        String str = att.getValueAsString();
        return str;
    }
    
    @Override
    public void setValueFromString(String key, String value) throws NumberFormatException {
        if (!key.equals(PROP_typeName)){
            IBMAttribute att = mapAllAttributes.get(key);
            att.parseValue(value);
            setValue(key,att.getValue());
        }
    }
}
