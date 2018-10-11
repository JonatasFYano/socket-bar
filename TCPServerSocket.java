import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import restaurant.*;

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
            String split[] = clientSentence.split(" ");
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
                        outToClient.writeBytes(pedidoAlterado.getID() + " " +
                            pedidoAlterado.getWaiterID() + " " +
                            pedidoAlterado.getItems() + " " + 
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
                    outToClient.writeBytes(cozinha + "\n");
                    break;

                case "update":
                    break;

                default:
                    System.out.println("Este comando não é válido");
            }
        }

        // capitalizedSentence = clientSentence.toUpperCase() + "\n";
        // outToClient.writeBytes(capitalizedSentence);
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
}