package api.models;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import jakarta.persistence.*;

@Entity
@Table(name = "produtos")
@SQLDelete(sql = "UPDATE produtos SET ativo = false WHERE id = ?")
@Where(clause = "ativo = true")                // consultas padrão só veem ativos
public class ProdutoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private double preco;

    @Column
    private String descricao = "";

    @Column(nullable = false)
    private boolean ativo = true;              // default true

    /* ---------- construtores ---------- */
    public ProdutoModel() {}

    public ProdutoModel(int id, String nome, double preco, String descricao, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public ProdutoModel(String nome, double preco, String descricao) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
    }

    /* ---------- getters / setters ---------- */
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
