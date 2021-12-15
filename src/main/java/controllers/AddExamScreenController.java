package controllers;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Exam;
import models.User;
import repositories.ExamRepository;
import services.AuthenticationService;
import services.ExamsService;

public class AddExamScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField examName;

    @FXML
    private Label errorMessage;

    @FXML
    void add(ActionEvent event) {
        try {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            ExamsService examsService = new ExamsService();
            Exam exam = new Exam();
            exam.setName(examName.getText());
            exam.setProfessor(user.get());
            examsService.addExam(exam);
            Stage stage = (Stage) examName.getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));

            stage.close();
        } catch (GenericException | URISyntaxException e) {
            errorMessage.setText(e.getMessage());
        }
    }

    @FXML
    void initialize() {

    }
}
