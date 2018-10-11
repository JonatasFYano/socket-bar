import java.io.*;
import java.net.*;

/*Client, que possui o socket que irá se comunicar com o Server. Para conectar o server, basta mudar a instancia do **clientSocket**, colocando
o IP do servidor.
Aqui sao feitas 5 possíveis chamadas ao servidor: order, change, delete, list e update. Cada uma recebe um comando do input do cliente, e envia esse comando para que o servidor
realize determinada ação. 

*******Informaçõe mais completas sobre os comandos olhas documentação*******

Para que a comunicação seja mais simples, pede-se que ambos cliente e servidor estevam na mesma rede, pois caso contrário seria necessário que o servidor
habilitasse um IP público que seja alcançavel pelo cliente.
*/

/*
O usuário entratá com uma opção variando de 1 a 5, cada uma executa um comando.
Existem validações para verificar se o comando está seguindo as específicações, mas não gastei muito tempo pensando em todas as possibilidades de falha.. Acho que não precisa disso
pro EP.

O intuito é que o switch (controller) identifique a opção, e passe a responsabilidade de enviar e receber os dados do servidor para a função (Repository), e que enfim seja exibida
a informação para o usuário (View).
Como é um EP com foco nos sockets, não fiz uma estrutura MVC em sí, mas a ideia é que o código esteja arquitetado minimamente em um MVC!!
*/

class TCPClientSocket {
    public static void main(String argv[]) throws Exception{
        String operacao;
        String modifiedSentence;
        while(true){
            System.out.println("Ola, que operacao gostaria de realizar?\n");
            System.out.println("1 - Novo Pedido\n");
            System.out.println("2 - Alterar Pedido\n");
            System.out.println("3 - Deletar Pedido\n");
            System.out.println("4 - Listar Pedidos\n");
            System.out.println("5 - Atualizar status do Pedido\n");
            System.out.println("Insira o numero da operacao:");
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            
            //Para adequar o programa ao seu ambiente, altere o IP aqui configurado******
            /******************************** */
            Socket clientSocket = new Socket("10.128.67.68", 6789);
            /******************************** */

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            operacao = inFromUser.readLine();
            BufferedReader comandoBuffer;
            String comando;
            switch (operacao) {

                //Operação de inserir um Novo Pedido
                case "1":
                    System.out.println("Novo Pedido!!\n");
                    System.out.println("Estrutura: order_<waiter_id>_<table_id>_<items>_<obs>\n");
                    System.out.println("Escreva seu novo pedido:");
                    comandoBuffer = new BufferedReader(new InputStreamReader(System.in));
                    comando = comandoBuffer.readLine();
                    if(!validarComando(comando, operacao)){
                        System.out.println("Comando invalido, tente novamente seguindo a estrutura do comando!!");
                        break;
                    }
                    System.out.println("Teste");
                    String idNovoPedido = novoPedido(comando, outToServer, inFromServer);
                    System.out.println("ID do seu pedido: " + idNovoPedido);
                    break;

                //Operação Alterar um Pedido
                case "2":
                    System.out.println("Alterar Pedido!!\n");
                    System.out.println("Estrutura: change_<waiter_id>_<order_id>_<items>_<obs>\n");
                    System.out.println("Altere seu pedido:");
                    comandoBuffer = new BufferedReader(new InputStreamReader(System.in));
                    comando = comandoBuffer.readLine();
                    if(!validarComando(comando, operacao)){
                        System.out.println("Comando invalido, tente novamente seguindo a estrutura do comando!!");
                        break;
                    }
                    String idPedidoAlterado = alterarPedido(comando, outToServer, inFromServer);
                    if(idPedidoAlterado != null){
                        String idPedidoAlteradoSplit[] =  idPedidoAlterado.split("_");
                        System.out.println("Pedido de ID " + idPedidoAlteradoSplit[0] + 
                            " Anotado pelo garcom de ID " + idPedidoAlteradoSplit[1] + 
                            " Possui agora itens: " + idPedidoAlteradoSplit[2] +
                            " E as seguintes observacoes: " + idPedidoAlteradoSplit[3]);
                    }else{
                        System.out.println("ID digitado nao corresponde a nenhum pedido =(, tente novamente\n\n");
                    }
                    break;

                //Operação para Deletar um Pedido
                case "3":
                    System.out.println("Delete um Pedido!!\n");
                    System.out.println("Estrutura: delete_<waiter_id>_<order_id>\n");
                    System.out.println("Delete seu pedido:");
                    comandoBuffer = new BufferedReader(new InputStreamReader(System.in));
                    comando = comandoBuffer.readLine();
                    if(!validarComando(comando, operacao)){
                        System.out.println("Comando invalido, tente novamente seguindo a estrutura do comando!!");
                        break;
                    }
                    boolean pedidoDeletado = deletarPedido(comando, outToServer, inFromServer);
                    if(pedidoDeletado){
                        System.out.println("Pedido Deletado com sucesso");
                    }else{
                        System.out.println("Pedido nao existe, sendo assim exclusao falhou");
                    }
                    break;

                //Opção para Listar Pedidos
                case "4":
                    System.out.println("Liste os Pedidos!!\n");
                    System.out.println("Estrutura: list_<order_id>_<order_status>_<waiter_id>_<table_id>\n");
                    System.out.println("Escreva como quer a lista:");
                    comandoBuffer = new BufferedReader(new InputStreamReader(System.in));
                    comando = comandoBuffer.readLine();
                    if(!validarComando(comando, operacao)){
                        System.out.println("Comando invalido, tente novamente seguindo a estrutura do comando!!");
                        break;
                    }
                    String listaPedidos = listarPedidos(comando, outToServer, inFromServer);
                    if(listaPedidos == null || listaPedidos == ""){
                        System.out.println("Nao existem pedidos para exibir!");
                        break;
                    }
                        System.out.println("Lista de Pedidos:\n");
                        String pedidosExibir = "";
                        if(listaPedidos.contains("/")){
                            String listaPedidosSplit[] = listaPedidos.split("/");
                            for(int i = 0 ; i < listaPedidosSplit.length ; i++){
                                String pedido[] = listaPedidosSplit[i].split("_");
                                pedidosExibir += i + ":\n\tID: " + pedido[0] +
                                    "\n\tStatus do pedido: " + pedido[1] + 
                                    "\n\tID do atendente: " + pedido[2] +
                                    "\n\tID da mesa: " + pedido[3] +
                                    "\n\tItens: " + pedido[4] + 
                                    "\n\tObservacoes: " + pedido[5] + "\n";
                            }
                        }else{
                            String pedido[] = listaPedidos.split("_");
                            pedidosExibir +="1: \n\t ID: " + pedido[0] +
                                "\n\tStatus do pedido: " + pedido[1] + 
                                "\n\tID do atendente: " + pedido[2] +
                                "\n\tID da mesa: " + pedido[3] +
                                "\n\tItens: " + pedido[4] + 
                                "\n\tObservacoes: " + pedido[5];
                        }
                        System.out.println(pedidosExibir);
                    break;

                //Opção Para atualizar o Status de um pedido
                case "5":
                    System.out.println("Atualize o Status do Pedido!!\n");
                    System.out.println("Estrutura: update_<order_id>_<order_status>\n");
                    System.out.println("Escreva o novo status do Pedido:");
                    comandoBuffer = new BufferedReader(new InputStreamReader(System.in));
                    comando = comandoBuffer.readLine();
                    if(!validarComando(comando, operacao)){
                        System.out.println("Comando invalido, tente novamente seguindo a estrutura do comando!!");
                        break;
                    }
                    String atualizaStatus = atualizarStatus(comando, outToServer, inFromServer);
                    if(atualizaStatus != null){
                        System.out.println(atualizaStatus);
                    }
                    else{
                        System.out.println("Algo deu errado =/");
                    }
                    break;


                default:
                     System.out.println("Este comando nao e valido, tente novamente");

             }
            clientSocket.close();
        }
    }

