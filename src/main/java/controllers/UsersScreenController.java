package controllers;

import helpers.PdfHelpers;
import helpers.StringHelpers;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Module;
import models.User;
import repositories.UserRepository;
import services.AuthenticationService;
import services.GroupService;
import services.UsersService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class UsersScreenController {
    private int page = 1;
    @FXML
    private ChoiceBox<String> asc;

    @FXML
    private TableView<User> dataTable;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> fullNameColumn;

    @FXML
    private TableColumn<?, ?> userNameColumn;

    @FXML
    private TableColumn<?, ?> roleColumn;

    @FXML
    private TableColumn<?, ?> subjectColumn;

    @FXML
    private Button addButton;

    @FXML
    private Label paginationText;

    @FXML
    private TextField searchBar;

    @FXML
    private ChoiceBox<String> sortBy;

    private int maxPage;
    private int elementsPerPageInt;
    private String sortByValue;
    private String ascValue;

    @FXML
    private Label paginationText1;

    @FXML
    private ChoiceBox<Integer> elementsPerPage;

    @FXML
    private Button saveButton;

    @FXML
    void savePdfFile(ActionEvent event) {
        UserRepository userRepository = new UserRepository();
        if(userRepository.getUsersCount() == 0){
            new Alert(Alert.AlertType.ERROR, "There is no data to generate report for!").show();
            return;
        }
        Stage stage = (Stage) searchBar.getScene().getWindow();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose the save directory");
        File file = directoryChooser.showDialog(stage);
        if(file == null)
            return;
        UsersService usersService = new UsersService();
        String tables = usersService.getTableUser();
        String fileName = "Users Generated Reports "+System.currentTimeMillis()+".pdf";
        boolean success = HtmlToPdf.create()
                .object(HtmlToPdfObject.forHtml(PdfHelpers.tableStyle() + PdfHelpers.pdfHeader("Users") + PdfHelpers.pdfTable(new String[]{"Id", "Full Name", "Username", "Role", "Subject"}) + tables + PdfHelpers.pdfFooter()))
                .convert(file.getAbsolutePath() + System.getProperty("file.separator") + fileName);

        if(success){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Saved successfully on "+file.getAbsolutePath()+". Do you want to open it?",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            if(alert.getResult() == ButtonType.YES){
                try {
                    Desktop.getDesktop().open(new File(file.getAbsolutePath() + System.getProperty("file.separator") + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else{
            new Alert(Alert.AlertType.ERROR, "Some error just happened while saving the file").showAndWait();
        }
    }

    public void initialize(){
        UserRepository userRepository = new UserRepository();
        UsersService usersService = new UsersService();
        userRepository.getFields(User.class).forEach(choice -> {
            sortBy.getItems().add(StringHelpers.snakeCaseToTitleCase(choice));
        });
        sortBy.getSelectionModel().selectFirst();

        sortBy.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            sortByValue = sortBy.getItems().get((Integer) number2);
            populateTable();
        });
        sortByValue = sortBy.getItems().get(0);

        asc.getItems().add("ASC");
        asc.getItems().add("DESC");
        asc.getSelectionModel().selectFirst();
        ascValue = "ASC";

        asc.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            ascValue = asc.getItems().get((Integer) number2);
            populateTable();
        });

        elementsPerPage.getItems().add(5);
        elementsPerPage.getItems().add(10);
        elementsPerPage.getItems().add(18);
        elementsPerPage.getItems().add(30);

        elementsPerPage.getSelectionModel().select(2);
        elementsPerPageInt = 18;

        elementsPerPage.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            elementsPerPageInt = elementsPerPage.getItems().get((Integer) number2);
            populateTable();
        });

        ContextMenu cm = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        MenuItem editMenuItem = new MenuItem("Edit");
        cm.getItems().add(deleteMenuItem);
        cm.getItems().add(editMenuItem);

        dataTable.addEventHandler(MouseEvent.MOUSE_CLICKED, t -> {
            if (t.getButton() == MouseButton.SECONDARY)
            {
                cm.show(dataTable, t.getScreenX(), t.getScreenY());
            }
        });

        deleteMenuItem.setOnAction(e -> {
            User user = dataTable.getSelectionModel().getSelectedItem();
            if(user != null && !user.equals(AuthenticationService.getInstance().getUser().get())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + user.getUsername() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                if(user.getGroupList().size() > 0 || user.getExamList().size() > 0){
                    alert.setContentText(user.getUsername()+" has exams & groups associated with it. If you delete this user all of their data will be deleted too! Are you sure you want to delete it?");
                }
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    usersService.removeUser(user);
                    populateTable();
                }
            }


        });

        editMenuItem.setOnAction(e -> {
            Parent root;
            try
            {
                User user = dataTable.getSelectionModel().getSelectedItem();

                FXMLLoader loader = new FXMLLoader(getClass().
                        getResource(Module.EDIT_USER.getResource()));

                root = loader.load();
                EditUserScreenController esc = loader.getController();
                esc.loadUser(user);
                Stage stage = new Stage();
                stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
                stage.setTitle(Module.EDIT_USER.getTitle());
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) dataTable).getScene().getWindow());
                stage.setScene(new Scene(root, 466.0, 575.0));
                stage.showAndWait();
                populateTable();
            }
            catch (IOException | URISyntaxException ex)
            {
                ex.printStackTrace();
            }
        });
        populateTable();
    }
    public void populateTable()
    {
        elementsPerPage.setDisable(true);
        UsersService usersService = new UsersService();
        dataTable.refresh();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));

        List<User> subjects = usersService
                .search(searchBar.getText(),page,elementsPerPageInt,sortByValue,ascValue.equals("ASC"));
        ObservableList<User> data = FXCollections.observableList(subjects);
        dataTable.setItems(data);
        maxPage = usersService.getMaxPages();
        paginationText.setText(String.format("page %d of %d", page, maxPage));

        elementsPerPage.setDisable(false);
    }
    @FXML
    void nextPage(ActionEvent event) {
        if(page+1 <= maxPage){
            page++;
            populateTable();
        }
    }

    @FXML
    void openAddUserScreen(ActionEvent event) {
        Parent root;
        try
        {
            root = FXMLLoader.load((getClass().
                    getResource(Module.ADD_USER.getResource())));
            Stage stage = new Stage();
            stage.setTitle(Module.ADD_USER.getTitle());
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) (event.getSource())).getScene().getWindow());
            stage.setScene(new Scene(root, 466.0, 575.0));
            stage.showAndWait();
            populateTable();
        }
        catch (IOException | URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void previousPage(ActionEvent event) {
        if(page-1 > 0){
            page--;
            populateTable();
        }
    }

    @FXML
    void search(KeyEvent event) {
        populateTable();
    }

}
