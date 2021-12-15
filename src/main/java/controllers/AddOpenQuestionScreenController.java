package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Exam;
import models.Question;
import org.apache.commons.io.FileUtils;
import services.QuestionService;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Base64;

public class AddOpenQuestionScreenController {

    @FXML
    private TextField question;

    @FXML
    private Label errorMessage;

    @FXML
    private TextField linesOfAnswer;

    @FXML
    private Button imageButton;

    @FXML
    private TextField answer;

    @FXML
    private TextField points;

    @FXML
    private ImageView image;

    private Exam exam;

    private String imageLocation;

    public void setExam(Exam exam){
        this.exam = exam;
    }

    @FXML
    void add(ActionEvent event) {
        try {
            QuestionService questionService = new QuestionService();
            questionService.addOpenQuestion(question.getText(),
                                            linesOfAnswer.getText(),
                                            answer.getText(),
                                            points.getText(),
                                            imageLocation,
                                            exam);
            Stage stage = (Stage) question.getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.close();
        }catch (GenericException | URISyntaxException e){
            errorMessage.setText(e.getMessage());
        }
    }

    @FXML
    void selectImage(ActionEvent event) throws URISyntaxException {

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
