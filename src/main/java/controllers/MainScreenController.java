package controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import exceptions.GenericException;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Module;
import models.User;
import services.AuthenticationService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

public class MainScreenController {

    @FXML
    private VBox vbox;

    @FXML
    private Pane subPanel;

    @FXML
    void vboxClick(MouseEvent event) {

    }

    public void initialize() {
        AuthenticationService authenticationService = AuthenticationService.getInstance();

        authenticationService.getUser().ifPresent(user -> user.getRole().getModules()
                .forEach(module -> {
                    Button btn = new Button(module.getTitle());
                    btn.setAlignment(Pos.CENTER);
                    btn.setBackground(Background.EMPTY);
                    Font font = new Font(20);
                    btn.setTextFill(Color.WHITE);
                    btn.setFont(font);
                    btn.setCursor(Cursor.HAND);
                    EventHandler<Event> eventHandler = e -> {
                        Parent window1;
                        try {
                            window1 = FXMLLoader.load(getClass().getResource(module.getResource()));
                            subPanel.getChildren().setAll(window1);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                    };

                    btn.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                    vbox.getChildren().add(btn);
                }));

                Button btn = new Button("Log Out");
                btn.setAlignment(Pos.CENTER);
                btn.setBackground(Background.EMPTY);
                Font font = new Font(20);
                btn.setTextFill(Color.WHITE);
                btn.setFont(font);
                btn.setCursor(Cursor.HAND);
                EventHandler<Event> eventHandler = e -> {
                    authenticationService.logOut();
                    Stage stage = (Stage) vbox.getScene().getWindow();
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource(Module.LOGIN.getResource()));
                        Stage primaryStage = new Stage();
                        primaryStage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
                        primaryStage.setTitle(Module.LOGIN.getTitle());
                        primaryStage.setScene(new Scene(root, 449.0, 417.0));
                        primaryStage.setResizable(false);
                        primaryStage.show();
                    } catch (IOException | URISyntaxException ioException) {
                        ioException.printStackTrace();
                    }
                    stage.close();



                };

                btn.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                vbox.getChildren().add(btn);

                User user = authenticationService.getUser().get();
                List<Module> modules = user.getRole().getModules();
                Parent window = null;
                try {
                    window = FXMLLoader.load(getClass().getResource(modules.get(0).getResource()));
                    subPanel.getChildren().setAll(window);
                } catch (IOException e) {
                    e.printStackTrace();
                }

    }

}
