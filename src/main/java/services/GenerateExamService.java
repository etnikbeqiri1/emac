package services;

import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;
import io.woo.htmltopdf.PdfPageSize;
import javafx.print.Printer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class GenerateExamService {
    private final StringBuilder questions = new StringBuilder();
    private final String examName;
    private final String professor;
    private final String info;
    private final String subject;

    static String readFile(File file)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(file.getPath()));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public GenerateExamService(String subject, String name, String professor, String info) {
        this.examName = name;
        this.professor = professor;
        this.info = info;
        this.subject = subject;
    }

    public void addQuestion(String question, String answer1,String answer2,String answer3,String answer4){
        String CHOICE_QUESTION_TEMPLATE = "<div class=\"card\" style=\"page-break-inside: avoid;\"> <div class=\"card-header\"> $question </div> <div class=\"card-body\"> ○ $answer1<br> ○ $answer2<br> ○ $answer3<br> ○ $answer4<br> </div> </div> <br>";
        questions.append(CHOICE_QUESTION_TEMPLATE.replace("$question",question)
                .replace("$answer1",answer1)
                .replace("$answer2",answer2)
                .replace("$answer3",answer3)
                .replace("$answer4",answer4));
    }

    public void addQuestion(String question, String answer1,String answer2,String answer3,String answer4,String image){
        String CHOICE_IMAGE_QUESTION_TEMPLATE = "<div class=\"card\" style=\"page-break-inside: avoid;\"> <div class=\"card-header\"> $question </div> <div class=\"row\"> <div class=\"col-8\"> <div class=\"card-body\"> ○ $answer1<br> ○ $answer2<br> ○ $answer3<br> ○ $answer4<br> </div> </div> <div class=\"col-4\"> <img src=\"$image\" style=\"width: 100%;\"> </div> </div> </div> <br>";
        questions.append(CHOICE_IMAGE_QUESTION_TEMPLATE.replace("$question",question)
                .replace("$answer1",answer1)
                .replace("$answer2",answer2)
                .replace("$answer3",answer3)
                .replace("$answer4",answer4)
                .replace("$image",image));
    }

    public void addQuestion(String question, int lines,String image){
        StringBuilder brs = new StringBuilder();
        for(int i=0; i < lines;i++) brs.append("<br />");
        String OPEN_IMAGE_QUESTION_TEMPLATE = "<div class=\"card\" style=\"page-break-inside: avoid;\"> <div class=\"card-header\"> $question </div> <div class=\"row\"> <div class=\"col-8\"> <div class=\"card-body\"> $brs </div> </div> <div class=\"col-4\"> <img src=\"$image\" style=\"width: 100%;\"> </div> </div> </div> <br>";
        questions.append(OPEN_IMAGE_QUESTION_TEMPLATE.replace("$question",question)
                .replace("$brs",brs)
                .replace("$image",image));

    }

    public void addQuestion(String question, int lines){
        StringBuilder brs = new StringBuilder();
        for(int i=0; i < lines;i++) brs.append("<br />");
        String OPEN_QUESTION_TEMPLATE = "<div class=\"card\" style=\"page-break-inside: avoid;\"> <div class=\"card-header\"> $question </div> <div class=\"card-body\"> $brs </div> </div> <br>";
        questions.append(OPEN_QUESTION_TEMPLATE.replace("$question",question)
                .replace("$brs",brs));
    }

    private String createDocument() {
        try {
            File file = new File(Objects.requireNonNull(this.getClass().getResource("/templates/exam.html")).toURI());
            String document = readFile(file);
            return document.replace("$subject", subject)
                    .replace("$examname", examName)
                    .replace("$professor",professor)
                    .replace("$info",info)
                    .replace("$questions" , questions);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean render(String destination){
        return HtmlToPdf.create()
                .object(HtmlToPdfObject.forHtml(createDocument()))
                .disableSmartShrinking(true)
                .convert(destination);

    }
}
