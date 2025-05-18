<%@ page contentType="text/html; charset=UTF-8" language="java" %>
    <!DOCTYPE html>
    <html>

    <head>
        <title>Cadastro de Conta</title>
        <script src="https://unpkg.com/@tailwindcss/browser@4"></script>
        <link rel="stylesheet" href="./assets/css/botaoVoltar.css">
    </head>

    <body>

        <div class="flex min-h-full flex-col justify-center px-6 py-12 lg:px-8">

            <div>
                <button class="flex w-30 h-fit items-center cursor-pointer justify-center rounded-md bg-white px-4 py-1.5 text-sm font-semibold text-indigo-600 shadow-sm border border-indigo-600 transition-all duration-200 ease-in-out hover:bg-gray-100 focus-visible:outline focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                    <a href="login.jsp" class="flex w-full text-base cursor-pointer">&larr; Voltar</a>
                </button>
                <div class="sm:mx-auto sm:w-full sm:max-w-sm">
                    <img class="mx-auto h-10 w-auto"
                    src="https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=600"
                    alt="Your Company">
                    <h2 class="mt-10 text-center text-2xl/9 font-bold tracking-tight text-gray-900">
                        Cadastrar usuário
                    </h2>
                </div>
            </div>

            <h3
                class="bg-red-300 text-red-900 font-semibold text-base w-80 rounded-lg m-2 mx-auto justify-center text-center">
                ${erro}
            </h3>

            <div class="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                <form class="space-y-6" action="cadastroConta" method="POST">
                    <div>
                        <label for="nome" class="block text-sm/6 font-medium text-gray-900">Nome completo</label>
                        <div class="mt-2">
                            <input type="text" name="nome" id="nome" autocomplete="name" value="${nome}" required
                                class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6">
                        </div>
                    </div>

                    <div>
                        <label for="idade" class="block text-sm/6 font-medium text-gray-900">Idade</label>
                        <div class="mt-2">
                            <input type="number" name="idade" id="idade" value="${idade}" required
                                class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6">
                        </div>
                    </div>

                    <div>
                        <label for="email" class="block text-sm/6 font-medium text-gray-900">Email</label>
                        <div class="mt-2">
                            <input type="email" name="email" id="email" autocomplete="email" value="${email}" required
                                class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6">
                        </div>
                    </div>

                    <div>
                        <label for="telefone" class="block text-sm/6 font-medium text-gray-900">Número de telefone</label>
                        <div class="mt-2">
                            <input type="text" name="telefone" id="telefone" autocomplete="mobile" value="${telefone}" required
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
                        <label for="confirmacaoSenha" class="block text-sm/6 font-medium text-gray-900">Confirmar
                            senha</label>
                        <div class="mt-2">
                            <input type="password" name="confirmacaoSenha" id="confirmacaoSenha"
                                value="${confirmacaoSenha}" required
                                class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6">
                        </div>
                    </div>
                    <div>
                        <button type="submit"
                            class="flex w-full justify-center cursor-pointer rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600">
                            Cadastrar
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </body>

    </html>