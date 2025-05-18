<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"       prefix="c"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>

<%
    /* -------- redireciona se não for admin (fica só aqui para acessar HttpSession) -------- */
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
                                <td class="py-2 px-4 border-b text-center space-x-1">

                                    <!-- EDITAR -->
                                    <form action="${pageContext.request.contextPath}/produto/editar"
                                          method="post" class="inline">
                                        <input type="hidden" name="id"               value="${p.id}">
                                        <input type="hidden" name="nome-produto"     value="${p.nome}">
                                        <input type="hidden" name="preco-produto"    value="${p.preco}">
                                        <input type="hidden" name="descricao-produto"value="${p.descricao}">
                                        <button class="bg-blue-100 hover:bg-blue-200 text-blue-800
                                                           py-1 px-2 rounded text-xs">
                                            Editar
                                        </button>
                                    </form>

                                    <!-- EXCLUIR -->
                                    <form action="${pageContext.request.contextPath}/produto/deletar"
                                          method="post" class="inline"
                                          onsubmit="return confirm('Tem certeza?');">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button class="bg-red-100 hover:bg-red-200 text-red-800
                                                           py-1 px-2 rounded text-xs">
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

    <!-- FORMULÁRIO DE CADASTRO -->
    <form action="${pageContext.request.contextPath}/produto"
          method="post"
          class="bg-white p-6 rounded shadow-md space-y-4">

        <h3 class="text-xl font-semibold">Adicionar Novo Produto</h3>

        <label class="block">
            <span class="text-gray-700">Nome do Produto</span>
            <input name="nome-produto" type="text"
                   class="mt-1 border rounded p-2 w-full" required>
        </label>

        <label class="block">
            <span class="text-gray-700">Preço</span>
            <input name="preco-produto" type="number" step="0.01"
                   class="mt-1 border rounded p-2 w-full" required>
        </label>

        <label class="block">
            <span class="text-gray-700">Descrição</span>
            <input name="descricao-produto" type="text"
                   class="mt-1 border rounded p-2 w-full" required>
        </label>

        <button type="submit"
                class="bg-blue-600 hover:bg-blue-500 text-white p-2 rounded w-full md:w-auto">
            Adicionar Produto
        </button>
    </form>

</div>
</body>
</html>
