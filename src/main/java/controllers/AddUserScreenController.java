package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Role;
import models.Subject;
import models.User;
import repositories.SubjectRepository;
import services.SubjectsService;
import services.UsersService;

import javax.jws.soap.SOAPBinding;
import java.net.URISyntaxException;
import java.util.Arrays;

public class AddUserScreenController {

    @FXML
    private TextField userFullName;

    @FXML
    private Label errorMessage;

    @FXML
    private TextField userUsername;

    @FXML
    private TextField userPassword;

    @FXML
    private ChoiceBox<Role> userRole;

    @FXML
    private ChoiceBox<Subject> userSubject;

    @FXML
    void add(ActionEvent event) {
        UsersService usersService = new UsersService();
        User user = new User();
        try {
            user.setUsername(userUsername.getText());
            user.setFullName(userFullName.getText());
            user.setSubject(userSubject.getValue());
            user.setRole(userRole.getValue());
            user.setPassword(userPassword.getText());

            usersService.addUser(user);

            Stage stage = (Stage) userPassword.getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.close();
        } catch (GenericException | URISyntaxException e) {
            errorMessage.setText(e.getMessage());
        }
    }

    public void initialize(){
        Arrays.stream(Role.values()).forEach(role -> {
            userRole.getItems().add(role);
        });
        userRole.getSelectionModel().selectFirst();
        SubjectRepository subjectRepository = new SubjectRepository();

        subjectRepository.listAll(Subject.class).forEach(subject -> {
            userSubject.getItems().add(subject);
        });
        userSubject.getSelectionModel().selectFirst();
    }

}
