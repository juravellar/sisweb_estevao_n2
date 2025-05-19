<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8" />
  <title>Pedido Confirmado</title>

<script src="https://unpkg.com/@tailwindcss/browser@4"></script>
</head>
<body class="bg-gray-50 flex items-center justify-center min-h-screen font-sans">

<div class="bg-white shadow-md rounded-lg p-10 max-w-md text-center space-y-6">

  <h1 class="text-4xl font-extrabold text-green-600">✅ Pedido realizado com sucesso!</h1>

  <p class="text-gray-700 text-lg">
    Obrigado pela compra. Em breve você receberá um e-mail com os detalhes.
  </p>

  <a href="telaInicial.jsp"
     class="inline-block bg-indigo-600 text-white px-6 py-3 rounded-md font-semibold
                  hover:bg-indigo-500 transition-colors duration-200">
    Voltar à loja
  </a>

</div>

</body>
</html>
