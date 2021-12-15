package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Group;
import models.Subject;
import services.GroupService;
import services.SubjectsService;

import java.net.URISyntaxException;

public class EditGroupScreenController {
    private Group group;
    @FXML
    private TextField groupName;

    @FXML
    private Label errorMessage;

    void loadGroup(Group group){
        groupName.setText(group.getName());
        this.group = group;
    }

    @FXML
    void edit(ActionEvent event) {
        GroupService groupService = new GroupService();
        try{
            group.setName(groupName.getText());
            groupService.editGroup(group);
            close();
        }catch (GenericException e){
            errorMessage.setText(e.getMessage());
        }
    }

    void close()  {
        Stage stage = (Stage) groupName.getScene().getWindow();
        stage.close();
    }

}
