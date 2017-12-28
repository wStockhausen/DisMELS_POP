/*
 * LarvaStageAttributes.java
 */

package wts.models.DisMELS.IBMs.POP.Larva;

import java.util.*;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.IBMs.POP.NewAttributes;
import wts.models.DisMELS.framework.AbstractLHSAttributes;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttribute;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttributeDouble;
import wts.models.DisMELS.framework.LifeStageAttributesInterface;

/**
 * DisMELS class representing attributes for POP larvae with maternal effects.
 * <p>
 * The complete list of attributes and keys are (in order):
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
 *  <li> MaternalAge - "maternal age"
 *  <li> OGV - "oil globule volume"
 * </ul>
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class LarvaStageAttributes extends AbstractLHSAttributes {
    //attributes specific to this class
    /** number of keys new to this class */
    public static final int numNewAttributes = 2;
    /** key for the maternal age attribute */
    public static final String PROP_MaternalAge = "maternal age";
    /** key for the oil globule volume attribute */
    public static final String PROP_OGV         = "oil globule volume";
    
    /** total number of attributes for this class */
    protected static final int totNumAttributes = AbstractLHSAttributes.numAttributes+NewAttributes.numNewAttributes+LarvaStageAttributes.numNewAttributes;
    
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
   
    /** flag to print debugging info */
    public static boolean debug = false;
    /** the class logger for debugging info */
    private static final Logger logger = Logger.getLogger(LarvaStageAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    public LarvaStageAttributes(){
        super("NULL");
        finishInstantiation();
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    public LarvaStageAttributes(String typeName) {
        super(typeName);
        finishInstantiation();
    }
    
    private void finishInstantiation(){
        if (mapAllAttributes.isEmpty()){
            //set static field information
            ////attributes map
            mapAllAttributes.putAll(AbstractLHSAttributes.mapAttributes);//add from AbstractLHSAttributes
            mapAllAttributes.putAll(NewAttributes.getNewMapAttributes());//add from NewAttributes
            ////add attributes defined in this class
            String key;
            key = PROP_MaternalAge; mapAllAttributes.put(key, new IBMAttributeDouble(key,key));
            key = PROP_OGV;         mapAllAttributes.put(key, new IBMAttributeDouble(key,key));
            ////keys
            allKeys.addAll(AbstractLHSAttributes.keys);//add from AbstractLHSAttributes
            allKeys.addAll(NewAttributes.getNewKeys());//add from NewAttributes
            allKeys.add(PROP_MaternalAge);
            allKeys.add(PROP_OGV);
            ////create aKeys
            Iterator<String> it = allKeys.iterator();
            int j = 0; it.next();//skip typeName
            while (it.hasNext()) aKeys[j++] = it.next();
        }
        //set instance information with dummy values (already done for superclass)
        mapValues.putAll(NewAttributes.getNewMapValues());
        mapValues.put(PROP_MaternalAge, new Double(0.0));
        mapValues.put(PROP_OGV,         new Double(0.0));
    }
    
    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        LarvaStageAttributes clone = new LarvaStageAttributes(typeName);
        for (String key: allKeys) clone.setValue(key,this.getValue(key));
        return clone;
    }

    /**
     * Returns a new instance constructed from the values of the string[].
     * The first value in the string vector must be the type name.
     * Values are set internally by calling setValues(strv) on the new instance.
     * @param strv - vector of values (as Strings) 
     * @return - the new instance
     */
    @Override
    public LarvaStageAttributes createInstance(final String[] strv) {
        LarvaStageAttributes atts = new LarvaStageAttributes(strv[0]);//this sets atts.typeName
        atts.setValues(strv);
        return atts;
    }

    /**
     * Returns the attribute values as an ArrayList (including typeName).
     * 
     * @return 
     */
    @Override
    public ArrayList getArrayList() {
        ArrayList a = super.getArrayList();
        for (String key: NewAttributes.getNewKeys()) a.add(getValue(key));
        a.add(getValue(PROP_MaternalAge));
        a.add(getValue(PROP_OGV));
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
        str = str+cc+getValueAsString(PROP_MaternalAge);
        str = str+cc+getValueAsString(PROP_OGV);
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
     * in the order allKeys is defined.
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
            //set attributes from NewAttributes class
            for (String key: NewAttributes.getNewKeys()) setValueFromString(key,strv[j++]);
            //set attributes defined specifically in this class
            setValueFromString(PROP_MaternalAge,strv[j++]);
            setValueFromString(PROP_OGV,strv[j++]);
        } catch (java.lang.IndexOutOfBoundsException ex) {
            //@TODO: should throw an exception here that identifies the problem
            String[] aKeys = new String[LarvaStageAttributes.allKeys.size()];
            aKeys = LarvaStageAttributes.allKeys.toArray(aKeys);
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
            String[] aKeys = new String[LarvaStageAttributes.allKeys.size()];
            aKeys = LarvaStageAttributes.allKeys.toArray(aKeys);
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
        if (debug) logger.info("getting '"+key+"' for id "+getValue(LifeStageAttributesInterface.PROP_id).toString());
        Object val = getValue(key);
        IBMAttribute att = mapAllAttributes.get(key);
        att.setValue(val);
        String str = att.getValueAsString();
        if (debug) logger.info("--value: "+str);
        return str;
    }
    
    @Override
    public void setValueFromString(String key, String value) throws NumberFormatException {
        if (!key.equals(PROP_typeName)){
            if (debug) logger.info("setting '"+key+"' to "+value+" for id "+getValue(LifeStageAttributesInterface.PROP_id).toString());
            IBMAttribute att = mapAllAttributes.get(key);
            att.parseValue(value);
            setValue(key,att.getValue());
            if (debug) logger.info("--value set = "+getValue(key).toString());
        }
    }
}
