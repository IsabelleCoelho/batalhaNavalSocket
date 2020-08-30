import java.net.*;
import java.util.LinkedList;
import java.io.*;

public class Servidor{
    private static LinkedList<Jogador> clientes;
    public static void main(String[] args) throws IOException{
        ServerSocket server = null;
        clientes = new LinkedList<Jogador>();
        try {
            server = new ServerSocket(40009);
        } catch (IOException e) {
            System.err.println("Não foi possível ouvir a porta 40009");
            System.exit(1);
        }

        Socket cliente = null;
        System.out.println("Servidor instanciado");
        while(true){
            System.out.println("Esperando clientes...");
            try{
                cliente = server.accept();
                clientes.add(new Jogador(cliente));
                System.out.println("Criado um cliente e adicionado na fila");;
            }
            catch (IOException e) {
                System.err.println("Não foi possível conectar ao cliente");
                System.exit(1);
            }
            //quando tiverem, pelo menos dois clientes na fila, pode-se iniciar uma partida
            if (clientes.size() == 2) {
                System.out.println("Tem dois clientes! Iniciando a partida!");
                new Partida(clientes.get(0), clientes.get(1));
                //removendo os dois clientes (que vão entrar na partida) da fila
                clientes.remove(0);
                clientes.remove(0);
            }
        }
    }
}