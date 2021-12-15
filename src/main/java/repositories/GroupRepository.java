package repositories;

import config.HibernateConfig;
import models.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.AuthenticationService;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

public class GroupRepository extends AbstractRepository {

    AuthenticationService authenticationService;

    public GroupRepository(){
        authenticationService = AuthenticationService.getInstance();
    }

    public List<Group> listGroups(String search, int from, int limit, String sortBy, Boolean asc){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("from Group where professor = :professor AND (name like :needle) ORDER BY %s %s", sortBy, asc ? "ASC" : "DESC"));
            query.setParameter("needle", String.format("%%%s%%", search));
            query.setParameter("professor", authenticationService.getUser().get());

            query.setFirstResult(from);
            query.setMaxResults(limit);
            return (List<Group>) query.list();
        }
    }

    public List<Group> listAllGroups(){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            AuthenticationService auth = AuthenticationService.getInstance();
            Optional<User> user = auth.getUser();
            Query query = session.createQuery(
                    String.format("from Group where professor = :professor"));
            query.setParameter("professor", user.get());
            return (List<Group>) query.list();
        }
    }

    public List<Group> getGroups(){
        return this.listAll(Group.class);
    }
    public List<Group> getGroups(User user){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(String.format("from Group where professor = :professor")).setParameter("professor",user).list();
        }
    }
    public long countResults(String search){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("select count(*) from Group where professor = :professor AND (name like :needle)"));
            query.setParameter("needle", String.format("%%%s%%", search));
            query.setParameter("professor", authenticationService.getUser().get());
            return (long) query.uniqueResult();
        }
    }

    public int getGroupCount(){
        return this.listAll(Group.class).size();
    }
    
    public boolean existsByName(String name){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("select count(*) from Group where professor = :professor AND (name = :name)"));
            query.setParameter("name", name);
            query.setParameter("professor", authenticationService.getUser().get());
            return !query.uniqueResult().equals(0L);
        }
    }
}
