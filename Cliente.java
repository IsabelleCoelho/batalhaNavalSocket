import java.io.*;
import java.net.*;

public class Cliente extends Thread{
    static String mensagemRecebida;
    static Socket socket;
    static PrintWriter escrita;
    static BufferedReader leitura;
    private Cliente(String hostName){
        mensagemRecebida = "";
        try{
            socket = new Socket(hostName, 40009);
            escrita = new PrintWriter(socket.getOutputStream(), true);
            leitura = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (UnknownHostException e){
            System.err.println("Host não reconhecido");
            System.exit(1);
        }
        catch (IOException e){
            System.err.println("Não foi possível acessar o IO para conectar no host: " + hostName);
            System.exit(1);
        }
        start();
    }
    public static void main(String[] args) throws IOException{
        String hostName = "127.0.0.1"; //se um IP não for passado por argumento
        //se um IP for passado por argumento
        if(args.length == 1){
            hostName = args[0];
        }
        new Cliente(hostName);
        
        BufferedReader entradaTeclado = new BufferedReader(new InputStreamReader(System.in));
        
        String mensagemEnviada;
        System.out.println("------BATALHA NAVAL------");
        System.out.println("Lembretes: \n- O tabuleiro possui dimensões 10x10 \n- Você deve colocar no tabuleiro dois submarinos, um contra-torpedeiro e um porta-aviões \n- Um submarino ocupa duas casas, um contratorpedeiro ocupa 3 casas e um porta-aviões ocupa 5 casas");
        System.out.println("- As coordenadas e a direcao(C - cima, D - direita, E - esquerda, B - baixo) que deseja posicionar cada navio no seguinte formato:\n numero-numero-direcao");
        System.out.println("- Digite 1 caso queira ver seu campo");
        System.out.println("- Digite 2 caso queira ver o campo inimigo");
        System.out.println("Preencha seu tabuleiro!");
        System.out.println("Ordem de preenchimento: submarino, submarino, contra-torpedeiro, porta-avioes.\nDigite abaixo as coordenadas que deseja posicionar seus navios.");
        while(!mensagemRecebida.equals("Fim de Jogo!")){
            mensagemEnviada = entradaTeclado.readLine();
            escrita.println(mensagemEnviada);
        }
        //Encerrando comunicação com a rede
        escrita.close();
        leitura.close();
        entradaTeclado.close();
        socket.close();
    }

    @Override
    public void run() {
        while(!mensagemRecebida.equals("Fim de Jogo!")){
            try{
                mensagemRecebida = leitura.readLine();
                System.out.println(mensagemRecebida);
            }
            catch(IOException e){
                System.exit(1);
            }
        }
        System.out.println("Pressione \"ENTER\" para finalizar");
    }
}
