package Controllers;


import Facade.MainFacade;
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
import model.*;

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

    MainFacade facade = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Записуємо порожній рядок замість "No content in table":
        table.setPlaceholder(new Label("Now Is empty!"));
        yearOfOpening();
        facade = MainFacade.getInstance();
        facade.initAllFields(nameOfStation, yearOfOpening,name, year, table,
                commentColumn, oclockColumn, numOfPassColumn);
    }

    private void yearOfOpening(){
        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        yearOfOpening.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches())
                yearOfOpening.setText(oldValue);
        });
    }

    @FXML public void saveXml(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.save();
    }

    @FXML public void getXml(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.get();
    }

    @FXML public void about(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.about();
    }

    @FXML public void minPasHour(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.minPasHour();
    }

    @FXML public void maxCommWords(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.maxCommWords();

    }

    @FXML public void sumOfPas(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.sumOfPas();
    }

    @FXML public void sortByPass(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.sortByPass();
    }

    @FXML public void sortByComm(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.sortByComm();
    }

    @FXML public void enterData(ActionEvent actionEvent) {
        facade = MainFacade.getInstance();
        facade.enterData(nameOfStation, yearOfOpening, name, year);
    }
}
