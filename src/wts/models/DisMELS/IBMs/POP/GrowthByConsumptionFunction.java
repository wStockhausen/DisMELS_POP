/*
 * GrowthByConsumptionFunction.java
 */
package wts.models.DisMELS.IBMs.POP;

import com.wtstockhausen.utils.RandomNumberGenerator;
import java.util.logging.Logger;
import org.openide.util.lookup.ServiceProvider;
import org.openide.util.lookup.ServiceProviders;
import wts.models.DisMELS.framework.GlobalInfo;
import wts.models.DisMELS.framework.IBMFunctions.AbstractIBMFunction;
import wts.models.DisMELS.framework.IBMFunctions.IBMFunctionInterface;
import wts.models.DisMELS.framework.IBMFunctions.IBMGrowthFunctionInterface;

/**
 * This class provides an implementation of a growth function based on modeled consumption rates.
 * <p>
 * Type:
 * <ul><li>Consumption-driven growth</ul>
 * 
 * Parameters (by key):
 * <ul>
 *  <li> pCexp  - Double - coefficient in exponent for consumption rate
 *  <li> pLnQ10 - Double - ln-scale Q10 value
 *  <li> pOnT   - flag (0/1) to include temperature effects
 *  <li> pT0    - Double - nominal temperature to use
 *  <li> sigOGV - Double  - std. deviation in random component for consumption
 *  <li> pMaxAE - Double  - max  assimilation efficiency
 *  <li> pB     - Double - coefficient in exponent for max assimilation efficiency
 *  <li> pSDAE      - Double - assumed constant for SDA+E
 *  <li> pMetRstNum -Double - numerator coefficient for routine metabolism
 *  <li> pMetRstDen - Double - denominator coefficient for routine metabolism
 *  <li> pActMult   - Double - activity multiplier for active metabolism
 * </ul>
 * Variables:
 * <ul>
 *  <li> vars - double[]{W,T,L}, where 
 *   <ul>
 *    <li> W - double - weight of individual (micrograms)
 *    <li> T - double - temperature (deg C)
 *    <li> L - flag (0/1) indicating daylight (=1) or night (=0)
 *  </ul>
 * </ul>
 * Value:
 * <ul><li>G - Double -  daily growth rate in micrograms/day</ul>
 * Calculation:
 * <ul>
 *  <li>  if (stdvCons>0) rnd = rng.computeNormalVariate(); //random number
 *  <li>  C = 10.0^[pCexp*log10(W)-0.27];                 //consumption rate, without temperature dependence
 *  <li>  if (pOnT) C = C*exp[pLnQ10/10.0*(T-pT0)];       //consumption rate w/ temperature dependence
 *  <li>  C = C+rnd*stdvCons;                             //random effect (TODO: stdvCons should be adjusted for bio time step)
 *  <li>  AE     = pMaxAE*[1.0-0.25*exp(-pB*(W-10))];     //assimilation efficiency
 *  <li>  metRst = (pMetRstNum*W)/(pMetRstDen+W);         //resting metabolism
 *  <li>  metAct = pActMult*metRst;                       //active metabolism
 *  <li>  TC     = (1-L)*metRst + L*metAct + C*pSDAE;     //rate (micrograms/day) of total metabolic costs [pSDAE = SDA + E]
 *  <li>  G      = (C * AE) - TC;                         //growth rate in micrograms/day
 * </ul>
 * 
 * @author William.Stockhausen
 */
@ServiceProviders(value={
    @ServiceProvider(service=IBMFunctionInterface.class)}
)
public class GrowthByConsumptionFunction extends AbstractIBMFunction implements IBMGrowthFunctionInterface {
    
