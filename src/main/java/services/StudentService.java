package services;

import exceptions.GenericException;
import helpers.StringHelpers;
import models.Group;
import models.Student;
import repositories.GroupRepository;
import repositories.StudentRepository;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentService {

    private StudentRepository studentRepository;
    private int maxPages;

    public StudentService(){ studentRepository = new StudentRepository(); }

    public String getTableStudent(){
        List<Student> students = studentRepository.listAllStudent();
        Iterator<Student> studentIterator = students.iterator();
        String tableContent = "";
        while(studentIterator.hasNext()){
            Student student = studentIterator.next();
            tableContent += "<tr>";
            tableContent += "<td>"+student.getId()+"</td>";
            tableContent += "<td>"+student.getName()+"</td>";
            tableContent += "<td>"+student.getGroup()+"</td>";
            tableContent += "<td>"+student.getPoints()+"</td>";
            tableContent += "</tr>";
        }
        tableContent += "</tbody>";
        tableContent += "</table>";
        return tableContent;
    }

    public void addStudent(Student student) throws GenericException{
        validateStudent(student);
        student.setName(StringHelpers.FullNameFormat(student.getName()));
        studentRepository.save(student);
    }
    public List<Student> search(String keyword, int page, int elementsPerPage, String sortBy, Boolean asc) {
        int from = (page - 1) * elementsPerPage;
        maxPages = 1+(int) Math.ceil(studentRepository.countResults(keyword)/elementsPerPage);
        return studentRepository.listStudent(keyword, from, elementsPerPage, StringHelpers.titleCaseToSnakeCase(sortBy), asc);
    }

    public int getMaxPages(){ return maxPages; }

    public void removeStudent(Student student){
        studentRepository.delete(student);
    }

    public void editStudent(Student student) throws GenericException {
        validateStudent(student);
        student.setName(StringHelpers.FullNameFormat(student.getName()));
        studentRepository.update(student);
    }

    public void validateStudent(Student student) throws GenericException{
        GroupRepository groupRepository = new GroupRepository();
        Group group = student.getGroup();
        if(student.getName() == null || student.getName().trim() == null) { throw new GenericException("Student name cant be blank"); }
        if(group == null){ throw new GenericException("Please select your group"); }
        if(!groupRepository.getGroups().contains(group)) { throw new GenericException("Group that you selected cannot be found"); }
        if(student.getPoints() != null && student.getPoints() < 0) { throw new GenericException("Points cannot be negative"); }
    }

    public Map<String,Integer> getChartData(){
        StudentRepository studentRepository = new StudentRepository();
        Map<String,Integer> data = new HashMap<>();
        AtomicInteger nonGradedStudents = new AtomicInteger();
        AtomicInteger failedStudents = new AtomicInteger();
        AtomicInteger under60Students = new AtomicInteger();
        AtomicInteger under70Students = new AtomicInteger();
        AtomicInteger under80Students = new AtomicInteger();
        AtomicInteger under90Students = new AtomicInteger();
        AtomicInteger under100Students = new AtomicInteger();
        studentRepository.listAllStudent().forEach(student -> {
            if(student.getPoints() == null){
                nonGradedStudents.getAndIncrement();
            }else if(student.getPoints() < 50){
                failedStudents.getAndIncrement();
            }else if(student.getPoints() < 60){
                under60Students.getAndIncrement();
            }else if(student.getPoints() < 70){
                under70Students.getAndIncrement();
            }else if(student.getPoints() < 80){
                under80Students.getAndIncrement();
            }else if(student.getPoints() < 90){
                under90Students.getAndIncrement();
            }else{
                under100Students.getAndIncrement();
            }
        });
        data.put("Not Graded", nonGradedStudents.get());
        data.put("Failed", failedStudents.get());
        data.put("50-60", under60Students.get());
        data.put("60-70", under70Students.get());
        data.put("70-80", under80Students.get());
        data.put("80-90", under90Students.get());
        data.put("90-100", under100Students.get());
        return data;
    }
    public Map<String,Integer> getAdminChartData(){
        StudentRepository studentRepository = new StudentRepository();
        Map<String,Integer> data = new HashMap<>();
        AtomicInteger nonGradedStudents = new AtomicInteger();
        AtomicInteger failedStudents = new AtomicInteger();
        AtomicInteger under60Students = new AtomicInteger();
        AtomicInteger under70Students = new AtomicInteger();
        AtomicInteger under80Students = new AtomicInteger();
        AtomicInteger under90Students = new AtomicInteger();
        AtomicInteger under100Students = new AtomicInteger();
        studentRepository.listAll(Student.class).forEach(student -> {
            if(student.getPoints() == null){
                nonGradedStudents.getAndIncrement();
            }else if(student.getPoints() < 50){
                failedStudents.getAndIncrement();
            }else if(student.getPoints() < 60){
                under60Students.getAndIncrement();
            }else if(student.getPoints() < 70){
                under70Students.getAndIncrement();
            }else if(student.getPoints() < 80){
                under80Students.getAndIncrement();
            }else if(student.getPoints() < 90){
                under90Students.getAndIncrement();
            }else{
                under100Students.getAndIncrement();
            }
        });
        data.put("Not Graded", nonGradedStudents.get());
        data.put("Failed", failedStudents.get());
        data.put("50-60", under60Students.get());
        data.put("60-70", under70Students.get());
        data.put("70-80", under80Students.get());
        data.put("80-90", under90Students.get());
        data.put("90-100", under100Students.get());
        return data;
    }
}
