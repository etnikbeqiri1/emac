package repositories;

import config.HibernateConfig;
import exceptions.GenericException;
import models.Group;
import models.User;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.AuthenticationService;

import java.util.List;
import java.util.Optional;

public class UserRepository extends AbstractRepository {
    public boolean checkLogin(String username, String password) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    "from User where username=:username and password=:password");
            query.setParameter("username", username);
            query.setParameter("password", password);
            List<User> users = query.list();
            System.out.println(users);
            return users.size() > 0;
        }
    }
    public List<User> listAllUsers(){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("from User"));
            return (List<User>) query.list();
        }
    }

    public User getUserByUsername(String username) throws GenericException{
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    "from User where username=:username");
            query.setParameter("username", username);
            List<User> users = query.list();
            if(users.size() == 0){
                throw new GenericException("User with username "+username+" does not exist");
            }
            return users.get(0);
        }
    }
    public int getUsersCount(){
        return this.listAll(User.class).size();
    }


    public List<User> listUsers(String search, int from, int limit, String sortBy, Boolean asc){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("from User where user_full_name like :needle OR username like :needle ORDER BY %s %s", sortBy, asc ? "ASC" : "DESC"));
            query.setParameter("needle", String.format("%%%s%%", search));
            query.setFirstResult(from);
            query.setMaxResults(limit);
            return (List<User>) query.list();
        }
    }


    public long countResults(String search){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("select count(*) from User where user_full_name like :needle OR username like :needle"));
            query.setParameter("needle", String.format("%%%s%%", search));
            return (long) query.uniqueResult();
        }
    }

    public boolean existsByUsername(String username){
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Query query = session.createQuery(
                    String.format("from User where username = :needle"));
            query.setParameter("needle", username);
            return query.list().size() > 0;
        }
    }
}
