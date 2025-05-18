package api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "itens_carrinho")
public class ItemCarrinhoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UsuarioModel usuario;

    @ManyToOne
    private ProdutoModel produto;

    private int quantidade;

    private double precoTotal;

    // Getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UsuarioModel getUsuario() { return usuario; }
    public void setUsuario(UsuarioModel usuario) { this.usuario = usuario; }

    public ProdutoModel getProduto() { return produto; }
    public void setProduto(ProdutoModel produto) { this.produto = produto; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
}
