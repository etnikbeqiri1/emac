package services;

import exceptions.GenericException;
import helpers.StringHelpers;
import models.*;
import repositories.QuestionRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class QuestionService {
    public void addOpenQuestion(String questionText, String lines, String answer, String points, String image, Exam exam) throws GenericException {
        isValid(questionText, lines, points);

        Question question = new Question();
        question.setText(questionText);
        question.setAnswer(answer);
        question.setPoints(Integer.parseInt(points));
        question.setAnswerRows(Integer.parseInt(lines));
        question.setImage(image);
        question.setExam(exam);
        question.setProfessor(AuthenticationService.getInstance().getUser().get());
        question.setType(QuestionType.OPEN_QUESTION);
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.save(question);
    }

    private void isValid(String questionText, String lines, String points) throws GenericException {
        if (questionText == null || questionText.isEmpty()) {
            throw new GenericException("The Question is required");
        }

        if (lines == null || !StringHelpers.isValidInteger(lines) || Integer.parseInt(lines) <= 0) {
            throw new GenericException("Invalid lines to answer");
        }

        if (points == null || !StringHelpers.isValidInteger(points) || Integer.parseInt(points) <= 0) {
            throw new GenericException("Invalid points to answer");
        }
    }

    public void editOpenQuestion(Question question, String questionText, String lines, String answer, String points, String image, Exam exam) throws GenericException {
        isValid(questionText, lines, points);

        question.setText(questionText);
        question.setAnswer(answer);
        question.setPoints(Integer.parseInt(points));
        question.setAnswerRows(Integer.parseInt(lines));
        question.setImage(image);
        question.setExam(exam);
        question.setProfessor(AuthenticationService.getInstance().getUser().get());
        question.setType(QuestionType.OPEN_QUESTION);
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.update(question);
    }

    public void addOptionQuestion(String questionText, String optionA, String optionB, String optionC, String optionD, boolean radioA, boolean radioB, boolean radioC, boolean radioD, String points, String image, Exam exam) throws GenericException {
        if (questionText == null || questionText.isEmpty()) {
            throw new GenericException("The Question is required");
        }


        if(!optionA.isEmpty()){
            if (!optionB.isEmpty()){

                if(optionC.isEmpty() && !optionD.isEmpty()){
                    throw new GenericException("Please Fill the Input box field for C) inorder ");
                }


            }else {
                throw new GenericException("Please Fill the Input box field for B) inorder ");
            }
        }else {
            throw new GenericException("Please Fill the Input box field for A) inorder ");
        }


        if(!radioA && !radioB && !radioC && !radioD){
            throw new GenericException("Please Select one Radio Box! ");
        }
        if(optionA == null || optionA.isEmpty() && radioA){
            throw new GenericException("Please Fill the Input box field for A) to check Radio Box! ");
        }
        if(optionB == null || optionB.isEmpty() && radioB){
            throw new GenericException("Please Fill the Input box field for B) to check Radio Box! ");
        }
        if(optionC == null || optionC.isEmpty() && radioC){
            throw new GenericException("Please Fill the Input box field for C) to check Radio Box! ");
        }
        if(optionD == null || optionD.isEmpty() && radioD){
            throw new GenericException("Please Fill the Input box field for D) to check Radio Box! ");
        }


        String answer = "";
        if (radioA) {
            answer += "a";
        }
        if (radioB) {
            answer += "b";
        }
        if (radioC) {
            answer += "c";
        }
        if (radioD) {
            answer += "d";
        }


        if (points == null || !StringHelpers.isValidInteger(points) || Integer.parseInt(points) <= 0) {
            throw new GenericException("Invalid points to answer");
        }

        Question question = new Question();
        question.setText(questionText);
        question.setAnswer(answer);
        question.setVariantA(optionA);
        question.setVariantB(optionB);
        question.setVariantC(optionC);
        question.setVariantD(optionD);
        question.setPoints(Integer.parseInt(points));
        question.setImage(image);
        question.setExam(exam);
        question.setProfessor(AuthenticationService.getInstance().getUser().get());
        question.setType(QuestionType.CLOSED_QUESTION);
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.save(question);
    }

    public void editOptionQuestion(Question question, String questionText, String optionA, String optionB, String optionC, String optionD, boolean radioA, boolean radioB, boolean radioC, boolean radioD, String points, String image, Exam exam) throws GenericException {
        if (questionText == null || questionText.isEmpty()) {
            throw new GenericException("The Question is required");
        }


        if(!optionA.isEmpty()){
            if (!optionB.isEmpty()){

                if(optionC.isEmpty() && !optionD.isEmpty()){
                    throw new GenericException("Please Fill the Input box field for C) inorder ");
                }


            }else {
                throw new GenericException("Please Fill the Input box field for B) inorder ");
            }
        }else {
            throw new GenericException("Please Fill the Input box field for A) inorder ");
        }


        if(radioA == false && radioB == false && radioC == false && radioD == false){
            throw new GenericException("Please Select one Radio Box! ");
        }
        if(optionA == null || optionA.isEmpty() && radioA){
            throw new GenericException("Please Fill the Input box field for A) to check Radio Box! ");
        }
        if(optionB == null || optionB.isEmpty() && radioB){
            throw new GenericException("Please Fill the Input box field for B) to check Radio Box! ");
        }
        if(optionC == null || optionC.isEmpty() && radioC){
            throw new GenericException("Please Fill the Input box field for C) to check Radio Box! ");
        }
        if(optionD == null || optionD.isEmpty() && radioD){
            throw new GenericException("Please Fill the Input box field for D) to check Radio Box! ");
        }


        String answer = "";
        if (radioA) {
            answer += "a";
        }
        if (radioB) {
            answer += "b";
        }
        if (radioC) {
            answer += "c";
        }
        if (radioD) {
            answer += "d";
        }


        if (points == null || !StringHelpers.isValidInteger(points) || Integer.parseInt(points) <= 0) {
            throw new GenericException("Invalid points to answer");
        }
        question.setText(questionText);
        question.setAnswer(answer);
        question.setVariantA(optionA);
        question.setVariantB(optionB);
        question.setVariantC(optionC);
        question.setVariantD(optionD);
        question.setPoints(Integer.parseInt(points));
        question.setImage(image);
        question.setExam(exam);
        question.setProfessor(AuthenticationService.getInstance().getUser().get());
        question.setType(QuestionType.CLOSED_QUESTION);
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.update(question);
    }

    public void removeQuestion(Question question){
        QuestionRepository questionRepository = new QuestionRepository();
        questionRepository.delete(question);
    }

    public boolean generateExam(Exam exam, Map<String,String> set, String info,String destincation){
        QuestionRepository questionRepository = new QuestionRepository();
        List<Question> questionList =  new ArrayList<>();

        set.keySet().forEach(key -> {
            questionList.addAll(questionRepository.getQuestionsByPoints(exam,Integer.parseInt(key),Integer.parseInt(set.get(key))));
        });

        Collections.shuffle(questionList);
        AuthenticationService authenticationService = AuthenticationService.getInstance();
        User user = authenticationService.getUser().get();
        GenerateExamService generateExamService = new GenerateExamService(user.getSubject().getName(),
                    exam.getName(),user.getFullName(),info);
        AtomicReference<Integer> i = new AtomicReference<>(1);
        questionList.forEach(question -> {
            String questionText = String.format("%d. %s (%d points)", i.get(), question.getText(), question.getPoints());
            i.getAndSet(i.get() + 1);
            if(question.getType() == QuestionType.OPEN_QUESTION){
                if(question.getImage() == null){
                    generateExamService.addQuestion(questionText,question.getAnswerRows());
                }else{
                    generateExamService.addQuestion(questionText,question.getAnswerRows(),question.getImage());
                }
            }else if(question.getType() == QuestionType.CLOSED_QUESTION){
                if(question.getImage() == null){
                    generateExamService.addQuestion(questionText,
                            question.getVariantA(),
                            question.getVariantB(),
                            question.getVariantC(),
                            question.getVariantD());
                }else{
                    generateExamService.addQuestion(questionText,
                            question.getVariantA(),
                            question.getVariantB(),
                            question.getVariantC(),
                            question.getVariantD(),
                            question.getImage());
                }
            }
        });
        return generateExamService.render(destincation);
    }

}
