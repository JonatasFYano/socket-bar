import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import restaurant.*;

/*
Classe responsável por tratar a comunicação do servidor com o cliente.

O socket recebe a cadeia de bytes, o transforma em String, e decide o que fazer no Switch Case.

Os pedidos são armazenados em instâncias da classe Pedido, que por sua vez são guardadas na instancia da classe cozinha. A cozinha possui todos os pedidos enquanto o servidor
estiver de pé.

Assim que tudo for devidamente realizado, escreve a resposta prevista na documentação e envia de volta para o socket do cliente.
*/

class TrataCliente implements Runnable {

  private BufferedReader inFromClient;
  private DataOutputStream outToClient;
  private Cozinha cozinha;


  public TrataCliente (Socket connectionSocket, Cozinha cozinha) throws Exception {
    inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
    outToClient = new DataOutputStream(connectionSocket.getOutputStream());
    cozinha = cozinha;
  }

  public void run () {
    try {
      Random gerador = new Random();
      String clientSentence = inFromClient.readLine();
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

    } catch (Exception ex) {
      System.out.println("ERROR:\n" + ex);
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
            if(pedido.getStatus().equals("1")) {
              pedido.setWaiterID(waiterID);
              pedido.setItems(itens);
              pedido.setObs(obs);
              return pedido;
            } else {
              return null;
              // return "Pedido de id " + orderID + " possui status " + pedido.getStatus() + ".Portanto, não pode ser alterado.";
            }

          }
      }
      return null;
  }

  public static boolean deletarPedido(String[] split, Cozinha cozinha){
      String waiterID = split[1];
      String orderID = split[2];
      for(Pedido pedido : cozinha.pedidos){
          if(pedido.getID().equals(orderID)){
            if(pedido.getStatus().equals("1")) {
              cozinha.pedidos.remove(cozinha.pedidos.indexOf(pedido));
              return true;
            }
            else {
              return false;
            }
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
