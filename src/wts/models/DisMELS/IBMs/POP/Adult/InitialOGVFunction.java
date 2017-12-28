/**
 * EggDevelopmentFunction.java
 */
package wts.models.DisMELS.IBMs.POP.Adult;

import com.wtstockhausen.utils.RandomNumberGenerator;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * This class provides an implementation of the initial oil globule volume for
 * POP larvae based on an analysis of maternal effects by Linsey Arnold.
 * <p>
 * Type:
 * <ul><li>Initial larval condition</ul>
 * Parameters (by key):
 * <ul>
 *  <li> c0 - Double - function intercept
 *  <li> c1 - Double - multiplier on maternal age (in years)
 *  <li> c2 - Double - nominal temperature to use
 *  <li> sigOGV - Double  - std. deviation in random component to development
 * </ul>
 * Variables:
 * <ul>
 *  <li> vars - double[]{A,W}, where <ul>
 *  <li> A - double - maternal age (in years)
 *  <li> W - double - week of year </ul>
 * </ul>
 * Value:
 * <ul><li>ogv - Double - initial oil globule volume</ul>
 * Calculation:
 * <ul>
 *  <li> eps   = N(0,sigRate) [random draw from a normal distribution)
 *  <li> ogv = c0 + c1*A + c2*W + eps
 * </ul>
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class InitialOGVFunction extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "Initial larval condition";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Maternal Effects OGV Function";
    /** function description */
    public static final String DEFAULT_descr = "A function for initial oil globule volume based on maternal effects.";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of an initial oil globule volume with maternal effects for"+
            "\n\t*      a Pacific ocean perch larva with additive random noise."+
            "\n\t* Type: "+
            "\n\t*      Initial larval condition"+
            "\n\t* Parameters (by key):"+
            "\n\t*      c0 - Double - function intercept"+
            "\n\t*      c1 - Double - multiplier on maternal age (in years)"+
            "\n\t*      c2 - Double - nominal temperature to use"+
            "\n\t*      sigOGV - Double  - std. deviation in random component to development"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[]{A,W}, where"+
            "\n\t*          A - double - maternal age (in years)"+
            "\n\t*          W - double - week of year"+
            "\n\t* Value:"+
            "\n\t*      ogv - Double - initial oil globule volume"+
            "\n\t* Calculation:"+
            "\n\t*      eps   = N(0,sigOGV) [random draw from a normal distribution)"+
            "\n\t*      ogv = c0 + c1*A + c2*W + eps"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 4;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set the intercept coefficient */
    public static final String PARAM_c0 = "c0";
    /** key to set the maternal age multiplier */
    public static final String PARAM_c1 = "c1";
    /** key to set the week-of-year multiplier */
    public static final String PARAM_c2 = "c2";
    /** key to set standard deviation parameter */
    public static final String PARAM_sigOGV = "sigOGV";
    
    /** flag to print debugging info */
    public static boolean debug = false;
    /** logger for class */
    private static final Logger logger = Logger.getLogger(InitialOGVFunction.class.getName());
    
    /** value of rate parameter */
    private double c0 = 0.05;//default value
    /** value of the maternal age multiplier */
    private double c1 = 0.0007;
    /** value of the week-of-year multiplier */
    private double c2 = -0.002;
    /** value of the standard deviation parameter */
    private double sigOGV = 0;
    
    /** constructor for class */
    public InitialOGVFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_c0;  addParameter(key,Double.class, "intercept coefficient");
        setParameterValue(key, c0);
        key = PARAM_c1; addParameter(key,Double.class, "maternal age multiplier");
        setParameterValue(key, c1);
        key = PARAM_c2;    addParameter(key,Double.class, "week-of-year multiplier");
        setParameterValue(key, c2);
        key = PARAM_sigOGV;addParameter(key,Double.class, "std. dev. of oil globule volume");
        setParameterValue(key, sigOGV);
    }
    
    @Override
    public InitialOGVFunction clone(){
        InitialOGVFunction clone = new InitialOGVFunction();
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
                case PARAM_c0:
                    c0 = ((Double) value).doubleValue();
                    break;
                case PARAM_c1:
                    c1 = ((Double) value).doubleValue();
                    break;
                case PARAM_c2:
                    c2 = ((Double) value).doubleValue();
                    break;
                case PARAM_sigOGV:
                    sigOGV = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variable.
     * 
     * @param vars - the inputs variables, maternalAge and weekOfYear as a double[].
     *      maternalAge - maternal age, in years
     *      weekOfYear  - week-of-year
     * @return  - the initial oil globule volume, as a Double 
     */
    @Override
    public Double calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double maternalAge = lvars[i++];
        double weekOfYear = lvars[i++];
        double rnd = 0; 
        if (sigOGV>0) rnd = rng.computeNormalVariate(); 
        double ogv = c0+c1*maternalAge+c2*weekOfYear+rnd*sigOGV;
        if (debug){
            logger.info("\n--c0: "+c0+"; c1: "+c1+", c2: "+c2);
            logger.info("\n--matAge: "+maternalAge+"; week: "+weekOfYear+", ogv: "+ogv);
        }
        return ogv;
    }
}
