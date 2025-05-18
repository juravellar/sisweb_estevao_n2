package api.dao;

import api.config.HibernateUtil;
import api.models.UsuarioModel;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Optional;

public class UsuarioDao {

    public static Optional<UsuarioModel> buscarPorId(long id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(UsuarioModel.class, id));
        }
    }

    public static Optional<UsuarioModel> buscarPorEmail(String email) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery(
                            "FROM api.models.UsuarioModel WHERE email = :email",
                            UsuarioModel.class)
                    .setParameter("email", email)
                    .uniqueResultOptional();
        }
    }

    public static void salvar(UsuarioModel usuario) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.persist(usuario);
            tx.commit();
        }
    }

    public static void atualizar(UsuarioModel usuario) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.merge(usuario);
            tx.commit();
        }
    }

    public static boolean temUsuariosCadastrados() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long count = session.createQuery("select count(u) from UsuarioModel u", Long.class).uniqueResult();
            return count != null && count > 0;
        }
    }
}
