package alteditor;

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

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.application.Platform.exit;

public class Controller implements Initializable {
    //    configeure Table
    @FXML
    private TableView<EditorEvalLocs> tableView;
    @FXML
    private TableColumn<EditorEvalLocs, String> nameColumn;
    @FXML
    private Label altLabel;
    @FXML
    private TableColumn<EditorEvalLocs, String> valueColumn;
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


    public  void initlocations() {
//        simulatiing initilizing locations
        ObservableList<EditorEvalLocs> locations = FXCollections.observableArrayList();
        locations.add(new EditorEvalLocs("Healdsburg", "10", "GreaterThan", "ShowDialog", "Bridge needs to be closed."));
        locations.add(new EditorEvalLocs("Healdsburg", "15", "GreaterThan", "ShowDialog", "Campground Evacuation"));
        locations.add(new EditorEvalLocs("Feliz Ck", "25", "GreaterThan", "ShowMessage", "Levee Overtopping"));
        this._locations = locations;

    }


        @Override
        public void initialize (URL location, ResourceBundle resources){
            altLabel.setText("Alternative Name");
            nameColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("name"));
            valueColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("value"));
            operatorColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("operator"));
            actionColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("actions"));
            messageColumn.setCellValueFactory(new PropertyValueFactory<EditorEvalLocs, String>("message"));
            initlocations();
            tableView.setItems(_locations);
            tableView.setEditable(true);

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
            valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            valueColumn.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<EditorEvalLocs, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<EditorEvalLocs, String> s) {
                            ((EditorEvalLocs) s.getTableView().getItems().get(
                                    s.getTablePosition().getRow())
                            ).setValue(s.getNewValue());
                        }
                    }
            );
            operatorColumn.setCellFactory(TextFieldTableCell.forTableColumn());
            operatorColumn.setOnEditCommit(
                    new EventHandler<TableColumn.CellEditEvent<EditorEvalLocs, String>>() {
                        @Override
                        public void handle(TableColumn.CellEditEvent<EditorEvalLocs, String> s) {
                            ((EditorEvalLocs) s.getTableView().getItems().get(
                                    s.getTablePosition().getRow())
                            ).setOperator(s.getNewValue());
                        }
                    }
            );
        }
        public void newLocation () {
            _locations.add(new EditorEvalLocs("SetThisValue", "SetThisValue", "SetThisValue", "SetThisValue", "SetThisValue"));


        }
        public void close () {
            exit();

        }
    }