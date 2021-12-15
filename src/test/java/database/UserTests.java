package database;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Module;
import models.Role;
import models.Subject;
import models.User;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;
import repositories.SubjectRepository;
import repositories.UserRepository;

import static org.assertj.core.api.Assertions.*;

public class UserTests extends ApplicationTest {

    private UserRepository userRepository;
    private SubjectRepository subjectRepository;


    public UserTests() {
        userRepository = new UserRepository();
        subjectRepository = new SubjectRepository();
    }

    @Test
    public void createAdminUser() {
        Subject subject = new Subject();
        subject.setName("NONE");
        Subject createdSubject = subjectRepository.save(subject);

        User user = new User();
        user.setPassword("123456");
        user.setFullName("Salep Lemaja");
        user.setSubject(createdSubject);
        user.setUsername("slemaja" + System.nanoTime());
        user.setRole(Role.ADMIN);
        User createdUser = userRepository.save(user);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getFullName()).isEqualTo(user.getFullName());
        assertThat(createdUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(createdUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(createdUser.getRole()).isEqualTo(user.getRole());
    }

    @Test
    public void createNonAdminUser() {
        Subject subject = new Subject();
        subject.setName("NONE");
        Subject createdSubject = subjectRepository.save(subject);

        User user = new User();
        user.setPassword("123456");
        user.setFullName("Salep Lemaja");
        user.setSubject(createdSubject);
        user.setUsername("slemaja" + System.nanoTime());
        user.setRole(Role.USER);
        User createdUser = userRepository.save(user);

        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getFullName()).isEqualTo(user.getFullName());
        assertThat(createdUser.getUsername()).isEqualTo(user.getUsername());
        assertThat(createdUser.getPassword()).isEqualTo(user.getPassword());
        assertThat(createdUser.getRole()).isEqualTo(user.getRole());
    }

    @Test
    public void createUserAndLogin(){
//        Subject subject = new Subject();
//        subject.setName("NONE");
//        Subject createdSubject = subjectRepository.save(subject);
//
//        User user = new User();
//        user.setPassword("123456");
//        user.setFullName("Salep Lemaja");
//        user.setSubject(createdSubject);
//        user.setUsername("slemaja" + System.nanoTime());
//        user.setRole(Role.USER);
//        User createdUser = userRepository.save(user);
//
//        assertThat(createdUser).isNotNull();

        clickOn("#usernameField");
        write("admin");

        clickOn("#passwordField");
        write("admin");

        clickOn("#loginButton");
    }

    @Override
    public void start (Stage stage) throws Exception {
        Parent root = FXMLLoader.load(this.getClass().getResource(Module.LOGIN.getResource()));
        stage.setTitle(Module.LOGIN.getTitle());
        stage.setScene(new Scene(root, 449.0, 417.0));
        stage.setResizable(false);
        stage.show();
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}
