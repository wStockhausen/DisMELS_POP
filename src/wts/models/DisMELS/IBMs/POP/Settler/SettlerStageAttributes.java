/*
 * SettlerStageAttributes.java
 */

package wts.models.DisMELS.IBMs.POP.Settler;

import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import wts.models.DisMELS.IBMs.POP.AbstractPOPAttributes;

/**
 * DisMELS class representing attributes for POP settlers.
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
 * </ul>
 */
@ServiceProvider(service=wts.models.DisMELS.framework.LifeStageAttributesInterface.class)
public class SettlerStageAttributes extends AbstractPOPAttributes {
    protected static final String[] shortNames = new String[totNumAttributes];
   
    /** the class logger for debugging info */
    private static final Logger logger = Logger.getLogger(SettlerStageAttributes.class.getName());
    
    /**
     * This constructor is provided only to facilitate the ServiceProvider functionality.
     * DO NOT USE IT!!
     */
    public SettlerStageAttributes(){
        super("NULL");
    }
    
    /**
     * Creates a new attributes instance with type name 'typeName'.
     */
    public SettlerStageAttributes(String typeName) {
        super(typeName);
    }
    
    /**
     * Returns a deep copy of the instance.  Values are copied.  
     * Any listeners on 'this' are not(?) copied, so these need to be hooked up.
     * @return - the clone.
     */
    @Override
    public Object clone() {
        SettlerStageAttributes clone = new SettlerStageAttributes(typeName);
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
    public SettlerStageAttributes createInstance(final String[] strv) {
        SettlerStageAttributes atts = new SettlerStageAttributes(strv[0]);//this sets atts.typeName
        atts.setValues(strv);
        return atts;
    }
    
}
