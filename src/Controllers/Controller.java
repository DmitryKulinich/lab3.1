package Controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import main.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import tableCell.textFieldTableCell;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class Controller implements Initializable {
    @FXML TextField nameOfStation;
    @FXML TextField yearOfOpening;
    @FXML Label name;
    @FXML Label year;
    @FXML TableView<hour> table = new TableView<>();
    @FXML TableColumn<hour, String> commentColumn;
    @FXML TableColumn<hour, Integer> oclockColumn;
    @FXML TableColumn<hour, Integer> numOfPassColumn;


    private ObservableList<hour> observableList;
    private metroStation current = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Записуємо порожній рядок замість "No content in table":
        table.setPlaceholder(new Label("Now Is empty!"));
        yearOfOpening();
    }

    private void yearOfOpening(){
        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        yearOfOpening.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches())
                yearOfOpening.setText(oldValue);
        });
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

    @FXML public void saveXml(ActionEvent actionEvent) {
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

    @FXML public void getXml(ActionEvent actionEvent) {
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

    @FXML public void about(ActionEvent actionEvent) {
        //графический контейнер
        Stage stage = new Stage();
        try {
            about(stage);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void about(Stage stage) throws IOException{
        //Загрузили ресурс файла
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/about.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void minPasHour(ActionEvent actionEvent) {
        hour min = current.getHoursElement(0);
        for(hour item:current.getHours()){
            if(min.getNumberOfPassenger() > item.getNumberOfPassenger())
                min = item;
        }
        for(hour item:current.getHours()){
            if(min.getNumberOfPassenger().equals(item.getNumberOfPassenger())){
                minPassHour(item);
            }
        }
    }
    private void minPassHour(hour min){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Min passenger hour");
        alert.setHeaderText(null);
        alert.setContentText("Min hour is:\nOclock: " + min.getOclock()+"\nNumber Of passenger: "
                + min.getNumberOfPassenger() + "\nComment: " + min.getComment());
        alert.show();
    }

    @FXML public void maxCommWords(ActionEvent actionEvent) {
        hour max = current.getHoursElement(0);
        for(hour item:current.getHours()){
            if(max.countOfWords() < item.countOfWords())
                max = item;
        }
        for(hour item:current.getHours()){
            if(max.countOfWords().equals(item.countOfWords()))
                maxCommWords(item);
        }

    }
    private void maxCommWords(hour max){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Max Commentary Words");
        alert.setHeaderText(null);
        alert.setContentText("Max hour is:\nOclock: " + max.getOclock()+"\nNumber Of passenger: "
                + max.getNumberOfPassenger() + "\nComment: " + max.getComment());
        alert.show();
    }

    @FXML public void sumOfPas(ActionEvent actionEvent) {
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

    @FXML public void sortByPass(ActionEvent actionEvent) {
        current.sortByPasenger();
        updateTable();
    }

    @FXML public void sortByComm(ActionEvent actionEvent) {
        current.sortByCommentWords();
        updateTable();
    }

    @FXML public void enterData(ActionEvent actionEvent) {
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

    private void updateComm(CellEditEvent<hour,String> t){
        hour h = (hour) t.getTableView().getItems().get(t.getTablePosition().getRow());
        h.setComment(t.getNewValue());
    }

    private void updatePass(CellEditEvent<hour,Integer> t){
        hour h = (hour) t.getTableView().getItems().get(t.getTablePosition().getRow());
        h.setNumberOfPassenger(t.getNewValue());

    }
}
