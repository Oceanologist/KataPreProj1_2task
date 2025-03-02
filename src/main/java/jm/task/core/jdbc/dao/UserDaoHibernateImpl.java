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
        String sql = "CREATE TABLE IF NOT EXISTS kata_preproj_1_2task.USERS_TABLE (" +
                "  ID BIGINT NOT NULL AUTO_INCREMENT," +
                "  NAME VARCHAR(100) NOT NULL," +
                "  LAST_NAME VARCHAR(100) NOT NULL," +
                "  AGE TINYINT NOT NULL," +
                "  PRIMARY KEY (ID));";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Ошибка при создании таблицы");
            throw new RuntimeException(e);
        }
        if (session != null)
            session.close();


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
            if (transaction != null)
                transaction.rollback();
            System.err.println("Ошибка при удалении таблицы");
            throw new RuntimeException(e);
        }
        if (session != null)
            session.close();

    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO USERS_TABLE ( NAME, LAST_NAME, AGE) VALUES (:name, :lastName, :age)";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).
                    setParameter("NAME", name).
                    setParameter("LAST_NAME", lastName).
                    setParameter("AGE", age).
                    executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Ошибка при внесении " + name + lastName + " в таблицу");
            e.printStackTrace();
        }
        if (session != null)
            session.close();

    }


    @Override
    public void removeUserById(long id) {
        String sql = "DELETE FROM kata_preproj_1_2task.USERS_TABLE WHERE id =:ID";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).
                    setParameter("ID", id).
                    executeUpdate();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Ошибка при удалении пользователя под № " + id + " из таблицы");
            e.printStackTrace();
        }
        if (session != null)
            session.close();

    }


    @Override
            /* вообще не уверен нужна-ли здесь транзакция, по идее мы непроводим
            в этом методе изменение данных в таблице, решил не писать.
                            */
    public List<User> getAllUsers() {
        Session session = Util.getSessionFactory().openSession();

        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM kata_preproj_1_2task.USERS_TABLE";

        try {
            Query<User> query = session.createSQLQuery(sql).addEntity(User.class);
            userList = query.list();


        } catch (HibernateException e) {
            System.err.println("Ошибка при получении данных:");
        }
        session.close();
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        String sql = "TRUNCATE TABLE kata_preproj_1_2task.USERS_TABLE";
        Session session = Util.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.createNativeQuery(sql).
                    executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null)
                transaction.rollback();
            System.err.println("Ошибка при удалении пользователей в таблице");
            e.printStackTrace();
        }
        if (session != null)
            session.close();

    }

}
