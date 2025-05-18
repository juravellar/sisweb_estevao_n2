<%@ page import="java.nio.charset.StandardCharsets" %>
<%@ page import="java.net.URLEncoder" %><%
    String tipo = (String) session.getAttribute("tipo");
    if (tipo == null || !tipo.equals("admin")) {
        String mensagemErro = "Sem permissão para acessar esta página.";
        String erroEncode = URLEncoder.encode(mensagemErro, StandardCharsets.UTF_8.toString());
        response.sendRedirect("login.jsp?erro=" + erroEncode);
        return;
    }
%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"       prefix="c"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn" %>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title>Painel Admin - Início</title>

    <!-- Tailwind CDN (para protótipo) -->
    <script src="https://unpkg.com/@tailwindcss/browser@4"></script>

    <link rel="stylesheet" href="./assets/css/botaoVoltar.css">
</head>

<body class="bg-gray-100 font-sans leading-normal tracking-normal">

<div class="container mx-auto px-4 py-8 space-y-6">

    <!-- Cabeçalho -->
    <div class="flex w-full items-center">
        <button class="flex w-30 h-fit items-center justify-center rounded-md bg-white px-4 py-1.5 text-sm
                       font-semibold text-indigo-600 shadow-sm border border-indigo-600 hover:bg-gray-100">
            <a href="login.jsp" class="flex w-full text-base cursor-pointer">&larr; Voltar</a>
        </button>
        <h1 class="flex-1 text-3xl font-bold text-center">Painel do Administrador</h1>
    </div>

    <!-- Feedback -->
    <c:if test="${not empty erro}">
        <div class="bg-red-200 text-red-900 p-3 rounded">${erro}</div>
    </c:if>
    <c:if test="${not empty sucesso}">
        <div class="bg-green-200 text-green-900 p-3 rounded">${sucesso}</div>
    </c:if>

    <!-- Lista de produtos em formato de tabela -->
    <div class="bg-white p-6 rounded shadow-md">
        <h2 class="text-xl font-semibold mb-4">Produtos Cadastrados</h2>

        <c:choose>
            <c:when test="${empty produtos}">
                <p class="text-gray-700 mb-4">Nenhum produto cadastrado!</p>
            </c:when>
            <c:otherwise>
                <p class="text-green-600 mb-4">
                    Foram encontrados <strong>${fn:length(produtos)}</strong> produtos no sistema.
                </p>
                <div class="overflow-x-auto">
                    <table class="min-w-full bg-white border border-gray-200">
                        <thead>
                        <tr class="bg-gray-100">
                            <th class="py-2 px-4 border-b text-left">Nome</th>
                            <th class="py-2 px-4 border-b text-left">Preço</th>
                            <th class="py-2 px-4 border-b text-center">Ações</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="produto" items="${produtos}">
                            <tr class="hover:bg-gray-50">
                                <td class="py-2 px-4 border-b">${produto.nome}</td>
                                <td class="py-2 px-4 border-b">R$ ${produto.preco}</td>
                                <td class="text-center py-2 px-4 border-b">
                                    <!-- EDITAR -->
                                    <form action="${pageContext.request.contextPath}/produto/editar" method="post" style="display:inline;">
                                        <input type="hidden" name="id"               value="${produto.id}"/>
                                        <input type="hidden" name="nome-produto"     value="${produto.nome}"/>
                                        <input type="hidden" name="preco-produto"    value="${produto.preco}"/>
                                        <input type="hidden" name="descricao-produto" value="${produto.descricao}"/>
                                        <button class="bg-blue-100 hover:bg-blue-200 text-blue-800 py-1 px-2 rounded text-xs">
                                            Editar
                                        </button>
                                    </form>
                                    <!-- EXCLUIR -->
                                    <form action="${pageContext.request.contextPath}/produto/deletar" method="post" style="display:inline;"
                                          onsubmit="return confirm('Tem certeza?');">
                                        <input type="hidden" name="id" value="${produto.id}"/>
                                        <button class="bg-red-100 hover:bg-red-200 text-red-800 py-1 px-2 rounded text-xs">
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
    </div>

    <!-- Formulário de cadastro -->
    <form action="cadastroProduto" method="post"
          class="bg-white p-6 rounded shadow-md mb-4 space-y-4">
        <h3 class="text-xl font-semibold">Adicionar Novo Produto</h3>

        <div>
            <label for="nome-produto" class="block text-gray-700">Nome do Produto</label>
            <input type="text" name="nome-produto" id="nome-produto"
                   class="border rounded p-2 w-full" required>
        </div>

        <div>
            <label for="preco-produto" class="block text-gray-700">Preço</label>
            <input type="number" step="0.01" name="preco-produto" id="preco-produto"
                   class="border rounded p-2 w-full" required>
        </div>

        <div>
            <label for="descricao-produto" class="block text-gray-700">Descrição</label>
            <input type="text" name="descricao-produto" id="descricao-produto"
                   class="border rounded p-2 w-full" required>
        </div>

        <div class="botoes-acao mt-6 flex justify-between">
            <button type="submit"
                    class="bg-blue-600 hover:bg-blue-500 text-white p-2 rounded w-full md:w-auto">
                Adicionar Produto
            </button>
        </div>
    </form>

</div>
</body>
</html>