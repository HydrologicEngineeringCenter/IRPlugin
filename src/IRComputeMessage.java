import hec.heclib.util.HecTime;

public class IRComputeMessage implements Action {


    public boolean computeAction(IRAlt alt, EvaluationLocation eloc, String time) {
        alt.addComputeMessage(" Exceeded Threshold  " + eloc.get_evalValue() + " at: \n" + eloc.get_location().getName() + " " + eloc.get_location().getModelToLinkTo() + " at time " + time);
        return false;
    }
}
