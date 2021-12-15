package controllers;

import helpers.PdfHelpers;
import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Group;
import models.Module;
import models.Student;
import models.Subject;
import repositories.GroupRepository;
import repositories.SubjectRepository;
import services.GroupService;
import services.StudentService;
import services.SubjectsService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import static helpers.StringHelpers.snakeCaseToTitleCase;

public class GroupsScreenController {

    private int page = 1;
    @FXML
    private TableView<Group> dataTable;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> subjectColumn;

    @FXML
    private Button addButton;

    @FXML
    private TextField searchBar;

    @FXML
    private ChoiceBox<String> asc;

    @FXML
    private ChoiceBox<String> sortBy;

    private int maxPage;
    private int elementsPerPageInt;
    private String sortByValue;
    private String ascValue;

    @FXML
    private ChoiceBox<Integer> elementsPerPage;

    @FXML
    private Label paginationText;

    @FXML
    private Button saveButton;

    @FXML
    void openAddStudentScreen(ActionEvent event) {
        Parent root;
        try
        {
            root = FXMLLoader.load((getClass().
                    getResource(Module.ADD_GROUP.getResource())));
            Stage stage = new Stage();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.setTitle(Module.ADD_GROUP.getTitle());
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) (event.getSource())).getScene().getWindow());
            stage.setScene(new Scene(root, 466.0, 264.0));
            stage.showAndWait();
            populateTable();
        }
        catch (IOException | URISyntaxException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    void savePdfFile(ActionEvent event) throws URISyntaxException {
        GroupRepository groupRepository = new GroupRepository();
        if(groupRepository.getGroupCount() == 0){
            new Alert(Alert.AlertType.ERROR, "There is no data to generate report for!").show();
            return;
        }
        Stage stage = (Stage) searchBar.getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose the save directory");
        File file = directoryChooser.showDialog(stage);
        if(file == null)
            return;
        GroupService groupService = new GroupService();
        String tables = groupService.getTableGroup();
        String fileName = "Groups Generated Reports "+System.currentTimeMillis()+".pdf";
        boolean success = HtmlToPdf.create()
                .object(HtmlToPdfObject.forHtml(PdfHelpers.tableStyle() + PdfHelpers.pdfHeader("Groups") + PdfHelpers.pdfTable(new String[]{"Id", "Group Name"}) + tables + PdfHelpers.pdfFooter()))
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
        GroupRepository groupRepository = new GroupRepository();
        GroupService groupService = new GroupService();
        groupRepository.getFields(Group.class).forEach(choice -> {
            sortBy.getItems().add(snakeCaseToTitleCase(choice));
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
            Group group = dataTable.getSelectionModel().getSelectedItem();
            if(group != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + group.getName() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                if(group.getStudentList().size() > 0)
                    alert.setContentText("This group has students on it. If you delete this group, the students will be deleted too. Do you want to delete this group?");
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    groupService.removeGroup(group);
                    populateTable();
                }
            }


        });

        editMenuItem.setOnAction(e -> {
            Parent root;
            try
            {
                Group group = dataTable.getSelectionModel().getSelectedItem();

                FXMLLoader loader = new FXMLLoader(getClass().
                        getResource(Module.EDIT_GROUP.getResource()));

                root = loader.load();
                EditGroupScreenController esc = loader.getController();
                esc.loadGroup(group);
                Stage stage = new Stage();
                stage.setTitle(Module.EDIT_GROUP.getTitle());
                stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) dataTable).getScene().getWindow());
                stage.setScene(new Scene(root, 466.0, 264.0));
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

        GroupService groupService = new GroupService();
        dataTable.refresh();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        List<Group> groups = groupService
                .search(searchBar.getText(),page,elementsPerPageInt,sortByValue,ascValue.equals("ASC"));
        ObservableList<Group> data = FXCollections.observableList(groups);
        dataTable.setItems(data);
        maxPage = groupService.getMaxPages();
        paginationText.setText(String.format("page %d of %d", page, maxPage));

        elementsPerPage.setDisable(false);
    }
    @FXML
    void search(KeyEvent event) {
        populateTable();
    }

    @FXML
    void nextPage(ActionEvent event) {
        if(page+1 <= maxPage){
            page++;
            populateTable();
        }
    }

    @FXML
    void previousPage(ActionEvent event) {
        if(page-1 > 0){
            page--;

            populateTable();
        }
    }

}
