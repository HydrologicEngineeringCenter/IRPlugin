package alteditor;

import hec2.model.DataLocation;
import hec2.model.DataLocationComputeType;
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
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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

    private IRAlt _irAlt;

    public void set_irAlt(IRAlt _irAlt) {
        this._irAlt = _irAlt;
    }


    public void initlocations() {
//        simulatiing initilizing locations
        ObservableList<EditorEvalLocs> locations = FXCollections.observableArrayList();
        List<EvaluationLocation> locs = _irAlt.get_evalLocs();
        if (locs != null) {
            for (EvaluationLocation loc : locs) {
                String name = loc.get_location().getName();
                Integer val = loc.get_evalValue();
                String operator = loc.get_operator();
                String action = loc.get_actions();
                String message = loc.get_actionMessage();
                locations.add(new EditorEvalLocs(name, val, operator, action, message));
            }
            this._locations = locations;
        } else {
            this._locations = locations;
            newLocation();
        }
        altLabel.setText(_irAlt.getName());
        tableView.setItems(_locations);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        altLabel.setText("");
        nameColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("name"));
        Label l = new Label("Location Name");
        l.setTooltip(new Tooltip("Name of the Data Location"));
        nameColumn.setGraphic(l);

        valueColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, Integer>("value"));
        Label v = new Label("Threshold Value");
        v.setTooltip(new Tooltip("Integer value to trigger action"));
        valueColumn.setGraphic(v);

        operatorColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("operator"));
        Label o = new Label("Operator");
        o.setTooltip(new Tooltip("Currently is not used\n In future will be either: 'GreaterThan' or 'LessThan'" ));
        operatorColumn.setGraphic(o);

        actionColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("actions"));
        Label a = new Label("Action");
        a.setTooltip(new Tooltip("Action performed when condition is met\n Current Options: 'ShowMessage' or 'ShowDialog'" ));
        actionColumn.setGraphic(a);

        messageColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("message"));
        Label m = new Label("Message");
        m.setTooltip(new Tooltip("Message displayed when condition is met" ));
        messageColumn.setGraphic(m);

        tableView.setEditable(true);
//Making the Columns Editable
//---------------------------------------------------------------------------------------------------------------------------
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<EditorEvalLocs, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<EditorEvalLocs, String> t) {
                        (t.getTableView().getItems().get(
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
                        (t.getTableView().getItems().get(
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
                        (t.getTableView().getItems().get(
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
                        (t.getTableView().getItems().get(
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
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setMessage(t.getNewValue());
                    }
                }
        );
//---------------------------------------------------------------------------------------------------------------------------
    }

    public void newLocation() {
        _locations.add(new EditorEvalLocs("SetThisValue", 0, "GreaterThan", "ShowMessage,ShowDialog", "SetThisValue"));

    }

    public void close() {
        exit();

    }

    public void saveLocations() {
        ArrayList<EvaluationLocation> newEvalLocs = new ArrayList<>();
        ArrayList<DataLocation> newDls = new ArrayList<>();
//        Creating Evaluation Locations from EditorEvalLocs
        for (EditorEvalLocs editorEvalLocs : _locations) {
            String name = editorEvalLocs.getName();
            Integer val = editorEvalLocs.getValue();
            String operator = editorEvalLocs.getOperator();
            String action = editorEvalLocs.getActions();
            String message = editorEvalLocs.getMessage();
            DataLocation dloc = new DataLocation(_irAlt.getModelAlt(), name, "Any");
            dloc.setComputeType(DataLocationComputeType.Computed);
            newDls.add(dloc);
            newEvalLocs.add(new EvaluationLocation(dloc, val, operator, action, message));
        }
//        Setting evaluation Locations and Data Locations to the IRalt.
        _irAlt.set_evalLocs(newEvalLocs);
        List<DataLocation> altDLocs = _irAlt.get_dataLocations();
//        If there are existing data locations, check names and don't add duplicates.
        if (altDLocs.size() > 0) {
            List<String> Dlnames = new ArrayList<>();
            for (DataLocation altDl : altDLocs) {
                Dlnames.add(altDl.getName());
            }
            for (EvaluationLocation e : newEvalLocs) {
                for (String dlname : Dlnames) {
                    if (!(e.get_location().getName().equals(dlname))) {
                        altDLocs.add(e.get_location());
                    }
                }
            }
        }
//        If there are no Data Locations yet, (new alt) add all the Data Locations. Use distinct to avoid duplication.
        else {
            List<DataLocation> distinctElements = newDls.stream()
                    .distinct()
                    .collect(Collectors.toList());
            altDLocs.addAll(distinctElements);
        }
            _irAlt.saveData();

        }
    }