    /** function classification */
    public static final String DEFAULT_type = "individual growth";
    /** user-friendly function name */
    public static final String DEFAULT_name = "Growth-by-consumption function";
    /** function description */
    public static final String DEFAULT_descr = "A consumption/temperature-dependent growth function.";
    /** full description */
    public static final String DEFAULT_fullDescr = 
            "\n\t**************************************************************************"+
            "\n\t* This class provides an implementation of aconsumption-driven growth model for"+
            "\n\t*      Pacific ocean perch with additive random noise."+
            "\n\t* Type: "+
            "\n\t*      Consumption-driven growth"+
            "\n\t* Parameters (by key):"+
            "\n\t*  pCexp  - Double - coefficient in exponent for consumption rate"+
            "\n\t*  pLnQ10 - Double - ln-scale Q10 value"+
            "\n\t*  pOnT   - flag (0/1) to include temperature effects"+
            "\n\t*  pT0    - Double - nominal temperature to use"+
            "\n\t*  sigOGV - Double  - std. deviation in random component for consumption"+
            "\n\t*  pMaxAE - Double  - max  assimilation efficiency"+
            "\n\t*  pB     - Double - coefficient in exponent for max assimilation efficiency"+
            "\n\t*  pSDAE      - Double - assumed constant for SDA+E"+
            "\n\t*  pMetRstNum -Double - numerator coefficient for routine metabolism"+
            "\n\t*  pMetRstDen - Double - denominator coefficient for routine metabolism"+
            "\n\t*  pActMult   - Double - activity multiplier for active metabolism"+
            "\n\t* Variables:"+
            "\n\t*      vars - double[]{W,T,L}, where"+
            "\n\t*          W - double - weight (micrograms)"+
            "\n\t*          T - double - temperature (deg C)"+
            "\n\t*          L - double - flag (0/1) indicating daytime (1) or nighttime (0)"+
            "\n\t* Value:"+
            "\n\t*      ogv - Double - initial oil globule volume"+
            "\n\t* Calculation:"+
            "\n\t*   if (stdvCons>0) rnd = rng.computeNormalVariate(); //random number"+
            "\n\t*   C = 10.0^[pCexp*log10(W)-0.27];                 //consumption rate, without temperature dependence"+
            "\n\t*   if (pOnT) C = C*exp[pLnQ10/10.0*(T-pT0)];       //consumption rate w/ temperature dependence"+
            "\n\t*   C = C+rnd*stdvCons;                             //random effect (TODO: stdvCons should be adjusted for bio time step)"+
            "\n\t*   AE     = pMaxAE*[1.0-0.25*exp(-pB*(W-10))];     //assimilation efficiency"+
            "\n\t*   metRst = (pMetRstNum*W)/(pMetRstDen+W);         //resting metabolism"+
            "\n\t*   metAct = pActMult*metRst;                       //active metabolism"+
            "\n\t*   TC     = (1-L)*metRst + L*metAct + C*pSDAE;     //rate (micrograms/day) of total metabolic costs [pSDAE = SDA + E]"+
            "\n\t*   G      = (C * AE) - TC;                         //growth rate in micrograms/day"+
            "\n\t* "+
            "\n\t* author: William.Stockhausen"+
            "\n\t**************************************************************************";
    /** random number generator */
    protected static final RandomNumberGenerator rng = GlobalInfo.getInstance().getRandomNumberGenerator();

    /** number of settable parameters */
    public static final int numParams = 10;
    /** number of sub-functions */
    public static final int numSubFuncs = 0;

    /** key to set the exponential coefficient for consumption */
    public static final String PARAM_Cexp = "pCexp";
    /** key to set the maternal age multiplier */
    public static final String PARAM_OnT = "pOnT";
    /** key to set ln-scale Q10 */
    public static final String PARAM_LnQ10 = "pLnQ10";
    /** key to set temperature baseline */
    public static final String PARAM_T0 = "pT0";
    /** key to set standard deviation parameter */
    public static final String PARAM_stdvCons = "stdvCons";
    
    /** key to set the max assimilation efficiency */
    public static final String PARAM_MaxAE = "maxAE";
    /** key to set shape parameter ("b") for the assimilation efficiency  */
    public static final String PARAM_B = "pB";
    
