package com.deniss.neotech;

import com.deniss.neotech.db.HibernateUtil;
import com.deniss.neotech.db.StoredTime;
import org.apache.log4j.Logger;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;


/**
 * Class showing data from db in inserting orders
 */
class Reporter {

    private final Logger logger = Logger.getLogger(Reporter.class);

    void showReport() {
        logger.info("------------------------------------------------------------------------------");
        try {

            Session session = HibernateUtil.getSessionFactory().openSession();
            ScrollableResults scrollableResults = session.createQuery("from StoredTime ").setReadOnly(true).setCacheable(false).scroll(ScrollMode.FORWARD_ONLY);

            int count = 0;
            while (scrollableResults.next()) {
                logger.info("Stored time: " + ((StoredTime) scrollableResults.get(0)).getTheTime());

                if (++count > 0 && count % 10 == 0) {
                    logger.info("Loaded " + count + " results");
                }
            }
        } catch (Exception e) {
            logger.error("Unexpected exception during attempt to load data from db ", e);

        }
        logger.info("---------------------------End of report---------------------------------------");
    }
}
