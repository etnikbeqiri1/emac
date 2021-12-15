package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import services.GroupService;
import services.SubjectsService;

import java.net.URISyntaxException;

public class AddGroupScreenController {
    @FXML
    private TextField groupName;

    @FXML
    private Label errorMessage;

    @FXML
    void add(ActionEvent event) {
        GroupService groupService = new GroupService();
        try {
            groupService.addGroup(groupName.getText());

            Stage stage = (Stage) groupName.getScene().getWindow();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));

            stage.close();
        } catch (GenericException | URISyntaxException e) {
            errorMessage.setText(e.getMessage());
        }
    }
}
