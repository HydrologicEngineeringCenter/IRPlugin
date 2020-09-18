import hec.heclib.util.HecTime;

public interface Action {
    public boolean computeAction(IRAlt alt, EvaluationLocation elloc, String time);

}
