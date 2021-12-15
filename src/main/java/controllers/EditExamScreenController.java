package controllers;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Exam;
import services.ExamsService;

public class EditExamScreenController {
    private Exam exam;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField examName;

    @FXML
    private Label errorMessage;

    @FXML
    void edit(ActionEvent event) {
        ExamsService examsService = new ExamsService();
        try {
            exam.setName(examName.getText());
            examsService.update(exam);
            Stage stage = (Stage) examName.getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.close();
        } catch (GenericException | URISyntaxException e) {
            errorMessage.setText(e.getMessage());
        }
    }

    public void loadExam(Exam ex){
        this.exam = ex;
        examName.setText(ex.getName());
    }

    @FXML
    void initialize()  {


    }
}
