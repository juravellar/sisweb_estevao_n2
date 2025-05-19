package api.dao;

import java.util.List;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import api.util.HibernateUtil;
import api.models.ProdutoModel;

/**
 * DAO comum com métodos utilitários estáticos e Sessão/Hibernate Try‑with‑resources.
 */
public class ProdutoDao {

    public static Optional<ProdutoModel> buscarPorId(long id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(s.get(ProdutoModel.class, id));
        }
    }

    public static Optional<ProdutoModel> buscarPorNome(String nome) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM ProdutoModel WHERE nome = :nome", ProdutoModel.class)
                    .setParameter("nome", nome)
                    .uniqueResultOptional();
        }
    }

    public static Optional<ProdutoModel> buscarPorDescricao(String descricao) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM ProdutoModel WHERE descricao = :descricao", ProdutoModel.class)
                    .setParameter("descricao", descricao)
                    .uniqueResultOptional();
        }
    }

    public static Optional<ProdutoModel> buscarPorPreco(double preco) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM ProdutoModel WHERE preco = :preco", ProdutoModel.class)
                    .setParameter("preco", preco)
                    .uniqueResultOptional();
        }
    }

    // NOVO: busca combinada para checar produto duplicado
    public static Optional<ProdutoModel> buscarPorNomePrecoDescricao(String nome, double preco, String descricao) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM ProdutoModel p WHERE p.nome = :nome AND p.preco = :preco AND p.descricao = :descricao";
            Query<ProdutoModel> query = s.createQuery(hql, ProdutoModel.class);
            query.setParameter("nome", nome);
            query.setParameter("preco", preco);
            query.setParameter("descricao", descricao);
            query.setMaxResults(1);
            return query.uniqueResultOptional();
        }
    }

    public static List<ProdutoModel> listarTodos() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM ProdutoModel", ProdutoModel.class).list();
        }
    }

    public static void salvar(ProdutoModel produto) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.persist(produto);
            tx.commit();
        }
    }

    public static void editar(Optional<ProdutoModel> produto) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.merge(produto.orElseThrow());
            tx.commit();
        }
    }

    public static void deletar(ProdutoModel produto) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = s.beginTransaction();
            s.remove(produto);
            tx.commit();
        }
    }

    public static List<ProdutoModel> buscarPorFiltros(String busca, Double precoMin, Double precoMax) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM ProdutoModel p WHERE 1=1 ";

            if (busca != null && !busca.isBlank()) {
                hql += "AND (p.nome LIKE :busca OR str(p.id) = :buscaExata) ";
            }
            if (precoMin != null) {
                hql += "AND p.preco >= :precoMin ";
            }
            if (precoMax != null) {
                hql += "AND p.preco <= :precoMax ";
            }

            Query<ProdutoModel> query = s.createQuery(hql, ProdutoModel.class);

            if (busca != null && !busca.isBlank()) {
                query.setParameter("busca", "%" + busca + "%");
                query.setParameter("buscaExata", busca);
            }
            if (precoMin != null) {
                query.setParameter("precoMin", precoMin);
            }
            if (precoMax != null) {
                query.setParameter("precoMax", precoMax);
            }

            return query.list();
        }
    }
}
