package controllers;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import models.Student;
import repositories.GroupRepository;
import services.GroupService;
import services.StudentService;

public class EditStudentScreenController {
    private Student student;
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
    private TextField studentPoints;

    @FXML
    void edit(ActionEvent event) {
        try {
            StudentService studentService = new StudentService();
            String userFullName = this.userFullName.getText();
            Group studentGroup = this.studentGroup.getSelectionModel().getSelectedItem();
            String studentPoints = this.studentPoints.getText();

            if(!helpers.StringHelpers.isValidInteger(studentPoints))
                errorMessage.setText("Points can only be numbers");

            Integer points = Integer.parseInt(studentPoints);
            this.student.setName(userFullName);
            this.student.setGroup(studentGroup);
            this.student.setPoints(points);
            studentService.editStudent(this.student);
            close();
        } catch (GenericException e) {
            errorMessage.setText(e.getMessage());
        }

    }

    void close() {
        Stage stage = (Stage) userFullName.getScene().getWindow();
        stage.close();
    }

    public void loadStudent(Student student){
        userFullName.setText(student.getName());

        if(student.getPoints() != null)
            studentPoints.setText(student.getPoints().toString());

        studentGroup.getSelectionModel().select(student.getGroup());

        this.student = student;
    }
    @FXML
    void initialize() {
        GroupService groupService = new GroupService();
        List<Group> groupList = groupService.myGroups();
        Iterator<Group> i = groupList.iterator();
        while(i.hasNext()){
            studentGroup.getItems().add(i.next());
        }


    }
}
