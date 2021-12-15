package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Role;
import models.Subject;
import models.User;
import repositories.SubjectRepository;
import services.UsersService;

import java.util.Arrays;

public class EditUserScreenController {

    private User user;
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
    void edit(ActionEvent event) {
        try{
            UsersService usersService = new UsersService();

            user.setFullName(userFullName.getText());
            user.setUsername(userUsername.getText());
            user.setPassword(userPassword.getText());
            user.setRole(userRole.getValue());
            user.setSubject(userSubject.getValue());

            usersService.editUser(user);
            close();
        }catch (GenericException e){
            errorMessage.setText(e.getMessage());
        }


    }

    void loadUser(User user){

        userFullName.setText(user.getFullName());
        userUsername.setText(user.getUsername());
        userPassword.setText(user.getPassword());
        userRole.setValue(user.getRole());
        userSubject.setValue(user.getSubject());
        userUsername.setDisable(true);
        this.user = user;
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

    void close(){
        Stage stage = (Stage) userUsername.getScene().getWindow();
        stage.close();
    }

}
