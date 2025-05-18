<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Carrinho de Compras</title>
    <link rel="stylesheet" type="text/css" href="assets/css/carrinho.css">
    <link rel="stylesheet" href="./assets/css/botaoVoltar.css">
</head>
<body>

<button class="botao-voltar">
    <a href="telaInicial.jsp">&larr; Voltar</a>
</button>

<h1>Carrinho de Compras</h1>

<div class="container">

    <!-- Lista de produtos no carrinho -->
    <div class="produtos">
        <h2>Produtos</h2>
        <ul id="lista-produtos">
            <c:choose>
                <c:when test="${empty usuario.itensCarrinho}">
                    <p>O carrinho está vazio.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="item" items="${usuario.itensCarrinho}">
                        <li class="produto-item">
                            <strong class="nome-produto">${item.produto.nome}</strong>
                            <div class="informacoes">
                                <span>Preço: R$ ${item.produto.preco}</span>
                                <span>Quantidade: ${item.quantidade}</span>
                            </div>
                            <div class="controle-quantidade">
                                <a href="carrinho?acao=remover&id=${item.produto.id}" class="btn-quantidade">-</a>
                                <span class="quantidade-atual">${item.quantidade}</span>
                                <a href="carrinho?acao=adicionar&id=${item.produto.id}" class="btn-quantidade">+</a>
                            </div>
                        </li>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>

    <!-- Informações do usuário -->
    <div class="usuario">
        <h2>Informações do Usuário</h2>
        <p><strong>Nome:</strong> ${usuario.nome}</p>
        <p><strong>Email:</strong> ${usuario.email}</p>
    </div>

    <!-- Total -->
    <div class="total">
        <h2>Total do Carrinho</h2>
        <c:set var="total" value="0"/>
        <c:forEach var="item" items="${usuario.itensCarrinho}">
            <c:set var="total" value="${total + (item.produto.preco * item.quantidade)}"/>
        </c:forEach>
        <p><strong>Valor Total:</strong> R$ ${total}</p>
    </div>

    <!-- Botão de finalizar compra -->
    <form action="carrinho" method="post" class="form-finalizar">
        <input type="hidden" name="acao" value="finalizar"/>
        <button type="submit" class="finalizar-compra">Finalizar Compra</button>
    </form>

</div>
</body>
</html>