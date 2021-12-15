
package controllers;

import exceptions.GenericException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Module;
import org.hibernate.service.spi.ServiceException;
import repositories.UserRepository;
import services.AuthenticationService;

import java.io.IOException;
import java.net.URISyntaxException;

public class LoginScreenController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    void login(ActionEvent event) {

        AuthenticationService authenticationService = AuthenticationService.getInstance();
        UserRepository userRepository = new UserRepository();

        String username = usernameField.getText().trim().toLowerCase();
        String password = passwordField.getText().trim();

        try {
            authenticationService.login(username,password);
            authenticationService.setUser(userRepository.getUserByUsername(username));

            if(authenticationService.getUser().isPresent()){
                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(this.getClass().getResource(Module.MAIN_SCREEN.getResource()));
                primaryStage.setTitle(Module.MAIN_SCREEN.getTitle());
                primaryStage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
                primaryStage.setScene(new Scene(root, 1495.0, 758.0));
                primaryStage.setResizable(false);
                primaryStage.show();
            }

            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.close();

        } catch (GenericException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }catch (ServiceException e){
            new Alert(Alert.AlertType.ERROR, "It seems like your database is off. Please contact your administrator").showAndWait();
        }
    }

}
