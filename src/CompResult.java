import java.util.ArrayList;
import java.util.List;

public class CompResult {
    public boolean successfulCompute = false;
    public List<String> computeMessages = new ArrayList<>();
    public List<String> errorMessages = new ArrayList<>();


    private CompResult(){}

    public static class Builder {
        public boolean successfulCompute = false;
        public List<String> computeMessages = new ArrayList<>();
        public List<String> errorMessages = new ArrayList<>();

        public Builder compSuccess(boolean success){
            this.successfulCompute = success;
            return this;
        }
        public Builder compMessages(List<String> messages) {
            this.computeMessages = messages;
            return this;
        }
        public Builder errorMessages(List<String> emessages){
            this.errorMessages = emessages;
            return this;
        }
        public CompResult build(){
            CompResult result = new CompResult();
            result.computeMessages = this.computeMessages;
            result.successfulCompute = this.successfulCompute;
            result.errorMessages = this.errorMessages;
            return result;
        }
    }
}
