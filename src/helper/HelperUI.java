/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author OUSSAMA EZZIOURI
 *
 * User Interface Helper class to manage commun actions to be executed on
 * graphical objects.
 *
 */
public class HelperUI {

    public static String APP_NAME = "Jit Point of sale";
    public static String APP_VERSION = "1.0";
    public static Image APP_ICON_PATH = new Image("images/icon.png");

    public void maximizeScene(Scene scene, Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }

    //----------------------- END LOG and Messages -----------------------------
    //----------------------------- Date And Time ------------------------------
    /**
     *
     * @return
     */
    public static String getStrTimeStamp() {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format : Patter of datetime example : yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getStrTimeStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(today);

        return reportDate;
    }

    /**
     *
     * @param format
     * @return
     */
    public static Date getTimeStamp(String format) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        if (format == null) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateFormat df = new SimpleDateFormat(format);

        // Get the date today using Calendar object.
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(reportDate);
        } catch (ParseException ex) {
            Logger.getLogger(Bootstrap.class.getName()).log(Level.SEVERE, null, ex);
        }
        return date;
    }

    /**
     *
     * @param end
     * @param start
     * @return
     */
    public static long DiffInHours(Date end, Date start) {
        int duration = (int) (end.getTime() - start.getTime());
        long diffInHr = TimeUnit.MILLISECONDS.toHours(duration);
        return diffInHr;
    }

    /**
     *
     * @param end
     * @param start
     * @return
     */
    public static long DiffInMinutes(Date end, Date start) {
        int duration = (int) (end.getTime() - start.getTime());
        long diffInMin = TimeUnit.MILLISECONDS.toMinutes(duration);
        return diffInMin;
    }
    //-------------------------- End Date And Time -----------------------------

    //------------------------ Center JDialog in screen ------------------------
    /**
     *
     * @param jdialog
     *
     * Center the jdialog in the center of the screen
     */
    public static void centerJDialog(JDialog jdialog) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - jdialog.getWidth()) / 2;
        int y = (screenSize.height - jdialog.getHeight()) / 2;
        jdialog.setLocation(x, y);
    }

    /**
     *
     * @param jframe
     *
     * Center the jframe in the center of the screen
     */
    public static void centerJFrame(JFrame jframe) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - jframe.getWidth()) / 2;
        int y = (screenSize.height - jframe.getHeight()) / 2;
        jframe.setLocation(x, y);
    }

}
