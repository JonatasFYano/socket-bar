import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import restaurant.*;

/*
Servidor, possui o socket que recebe requisições do Client. Expôs-se a porta 6789, mas qualquer uma será válida (menos as usadas pelo sistema =p)

O socket recebe a cadeia de bytes, o transforma em String, e decide o que fazer no Switch Case.
Os pedidos são armazenados em instâncias da classe Pedido, que por sua vez são guardadas na instancia da classe cozinha. A cozinha possui todos os pedidos enquanto o servidor
estiver de pé.

Assim que tudo for devidamente realizado, escreve a resposta prevista na documentação e envia de volta para o socket do cliente.
*/


class TCPServerSocket {

public static void main(String argv[]) throws Exception{
    String clientSentence;
    String capitalizedSentence;
    ServerSocket welcomeSocket = new ServerSocket(6789);
    System.out.println("Cozinha Ouvindo");
    Cozinha cozinha = new Cozinha();
    cozinha.pedidos = new ArrayList<Pedido>();
    Random gerador = new Random();
    while(true) {
        Socket connectionSocket = welcomeSocket.accept();
        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        clientSentence = inFromClient.readLine();
        if(clientSentence != null && clientSentence.length() > 0){     
            String split[] = clientSentence.split("_");
            System.out.println(split[0]);
    
            switch (split[0]) {
                case "order":
                    Pedido novoPedido = novoPedido(split, gerador);
                    System.out.println(novoPedido.getID());
                    cozinha.pedidos.add(novoPedido);
                    outToClient.writeBytes(novoPedido.getID() + "\n");
                    break;

                case "change":
                    Pedido pedidoAlterado = alteraPedido(split, cozinha);
                    if(pedidoAlterado != null){
                        System.out.println(pedidoAlterado.getID());
                        outToClient.writeBytes(pedidoAlterado.getID() + "_" +
                            pedidoAlterado.getWaiterID() + "_" +
                            pedidoAlterado.getItems() + "_" + 
                            pedidoAlterado.getObs() + "\n");
                    }else{
                        outToClient.writeBytes("false" + "\n");
                    }
                    break;

                case "delete":
                    boolean pedidoDeletado = deletarPedido(split, cozinha);
                    outToClient.writeBytes(pedidoDeletado + "\n");
                    break;

                case "list":
                    String listaPedidos = listarPedidos(split, cozinha);
                    if(listaPedidos == null){
                        outToClient.writeBytes("false" + "\n");
                    }
                    outToClient.writeBytes(listaPedidos + "\n");
                    break;

                case "update":
                    String atualizaStatus = atualizarStatus(split, cozinha);
                    if(atualizaStatus != null){
                        outToClient.writeBytes(atualizaStatus + "\n");
                    }
                    else{
                        outToClient.writeBytes("false" + "\n");
                    }
                    break;

                default:
                    System.out.println("Este comando não é válido");
            }
        }
        }
    }

    public static Pedido novoPedido (String[] split, Random gerador){
        String waiterID = split[1];
        String tableID = split[2];
        String itens = split[3];
        String obs = split[4];
        Pedido novoPedido = new Pedido(waiterID, tableID, itens, obs);
        String ID = Integer.toString(gerador.nextInt());
        novoPedido.setID(ID);
        novoPedido.setStatus(Integer.toString(1));
        return novoPedido;
    }

    public static Pedido alteraPedido(String[] split, Cozinha cozinha){
        String waiterID = split[1];
        String orderID = split[2];
        String itens = split[3];
        String obs = split[4];
        for(Pedido pedido : cozinha.pedidos){
            if(pedido.getID().equals(orderID)){
                pedido.setWaiterID(waiterID);
                pedido.setItems(itens);
                pedido.setObs(obs);
                return pedido;
            }
        }
        return null;
    }
    
    public static boolean deletarPedido(String[] split, Cozinha cozinha){
        String waiterID = split[1];
        String orderID = split[2];
        for(Pedido pedido : cozinha.pedidos){
            if(pedido.getID().equals(orderID)){
                cozinha.pedidos.remove(cozinha.pedidos.indexOf(pedido));
                return true;
            }
        }
        return false;
    }

    public static String listarPedidos(String[] split, Cozinha cozinha){
        String orderID = null;
        if(split.length > 1){
            orderID = split[1];
        }
        if(orderID != null){
            for(Pedido pedido : cozinha.pedidos){
                if(pedido.getID().equals(orderID)){
                    String pedidoString = pedido.getID() + "_" +
                        pedido.getStatus() + "_" +
                        pedido.getWaiterID() + "_" + 
                        pedido.getTableID() + "_" + 
                        pedido.getItems() + "_" + 
                        pedido.getObs();
                    return pedidoString;
                }
            }
        }
        else{
            String pedidoString = null;
            for(Pedido pedido : cozinha.pedidos){
                pedidoString +=  pedido.getID() + "_" +
                pedido.getStatus() + "_" +
                pedido.getWaiterID() + "_" + 
                pedido.getTableID() + "_" + 
                pedido.getItems() + "_" + 
                pedido.getObs() + "/";
            }
            return pedidoString;
        }
        return null;
    }

    public static String atualizarStatus(String[] split, Cozinha cozinha){
        String orderID = split[1];
        String orderStatus = split[2];
        for(Pedido pedido : cozinha.pedidos){
            if(pedido.getID().equals(orderID)){
                int index = cozinha.pedidos.indexOf(pedido);
                cozinha.pedidos.get(index).setStatus(orderStatus);
                return orderID + "_" + orderStatus;
            }
        }
        return null;
    }
}