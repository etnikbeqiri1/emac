package repositories;


import config.HibernateConfig;
import models.Student;
import models.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.AuthenticationService;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class StudentRepository extends AbstractRepository{

    public int getGradedPercentage(){
        List<Student> students =  this.listAllStudent();
        int gradedStudents = (int) students.stream().filter(student -> student.getPoints() == null).count();
        try{
            return 100-(gradedStudents*100/students.size());
        }catch (ArithmeticException e){
            return 0;
        }
    }
    public int getAllGradedPercentage(){
        List<Student> students =  this.listAll(Student.class);
        int gradedStudents = (int) students.stream().filter(student -> student.getPoints() == null).count();
        try{
            return 100-(gradedStudents*100/students.size());
        }catch (ArithmeticException e){
            return 0;
        }
    }
    public List<Student> listAllStudent(){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("from Student s where s.group.professor=:needle2"));
            query.setParameter("needle2", user.get());
            return (List<Student>) query.list();
        }
    }

    public int getStudentCount(){
        return this.listAllStudent().size();
    }

    public List<Student> listStudent(String search, int from, int limit, String sortBy, Boolean asc){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("from Student s where (s.name like :needle OR s.points like :needle OR s.group like :needle) AND s.group.professor=:needle2 ORDER BY %s %s", sortBy, asc ? "ASC" : "DESC"));
            query.setParameter("needle", String.format("%%%s%%", search));
            query.setParameter("needle2", user.get());
            query.setFirstResult(from);
            query.setMaxResults(limit);
            return (List<Student>) query.list();
        }
    }

    public long countResults(String search){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("select count(*) from Student where (name like :needle OR points like :needle OR group like :needle)  AND group.professor=:needle2"));
            query.setParameter("needle", String.format("%%%s%%", search));
            query.setParameter("needle2", user.get());
            return (long) query.uniqueResult();
        }
    }

}
