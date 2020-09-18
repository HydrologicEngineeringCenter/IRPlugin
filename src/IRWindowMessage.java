import com.rma.client.Browser;

import javax.swing.*;

public class IRWindowMessage implements Action
{
    public IRWindowMessage(){

    }

    public boolean computeAction(IRAlt alt, EvaluationLocation eloc, String time) {
        JOptionPane.showMessageDialog(Browser.getBrowserFrame(), (" Exceeded Threshold  " + eloc.get_evalValue() + " at: \n" + eloc.get_location().getName() + " " + eloc.get_location().getModelToLinkTo() + " at time " + time), "IR Message", JOptionPane.NO_OPTION);
        return false;
    }
}
