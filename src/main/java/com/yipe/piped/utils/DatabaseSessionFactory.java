package com.yipe.piped.utils;

import com.yipe.piped.consts.Constants;
import com.yipe.piped.utils.obj.db.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.Configuration;

public class DatabaseSessionFactory {

    private static  SessionFactory sessionFactory;

    static {
        try {
            final Configuration configuration = new Configuration();

            Constants.hibernateProperties.forEach(configuration::setProperty);
            configuration.configure();

            sessionFactory = configuration.addAnnotatedClass(User.class).addAnnotatedClass(Channel.class)
                    .addAnnotatedClass(Video.class).addAnnotatedClass(PubSub.class).addAnnotatedClass(Playlist.class)
                    .addAnnotatedClass(PlaylistVideo.class).addAnnotatedClass(UnauthenticatedSubscription.class).buildSessionFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Session createSession() {
        return sessionFactory.openSession();
    }

    public static StatelessSession createStatelessSession() {
        return sessionFactory.openStatelessSession();
    }

    public static void close() {
        sessionFactory.close();
    }
}
