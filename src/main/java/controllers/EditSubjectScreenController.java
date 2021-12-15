package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Student;
import models.Subject;
import services.SubjectsService;

public class EditSubjectScreenController {

    private Subject subject;

    @FXML
    private TextField subjectName;

    @FXML
    private Label errorMessage;

    void loadSubject(Subject subject){
        subjectName.setText(subject.getName());
        this.subject = subject;
    }

    public void edit(ActionEvent actionEvent) {
        SubjectsService subjectsService = new SubjectsService();
        try{
            subject.setName(subjectName.getText());
            subjectsService.editSubject(subject);
            close();
        }catch (GenericException e){
            errorMessage.setText(e.getMessage());
        }
    }

    void close(){
        Stage stage = (Stage) subjectName.getScene().getWindow();
        stage.close();
    }
}
