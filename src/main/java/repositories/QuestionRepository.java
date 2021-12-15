package repositories;

import config.HibernateConfig;
import models.Exam;
import models.Question;
import models.User;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import services.AuthenticationService;

import java.util.List;
import java.util.Optional;

public class QuestionRepository  extends AbstractRepository{
    public List<Question> listAllQuestions(){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("from Question where professor=:needle2"));
            query.setParameter("needle2", user.get());
            return (List<Question>) query.list();
        }
    }
    public List<Question> listQuestionsByExam(Exam exam){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("from Question where professor=:needle2 and exam=:needle1"));
            query.setParameter("needle2", user.get());
            query.setParameter("needle1", exam);
            return (List<Question>) query.list();

        }
    }

    public List<Integer> getDistinctPoints(Exam exam){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Criteria criteria = session.createCriteria(Question.class);
            criteria.setProjection(Projections.distinct(Projections.property("points")));
            criteria.add(Restrictions.eq("exam", exam));
            List<Integer> msgFromList = criteria.list();
            return msgFromList;
        }
    }

    public List<Question> getQuestionsByPoints(Exam exam, int points, int limit){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("from Question where professor=:needle2 and exam=:needle1 and points=:needle3 ORDER BY rand()"));
            query.setParameter("needle2", user.get());
            query.setParameter("needle1", exam);
            query.setParameter("needle3", points);
            query.setMaxResults(limit);
            return (List<Question>) query.list();
        }
    }

    public long countQuestionsByPoints(Exam exam, int points){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("select count(*) from Question where professor=:needle2 and exam=:needle1 and points=:needle3 "));
            query.setParameter("needle2", AuthenticationService.getInstance().getUser().get());
            query.setParameter("needle1", exam);
            query.setParameter("needle3", points);
            return (long) query.uniqueResult();
        }
    }
}
