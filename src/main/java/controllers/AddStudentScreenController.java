package controllers;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Group;
import models.Student;
import services.GroupService;
import services.StudentService;

public class AddStudentScreenController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField userFullName;

    @FXML
    private Label errorMessage;

    @FXML
    private ChoiceBox<Group> studentGroup;

    @FXML
    void add(ActionEvent event) {
        StudentService studentService = new StudentService();
        Student student = new Student();

        try {
            student.setName(userFullName.getText());
            student.setGroup(studentGroup.getSelectionModel().getSelectedItem());
            Stage stage = (Stage) userFullName.getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            studentService.addStudent(student);
            stage.close();
        } catch (GenericException | URISyntaxException e) {
           errorMessage.setText(e.getMessage());
        }
    }

    @FXML
    void initialize()   {
        List<Group> group = new GroupService().myGroups();
        Iterator<Group> iGroup = group.iterator();
        while(iGroup.hasNext()){
            Group next = iGroup.next();
            studentGroup.getItems().add(next);
        }
        studentGroup.getSelectionModel().selectFirst();

    }
}
