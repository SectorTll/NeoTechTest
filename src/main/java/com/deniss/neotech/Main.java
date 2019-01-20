package com.deniss.neotech;

import org.apache.log4j.Logger;

public class Main {


    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("App has been started.");


        if (args != null)
            for (String arg : args) {
                if ("-p".equalsIgnoreCase(arg)) {
                    logger.info("Argument -p is present report started..");
                    Reporter reporter = new Reporter();
                    reporter.showReport();
                    return;
                }
            }

        BaseFiller baseFiller = new BaseFiller();
        baseFiller.populate();
    }
}
