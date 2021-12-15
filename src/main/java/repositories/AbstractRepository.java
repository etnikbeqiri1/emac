package repositories;

import config.HibernateConfig;
import models.AuditItem;
import models.AuditItem.AuditAction;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Column;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static models.AuditItem.*;
import static models.AuditItem.AuditAction.*;
import static org.apache.commons.lang3.StringUtils.*;
import static services.AuthenticationService.getUser;

public class AbstractRepository {

    public <T> T save(T t) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Serializable entityId = session.save(t);
            transaction.commit();

            T createdEntity = (T) session.get(t.getClass(), entityId);

            saveAudit(CREATED, t, EMPTY, createdEntity.toString());

            return createdEntity;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public <T> void delete(T t) {
        Transaction transaction = null;
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(t);
            transaction.commit();
            transaction = null;
            transaction = session.beginTransaction();

            saveAudit(DELETED, t, t.toString(), EMPTY);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public <T> T update(T t) {

        T entityBeforeUpdated = null;
        Transaction transaction = null;

        try (Session session = HibernateConfig.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.update(t);
            transaction.commit();
            transaction = session.beginTransaction();

            saveAudit(UPDATED, t, EMPTY, t.toString());

            transaction.commit();

            return t;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> listAll(Class<T> clazz) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery(format("from %s", clazz.getSimpleName()), clazz).list();
        }
    }

    public <T> List<String> getFields(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(field -> {
                    field.setAccessible(true);
                    return field.getAnnotation(Column.class);
                })
                .filter(Objects::nonNull)
                .map(Column::name)
                .collect(Collectors.toList());
    }

    public <E> void saveAudit(AuditAction auditAction, E entity, String... representations) {
        Transaction transaction = null;
        Session session;
        try {
            session = HibernateConfig.getSessionFactory().openSession();
            transaction = session.beginTransaction();

            session.save(builder()
                    .action(auditAction)
                    .beforeContent(String.valueOf(representations[0]))
                    .user(getUser().orElse(null))
                    .afterContent(String.valueOf(representations[1]))
                    .entityName(entity.getClass().getSimpleName())
                    .build());

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