    /** key to set the numerator parameter for resting metabolism */
    public static final String PARAM_MetRstNum = "pMetRstNum";
    /** key to set the denominator parameter for resting metabolism */
    public static final String PARAM_MetRstDen = "pMetRstDen";
    /** key to set the multiplier for active metabolism */
    public static final String PARAM_ActMult = "pActMult";
    
    /** key to set the SDA + E constant */
    public static final String PARAM_SDAE = "pSDAE";
    
    /** flag to print debugging info */
    public static boolean debug = true;
    /** logger for class */
    private static final Logger logger = Logger.getLogger(GrowthByConsumptionFunction.class.getName());
    
    /** value of exponential consumption rate parameter */
    private double pCexp = 0.894;//default value
    /** value of the flag turning on temperature dependence */
    private boolean pOnT = false;
    /** value of the ln-scale Q10 */
    private double pLnQ10 = 2.0;
    /** value of the baseline temperature */
    private double pT0 = 4.5;
    /** value of the standard deviation parameter */
    private double stdvCons = 0;
    
    /** max assimilation efficiency parameter */
    private double pMaxAE = 0.80;//default value
    /** shape parameter for max assimilation efficiency */
    private double pB = 0.002;//default value

    /** numerator parameter for resting metabolism */
    private double pMetRstNum = 4500.0;//default value
    /** denominator parameter for resting metabolism */
    private double pMetRstDen = 45000.0;//default value
    /** active metabolism multiplier */
    private double pActMult = 2.5;//default value
    
    /** value for (SDA + E) constant */
    private double pSDAE = 0.30;//default value

    /** constructor for class */
    public GrowthByConsumptionFunction(){
        super(numParams,numSubFuncs,DEFAULT_type,DEFAULT_name,DEFAULT_descr,DEFAULT_fullDescr);
        String key; 
        key = PARAM_Cexp;    addParameter(key,Double.class, "exponential coefficient for consumption rate");
        setParameterValue(key, pCexp);
        key = PARAM_OnT;     addParameter(key,Boolean.class, "include temperature dependence?");
        setParameterValue(key, pOnT);
        key = PARAM_LnQ10;   addParameter(key,Double.class, "ln-scale Q10");
        setParameterValue(key, pLnQ10);
        key = PARAM_T0;      addParameter(key,Double.class, "baseline temperature (deg C)");
        setParameterValue(key, pT0);
        key = PARAM_stdvCons;addParameter(key,Double.class, "std. dev. of consumption rate");
        setParameterValue(key, stdvCons);
        
        key = PARAM_MaxAE;  addParameter(key,Double.class, "maximum asssimilation efficiency");
        setParameterValue(key, pMaxAE);
        key = PARAM_B;      addParameter(key,Double.class, "shape parameter for assimilation efficiency");
        setParameterValue(key, pB);
        
        key = PARAM_MetRstNum;  addParameter(key,Double.class, "numerator coefficient for resting metabolism");
        setParameterValue(key, pMetRstNum);
        key = PARAM_MetRstDen;  addParameter(key,Double.class, "denominator coefficient for resting metabolism");
        setParameterValue(key, pMetRstDen);
        key = PARAM_ActMult;    addParameter(key,Double.class, "active metabolism multiplier");
        setParameterValue(key, pActMult);
        
        key = PARAM_SDAE;  addParameter(key,Double.class, "constant fraction for SDA+E");
        setParameterValue(key, pSDAE);
    }
    
