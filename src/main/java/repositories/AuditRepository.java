package repositories;

import config.HibernateConfig;
import models.AuditItem;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class AuditRepository extends AbstractRepository {

    public List<AuditItem> getAudits() {
        return getAudits(50);
    }

    public List<AuditItem> getAudits(int limit) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {

            Query query = session.createQuery("from AuditItem order by localDateTime desc");

            query.setMaxResults(limit);

            return (List<AuditItem>) query.list();
        }
    }
}
