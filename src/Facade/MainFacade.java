package Facade;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import model.ReadWriteXML;
import model.hour;
import model.metroStation;
import tableCell.textFieldTableCell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainFacade {
    private static MainFacade instance = null;

    private MainFacade(){}

    public static MainFacade getInstance(){
        if(instance == null){
            instance = new MainFacade();
        }
        return instance;
    }


    TextField nameOfStation;
    TextField yearOfOpening;
    Label name;
    Label year;
    TableView<hour> table;
    TableColumn<hour, String> commentColumn;
    TableColumn<hour, Integer> oclockColumn;
    TableColumn<hour, Integer> numOfPassColumn;


    private ObservableList<hour> observableList;
    private metroStation current = null;

    public void initAllFields(TextField nameOfStation, TextField yearOfOpening, Label name, Label year,
                              TableView<hour> table, TableColumn<hour, String> commentColumn,
                              TableColumn<hour, Integer> oclockColumn, TableColumn<hour, Integer> numOfPassColumn){
        this.nameOfStation = nameOfStation;
        this.yearOfOpening = yearOfOpening;
        this.name = name;
        this.year = year;
        this.table = table;
        this.commentColumn = commentColumn;
        this.oclockColumn = oclockColumn;
        this.numOfPassColumn = numOfPassColumn;
    }

    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Помилка");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void saveXml() {
        if(current != null) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save to xml");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("XML", "*.xml")
            );
            File file;
            if ((file = fileChooser.showSaveDialog(null)) != null) {
                try {
                    // updateSourceData(); // оновлюємо дані в моделі
                    ReadWriteXML rw = new ReadWriteXML(current);
                    rw.WriteXML(file.getCanonicalPath());
                    showMessage("Результати успішно збережені");
                } catch (Exception e) {
                    showError("Помилка запису в файл");
                }
            }
        }else{
            showError("No file to save!");
        }
    }

    public void getXml() {
        ReadWriteXML rw = new ReadWriteXML();
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("XML", "*.xml")

        );
        File selectedFile = fileChooser.showOpenDialog(stage);
        current = rw.readXML(selectedFile);
        updateTable();
    }

    public void about() {
        //графический контейнер
        Stage stage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/about.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }


    public void minPasHour() {
        hour min = current.getHoursElement(0);
        for(hour item:current.getHours()){
            if(min.getNumberOfPassenger() > item.getNumberOfPassenger())
                min = item;
        }
        for(hour item:current.getHours()){
            if(min.getNumberOfPassenger().equals(item.getNumberOfPassenger())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Min passenger hour");
                alert.setHeaderText(null);
                alert.setContentText("Min hour is:\nOclock: " + min.getOclock()+"\nNumber Of passenger: "
                        + min.getNumberOfPassenger() + "\nComment: " + min.getComment());
                alert.show();
            }
        }
    }

    public void maxCommWords() {
        hour max = current.getHoursElement(0);
        for(hour item:current.getHours()){
            if(max.countOfWords() < item.countOfWords())
                max = item;
        }
        for(hour item:current.getHours()) {
            if (max.countOfWords().equals(item.countOfWords())) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Max Commentary Words");
                alert.setHeaderText(null);
                alert.setContentText("Max hour is:\nOclock: " + max.getOclock() + "\nNumber Of passenger: "
                        + max.getNumberOfPassenger() + "\nComment: " + max.getComment());
                alert.show();
            }
        }
    }

    public void sumOfPas() {
        Integer sum = 0;
        for(hour item:current.getHours()){
            sum += item.getNumberOfPassenger();
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SumOfPassenger");
        alert.setHeaderText(null);
        alert.setContentText("Sum = "+sum);
        alert.showAndWait();
    }

    public void sortByPass() {
        current.sortByPasenger();
        updateTable();
    }

    public void sortByComm() {
        current.sortByCommentWords();
        updateTable();
    }

    public void enterData(TextField nameOfStation, TextField yearOfOpening, Label name, Label year) {
        current = new metroStation(nameOfStation.getText(),Integer.parseInt(yearOfOpening.getText()));
        updateTable();
        name.setText(nameOfStation.getText());
        year.setText(yearOfOpening.getText());
    }

    private void updateTable(){
        List<hour> list = new ArrayList<>();
        observableList = FXCollections.observableList(list);
        for (int i = 0; i < 24; i++)
            list.add(current.getHoursElement(i));
        numOfPassColumn.setEditable(true);
        commentColumn.setEditable(true);
        oclockColumn.setEditable(true);
        table.setEditable(true);

        numOfPassColumn.setCellValueFactory(new PropertyValueFactory<hour, Integer>("NumberOfPassenger"));
        numOfPassColumn.setCellFactory(textFieldTableCell.forTableColumn(new IntegerStringConverter()));
        numOfPassColumn.setOnEditCommit(t -> updatePass(t));

        commentColumn.setCellValueFactory(new PropertyValueFactory<hour, String>("Comment"));
        commentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        commentColumn.setOnEditCommit(t -> updateComm(t));

        oclockColumn.setCellValueFactory(new PropertyValueFactory<hour, Integer>("Oclock"));

        table.setItems(observableList);

        name.setText(current.getName());
        year.setText(Integer.toString(current.getYearOfOpening()));
    }

    private void updateComm(TableColumn.CellEditEvent<hour,String> t){
        hour h = (hour) t.getTableView().getItems().get(t.getTablePosition().getRow());
        h.setComment(t.getNewValue());
    }

    private void updatePass(TableColumn.CellEditEvent<hour,Integer> t){
        hour h = (hour) t.getTableView().getItems().get(t.getTablePosition().getRow());
        h.setNumberOfPassenger(t.getNewValue());

    }
}
