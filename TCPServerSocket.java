import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;
import restaurant.*;

/*
Servidor, possui o socket que recebe requisições do Client. Expôs-se a porta 6789, mas qualquer uma será válida (menos as usadas pelo sistema =p)

Assim que a conexão é aceita, o servidor cria uma thread para cuidar da conexão com aquele cliente. Assim permanecendo livre para estabelecer novas conexões.
*/


class TCPServerSocket {

  static Cozinha cozinha = new Cozinha();
  // cozinha.pedidos = new ArrayList<Pedido>();

public static void main(String argv[]) throws Exception{
    String clientSentence;
    cozinha.pedidos = new ArrayList<Pedido>();
    ServerSocket welcomeSocket = new ServerSocket(6789);
    System.out.println("Cozinha Ouvindo");
    while(true) {
        Socket connectionSocket = welcomeSocket.accept();

        TrataCliente tc = new TrataCliente(connectionSocket, cozinha);

        new Thread(tc).start();
        }
    }
}
