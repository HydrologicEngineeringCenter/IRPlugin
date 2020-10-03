package irplugin;

import com.rma.client.Browser;
import com.rma.io.DssFileManagerImpl;

import hec.heclib.dss.DSSPathname;
import hec.heclib.util.HecTime;
import hec.io.DSSIdentifier;
import hec.io.TimeSeriesContainer;
import hec2.model.DataLocation;
import org.jdom.Element;

import javax.swing.*;
import java.util.*;

public class EvaluationLocation {
    private Integer _evalValue;
    private String _operator;
    private String _actions;
    private DataLocation _location;
    private String _actionMessage;
    private String _name;

    public Integer get_evalValue() {
        return _evalValue;
    }

    public String get_operator() {
        return _operator;
    }

    public String get_actions() {
        return _actions;
    }

    public DataLocation get_location() {
        return _location;
    }

    public String get_actionMessage() {
        return _actionMessage;
    }

    public EvaluationLocation(DataLocation location, Integer evalValue, String operator, String actions, String actionMessage) {
        _location = location;
        _evalValue = evalValue;
        _operator = operator;
        _actions = actions;
        _actionMessage = actionMessage;
    }

    public Element writeToXML() {
//        write an EvaluationLocation to xml elements
        Element ele = new Element("EvaluationLocation");
        ele.setAttribute("Value", get_evalValue().toString());
        ele.setAttribute("Operator", get_operator().toString());
        ele.setAttribute("Actions", get_actions().toString());
        ele.setAttribute("AMessage", get_actionMessage().toString());
        return ele;
    }

    ;

    public static EvaluationLocation readFromXML(Element ele, DataLocation loc) {
        //    create an EvaluationLocation from an existing XML alternative file
        Integer value = Integer.parseInt(ele.getAttribute("Value").getValue());
        String op = ele.getAttribute("Operator").getValue();
        String act = ele.getAttribute("Actions").getValue();
        String actM = ele.getAttribute("AMessage").getValue();
        return new EvaluationLocation(loc, value, op, act, actM);
    }

    public CompResult compute(DSSIdentifier DSSid) {
        List<String> compMessages = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();
        Map<String,Double> reportTimeValue = new LinkedHashMap<>();
        boolean compSuccess = true;
        ArrayList<String> actList = parseActionsString(this.get_actions());
//        Need to place some checks on whether the data is there
        TimeSeriesContainer tsc = DssFileManagerImpl.getDssFileManager().readTS(DSSid, false);
        for (int i = 0; i < tsc.values.length; i++) {
            System.out.println(tsc.values[i]);
                if (tsc.values[i] > _evalValue) {
                    int times = tsc.times[i];
                    HecTime hectime = new HecTime();
                    hectime.set(times);
                    String humantime = hectime.dateAndTime();
                    reportTimeValue.put(humantime, tsc.values[i]);
                }
        }
        Map.Entry<String,Double> firstpair =  reportTimeValue.entrySet().iterator().next();
        for (String act : actList) {
            if (act.compareToIgnoreCase("ShowMessage") == 0) {
                compMessages.add("---------  Threshold value was exceeded--------- \n"
                        + "THRESHOLD VALUE: " + _evalValue + "\n"
                        + "TIME OF EXCEEDANCE:" + firstpair.getKey() + "\n"
                        + "ACTION MESSAGE: " + get_actionMessage() + "\n"
                        + "EVALUATION LOCATION: " + this.get_location().getName() + "\n"
                        + "MODEL ALTERNATIVE: " + this.get_location().getModelToLinkTo());
            } else if (act.compareToIgnoreCase("ShowDialog") == 0) {
                String label =("--------- Threshold value was exceeded---------- \n"
                        + "THRESHOLD VALUE: " + _evalValue + "\n"
                        + "TIME OF EXCEEDANCE:" + firstpair.getKey() + "\n"
                        + "ACTION MESSAGE: " + get_actionMessage() + "\n"
                        + "EVALUATION LOCATION: " + this.get_location().getName() + "\n"
                        + "MODEL ALTERNATIVE: " + this.get_location().getModelToLinkTo() + "\n");
                JOptionPane.showMessageDialog(Browser.getBrowserFrame(), label, "IR Message", JOptionPane.INFORMATION_MESSAGE);
            } else {
                errorMessages.add("'" + act + "'" + " is not a valid action");
                compSuccess = false;
            }
        }

        CompResult result = new CompResult.Builder()
                .compMessages(compMessages)
                .compSuccess(compSuccess)
                .errorMessages(errorMessages)
                .build();

        return result;
    }
    public static ArrayList<String> parseActionsString(String actions){
        String[] elements = actions.split(",");
        List<String> actionsList = Arrays.asList(elements);
        ArrayList<String> arrActions = new ArrayList<String>(actionsList);
        return arrActions;
    }
}
