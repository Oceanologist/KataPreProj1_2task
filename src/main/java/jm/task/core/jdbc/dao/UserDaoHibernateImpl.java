package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jm.task.core.jdbc.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS kata_preproj_1_2task.USERS_TABLE (" + "  ID BIGINT NOT NULL AUTO_INCREMENT," + "  NAME VARCHAR(100) NOT NULL," + "  LAST_NAME VARCHAR(100) NOT NULL," + "  AGE TINYINT NOT NULL," + "  PRIMARY KEY (ID));";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Ошибка при создании таблицы");
            throw new RuntimeException(e);
        } finally {
            session.close();
        }

    }


    @Override
    public void dropUsersTable() {
        String sql = "DROP TABLE IF EXISTS USERS_TABLE ";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Ошибка при удалении таблицы");
            throw new RuntimeException(e);
        } finally {
            session.close();
        }

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        User user = new User(name, lastName, age);
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Ошибка при внесении " + name + lastName + " в таблицу");
            e.printStackTrace();
        } finally {
            session.close();
        }

    }


    @Override
    public void removeUserById(long id) {
        String hql = "DELETE FROM User WHERE id =:userId";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createQuery(hql).setParameter("userId", id);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Ошибка при удалении пользователя под № " + id + " из таблицы");
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    @Override
    public List<User> getAllUsers() {
        Session session = Util.getSessionFactory().openSession();
        List<User> userList = new ArrayList<>();
        try {
            session.beginTransaction();
            userList = session.createQuery("select s from User as s").getResultList();
            session.getTransaction().commit();
        } catch (HibernateException e) {
            System.err.println("Ошибка при получении данных:");
        } finally {
            session.close();
        }

        return userList;
    }

    @Override
    public void cleanUsersTable() {
        String hql = "TRUNCATE TABLE kata_preproj_1_2task.USERS_TABLE";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createNativeQuery(hql).executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Ошибка при удалении пользователей в таблице");
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

}
