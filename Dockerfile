FROM openjdk:21-jdk-slim

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Instala ferramentas adicionais (se necessário)
RUN apt-get update && apt-get install -y \
    maven \
    && rm -rf /var/lib/apt/lists/*

# Define a variável de ambiente para evitar problemas com encoding
ENV LANG=C.UTF-8

# Comando padrão para manter o contêiner rodando
CMD ["bash"]
