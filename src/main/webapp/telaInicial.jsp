<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="produtos" value="${sessionScope.produtos}" />
<c:set var="usuario" value="${sessionScope.usuario}" />

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>StockBridge</title>

    <!-- Tailwind CDN para protótipo -->
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>

    <link rel="stylesheet" href="./assets/css/telaInicial.css">
    <link rel="stylesheet" href="./assets/css/botaoVoltar.css">
</head>
<body>

<!-- Botão voltar -->
<button class="flex w-30 h-fit items-center cursor-pointer justify-center rounded-md bg-white px-4 py-1.5 text-sm
               font-semibold text-indigo-600 shadow-sm border border-indigo-600 hover:bg-gray-100">
    <a href="login.jsp" class="flex w-full text-base cursor-pointer">&larr; Sair da conta</a>
</button>

<div class="flex min-h-full flex-col justify-center w-full px-6 py-12 lg:px-8">
    <!-- Cabeçalho -->
    <div class="sm:mx-auto sm:w-full sm:max-w-sm">
        <img class="mx-auto h-10 w-auto"
             src="https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=600"
             alt="Marketplace">
        <h2 class="mt-10 text-center text-2xl font-bold tracking-tight text-gray-900">
            Bem-vindo, <c:out value="${empty usuario ? 'Visitante' : usuario.nome}" />!
        </h2>
        <p class="mt-2 text-center text-sm text-gray-500">
            Encontre os melhores produtos com os melhores preços
        </p>
    </div>

    <!-- Lista de produtos -->
    <div class="mt-10 sm:mx-auto w-full">
        <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
            <c:choose>
                <c:when test="${empty produtos}">
                    <p class="text-gray-700 mb-4">Nenhum produto encontrado.</p>
                </c:when>
                <c:otherwise>
                    <c:forEach var="produto" items="${produtos}">
                        <div class="card-produto border rounded p-4 shadow hover:shadow-lg transition">
                            <img
                                    src="https://prd.place/400?id=${produto.nome}"
                                    alt="${produto.nome}"
                                    class="produto-imagem mb-4 rounded"
                            />
                            <div class="produto-conteudo">
                                <h3 class="nome-produto text-lg font-semibold mb-2">${produto.nome}</h3>
                                <p class="desc-produto text-gray-600 mb-4">${produto.descricao}</p>

                                <div class="produto-comprar flex justify-between items-center">
                                    <span class="preco text-indigo-700 font-bold text-lg">R$ ${produto.preco}</span>
                                    <button
                                            class="botao-adicionar-carrinho bg-indigo-600 text-white px-3 py-1 rounded hover:bg-indigo-500"
                                            onclick="adicionarAoCarrinho(${produto.id})"
                                            type="button"
                                    >
                                        Adicionar ao carrinho
                                    </button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>

        </div>

        <!-- Botões de ação -->
        <div class="botoes-acao mt-6 flex justify-between">
            <form action="produtos" method="get">
                <button class="botao-ver-mais-produtos">Ver mais produtos</button>
            </form>

            <!-- vai ao servlet para exibir carrinho persistente -->
            <form action="carrinho" method="get">
                <button class="botao-carrinho cursor-pointer">Meu carrinho</button>
            </form>
        </div>
    </div>
</div>

<script>
    function adicionarAoCarrinho(id) {
        const formData = new URLSearchParams();
        formData.append('acao', 'adicionar');
        formData.append('id', id);

        fetch('carrinho', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: formData.toString()
        })
            .then(res => {
                if (res.ok) {
                    alert('Produto adicionado ao carrinho!');
                    // opcional: atualizar a página ou carrinho visual
                } else {
                    alert('Erro ao adicionar produto ao carrinho');
                }
            })
            .catch(() => alert('Erro ao adicionar produto ao carrinho'));
    }
</script>
</body>
</html>
