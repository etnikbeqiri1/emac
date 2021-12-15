import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Module;
import models.Role;
import models.Subject;
import models.User;
import repositories.SubjectRepository;
import repositories.UserRepository;
import services.AuthenticationService;



public class EmacApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(this.getClass().getResource(Module.LOGIN.getResource()));
        primaryStage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
        primaryStage.setTitle(Module.LOGIN.getTitle());
        primaryStage.setScene(new Scene(root, 449.0, 417.0));
        primaryStage.setResizable(false);
        primaryStage.show();

    }
}
