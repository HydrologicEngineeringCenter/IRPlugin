package alteditor;

import hec2.model.DataLocation;
import irplugin.EvaluationLocation;
import irplugin.IRAlt;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.application.Platform.exit;

public class
Controller implements Initializable {
    //    configeure Table
    @FXML
    private TableView<EditorEvalLocs> tableView;
    @FXML
    private TableColumn<EditorEvalLocs, String> nameColumn;
    @FXML
    private Label altLabel;
    @FXML
    private TableColumn<EditorEvalLocs, Integer> valueColumn;
    @FXML
    private TableColumn<EditorEvalLocs, String> operatorColumn;
    @FXML
    private TableColumn<EditorEvalLocs, String> actionColumn;
    @FXML
    private TableColumn<EditorEvalLocs, String> messageColumn;

    private ObservableList<EditorEvalLocs> _locations;

    public ObservableList<EditorEvalLocs> get_locations() {
        return _locations;
    }

    private IRAlt _irAlt;

    public void set_irAlt(IRAlt _irAlt) {
        this._irAlt = _irAlt;
    }





    public  void initlocations() {
//        simulatiing initilizing locations
        ObservableList<EditorEvalLocs> locations = FXCollections.observableArrayList();
//        locations.add(new EditorEvalLocs("Healdsburg", "10", "GreaterThan", "ShowDialog", "Bridge needs to be closed."));
//        locations.add(new EditorEvalLocs("Healdsburg", "15", "GreaterThan", "ShowDialog", "Campground Evacuation"));
//        locations.add(new EditorEvalLocs("Feliz Ck", "25", "GreaterThan", "ShowMessage", "Levee Overtopping"));
        List<EvaluationLocation> locs = _irAlt.get_evalLocs();
        for (EvaluationLocation loc :locs){
            String name = loc.get_location().getName();
            Integer val = loc.get_evalValue();
            String operator = loc.get_operator();
            String action = loc.get_actions();
            String message = loc.get_actionMessage();
            locations.add(new EditorEvalLocs(name, val, operator, action,message));
        }

        this._locations = locations;
        tableView.setItems(_locations);

    }


        @Override
        public void initialize (URL location, ResourceBundle resources){
            altLabel.setText("Alternative Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("name"));
            valueColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, Integer>("value"));
            operatorColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("operator"));
            actionColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("actions"));
            messageColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("message"));
//            initlocations();
//            tableView.setItems(_locations);
            tableView.setEditable(true);
//Making the Columns Editabale
//---------------------------------------------------------------------------------------------------------------------------
            nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            nameColumn.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<EditorEvalLocs, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<EditorEvalLocs, String> t) {
                            ((EditorEvalLocs) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                        }
                    }
                    );

            valueColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            valueColumn.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<EditorEvalLocs, Integer>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<EditorEvalLocs, Integer> t) {
                            ((EditorEvalLocs) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setValue(t.getNewValue());
                        }
                    }
            );
            operatorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            operatorColumn.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<EditorEvalLocs, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<EditorEvalLocs, String> t) {
                            ((EditorEvalLocs) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setOperator(t.getNewValue());
                        }
                    }
            );
            actionColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            actionColumn.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<EditorEvalLocs, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<EditorEvalLocs, String> t) {
                            ((EditorEvalLocs) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setActions(t.getNewValue());
                        }
                    }
            );
            messageColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            messageColumn.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<EditorEvalLocs, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<EditorEvalLocs, String> t) {
                            ((EditorEvalLocs) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setMessage(t.getNewValue());
                        }
                    }
            );
//---------------------------------------------------------------------------------------------------------------------------
    }
        public void newLocation () {
            _locations.add(new EditorEvalLocs("SetThisValue", 0, "SetThisValue", "SetThisValue", "SetThisValue"));

        }
        public void close () {
            exit();

        }

        public void saveLocations (){
        ArrayList<EvaluationLocation> newEvalLocs = new ArrayList<>();
        ArrayList<DataLocation> newDls = new ArrayList<>();
        for (EditorEvalLocs editorEvalLocs : _locations){
            String name = editorEvalLocs.getName();
            Integer val = editorEvalLocs.getValue();
            String operator = editorEvalLocs.getOperator();
            String action = editorEvalLocs.getActions();
            String message = editorEvalLocs.getMessage();
            DataLocation dloc = new DataLocation( _irAlt.getModelAlt(),name, "Any");
            newDls.add(dloc);
            newEvalLocs.add(new EvaluationLocation(dloc, val,operator,action,message));
        }
        _irAlt.set_evalLocs(newEvalLocs);
//        List<DataLocation> altLocs = _irAlt.get_dataLocations();
//        for (DataLocation dl : newDls){
//            for (DataLocation altDl : altLocs){
//                if (!dl.getName().equals(altDl.getName())){
//                    altLocs.add(dl);
//                }
//            }
//        }
//        _irAlt.saveData();

        }
    }