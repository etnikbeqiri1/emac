package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;
import repositories.ExamRepository;
import repositories.QuestionRepository;
import services.QuestionService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class QuestionsScreenController {

    @FXML
    private TableView<Question> dataTable;

    @FXML
    private TableColumn<?, ?> idColumn;

    @FXML
    private TableColumn<?, ?> questionColumn;

    @FXML
    private TableColumn<?, ?> questionTypeColumn;

    @FXML
    private TableColumn<?, ?> questionPointsColumn;

    @FXML
    private ChoiceBox<Exam> examChoiceBox;

    private Exam selectedExam;

    @FXML
    void addOpenQuestion(ActionEvent event) {
        if(selectedExam == null){
            new Alert(Alert.AlertType.ERROR, "Please choose an exam!").show();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource(Module.ADD_OPEN_QUESTION.getResource()));
        Parent root;
        try {
            root = loader.load();
            AddOpenQuestionScreenController esc = loader.getController();
            esc.setExam(selectedExam);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.setTitle(Module.ADD_OPEN_QUESTION.getTitle());
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) dataTable).getScene().getWindow());
            stage.setScene(new Scene(root, 466.0, 595.0));
            stage.showAndWait();
            populateTable();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void addOptionsQuestion(ActionEvent event) {
        if(selectedExam == null){
            new Alert(Alert.AlertType.ERROR, "Please choose an exam!").show();
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource(Module.ADD_OPTION_QUESTION.getResource()));
        Parent root;
        try {
            root = loader.load();
            AddOptionQuestionScreenController esc = loader.getController();
            esc.setExam(selectedExam);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.setTitle(Module.ADD_OPTION_QUESTION.getTitle());
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) dataTable).getScene().getWindow());
            stage.setScene(new Scene(root, 466.0, 717.0));
            stage.showAndWait();
            populateTable();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void generateExams(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().
                getResource(Module.GENERATE_EXAMS.getResource()));
        Parent root;
        try {
            root = loader.load();
            GenerateExamsScreenController esc = loader.getController();
            esc.setExam(selectedExam);
            Stage stage = new Stage();
            stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
            stage.setTitle(Module.GENERATE_EXAMS.getTitle());
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) dataTable).getScene().getWindow());
            stage.setScene(new Scene(root, 466.0, 615.0));
            stage.showAndWait();
            populateTable();
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    void populateTable(){
        QuestionRepository questionRepository = new QuestionRepository();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionColumn.setCellValueFactory(new PropertyValueFactory<>("text"));
        questionTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        questionPointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
        List<Question> questions = questionRepository.listQuestionsByExam(selectedExam);
        ObservableList<Question> data = FXCollections.observableList(questions);
        dataTable.setItems(data);

    }

    @FXML
    void initialize() {

        ExamRepository examRepository = new ExamRepository();

        examRepository.listAllExams().forEach(exam1 -> {
            examChoiceBox.getItems().add(exam1);
        });



        examChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            selectedExam = examChoiceBox.getItems().get((Integer) number2);
            populateTable();
        });

        examChoiceBox.getSelectionModel().selectFirst();
        populateTable();



        //test
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
              Question question = dataTable.getSelectionModel().getSelectedItem();
            if(question != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + question.getText() + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    QuestionService questionService = new QuestionService();
                    questionService.removeQuestion(question);
                    populateTable();
                }
            }


        });

        editMenuItem.setOnAction(e -> {
            Question question = dataTable.getSelectionModel().getSelectedItem();
            FXMLLoader loader = null;
            String title = "";
            if(question.getType() == QuestionType.OPEN_QUESTION){
                loader = new FXMLLoader(getClass().getResource(Module.EDIT_OPEN_QUESTION.getResource()));
                title = Module.EDIT_OPEN_QUESTION.getTitle();
            }else{
                loader = new FXMLLoader(getClass().getResource(Module.EDIT_OPTION_QUESTION.getResource()));
                title = Module.EDIT_OPTION_QUESTION.getTitle();
            }
            try {
                Parent root = loader.load();
                if(question.getType() == QuestionType.OPEN_QUESTION) {
                    EditOpenQuestionScreenController eosc = loader.getController();
                    eosc.loadQuestion(question);
                }else{
                    EditOptionQuestionScreenController eosc = loader.getController();
                    eosc.loadQuestion(question);
                }
                Stage stage = new Stage();
                stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
                stage.setTitle(title);
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(((Node) dataTable).getScene().getWindow());
                stage.setScene(new Scene(root, 466.0, 717.0));
                stage.showAndWait();
                populateTable();
            } catch (IOException | URISyntaxException ioException) {
                ioException.printStackTrace();
            }

        });

    }

}
