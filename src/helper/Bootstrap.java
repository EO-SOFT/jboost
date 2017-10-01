/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import static db.DBInstance.log;
import static db.DBInstance.logFileHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;

/**
 *
 * @author OUSSAMA EZZIOURI
 */
public class Bootstrap {
    /**
     *
     */
    private static MessageDigest digester;

    /**
     *
     */
    public static String HOSTNAME;
    
    /**
     *
     */
    public static String APP_NAME = "[YOUR APPLICATIO NAME]";
    
    /**
     *
     */
    public static String APP_VERSION = "v1.0";

    /**
     *
     */
    public final static Properties APP_PROP = new Properties();

    //----------------------------- MD5 BLOC ---------------------------------
    static {
        try {
            digester = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        try {
            HOSTNAME = InetAddress.getLocalHost().getHostName().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param str
     * @return
     */
    public static String crypt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("String to encript cannot be null or zero length");
        }

        digester.update(str.getBytes());
        byte[] hash = digester.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            } else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }
        return hexString.toString();
    }

    //-------------------------- Log folder Path ------------------------------
    /**
     *
     * @param path
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void InitDailyLogFile(String path) {
        // if the main log directory does not exist, create it        
        File log_dir = new File(path);
        if (!log_dir.exists()) {
            log_dir.mkdir();
        }

        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String today = dateFormat.format(now);
        File today_log_dir = new File(path + today);

        // if the today log directory does not exist, create it
        if (!today_log_dir.exists()) {
            today_log_dir.mkdir();
        }
        try {
            String log_path = today_log_dir + File.separator + today + ".log";
            File file = new File(log_path);

            if (file.createNewFile()) {
                System.out.println("File " + file.getName() + " is created!");
            } else {
                System.out.println("File " + file.getName() + " already exists.");
            }
            //Intilize the logFileHandler, formating etc...

            logFileHandler = new FileHandler(log_path, true);
            logFileHandler.setFormatter(new SimpleFormatter());
            //Add the file handler to log object.            
            log = Logger.getLogger(log_path);
            log.addHandler(logFileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param print_dir_path
     * @param palet_dir
     * @param galia_dir
     * @param dispatch_dir
     */
    @SuppressWarnings("CallToThreadDumpStack")
    public static void InitDailyDestPrintDir(String print_dir_path) {
        // if the printing output printing
        File print_dir = new File(print_dir_path);
        if (!print_dir.exists()) {
            print_dir.mkdir();
        }

        // if the today printing directory does not exist, create it
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String today = dateFormat.format(now);

        File today_print_dir = new File(print_dir_path + today);
        if (!today_print_dir.exists()) {
            today_print_dir.mkdir();
            log.info("Print directory [" + today_print_dir.getPath() + "] is created!\n");
        } else {
            log.info("Print directory [" + today_print_dir.getPath() + "] already exist!\n");
        }
    }

    /**
     * Load DBInstance.APP_PROP attribut values from config.properties
     */
    public static void loadConfigProperties() {

        /// read from file
        InputStream input = null;
        try {
            input = new FileInputStream(".\\src\\config.properties");
            // load a properties file
            APP_PROP.load(input);
            // get the property value and print it out
            System.out.println("Load properties file :\n " + APP_PROP.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Config properties error !", ERROR_MESSAGE);
            JOptionPane.showMessageDialog(null, "Properties file must be in the same folder as the .jar file.", "Config properties error !", WARNING_MESSAGE);
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
