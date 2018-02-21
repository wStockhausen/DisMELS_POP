/*
 * LengthAtWeightFunction.java
 */
package wts.models.DisMELS.IBMs.POP;

import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * This class provides an implementation of a length-at-weight function.
 * <p>
 * Type:
 * <ul><li>Length-at-weight</ul>
 * 
 * Parameters (by key):
 * <ul>
 *  <li> pAlpha  - Double - multiplicative coefficient for length-weight regression
 *  <li> pBeta - Double - power coefficient for length-weight regression
 * </ul>
 * Variables:
 * <ul>
 *  <li> vars - double[]{W}, where 
 *   <ul>
 *    <li> W - weight of individual (micrograms)
 *  </ul>
 * </ul>
 * Value:
 * <ul><li>L - Double -  length in mm corresponding to W </ul>
 * Calculation:
 * <ul>
 *  <li>  L = (W/pAlpha)^(1/pBeta);     //length (mm) corresponding to W, consistent with W = pAlpha*L^pBeta
 * </ul>
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class LengthAtWeightFunction extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "individual size";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Length-at-weight function";
    /** function description */
    public static final String DEFAULT_descr = "A length-at-weight function.";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of a length-at-weight function."+
            "\n\t* Type: "+
            "\n\t*      individual size"+
            "\n\t* Parameters (by key):"+
            "\n\t*  pAlpha  - Double - multiplicative coefficient for length-weight regression"+
            "\n\t*  pBeta - Double - power coefficient for length-weight regression"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[]{W}, where"+
            "\n\t*          W - double - weight (micrograms)"+
            "\n\t* Value:"+
            "\n\t*      L - Double -  length (in mm) corresponding to W"+
            "\n\t* Calculation:"+
            "\n\t*   L = (W/pAlpha)^(1/pBeta);     //length (mm) corresponding to W, consistent with W = pAlpha*L^pBeta"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    //protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 2;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set the multiplicative coefficient for length-weight regression */
    public static final String PARAM_Alpha = "pAlpha";
    /** key to set the power coefficient for length-weight regression */
    public static final String PARAM_Beta = "pBeta";
    
    /** flag to print debugging info */
    public static boolean debug = true;
    /** logger for class */
    private static final Logger logger = Logger.getLogger(LengthAtWeightFunction.class.getName());
    
    /** value of the multiplicative coefficient for length-weight regression */
    private double pAlpha = 0.1674;//default value
    /** value of the power coefficient for length-weight regression */
    private double pBeta = 3.837;

    /** constructor for class */
    public LengthAtWeightFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_Alpha;    addParameter(key,Double.class, "multiplicative coefficient for length-weight regression");
        setParameterValue(key, pAlpha);
        key = PARAM_Beta;     addParameter(key,Double.class, "power coefficient for length-weight regression");
        setParameterValue(key, pBeta);
    }
    
    @Override
    public LengthAtWeightFunction clone(){
        LengthAtWeightFunction clone = new LengthAtWeightFunction();
        clone.setFunctionType(getFunctionType());
        clone.setFunctionName(getFunctionName());
        clone.setDescription(getDescription());
        clone.setFullDescription(getFullDescription());
        for (String key: getParameterNames()) clone.setParameterValue(key,getParameter(key).getValue());
//        for (String key: getSubfunctionNames()) clone.setSubfunction(key,(IBMFunctionInterface)getSubfunction(key).clone());
        return clone;
    }
    
    /**
     * Sets the parameter value corresponding to the key associated with param.
     * 
     * @param param - the parameter key (name)
     * @param value - its value
     * @return 
     */
    @Override
    public boolean setParameterValue(String param,Object value){
        if (super.setParameterValue(param, value)){
            switch (param) {
                case PARAM_Alpha:
                    pAlpha = ((Double) value).doubleValue();
                    break;
                case PARAM_Beta:
                    pBeta = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variables.
     * 
     * @param vars - the input variables (individual weight) as a double[].
     * <ul>
     *      <li> wgt - individual weight, in micrograms
     * </ul>
     * @return  - Double - length in mm
     */
    @Override
    public Double calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double wgt = lvars[i++]; //individual weight
        double len  = Math.pow(wgt/pAlpha, 1.0/pBeta); //length
        if (debug){
            logger.info("\n--W: "+wgt+"; pAlpha: "+pAlpha   +"; pBeta : "+pBeta    +"; L : "+len);
        }
        return len;
    }
}