    @Override
    public GrowthByConsumptionFunction clone(){
        GrowthByConsumptionFunction clone = new GrowthByConsumptionFunction();
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
                case PARAM_Cexp:
                    pCexp = ((Double) value).doubleValue();
                    break;
                case PARAM_OnT:
                    pOnT = ((Boolean) value).booleanValue();
                    break;
                case PARAM_LnQ10:
                    pLnQ10 = ((Double) value).doubleValue();
                    break;
                case PARAM_T0:
                    pT0 = ((Double) value).doubleValue();
                    break;
                case PARAM_stdvCons:
                    stdvCons = ((Double) value).doubleValue();
                    break;
                case PARAM_MaxAE:
                    pMaxAE = ((Double) value).doubleValue();
                    break;
                case PARAM_B:
                    pB = ((Double) value).doubleValue();
                    break;
                case PARAM_MetRstNum:
                    pMetRstNum = ((Double) value).doubleValue();
                    break;
                case PARAM_MetRstDen:
                    pMetRstDen = ((Double) value).doubleValue();
                    break;
                case PARAM_ActMult:
                    pActMult = ((Double) value).doubleValue();
                    break;
                case PARAM_SDAE:
                    pSDAE = ((Double) value).doubleValue();
                    break;
            }
        }
        return false;
    }

    /**
     * Calculates the value of the function, given the current parameter params 
     * and the input variables.
     * 
     * @param vars - the input variables, individual weight and in situ temperature, as a double[].
     * <ul>
     *      <li> wgt - individual weight, in UNITS??
     *      <li> tmp - in situ temperature
     *      <li> lgt - flag indicating daytime (=1) or nighttime (=0)
     * </ul>
     * @return  - double[] with the elements
     * <ul>
     *      <li> the daily growth rate, in micrograms/day 
     *      <li> the consumption rate, in micrograms/day 
     *      <li> the assimilation efficiency 
     *      <li> the resting metabolism rate, in micrograms/day 
     *      <li> the active metabolism rate, in micrograms/day
     *      <li> the rate of total metabolic costs, in micrograms/day 
     * </ul>
     */
    @Override
    public double[] calculate(Object vars) {
        double[] lvars = (double[]) vars;//cast object to required double[]
        int i = 0;
        double wgt = lvars[i++]; //individual weight
        double tmp = lvars[i++]; //temperature
        double lgt = lvars[i++]; //flag indicating daytime (=1) or nighttime (=0)
        double rnd = 0; 
        if (stdvCons>0) rnd = rng.computeNormalVariate(); 
        double rtCons = Math.pow(10.0, pCexp*Math.log10(wgt)-0.27);      //consumption rate, without temperature dependence
        if (pOnT) rtCons *= Math.exp(pLnQ10/10.0*(tmp-pT0));             //consumption rate w/ temperature dependence
        rtCons += rnd*stdvCons;                                          //random effect (TODO: stdvCons should be adjusted for bio time step)
        double AE     = pMaxAE*(1.0-0.25*Math.exp(-pB*(wgt-10)));        //assimilation efficiency
        double metRst = (pMetRstNum*wgt)/(pMetRstDen+wgt);               //resting metabolism
        double metAct = pActMult*metRst;                                 //active metabolism
        double rtTC   = (1-lgt)*metRst + lgt*metAct + pSDAE*rtCons;      //rate of total metabolic costs [pSDAE = SDA + E]
        double rtGrw  = (rtCons * AE) - rtTC;                            //growth rate in micrograms/day
        if (debug){
            logger.info("\n--W: "+wgt+"; T: "+tmp+"; L: "+lgt); 
            logger.info("--pCexp     : "+pCexp   +";pLnQ10     : "+pLnQ10    +"; pT0   : "+pT0+"; rnd: "+rnd*stdvCons+"; rtCons: "+rtCons);
            logger.info("--pMaxAE    : "+pMaxAE    +"; pB        : "+pT0       +", AE    : "+AE);
            logger.info("--pMetRstNum: "+pMetRstNum+"; pMetRstDen: "+pMetRstDen+", metRst: "+metRst);
            logger.info("--pActMult  : "+pActMult  +"; light     : "+lgt       +", metAct: "+metAct);
            logger.info("--pSDAE     : "+pSDAE     +"; rtTC      : "+rtTC      +"; rtGrw : "+rtGrw);
        }
        double[] res = new double[]{rtGrw,rtCons,AE,metRst,metAct,rtTC};
        return res;
    }
}
