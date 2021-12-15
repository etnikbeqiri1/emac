package repositories;

import config.HibernateConfig;
import models.Subject;
import models.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class SubjectRepository extends AbstractRepository {
    public List<Subject> listSubjects(String search, int from, int limit, String sortBy, Boolean asc){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("from Subject where subject_name like :needle ORDER BY %s %s", sortBy, asc ? "ASC" : "DESC"));
            query.setParameter("needle", String.format("%%%s%%", search));
            query.setFirstResult(from);
            query.setMaxResults(limit);
            return (List<Subject>) query.list();
        }
    }
    public int getSubjectCount() { return this.listAll(Subject.class).size(); }
    public List<Subject> listAllSubjects(){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("from Subject"));
            return (List<Subject>) query.list();
        }
    }


    public long countResults(String search){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("select count(*) from Subject where subject_name like :needle"));
            query.setParameter("needle", String.format("%%%s%%", search));
            return (long) query.uniqueResult();
        }
    }
    public boolean existsByName(String name){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("select count(*) from Subject where subject_name = :name"));
            query.setParameter("name", name);
            return !query.uniqueResult().equals(0L);
        }
    }
}
