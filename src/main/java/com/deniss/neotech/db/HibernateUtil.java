package com.deniss.neotech.db;

    import com.deniss.neotech.Main;
    import org.apache.log4j.Logger;
    import org.hibernate.SessionFactory;
    import org.hibernate.cfg.Configuration;
    import org.hibernate.service.ServiceRegistry;
    import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();
    private static ServiceRegistry serviceRegistry;
    private static final Logger logger = Logger.getLogger(Main.class);

    private static SessionFactory buildSessionFactory() {
        try {

            Configuration configuration = new Configuration();
            configuration.configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();

            return configuration.buildSessionFactory(serviceRegistry);
        }
        catch (Throwable ex) {
            logger.error("SessionFactory creation failed.", ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}
