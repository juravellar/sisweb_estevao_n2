<%
    String erro = request.getParameter("erro");
%>
<script>
    <% if (erro != null) { %>
    alert("<%= erro %>");
    <% } %>
</script>
<%
    request.removeAttribute("erro");
%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Login</title>
        <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
    </head>

    <body>

        <div class="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">
            <div class="sm:mx-auto sm:w-full sm:max-w-sm">
                <img class="mx-auto h-10 w-auto"
                    src="https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=600"
                    alt="Your Company">
                <h2 class="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
                    Entrar na conta
                </h2>
            </div>

            <h3
                class="bg-red-300 text-red-900 font-semibold text-base w-80 rounded-lg m-2 mx-auto justify-center text-center">
                ${erro}
            </h3>

            <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                <form class="space-y-6" action="login" method="POST">

                    <div>
                        <label for="email" class="block text-sm/6 font-medium text-gray-900">Email</label>
                        <div class="mt-2">
                            <input type="email" name="email" id="email" autocomplete="email" value="${email}" required
                                class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6">
                        </div>
                    </div>

                    <div>
                        <label for="senha" class="block text-sm/6 font-medium text-gray-900">Senha</label>
                        <div class="mt-2">
                            <input type="password" name="senha" id="senha" autocomplete="current-password"
                                required
                                class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6">
                        </div>
                    </div>

                    <div>
                        <button type="submit"
                            class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                            Entrar
                        </button>
                        <p class="mt-4 text-center text-sm text-gray-600">
                            Ainda não tem uma conta?
                            <a href="cadastroConta.jsp" class="font-semibold text-indigo-600 hover:text-indigo-500">
                                Cadastre-se aqui
                            </a>
                        </p>
                    </div>
                </form>

            </div>
        </div>
    </body>

    </html>