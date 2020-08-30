import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Partida extends Thread{
    private Jogador jogadorUm;
    private Jogador jogadorDois;
    private Queue<Mensagem> mensagens;
    private Jogador vez;
    private HashMap<Jogador, Tabuleiro> tabuleiros;
    public Partida(Jogador jogadorUm, Jogador jogadorDois){
        this.jogadorUm = jogadorUm;
        this.jogadorDois = jogadorDois;
        mensagens = new ConcurrentLinkedQueue<Mensagem>(); //a estrutura de dados ConcurrentLinkedQueue já trata a concorrência
        setFilas();
        vez = jogadorUm;
        tabuleiros = new HashMap<Jogador, Tabuleiro>();
        preencherTabuleiros();
        start();
    }

    public void preencherTabuleiros(){
        tabuleiros.put(jogadorUm, new Tabuleiro());
        tabuleiros.put(jogadorDois, new Tabuleiro());
    }

    public void setFilas(){
        this.jogadorUm.setFila(mensagens);
        this.jogadorDois.setFila(mensagens);
    }

    public void tratarComando(Mensagem mensagem, String comandoInvalido){
        if (!mensagem.getComando().equals(comandoInvalido)) {
            switch (mensagem.getComando()) {
                case "preencher":
                    preencherNavio(mensagem);
                    break;
            
                case "menu":
                    menu(mensagem);;
                    break;
                
                case "atacar":
                    atacarNavio(mensagem);
                    break;
            }
        }
        else{
            mensagem.getJogador().enviarMensagem("Voce nao pode atacar nesse momento!");
        }
    }

    public void preencherNavio(Mensagem mensagem){
        if (mensagem.getJogador().estaPronto()){
            mensagem.getJogador().enviarMensagem("Você já preencheu todos os navios. Aguarde seu oponente!");
        }
        else{
            String navio = mensagem.getJogador().getNavio();
            String resposta = "";
            String[] coordenadas = mensagem.getMensagem().split("-");
            switch (navio) {
                case "submarino":
                    resposta = tabuleiros.get(mensagem.getJogador()).posicionarNavio(2, coordenadas[0].charAt(0), coordenadas[1], coordenadas[2].charAt(0));
                    break;
                case "contra-torpedeiro":
                    resposta = tabuleiros.get(mensagem.getJogador()).posicionarNavio(3, coordenadas[0].charAt(0), coordenadas[1], coordenadas[2].charAt(0));
                    break;
                case "porta-avioes":
                    resposta = tabuleiros.get(mensagem.getJogador()).posicionarNavio(5, coordenadas[0].charAt(0), coordenadas[1], coordenadas[2].charAt(0));
                    break;
            }
            mensagem.getJogador().enviarMensagem(resposta);
            if (resposta.equals("Navio posicionado")) {
                mensagem.getJogador().proximoPasso();
            }
        }
    }

    public void atacarNavio(Mensagem mensagem){
        if (mensagem.getJogador() != vez) {
            mensagem.getJogador().enviarMensagem("Não é a sua vez, espere mais um pouco");
        }
        else{
            String resposta = "";
            String[] coordenadas = mensagem.getMensagem().split("-");
            Jogador atual = vez;
            alterarVez();
            resposta = tabuleiros.get(vez).atacar(coordenadas[0].charAt(0), coordenadas[1]);
            atual.enviarMensagem(mensagem.getMensagem() + " " + resposta);
            vez.enviarMensagem(mensagem.getMensagem() + " " + resposta + "\nAgora é sua vez.");
        }
    }

    public void menu(Mensagem mensagem){
        String resposta = "";
        int opcao = Integer.parseInt(mensagem.getMensagem());
        switch (opcao) {
            case 1:
                resposta = tabuleiros.get(mensagem.getJogador()).toString();
                break;
            case 2:
                Jogador jogador = mensagem.getJogador() == jogadorUm ? jogadorDois : jogadorUm;
                resposta = tabuleiros.get(jogador).tabuleiroParaOponente();
                break;
        }
        mensagem.getJogador().enviarMensagem(resposta);
    }

    //método responsável por intercalar a vez dos jogadores
    public void alterarVez(){
        if (vez == jogadorUm) {
            vez = jogadorDois;
        }
        else{
            vez = jogadorUm;
        }
    }

    public void run(){
        System.out.println("Iniciada a partida!");
        while(!jogadorUm.estaPronto() || !jogadorDois.estaPronto()){ //Posicionando navios
            if (mensagens.size() > 0) {
                tratarComando(mensagens.poll(), "atacar");
            }
        }
        jogadorUm.enviarMensagem("Iniciaram os ataques, você ataca primeiro");
        jogadorDois.enviarMensagem("Iniciaram os ataques, você ataca em segundo");
        while(tabuleiros.get(jogadorUm).getPedacosNavio() > 0 && tabuleiros.get(jogadorDois).getPedacosNavio() > 0 ){//Fase de ataques
            if(mensagens.size() > 0){
                tratarComando(mensagens.poll(), "preencher");
            }
        }
        if(tabuleiros.get(jogadorUm).getPedacosNavio() > 0){
            jogadorUm.enviarMensagem("Vitoria!!!");
            jogadorDois.enviarMensagem("Derrota");
        }
        else{
            jogadorDois.enviarMensagem("Vitoria!!!");
            jogadorUm.enviarMensagem("Derrota");
        }
        jogadorUm.enviarMensagem("Fim de Jogo!");
        jogadorDois.enviarMensagem("Fim de Jogo!");
        jogadorUm.encerrar();
        jogadorDois.encerrar();
    }
}
