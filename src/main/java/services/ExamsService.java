package services;

import exceptions.GenericException;
import helpers.StringHelpers;
import javafx.scene.control.Alert;
import models.Exam;
import repositories.ExamRepository;

import java.util.Iterator;
import java.util.List;

public class ExamsService {

    private int maxPages;
    private ExamRepository examsRepository;

    public ExamsService(){ examsRepository = new ExamRepository(); }

    public void addExam(Exam ex) throws GenericException{
        validateExam(ex);
        System.out.println(ex);
        ex.setName(StringHelpers.titleCase(ex.getName()));
        examsRepository.save(ex);
    }

    public String getTableExam(){
        List<Exam> exams = examsRepository.listAllExams();
        Iterator<Exam> examIterator = exams.iterator();
        String tableContent = "";
        while(examIterator.hasNext()){
            Exam ex = examIterator.next();
            tableContent += "<tr>";
            tableContent += "<td>"+ex.getId()+"</td>";
            tableContent += "<td>"+ex.getName()+"</td>";
            tableContent += "</tr>";
        }
        tableContent += "</tbody>";
        tableContent += "</table>";
        return tableContent;
    }

    public List<Exam> search(String keyword, int page, int elementsPerPage, String sortBy, Boolean asc) {
        int from = (page - 1) * elementsPerPage;
        maxPages = 1+(int) Math.ceil(examsRepository.countResults(keyword)/elementsPerPage);
        return examsRepository.listExams(keyword, from, elementsPerPage, StringHelpers.titleCaseToSnakeCase(sortBy), asc);
    }

    public void removeExam(Exam exam){
        this.examsRepository.delete(exam);
    }

    public int getMaxPages(){
        return this.maxPages;
    }

    public void validateExam(Exam ex) throws GenericException{
        if(ex.getName() == null || ex.getName().trim() == null)
            throw new GenericException("Please write an exam name");
        else if(ex.getName().length() < 3)
            throw new GenericException("Exam name must be more than 3 characters");
        else if(ex.getName().length() > 35)
            throw new GenericException("Exam name must not be more than 35 characters");

    }

    public void update(Exam exam) throws GenericException{
        validateExam(exam);
        examsRepository.update(exam);
    }


}
