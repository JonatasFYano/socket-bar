import java.io.*;
import java.net.*;
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
            Socket clientSocket = new Socket("10.128.67.68", 6789);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            operacao = inFromUser.readLine();
            BufferedReader comando;
            switch (operacao) {


                case "1":
                    System.out.println("Novo Pedido!!\n");
                    System.out.println("Estrutura: order <waiter_id> <table_id> <items> <obs>\n");
                    System.out.println("Escreva seu novo pedido:");
                    comando = new BufferedReader(new InputStreamReader(System.in));
                    //Validar o comando**************
                    String idNovoPedido = novoPedido(comando.readLine(), outToServer, inFromServer);
                    System.out.println("ID do seu pedido: " + idNovoPedido);
                    break;


                case "2":
                    System.out.println("Alterar Pedido!!\n");
                    System.out.println("Estrutura: change <waiter_id> <order_id> <items> <obs>\n");
                    System.out.println("Altere seu pedido:");
                    comando = new BufferedReader(new InputStreamReader(System.in));
                    //Validar o comando**************
                    String idPedidoAlterado = alterarPedido(comando.readLine(), outToServer, inFromServer);
                    if(idPedidoAlterado != null){
                        String idPedidoAlteradoSplit[] =  idPedidoAlterado.split(" ");
                        System.out.println("Pedido de ID " + idPedidoAlteradoSplit[0] + 
                            " Anotado pelo garcom de ID " + idPedidoAlteradoSplit[1] + 
                            " Possui agora itens: " + idPedidoAlteradoSplit[2] +
                            " E as seguintes observacoes: " + idPedidoAlteradoSplit[3]);
                    }else{
                        System.out.println("ID digitado nao corresponde a nenhum pedido =(, tente novamente\n\n");
                    }
                    break;


                case "3":
                    System.out.println("Delete um Pedido!!\n");
                    System.out.println("Estrutura: delete <waiter_id> <order_id>\n");
                    System.out.println("Delete seu pedido:");
                    comando = new BufferedReader(new InputStreamReader(System.in));
                    //Validar o comando**************
                    boolean pedidoDeletado = deletarPedido(comando.readLine(), outToServer, inFromServer);
                    if(pedidoDeletado){
                        System.out.println("Pedido Deletado com sucesso");
                    }else{
                        System.out.println("Pedido nao existe, sendo assim exclusao falhou");
                    }
                    break;


                case "4":
                    System.out.println("Liste os Pedidos!!\n");
                    System.out.println("Estrutura: list <order_id> <order_status> <waiter_id> <table_id>\n");
                    System.out.println("Escreva como quer a lista:");
                    comando = new BufferedReader(new InputStreamReader(System.in));
                    //Validar o comando**************
                    String listaPedidos = listarPedidos(comando.readLine(), outToServer, inFromServer);
                    break;


                case "5":
                    System.out.println("Atualize o Status do Pedido!!\n");
                    System.out.println("Estrutura: update <order_id> <order_status>\n");
                    System.out.println("Escreva o novo status do Pedido:");
                    comando = new BufferedReader(new InputStreamReader(System.in));
                    //Validar o comando**************
                    break;


                default:
                     System.out.println("Este comando não é válido");

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
            String cozinha = inFromServer.readLine();
            System.out.println(cozinha);
        }catch(IOException ex){
            System.out.println (ex.toString());
            return false;
        }

    }
}