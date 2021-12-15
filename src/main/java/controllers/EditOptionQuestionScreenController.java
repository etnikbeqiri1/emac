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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Exam;
import models.Question;
import services.QuestionService;

public class EditOptionQuestionScreenController {

    private Question questionT;
    private Exam exam;
    private String imageLocation;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

    @FXML
    void add(ActionEvent event) {

    }

    @FXML
    void edit(ActionEvent event) {
        try {
            System.out.println(question.getText()+" "+
                    optionA.getText()+" "+
                    optionB.getText()+" "+
                    optionC.getText()+" " +
                    optionD.getText()+ " "+
                    radioButtonA.isSelected()+" "+
                    radioButtonB.isSelected()+" "+
                    radioButtonC.isSelected()+" "+
                    radioButtonD.isSelected()+" "+
                    points.getText()+" "+
                    imageLocation+" "+
                    exam);
            QuestionService questionService = new QuestionService();
            questionService.editOptionQuestion(questionT,
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


    public void loadQuestion(Question question){
        this.questionT = question;
        this.exam = question.getExam();

        this.question.setText(question.getText());
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
        this.optionA.setText(question.getVariantA());
        this.optionB.setText(question.getVariantB());
        this.optionC.setText(question.getVariantC());
        this.optionD.setText(question.getVariantD());

        if(question.getAnswer().contains("a"))
            radioButtonA.setSelected(true);
        else if(question.getAnswer().contains("b"))
            radioButtonB.setSelected(true);
        else if(question.getAnswer().contains("c"))
            radioButtonC.setSelected(true);
        else if(question.getAnswer().contains("d"))
            radioButtonD.setSelected(true);


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
