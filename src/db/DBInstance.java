package db;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import interfaces.LoggerInterface;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import org.hibernate.Session;

/**
 *
 * @author OUSSAMA EZZIOURI
 * 
 * Database access class. It manage commun actions to be executed
 * on the database using an Hibernate session instance.
 * 
 */
public class DBInstance implements LoggerInterface{

    private static DBInstance instance;
    
    public DBInstance() {
    }
    
    public static DBInstance getInstance() {
        
     if(instance == null) {
        synchronized (DBInstance.class) {
           if(instance == null) {
              instance = new DBInstance();
              
           }
        }
     }     
     return instance;
   }

    private static Session session = HibernateUtil.getInstance().openSession();

    public static Session getSession() {        
        return session;
    }

    public static void setSession(Session session) {
        DBInstance.session = session;
    }
    
    //-------------------------- LOG and Messages -----------------------------
    /**
     *
     */
    public static Logger log = null;
    
    public static FileHandler logFileHandler;

    /**
     *
     */
    public static String ERR0000_DB_CONNECT_FAILED = "com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: Communications link failure";
    
    public static void startSession() {
        DBInstance.session.flush();
        DBInstance.session.clear();
        try {
            DBInstance.session.beginTransaction();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, DBInstance.ERR0000_DB_CONNECT_FAILED, "Database error !", ERROR_MESSAGE);
            System.err.println("Initial SessionFactory creation failed." + e.getMessage());
        }
    }

    @Override
    public void info(String msg) {
        this.log.info(msg);
    }

    @Override
    public void fine(String msg) {
        this.log.fine(msg);
    }

    @Override
    public void finer(String msg) {
        this.log.finer(msg);
    }

    @Override
    public void finest(String msg) {
        this.log.finest(msg);
    }

    @Override
    public void warning(String msg) {
        this.log.warning(msg);
    }

    @Override
    public void severe(String msg) {
        this.log.severe(msg);
    }

}
