package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import services.SubjectsService;

import java.net.URISyntaxException;

public class AddSubjectScreenController {

    @FXML
    private TextField subjectName;

    @FXML
    private Label errorMessage;

    @FXML
    void add(ActionEvent event) {
        SubjectsService subjectsService = new SubjectsService();
        try {
            subjectsService.addSubject(subjectName.getText());

            Stage stage = (Stage) subjectName.getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.close();
        } catch (GenericException | URISyntaxException e) {
            errorMessage.setText(e.getMessage());
        }
    }

}
