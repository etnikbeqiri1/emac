package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import repositories.ExamRepository;
import repositories.StudentRepository;
import repositories.UserRepository;
import services.StudentService;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDashboardScreenController {
    @FXML
    private Label activeUsersCount;

    @FXML
    private Label totalExamsCount;

    @FXML
    private Label gradedStudentsCount;

    @FXML
    private Pane chart;

    @FXML
    private Label studentsToBeGraded;

    @FXML
    private Label studentsFailed;

    @FXML
    private Label studentsPassed;

    public void initialize(){

        UserRepository userRepository = new UserRepository();
        ExamRepository examRepository = new ExamRepository();
        StudentRepository studentRepository = new StudentRepository();

        activeUsersCount.setText(studentRepository.getStudentCount()+"");
        totalExamsCount.setText(examRepository.getExamsCount()+"");
        gradedStudentsCount.setText(studentRepository.getGradedPercentage()+"%");

        //Preparing ObservbleList object
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        StudentService studentService = new StudentService();
        Map<String, Integer> chartData = studentService.getChartData();
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

        studentsToBeGraded.setText(chartData.get("Not Graded").toString());
        studentsFailed.setText(chartData.get("Failed").toString());
        studentsPassed.setText(studentsPassedInt.toString());
    }
}
