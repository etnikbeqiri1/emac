package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import repositories.ExamRepository;
import repositories.StudentRepository;
import repositories.UserRepository;
import services.AuditService;
import services.StudentService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminDashboardScreenController {

    @FXML
    private Label activeUsersCount;

    @FXML
    private Label totalExamsCount;

    @FXML
    private Label gradedStudentsCount;

    @FXML
    private Pane chart;

    @FXML
    private ListView<String> auditsList;

    private AuditService auditService;

    public void initialize(){
        auditService = new AuditService();

        UserRepository userRepository = new UserRepository();
        ExamRepository examRepository = new ExamRepository();
        StudentRepository studentRepository = new StudentRepository();

        activeUsersCount.setText(userRepository.getUsersCount()+"");
        totalExamsCount.setText(examRepository.getAllExamsCount()+"");
        gradedStudentsCount.setText(studentRepository.getAllGradedPercentage()+"%");

        //Preparing ObservbleList object
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        StudentService studentService = new StudentService();
        Map<String, Integer> chartData = studentService.getAdminChartData();
        AtomicInteger studentsPassedInt = new AtomicInteger();
        chartData.keySet().forEach(key->{
            pieChartData.add(new PieChart.Data(key, chartData.get(key)));
            if(!key.equals("Not Graded") && !key.equals("Failed")){
                studentsPassedInt.getAndAdd(chartData.get(key));
            }
        });

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setData(pieChartData);
        chart.getChildren().setAll(pieChart);

        auditsList.setItems(FXCollections.observableArrayList(auditService.getAuditItems()));
    }
}
