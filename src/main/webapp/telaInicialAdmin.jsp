<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"       prefix="c"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>

<%
    String tipo = (String) session.getAttribute("tipo");
    if (!"admin".equals(tipo)) {
        response.sendRedirect(
                "login.jsp?erro=" +
                        java.net.URLEncoder.encode("Sem permissão para acessar esta página.",
                                java.nio.charset.StandardCharsets.UTF_8)
        );
        return;
    }
%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Painel do Administrador</title>

    <!-- Tailwind (protótipo) -->
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>

    <link rel="stylesheet" href="assets/css/botaoVoltar.css">
</head>
<body class="bg-gray-100 font-sans leading-normal tracking-normal">

<div class="container mx-auto px-4 py-8 space-y-6">

    <!-- Cabeçalho -->
    <header class="flex items-center w-full">
        <a href="login.jsp"
           class="flex items-center rounded-md bg-white px-4 py-1.5
                  text-sm font-semibold text-indigo-600 shadow-sm border
                  border-indigo-600 hover:bg-gray-100">
            &larr; Sair da conta
        </a>
        <h1 class="flex-1 text-3xl font-bold text-center">Painel do Administrador</h1>
    </header>

    <!-- Feedback -->
    <c:if test="${not empty erro}">
        <div class="bg-red-200 text-red-900 p-3 rounded">${erro}</div>
    </c:if>
    <c:if test="${not empty sucesso}">
        <div class="bg-green-200 text-green-900 p-3 rounded">${sucesso}</div>
    </c:if>

    <!-- LISTA DE PRODUTOS -->
    <section class="bg-white p-6 rounded shadow-md">
        <h2 class="text-xl font-semibold mb-4">Produtos Cadastrados</h2>

        <c:choose>
            <c:when test="${empty produtos}">
                <p class="text-gray-700">Nenhum produto cadastrado!</p>
            </c:when>

            <c:otherwise>
                <p class="text-green-600 mb-4">
                    Foram encontrados <strong>${fn:length(produtos)}</strong> produtos.
                </p>

                <div class="overflow-x-auto">
                    <table class="min-w-full border border-gray-200 text-sm">
                        <thead class="bg-gray-100">
                        <tr>
                            <th class="py-2 px-4 border-b text-left">Nome</th>
                            <th class="py-2 px-4 border-b text-left">Preço</th>
                            <th class="py-2 px-4 border-b text-center">Ações</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="p" items="${produtos}">
                            <tr class="hover:bg-gray-50">
                                <td class="py-2 px-4 border-b">${p.nome}</td>
                                <td class="py-2 px-4 border-b">R$ ${p.preco}</td>
                                <td class="py-2 px-4 border-b text-center space-x-2">

                                    <!-- Botão Editar: carrega produto para edição -->
                                    <a href="telaInicialAdmin?editarId=${p.id}"
                                       class="bg-blue-100 hover:bg-blue-200 text-blue-800
                                              py-1 px-2 rounded text-xs inline-block">
                                        Editar
                                    </a>

                                    <!-- Botão Excluir -->
                                    <form action="produto/deletar" method="post" class="inline">
                                        <input type="hidden" name="id" value="${p.id}" />
                                        <button type="submit"
                                                class="bg-red-100 hover:bg-red-200
                                                       text-red-800 py-1 px-2 rounded text-xs"
                                                onclick="return confirm('Confirmar exclusão?')">
                                            Excluir
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </section>

    <!-- FORMULÁRIO DE CADASTRO / EDIÇÃO -->
    <section class="bg-white p-6 rounded shadow-md max-w-md mx-auto">
        <c:set var="produto" value="${produtoEditar}" />
        <c:set var="isEdicao" value="${not empty produto}" />

        <form action="produto${isEdicao ? '/editar' : ''}" method="post"
              class="space-y-4">

            <h3 class="text-xl font-semibold">
                <c:choose>
                    <c:when test="${isEdicao}">Editar Produto</c:when>
                    <c:otherwise>Adicionar Novo Produto</c:otherwise>
                </c:choose>
            </h3>

            <c:if test="${isEdicao}">
                <input type="hidden" name="id" value="${produto.id}" />
            </c:if>

            <label class="block">
                <span class="text-gray-700">Nome do Produto</span>
                <input type="text" name="nome-produto" required
                       class="mt-1 border rounded p-2 w-full"
                       value="${isEdicao ? produto.nome : ''}" />
            </label>

            <label class="block">
                <span class="text-gray-700">Preço</span>
                <input type="number" step="0.01" name="preco-produto" required
                       class="mt-1 border rounded p-2 w-full"
                       value="${isEdicao ? produto.preco : ''}" />
            </label>

            <label class="block">
                <span class="text-gray-700">Descrição</span>
                <input type="text" name="descricao-produto"
                       class="mt-1 border rounded p-2 w-full"
                       value="${isEdicao ? produto.descricao : ''}" />
            </label>

            <button type="submit"
                    class="bg-indigo-600 hover:bg-indigo-500
                           text-white p-2 rounded w-full md:w-auto">
                <c:choose>
                    <c:when test="${isEdicao}">Salvar Alterações</c:when>
                    <c:otherwise>Adicionar Produto</c:otherwise>
                </c:choose>
            </button>

            <c:if test="${isEdicao}">
                <a href="telaInicialAdmin"
                   class="ml-4 text-gray-600 hover:text-gray-900 inline-block mt-2">
                    Cancelar edição
                </a>
            </c:if>

        </form>
    </section>

</div>

</body>
</html>
