/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.util.ResourceBundle;

/**
 *
 * @author Oussama EZZIOURI
 */
public class T {
    
    public static String echo(String msgcode){
        
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("MessageBundle");
            return bundle.getString(msgcode);
        } catch (Exception e) {
            return "Message ["+msgcode+"] to be defined in i18n files for ["+Bootstrap.APP_PROP.getProperty("LANG")+"_"+Bootstrap.APP_PROP.getProperty("LOCAL")+"]";
        }
        
        
    }
    
}
