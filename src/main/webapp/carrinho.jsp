<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt" %>

<c:set var="carrinho" value="${requestScope.carrinho}" />
<c:set var="detalhes" value="${requestScope.detalhes}" />

<!DOCTYPE html>
<html lang="pt-BR" xml:lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Carrinho de Compras</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/carrinho.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/botaoVoltar.css">
</head>
<body>

<button class="botao-voltar">
    <a href="${pageContext.request.contextPath}/telaInicial.jsp">&larr; Voltar</a>
</button>

<h1>Carrinho de Compras</h1>

<div class="container">

    <!-- 1. Lista de produtos ------------------------------------------- -->
    <div class="produtos">
        <h2>Produtos</h2>

        <c:choose>
            <c:when test="${empty carrinho}">
                <p>O carrinho está vazio.</p>
            </c:when>

            <c:otherwise>
                <ul id="lista-produtos">
                    <c:set var="total" value="0" scope="page"/>
                    <c:forEach var="entry" items="${carrinho}">
                        <c:set var="idProd" value="${entry.key}"/>
                        <c:set var="qtd"    value="${entry.value}"/>
                        <c:set var="p" value="${detalhes[idProd]}"/>

                        <li class="produto-item">
                            <strong class="nome-produto">${p.nome}</strong>
                            <div class="informacoes">
                               <span>Preço: R$
                                   <fmt:formatNumber value="${p.preco}"
                                                     type="number"
                                                     minFractionDigits="2"/>
                               </span>
                                <span>Quantidade: ${qtd}</span>
                            </div>

                            <div class="controle-quantidade">
                                <a class="btn-quantidade"
                                   href="${pageContext.request.contextPath}/carrinho?acao=remover&id=${idProd}">-</a>
                                <span class="quantidade-atual">${qtd}</span>
                                <a class="btn-quantidade"
                                   href="${pageContext.request.contextPath}/carrinho?acao=adicionar&id=${idProd}">+</a>
                            </div>
                        </li>

                        <c:set var="total"
                               value="${total + (p.preco * qtd)}"
                               scope="page"/>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="usuario">
        <h2>Informações do Usuário</h2>
        <c:if test="${not empty sessionScope.usuario}">
            <p><strong>Nome:</strong>  ${sessionScope.usuario.nome}</p>
            <p><strong>Email:</strong> ${sessionScope.usuario.email}</p>
        </c:if>
    </div>

    <div class="total">
        <h2>Total do Carrinho</h2>
        <p><strong>Valor Total:</strong>
            R$ <fmt:formatNumber value="${total}" type="number"
                                 minFractionDigits="2"/>
        </p>
    </div>

    <button id="btn-finalizar"
            class="finalizar-compra bg-green-600 text-white px-3 py-1 rounded hover:bg-green-500"
            onclick="finalizarCompra()" type="button"
            <c:if test='${empty carrinho}'>disabled</c:if>>
        Finalizar Compra
    </button>

    <!-- mensagem de erro opcional -->
    <c:if test="${not empty requestScope.erro}">
        <p style="color:red">${requestScope.erro}</p>
    </c:if>

</div>
<script>
    function finalizarCompra() {
        const btn = document.getElementById('btn-finalizar');
        if (btn.hasAttribute('disabled')) return;

        const formData = new URLSearchParams();
        formData.append('acao', 'finalizar');

        fetch('${pageContext.request.contextPath}/carrinho', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString(),
            credentials: 'same-origin'
        })
            .then(async res => {
                if (res.ok) {
                    alert('Compra finalizada com sucesso!');
                    window.location.href = '${pageContext.request.contextPath}/pedidoConfirmado.jsp';
                } else {
                    const msg = await res.text();
                    alert(msg || 'Erro ao finalizar a compra');
                }
            })
            .catch(() => alert('Erro ao finalizar a compra'));
    }
</script>
</body>
</html>