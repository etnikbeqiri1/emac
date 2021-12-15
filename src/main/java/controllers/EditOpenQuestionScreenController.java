package controllers;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Exam;
import models.Question;
import services.QuestionService;

public class EditOpenQuestionScreenController {



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

    private Question questionT;
    private Exam exam;
    private String imageLocation;

    public void loadQuestion(Question question){

        this.questionT = question;
        this.exam = question.getExam();

        this.question.setText(question.getText());
        this.linesOfAnswer.setText(Integer.toString(question.getAnswerRows()));
        this.answer.setText(question.getAnswer());
        this.points.setText(Integer.toString(question.getPoints()));
        if(question.getImage() != null) {
            File selectedFile = new File(question.getImage());
            if (selectedFile.exists() && selectedFile.isFile()) {
                Image imagee = new Image(selectedFile.toURI().toString());
                this.image.setImage(imagee);
                this.imageButton.setText(selectedFile.getName());
                this.imageLocation = selectedFile.getAbsolutePath();

            }
        }
    }

    @FXML
    void edit(ActionEvent event) {
        try {
            QuestionService questionService = new QuestionService();

            questionService.editOpenQuestion(this.questionT, question.getText(),
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

        imageLocation = selectedFile.getAbsolutePath();

        imageButton.setText(selectedFile.getName());
        Image imagee = new Image(selectedFile.toURI().toString());
        image.setImage(imagee);
    }


    @FXML
    void initialize() {
    }
}
