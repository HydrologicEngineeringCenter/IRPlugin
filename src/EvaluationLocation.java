import com.rma.io.DssFileManagerImpl;
import hec.data.Parameter;

import hec.heclib.dss.DSSPathname;
import hec.heclib.dss.HecTimeSeries;
import hec.heclib.util.HecTime;
import hec.io.DSSIdentifier;
import hec.io.TimeSeriesContainer;
import hec.model.Operator;
import hec2.model.DataLocation;
import org.jdom.Element;

import java.util.ArrayList;

public class EvaluationLocation {
    private Integer _EvalValue;
    private String _operator;
    private String _action;
    private DataLocation _location;

    public Integer get_EvalValue() {
        return _EvalValue;
    }

    public String get_operator() {
        return _operator;
    }

    public String get_action() {
        return _action;
    }

    public DataLocation get_location() {
        return _location;
    }

    public EvaluationLocation(DataLocation location, Integer evalValue, String operator, String action) {
        _location = location;
        _EvalValue = evalValue;
        _operator = operator;
        _action = action;
    }

    public Element writeToXML() {
//        write an EvaluationLocation to xml elements
        Element ele = new Element("EvaluationLocation");
        ele.setAttribute("Value", get_EvalValue().toString());
        ele.setAttribute("Operator", get_operator().toString());
        ele.setAttribute("Action", get_action().toString());
        return ele;
    }

    ;

    public static EvaluationLocation readFromXML(Element ele, DataLocation loc) {
        //    create an EvaluationLocation from an existing XML alternative file
        Integer value = Integer.parseInt(ele.getAttribute("Value").getValue());
        String op = ele.getAttribute("Operator").getValue();
        String act = ele.getAttribute("Action").getValue();
        return new EvaluationLocation(loc, value, op, act);
    }

    public boolean compute(DSSIdentifier DSSid) {

        DSSPathname path = new DSSPathname(DSSid.getDSSPath());
        TimeSeriesContainer tsc = DssFileManagerImpl.getDssFileManager().readTS(DSSid, false);
        for (int i = 0; i < tsc.values.length; i++){
            System.out.println(tsc.values[i]);
            if (tsc.values[i] > _EvalValue) {
                int times = tsc.times[i];
                HecTime hectime = new HecTime();
                hectime.set(times);
                String humantime = hectime.dateAndTime();
                        System.out.println(" Exceeded Threshold  " + _EvalValue + " at " + this.get_location().getName() + " " + this.get_location().getModelToLinkTo() + " at time " +humantime);
                System.out.println("");
            }
        }
        return true;
    }
}
