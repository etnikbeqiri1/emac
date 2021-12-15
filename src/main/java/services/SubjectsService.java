package services;

import exceptions.GenericException;
import helpers.StringHelpers;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import lombok.Data;
import models.Subject;
import models.User;
import repositories.SubjectRepository;

import java.util.Iterator;
import java.util.List;

@Data
public class SubjectsService {
    private int maxPages;
    private SubjectRepository subjectRepository;

    public SubjectsService(){
        subjectRepository = new SubjectRepository();
    }

    public List<Subject> search(String keyword, int page, int elementsPerPage, String sortBy, Boolean asc) {
        int from = (page - 1) * elementsPerPage;
        maxPages = 1+(int) Math.ceil(subjectRepository.countResults(keyword)/elementsPerPage);
        return subjectRepository.listSubjects(keyword, from, elementsPerPage, StringHelpers.titleCaseToSnakeCase(sortBy), asc);
    }

    public String getTableSubjects(){
        List<Subject> subjects = subjectRepository.listAllSubjects();
        Iterator<Subject> subjectsIterator = subjects.iterator();
        String tableContent = "";
        while(subjectsIterator.hasNext()){
            Subject subject = subjectsIterator.next();
            tableContent += "<tr>";
            tableContent += "<td>"+subject.getId()+"</td>";
            tableContent += "<td>"+subject.getName()+"</td>";
            tableContent += "</tr>";
        }
        tableContent += "</tbody>";
        tableContent += "</table>";
        return tableContent;
    }

    public void addSubject(String subject) throws GenericException{
        if(subject == null || subject.isEmpty()) throw new GenericException("Please enter subject name");
        if(subjectRepository.existsByName(StringHelpers.titleCase(subject)))
            throw new GenericException("A subject with that name already exists");
        Subject subject1 = new Subject();
        subject1.setName(StringHelpers.titleCase(subject));
        subjectRepository.save(subject1);
    }

    public void removeSubject(Subject subject){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure that you want to delete this subject?", ButtonType.YES, ButtonType.NO);
        if(subject.getUserList().contains(AuthenticationService.getUser().get())){
            new Alert(Alert.AlertType.ERROR, "This subject contains you so it cant be deleted").show();
            return;
        }
        if(subject.getUserList().size() > 0)
            alert.setContentText("This subject has professors on it! If you delete this subject it will delete all the professors and their data too! Do you want to continue?");
        alert.showAndWait();
        if(alert.getResult() == ButtonType.YES)
            subjectRepository.delete(subject);
    }

    public void editSubject(Subject subject) throws GenericException {
        if(subject.getName() == null || subject.getName().isEmpty()) throw new GenericException("Please enter subject name");
        if(subjectRepository.existsByName(StringHelpers.titleCase(subject.getName())))
            throw new GenericException("A subject with that name already exists");

        subjectRepository.update(subject);
    }



}
