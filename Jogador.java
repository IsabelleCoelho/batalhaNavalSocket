import java.net.*;
import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

public class Jogador extends Thread{
    private Socket cliente;
    private PrintWriter escrita;
    private BufferedReader leitura;
    private Queue<Mensagem> fila;
    private LinkedList<String> toDoList;

    public Jogador(Socket cliente){
        this.cliente = cliente;
        try{
            this.escrita = new PrintWriter(cliente.getOutputStream(), true);
            this.leitura = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        }
        catch(IOException e){
            System.exit(1);
        } 
        fila = null;
        toDoList = new LinkedList<String>();
        toDoList.add("submarino");
        toDoList.add("submarino");
        toDoList.add("contra-torpedeiro");
        toDoList.add("porta-avioes");
        start();  
    }

    public void run(){
        String output = "";
        do{
            try{
                output = leitura.readLine();
                adicionarMensagem(output);
            }
            catch(IOException e){
                output = null;
            }
        }while(output != null);
        encerrar();
    }

    public boolean estaPronto(){ //se terminou de adicionar todos os navios
        return toDoList.size() == 0;
    }

    public void setFila(Queue<Mensagem> fila){
        this.fila = fila; //o ponteiro da fila agora aponta para a ConcurrentLinkedQueue da Partida. Isso foi feito para que fosse possível trabalhar com a mesma fila em classes diferentes.
    }

    public String getNavio(){
        return toDoList.get(0);
    }

    public void proximoPasso(){
        toDoList.removeFirst();
        
    }
    //método responsável por adicionar uma mensagem na fila de mensagens
    public void adicionarMensagem(String mensagem){
        Mensagem msg = new Mensagem(this, mensagem);
        if(limparEntrada(msg)){
            if (fila != null) {
                fila.add(msg);
            }
        }
    }

    public boolean limparEntrada(Mensagem mensagem){
        if (mensagem.cleannerMenu()) {
            mensagem.setComando("menu");
            return true;
        }
        else if (mensagem.cleannerPreencher()) {
            mensagem.setComando("preencher");
            return true;
        }
        else if (mensagem.cleannerAtacar()) {
            mensagem.setComando("atacar");
            return true;
        }
        else{
            escrita.println("Comando invalido");
            return false;
        }
    }

    public String encerrar(){
        String resultado;
        try{
            cliente.close();
            escrita.close();
            leitura.close();
            resultado = "Cliente encerrado com sucesso";
        }
        catch (IOException e){
            resultado = "Não foi possível encerrar o cliente";
        }
        return resultado;
    }

    public void enviarMensagem(String mensagem){
        escrita.println(mensagem);
    }
}