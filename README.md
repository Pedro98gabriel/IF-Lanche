# IF-Lanche

<img src="https://github.com/Pedro98gabriel/IF-Lanche/blob/master/imagens/tela_login.png" width="250" /> _
<img src="https://github.com/Pedro98gabriel/IF-Lanche/blob/master/imagens/tela_cadastro.png" width="250" /> _
<img src="https://github.com/Pedro98gabriel/IF-Lanche/blob/master/imagens/tela_inicial.png" width="250" /> _
<img src="https://github.com/Pedro98gabriel/IF-Lanche/blob/master/imagens/tela_cadastro_produto.png" width="250" />

## Sobre o projeto:
Este projeto é um aplicativo Android desenvolvido como parte do meu Trabalho de Conclusão de Curso em Análise e Desenvolvimento de Sistemas no IFPR, Campus Telêmaco Borba. O objetivo do aplicativo é facilitar a visualização dos itens disponíveis no cardápio da cantina do campus, oferecendo aos usuários uma forma rápida de acessar essas informações.

## Funcionalidades:
- **Visualização do Cardápio:** Usuários podem acessar e visualizar os itens disponíveis na cantina.

- **Cadastro de Usuários:** Permite que novos usuários se cadastrem no aplicativo.

- **Gerenciamento de Itens:** Permite que os funcionários realizem o cadastro, edição e exclusão (CRUD) dos itens disponíveis.

## Tecnologias Utilizadas:
- **Linguagem:** Kotlin
- **Plataforma:** Android
- **Banco de Dados:** Firebase
- **IDE:** Android Studio
  
## Como rodar

#### Pré-requisitos:

- **Ambiente de Desenvolvimento:** É recomendável usar o Android Studio para compilar e executar o aplicativo, mas você pode utilizar outra IDE compatível com desenvolvimento Android, como IntelliJ IDEA ou Eclipse, desde que tenha as configurações necessárias.

- **Java Development Kit (JDK):** É necessário ter o JDK instalado. A versão recomendada é a JDK 21 ou superior.

- **Git:** O Git deve estar instalado na sua máquina para clonar o repositório. Você pode baixá-lo clicando [aqui](https://git-scm.com/).

- **Hardware:** Um computador que atenda aos requisitos mínimos para rodar projetos no Android Studio ou em sua IDE escolhida.

### Instalando

**Clonando o repositório:**

```
$ git clone https://github.com/Pedro98gabriel/IF-Lanche.git
```

#### Configurando o Firebase:

Para utilizar o Firebase em seu projeto, siga os passos abaixo:

1. **Criar um Projeto no Firebase:**
   - Acesse o [Firebase Console](https://console.firebase.google.com/).
   - Clique em "Adicionar Projeto" e siga as instruções para criar um novo projeto.

2. **Configurar o Realtime Database:**
   - No console do Firebase, selecione seu projeto.
   - No menu lateral, clique em "Realtime Database".
   - Clique em "Criar Banco de Dados" e escolha as regras de segurança que melhor se adequem ao seu aplicativo.

3. **Ativar o Storage:**
   - No menu lateral, clique em "Storage".
   - Clique em "Começar" e siga as instruções para configurar o armazenamento.

4. **Configurar a Autenticação:**
   - No menu lateral, clique em "Authentication".
   - Clique em "Começar" e escolha o método de autenticação Email/Senha.

5. **Configurar o Firebase Cloud Messaging (opcional):**
   - No menu lateral, clique em "Cloud Messaging".
   - Siga as instruções para configurar o envio de notificações push.

6. **Baixar o arquivo `google-services.json`:**
   - No painel de configurações do projeto, clique em "Adicionar aplicativo" e siga as instruções.
   - Baixe o arquivo `google-services.json` e coloque-o na pasta `app/` do seu projeto.

Após seguir esses passos, você terá o Firebase configurado e pronto para uso no aplicativo, em caso de dúvidas a [documentação](https://firebase.google.com/docs/android/setup?hl=pt) poderá ser consultada.

#### Configurações Adicionais:

Para que o aplicativo funcione corretamente, é necessário adicionar algumas configurações no arquivo `local.properties` do seu projeto.

1. Abra o arquivo `local.properties`, localizado na pasta `Gradle Scripts`.
2. Adicione as seguintes linhas ao arquivo, substituindo `YOUR_VALUE` pelos valores desejados:
   - `BCRYPT_SALT=YOUR_VALUE` (substitua `YOUR_VALUE` pelo valor desejado para o salt do bcrypt)
   - `URL_FIREBASE=YOUR_VALUE` (substitua `YOUR_VALUE` pela URL do seu projeto no Firebase)
3. Salve o arquivo.
4. Conecte um dispositivo Android físico ou inicie um emulador Android.
5. Clique em "Run" para compilar e executar o aplicativo.
