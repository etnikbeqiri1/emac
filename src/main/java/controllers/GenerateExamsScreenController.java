package controllers;

import helpers.StringHelpers;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import models.Exam;
import models.Role;
import repositories.QuestionRepository;
import services.QuestionService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GenerateExamsScreenController {

    private final Map<String, String> questionSetList = new HashMap<>();

    @FXML
    private Label errorMessage;

    @FXML
    private TextField numberOfExams;

    @FXML
    private TextField examInfo;

    @FXML
    private TextField numberOfQuestions;

    @FXML
    private TableView<Map.Entry<String, String>> dataTable;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> pointsColumn;

    @FXML
    private TableColumn<Map.Entry<String, String>, String> numberOfQuestionColumn;

    @FXML
    private ProgressBar progressBar;

    private Exam exam;

    @FXML
    private Label questionsInfo;

    private Integer pointsDropdownValue;
    @FXML
    private ChoiceBox<Integer> pointsDropdown;

    @FXML
    void addQuestions(ActionEvent event) {
        QuestionRepository questionRepository = new QuestionRepository();
        if(!StringHelpers.isValidInteger(numberOfQuestions.getText())){
            errorMessage.setText("Please enter a valid number");
            return;
        }
        if(questionRepository.countQuestionsByPoints(exam,pointsDropdown.getValue()) < Integer.parseInt(numberOfQuestions.getText())){
            errorMessage.setText(String.format("There are not %s questions with %d points", numberOfQuestions.getText(), pointsDropdown.getValue()));
            return;
        }
        errorMessage.setText("");

        questionSetList.put(pointsDropdown.getValue().toString(),numberOfQuestions.getText());
        populateTable();
    }

    public void setExam(Exam exam){
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.getDistinctPoints(exam).forEach(point -> {
            questionSetList.put(point.toString(), "0");
            pointsDropdown.getItems().add(point);
        });
        pointsDropdown.getSelectionModel().selectFirst();
        pointsDropdown.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            numberOfQuestions.setText(questionSetList.get(pointsDropdown.getItems().get((Integer) number2).toString()));
        });
        populateTable();
        this.exam = exam;
    }

    @FXML
    void generateExams(ActionEvent event) throws URISyntaxException {
        if(!StringHelpers.isValidInteger(numberOfExams.getText())){
            errorMessage.setText("Please enter a valid number of exams");
            return;
        }

        int numberOfExamsInteger = Integer.parseInt(numberOfExams.getText());

        if(numberOfExamsInteger < 0){
            errorMessage.setText("Please enter a valid number of exams");
            return;
        }

        if(examInfo.getText().trim().isEmpty()){
            errorMessage.setText("Please enter information for ur exam");
            return;
        }

        int allQuestions = questionSetList.keySet().stream().mapToInt(key -> Integer.parseInt(questionSetList.get(key))).sum();

        if(allQuestions <= 0) {
            errorMessage.setText("There are no questions to generate exams");
            return;
        }
        errorMessage.setText("");

        Stage stage = (Stage) errorMessage.getScene().getWindow();
        stage.getIcons().add(new Image(this.getClass().getResource("/views/images/logo.png").toURI().toString()));
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose the save directory");
        File file = directoryChooser.showDialog(stage);
        if(file == null)
            return;
        QuestionService questionService = new QuestionService();
        File newDirectory = new File(String.format("%s/%s_%d/", file.getPath(), exam.getName().replace(" ","_").trim(), System.currentTimeMillis()));
        newDirectory.mkdirs();

        GenerateExamsTask generateExamsTask = new GenerateExamsTask(exam,questionSetList,numberOfExamsInteger, examInfo.getText(), newDirectory);
        progressBar.progressProperty().bind(generateExamsTask.progressProperty());

        Thread thread = new Thread(generateExamsTask);
        thread.start();

        generateExamsTask.setOnSucceeded(e ->{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exams generated successfully. "+newDirectory.getAbsolutePath()+". Do you want to open it?",
                    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.showAndWait();

            if(alert.getResult() == ButtonType.YES){
                try {
                    Desktop.getDesktop().open(newDirectory);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        });

        generateExamsTask.setOnFailed(e->{
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        });
    }

    void populateTable(){
        dataTable.refresh();
        pointsColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        numberOfQuestionColumn.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getValue()));
        ObservableList<Map.Entry<String, String>> items = FXCollections.observableArrayList(questionSetList.entrySet());

        int allQuestions = questionSetList.keySet().stream().mapToInt(key -> Integer.parseInt(questionSetList.get(key))).sum();
        int maxPoints = questionSetList.keySet().stream().mapToInt(key -> Integer.parseInt(key) * Integer.parseInt(questionSetList.get(key))).sum();

        questionsInfo.setText(String.format("%d Questions selected with Max Points %d", allQuestions, maxPoints));

        dataTable.setItems(items);
    }

    public void initialize(){


    }

}
