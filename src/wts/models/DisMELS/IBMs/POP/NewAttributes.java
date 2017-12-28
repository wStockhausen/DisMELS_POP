/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wts.models.DisMELS.IBMs.POP;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttribute;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttributeBoolean;
import wts.models.DisMELS.framework.IBMAttributes.IBMAttributeDouble;

/**
 * Class encapsulating new attributes defined for the POP IBM.
 * <p>
 * The new attributes are (in order):
 * <ul>
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
public class NewAttributes {
    /** Number of non-standard attributes defined by this class */
    public static final int numNewAttributes = 11;
    
    public static final String PROP_attached    = "attached";
    public static final String PROP_size        = "size (cm)";
    public static final String PROP_weight      = "weight (kg)";
    public static final String PROP_temperature = "temperature deg C";
    public static final String PROP_salinity    = "salinity";
    public static final String PROP_bathymetry  = "bathymetry";
    public static final String PROP_romsvar1    = "romsvar1";
    public static final String PROP_romsvar2    = "romsvar2";
    public static final String PROP_romsvar3    = "romsvar3";
    public static final String PROP_romsvar4    = "romsvar4";
    public static final String PROP_romsvar5    = "romsvar5";
    
    private static final Set<String> newKeys = new LinkedHashSet<>((int)(2*numNewAttributes));
    protected static final Map<String,IBMAttribute> newMapAttributes = new HashMap<>((int)(2*(numNewAttributes)));    
    private static final Map<String,Object> newMapValues = new HashMap<>((int)(2*numNewAttributes));
    
    /**
     * Get the set of keys for the new "global" attributes for this species.
     * 
     * @return Set<String> with the new keys
     */
    public static final Set<String> getNewKeys(){
        if (newKeys.isEmpty()){
            //set static field information
            String key;
            key = PROP_attached;   newKeys.add(key); 
            key = PROP_size;       newKeys.add(key); 
            key = PROP_weight;     newKeys.add(key); 
            key = PROP_temperature;newKeys.add(key); 
            key = PROP_salinity;   newKeys.add(key); 
            key = PROP_bathymetry; newKeys.add(key); 
            key = PROP_romsvar1;   newKeys.add(key); 
            key = PROP_romsvar2;   newKeys.add(key); 
            key = PROP_romsvar3;   newKeys.add(key); 
            key = PROP_romsvar4;   newKeys.add(key); 
            key = PROP_romsvar5;   newKeys.add(key); 
        }
        return newKeys;
    }
    
    public static final Map<String,IBMAttribute> getNewMapAttributes(){
        if (newMapValues.isEmpty()){
            if (newKeys.isEmpty()) getNewKeys();
            String key;
            key = PROP_attached;   newMapAttributes.put(key,new IBMAttributeBoolean(key,key));
            key = PROP_size;       newMapAttributes.put(key,new IBMAttributeDouble(key,key)); 
            key = PROP_weight;     newMapAttributes.put(key,new IBMAttributeDouble(key,key));  
            key = PROP_temperature;newMapAttributes.put(key,new IBMAttributeDouble(key,key));  
            key = PROP_salinity;   newMapAttributes.put(key,new IBMAttributeDouble(key,key)); 
            key = PROP_bathymetry; newMapAttributes.put(key,new IBMAttributeDouble(key,key)); 
            key = PROP_romsvar1;   newMapAttributes.put(key,new IBMAttributeDouble(key,key)); 
            key = PROP_romsvar2;   newMapAttributes.put(key,new IBMAttributeDouble(key,key));  
            key = PROP_romsvar3;   newMapAttributes.put(key,new IBMAttributeDouble(key,key)); 
            key = PROP_romsvar4;   newMapAttributes.put(key,new IBMAttributeDouble(key,key));  
            key = PROP_romsvar5;   newMapAttributes.put(key,new IBMAttributeDouble(key,key));  
        }
        return newMapAttributes;
    }
    
    public static final Map<String,Object> getNewMapValues(){
        if (newMapValues.isEmpty()){
            newMapValues.put(NewAttributes.PROP_attached,   new Boolean(false));
            newMapValues.put(NewAttributes.PROP_size,       new Double(0));
            newMapValues.put(NewAttributes.PROP_weight,     new Double(0));
            newMapValues.put(NewAttributes.PROP_temperature,new Double(-1));
            newMapValues.put(NewAttributes.PROP_salinity,   new Double(-1));
            newMapValues.put(NewAttributes.PROP_bathymetry, new Double(-1));
            newMapValues.put(NewAttributes.PROP_romsvar1,   new Double(-1));
            newMapValues.put(NewAttributes.PROP_romsvar2,   new Double(-1));
            newMapValues.put(NewAttributes.PROP_romsvar3,   new Double(-1));
            newMapValues.put(NewAttributes.PROP_romsvar4,   new Double(-1));
            newMapValues.put(NewAttributes.PROP_romsvar5,   new Double(-1));
        }
        return newMapValues;
    }
}
