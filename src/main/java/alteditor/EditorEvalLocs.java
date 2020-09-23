package alteditor;

import javafx.beans.property.SimpleStringProperty;

public class EditorEvalLocs {
    private SimpleStringProperty name;
    private SimpleStringProperty value;
    private SimpleStringProperty operator;
    private SimpleStringProperty actions;
    private SimpleStringProperty message;

    public String getName() {
        return name.get();
    }



    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value= new SimpleStringProperty(value);
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

    public EditorEvalLocs(String name, String value, String operator, String  action, String message){
        setName(name);
        setValue(value);
        setOperator(operator);
        setActions(action);
        setMessage(message);


    }
}
