package api.util;

import api.models.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {

    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();

    private HibernateUtil() { }   // evita instanciar

    private static SessionFactory buildSessionFactory() {
        try {
            // Carrega hibernate.cfg.xml e registra as entidades
            Configuration cfg = new Configuration().configure();   // /resources/hibernate.cfg.xml

            // Se preferir adicionar classes manualmente:
            cfg.addAnnotatedClass(UsuarioModel.class);
            cfg.addAnnotatedClass(ProdutoModel.class);
            cfg.addAnnotatedClass(PedidoModel.class);
            cfg.addAnnotatedClass(ItemPedidoModel.class);

            return cfg.buildSessionFactory(
                    new StandardServiceRegistryBuilder()
                            .applySettings(cfg.getProperties())
                            .build());
        } catch (Exception ex) {
            System.err.println("Falha ao criar SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }

    public static void close() {
        if (SESSION_FACTORY != null) SESSION_FACTORY.close();
    }
}
