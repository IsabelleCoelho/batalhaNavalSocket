import java.util.HashMap;

public class Tabuleiro {
    private HashMap<Character, Character[]> tabuleiro;
    private int pedacosDeNavio;

    public Tabuleiro(){
        tabuleiro = new HashMap<Character, Character[]>();
        for (int i = 0; i < 10; i++) {
            tabuleiro.put((char)('A'+i), new Character[10]);
        }
        for (Character key : tabuleiro.keySet()){
            Character[] linha = tabuleiro.get(key);
            for (int i = 0; i < linha.length; i++) {
                linha[i] = 'O';
            }
        }
        pedacosDeNavio = 0;
    }

    @Override
    public String toString(){
        String output = "  1 2 3 4 5 6 7 8 9 10\n";
        for (Character key : tabuleiro.keySet()){
            Character[] linha = tabuleiro.get(key);
            output += key.toString();
            for (int i = 0; i < linha.length; i++) {
                output += " " + linha[i];
            }
            output += "\n";
        }
        return output;
    }

    public String tabuleiroParaOponente(){
        String output = "  1 2 3 4 5 6 7 8 9 10\n";
        for (Character key : tabuleiro.keySet()){
            Character[] linha = tabuleiro.get(key);
            output += key.toString();
            for (int i = 0; i < linha.length; i++) {
                output += " " + (linha[i].equals('F') || linha[i].equals('A')? linha[i] : 'O');
            }
            output += "\n";
        }
        return output;
    }

    public String atacar(Character linha, String scoluna){
        int coluna = Integer.parseInt(scoluna);
        coluna = coluna - 1;
        if(tabuleiro.get(linha)[coluna] == 'N'){
            tabuleiro.get(linha)[coluna] = 'F';    
            pedacosDeNavio--;
            return "Fogo";
        }
        else if(tabuleiro.get(linha)[coluna] != 'O'){
            return "Posicao ja atacada";
        }
        tabuleiro.get(linha)[coluna] = 'A';
        return "Agua!";
    }

    public String posicionarNavio(int tamanhoNavio, Character linha, String scoluna, Character direcao){
        int coluna = Integer.parseInt(scoluna);
        coluna = coluna - 1;
        switch (direcao) {
            case 'C':
                if (((int)linha - tamanhoNavio)-1 < (int)'A'){
                    return "Posicao inválida";
                }
                for (int i = 0; i < tamanhoNavio; i++) {
                    if(tabuleiro.get((char)(linha-i))[coluna] == 'N'){
                        return String.format("Colisão com outro navio na posicao %c %d", (char)(linha-i), coluna+1);
                    }
                }
                for (int i = 0; i < tamanhoNavio; i++) {
                    tabuleiro.get((char)(linha-i))[coluna] = 'N';
                }
                break;
            case 'B':
                if (((int)linha + tamanhoNavio)-1 > (int)'J'){
                    return "Posicao inválida";
                }
                for (int i = 0; i < tamanhoNavio; i++) {
                    if(tabuleiro.get((char)(linha+i))[coluna] == 'N'){
                        return String.format("Colisão com outro navio na posicao %c %d", (char)(linha+i), coluna+1);
                    }
                }
                for (int i = 0; i < tamanhoNavio; i++) {
                    tabuleiro.get((char)(linha+i))[coluna] = 'N';
                }
                break;
            case 'D':
                if ((coluna + tamanhoNavio)-1 >= 10){
                    return "Posicao inválida";
                }
                for (int i = 0; i < tamanhoNavio; i++) {
                    if(tabuleiro.get((char)(linha))[coluna+i] == 'N'){
                        return String.format("Colisão com outro navio na posicao %c-%d", (char)(linha), coluna+i+1);
                    }
                }
                for (int i = 0; i < tamanhoNavio; i++) {
                    tabuleiro.get(linha)[coluna+i] = 'N';
                }
                break;
            case 'E':
                if ((coluna - tamanhoNavio)+1 < 0){
                    return "Posicao inválida";
                }
                for (int i = 0; i < tamanhoNavio; i++) {
                    if(tabuleiro.get((char)(linha))[coluna-i] == 'N'){
                        return String.format("Colisão com outro navio na posicao %c %d", (char)(linha), coluna-i+1);
                    }
                }
                for (int i = 0; i < tamanhoNavio; i++) {
                    tabuleiro.get(linha)[coluna-i] = 'N';
                }
                break;
        }
        pedacosDeNavio += tamanhoNavio;
        return "Navio posicionado";
    }

    public int getPedacosNavio(){
        return pedacosDeNavio;
    }

}