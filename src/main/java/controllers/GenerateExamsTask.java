package controllers;

import javafx.concurrent.Task;
import models.Exam;
import services.QuestionService;

import java.io.File;
import java.util.Map;

public class GenerateExamsTask extends Task<Boolean> {

    private final QuestionService questionService;
    private final int total;
    private final Map<String, String> questionSet;
    private final Exam exam;
    private final String info;
    private final File dir;

    public GenerateExamsTask(final Exam exam, Map<String, String> questionSet, int total, String info, File dir) {
        this.questionService = new QuestionService();
        this.exam = exam;
        this.questionSet = questionSet;
        this.total = total;
        this.info = info;
        this.dir = dir;

    }

    @Override
    protected Boolean call() throws Exception {
        for (int i = 0; i < total; i++) {
            if (questionService.generateExam(exam, questionSet, info, String.format("%s\\%d.pdf", dir.getAbsolutePath(), i))) {
                updateProgress(i+1, total);
            } else{
                return false;
            }

        }
        return true;
    }
}
