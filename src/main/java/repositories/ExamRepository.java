package repositories;

import config.HibernateConfig;
import models.Exam;
import models.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.AuthenticationService;

import java.util.List;
import java.util.Optional;

public class ExamRepository extends AbstractRepository{
    public int getExamsCount(){
        return this.listAllExams().size();
    }
    public int getAllExamsCount(){
        return this.listAll(Exam.class).size();
    }

    public List<Exam> listAllExams(){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("from Exam where professor=:needle2"));
            query.setParameter("needle2", user.get());
            return (List<Exam>) query.list();
        }
    }

    public List<Exam> listExams(String search, int from, int limit, String sortBy, Boolean asc){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("from Exam where name like :needle AND professor=:needle2 ORDER BY %s %s", sortBy, asc ? "ASC" : "DESC"));
            query.setParameter("needle2", user.get());
            query.setParameter("needle", String.format("%%%s%%", search));
            query.setFirstResult(from);
            query.setMaxResults(limit);
            return (List<Exam>) query.list();
        }
    }

    public long countResults(String search){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("select count(*) from Exam where name like :needle AND professor=:needle2"));
            query.setParameter("needle", String.format("%%%s%%", search));
            query.setParameter("needle2", user.get());
            return (long) query.uniqueResult();
        }
    }
}