    public static String novoPedido (String comando, DataOutputStream outToServer, BufferedReader inFromServer){
        try{
            outToServer.writeBytes(comando + "\n");
            String orderId = inFromServer.readLine();
            return orderId;
        }catch(IOException ex){
            System.out.println (ex.toString());
            return null;
        }

    }

    public static String alterarPedido (String comando, DataOutputStream outToServer, BufferedReader inFromServer){
        try{
            outToServer.writeBytes(comando + "\n");
            String orderId = inFromServer.readLine();
            if(orderId.equals("false")){
                return null;
            }else{
                return orderId;
            }
        }catch(IOException ex){
            System.out.println (ex.toString());
            return null;
        }

    }

    public static boolean deletarPedido (String comando, DataOutputStream outToServer, BufferedReader inFromServer){
        try{
            outToServer.writeBytes(comando + "\n");
            String pedidoDeletado = inFromServer.readLine();
            if(pedidoDeletado.equals("true")){
                return true;
            }else{
                return false;
            }
        }catch(IOException ex){
            System.out.println (ex.toString());
            return false;
        }

    }

    public static String listarPedidos (String comando, DataOutputStream outToServer, BufferedReader inFromServer){
        try{
            outToServer.writeBytes(comando + "\n");
            String pedidos = inFromServer.readLine();
            if(pedidos.equals("false")){
                return null;
            }
            else{
                return pedidos;
            }
        }catch(IOException ex){
            System.out.println (ex.toString());
            return null;
        }
    }
    public static String atualizarStatus (String comando, DataOutputStream outToServer, BufferedReader inFromServer){
        try{
            outToServer.writeBytes(comando + "\n");
            String pedidos = inFromServer.readLine();
            if(pedidos.equals("false")){
                return null;
            }
            else{
                String pedidoAtualizado[] = pedidos.split("_");
                String stringPedidoAtualizado = "ID do pedido: " + pedidoAtualizado[0] +
                    " com o Status atualizado: " + pedidoAtualizado[1];
                return stringPedidoAtualizado;
            }
        }catch(IOException ex){
            System.out.println (ex.toString());
            return null;
        }
    }

    public static boolean validarComando(String comando, String operacao){
        String aux[];
        if(comando != ""){
            switch(operacao){
                case "1":
                    aux = comando.split("_");
                    if(aux.length == 5){
                        return true;
                    }
                    break;
                case "2":
                    aux = comando.split("_");
                    if(aux.length == 5){
                        return true;
                    }
                    break;
                case "3":
                    aux = comando.split("_");
                    if(aux.length == 3){
                        return true;
                    }
                    break;
                case "4":
                    aux = comando.split("_");
                    if(aux.length <= 2 && aux.length > 0){
                        return true;
                    }
                    break;
                case "5":
                    aux = comando.split("_");
                    if(aux.length == 3){
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return false;
    }
}