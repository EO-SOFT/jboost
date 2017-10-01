package test;


import helper.Bootstrap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Oussama EZZIOURI
 */
public class Test {

    public static void main(String[] args) {
        //Load the properties of the application
        Bootstrap.loadConfigProperties();
        Bootstrap.InitDailyLogFile(Bootstrap.APP_PROP.getProperty("LOG_PATH"));
        Bootstrap.InitDailyDestPrintDir(Bootstrap.APP_PROP.getProperty("PRINT_DIR"));
    }

}
