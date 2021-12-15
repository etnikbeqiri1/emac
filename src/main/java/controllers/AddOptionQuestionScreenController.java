package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Exam;
import services.QuestionService;

import java.io.File;
import java.net.URISyntaxException;

public class AddOptionQuestionScreenController {

    @FXML
    private TextField question;

    @FXML
    private Label errorMessage;

    @FXML
    private Button imageButton;

    @FXML
    private TextField points;

    @FXML
    private ImageView image;

    @FXML
    private TextField optionA;

    @FXML
    private TextField optionB;

    @FXML
    private TextField optionC;

    @FXML
    private TextField optionD;

    @FXML
    private RadioButton radioButtonA;

    @FXML
    private RadioButton radioButtonC;

    @FXML
    private RadioButton radioButtonB;

    @FXML
    private RadioButton radioButtonD;

    private Exam exam;

    private String imageLocation;

    @FXML
    public void add(ActionEvent event) {
        try {
            QuestionService questionService = new QuestionService();
            questionService.addOptionQuestion(
                    question.getText(),
                    optionA.getText(),
                    optionB.getText(),
                    optionC.getText(),
                    optionD.getText(),
                    radioButtonA.isSelected(),
                    radioButtonB.isSelected(),
                    radioButtonC.isSelected(),
                    radioButtonD.isSelected(),
                    points.getText(),
                    imageLocation,
                    exam
            );
            Stage stage = (Stage) question.getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.close();
        } catch (GenericException | URISyntaxException e) {
            errorMessage.setText(e.getMessage());
        }
    }




    public void setExam(Exam exam) {
        this.exam = exam;
    }


    @FXML
    void selectImage(javafx.event.ActionEvent event) throws URISyntaxException {

        Stage stage = (Stage) question.getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter jpgExtensionFilter =
                new FileChooser.ExtensionFilter(
                        "JPEG (.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngExtensionFilter =
                new FileChooser.ExtensionFilter(
                        "PNG (.png)", "*.png");

        fileChooser.getExtensionFilters().add(jpgExtensionFilter);
        fileChooser.getExtensionFilters().add(pngExtensionFilter);
        fileChooser.setSelectedExtensionFilter(jpgExtensionFilter);
        File selectedFile = fileChooser.showOpenDialog(stage);
        try{
            imageLocation = selectedFile.getAbsolutePath();
            imageButton.setText(selectedFile.getName());
            Image imagee = new Image(selectedFile.toURI().toString());
            image.setImage(imagee);
        }catch(NullPointerException e){

        }
    }

}
