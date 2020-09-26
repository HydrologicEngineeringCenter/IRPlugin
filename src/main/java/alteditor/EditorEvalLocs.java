package alteditor;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class EditorEvalLocs {
    private SimpleStringProperty name;
    private SimpleIntegerProperty value;
    private SimpleStringProperty operator;
    private SimpleStringProperty actions;
    private SimpleStringProperty message;

    public String getName() {
        return name.get();
    }



    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public Integer getValue() {
        return value.get();
    }

    public void setValue(Integer value) {
        this.value= new SimpleIntegerProperty(value);
    }

    public String getOperator() {
        return operator.get();
    }


    public void setOperator(String operator) {
        this.operator= new SimpleStringProperty(operator);
    }

    public String getActions() {
        return actions.get();
    }


    public void setActions(String actions) {
        this.actions= new SimpleStringProperty(actions);
    }

    public String getMessage() {
        return message.get();
    }

    public void setMessage(String message) {
        this.message= new SimpleStringProperty(message);
    }

    public EditorEvalLocs(String name, Integer value, String operator, String  action, String message){
        setName(name);
        setValue(value);
        setOperator(operator);
        setActions(action);
        setMessage(message);


    }
}
