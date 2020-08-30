import java.util.regex.Pattern;
public class Mensagem {
    private Jogador jogador;
    private String mensagem;
    private String comando;

    public Mensagem(Jogador jogador, String mensagem){
        this.jogador = jogador;
        this.mensagem = mensagem;
    }

    public Jogador getJogador(){
        return this.jogador;
    }

    public String getMensagem(){
        return this.mensagem;
    }
    public void setComando(String comando){
        this.comando = comando;
    }

    public String getComando(){
        return comando;
    }

    //método responsável por analisar se a mensagem enviada pelo jogador está no padrão correto para seu objetivo
    //no caso, o cliente deve escrever 1, 2 ou 3 
    public boolean cleannerMenu(){ 
        return Pattern.matches("[123]", this.mensagem);
    }
    //método responsável por analisar se a mensagem enviada pelo jogador está no padrão correto para seu objetivo
    //no caso, o cliente deve enviar um dado no formato "letra-numero-direcao" para preencher o tabuleiro com seus navios
    public boolean cleannerPreencher(){
        String[] pattern = mensagem.split("-");
        if(pattern.length != 3){
            return false;
        }
        return Pattern.matches("[A-J]", pattern[0]) && Pattern.matches("[1-9]|10", pattern[1]) && Pattern.matches("[CBDE]", pattern[2]);
    }
    //método responsável por analisar se a mensagem enviada pelo jogador está no padrão correto para seu objetivo
    //no caso, o cliente deve enviar um dado no formato "letra-numero" indicando a casa que deseja atacar
    public boolean cleannerAtacar(){
        String[] pattern = mensagem.split("-");
        if(pattern.length != 2){
            return false;
        }
        return Pattern.matches("[A-J]", pattern[0]) && Pattern.matches("[1-9]|10", pattern[1]);
    }
    @Override
    public String toString(){
        return "Mensagem: " + mensagem;
    }
}