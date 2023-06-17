package Pecas;
import Auxiliar.InvalidValueException;
import Xadrez.Tabuleiro;
import auxiliar.Posicao;

public class Torre extends Peca{
    


    public Torre(String sAFileName, Posicao aPosicao, boolean bBrancas) {
           super(sAFileName, aPosicao, bBrancas);
    }
    @Override
    public String toString(){
        return "Torre";
    }
    @Override
    public boolean setPosicao(Posicao umaPosicao, Tabuleiro umTabuleiro) throws InvalidValueException {     
        if (umaPosicao.posicaoInvalida())
            throw new InvalidValueException();
        else{
            this.pPosicao.setPosicao(umaPosicao);
            return true;
        }
    }
    
    @Override
    public double limiteMovimento(){
        return 7;
    }
    
    @Override
    public boolean direcaoMovimento(Posicao pIncremento){  

        int pColuna = this.pPosicao.getColuna();
        int pLinha = this.pPosicao.getLinha();

        return((pColuna == pIncremento.getColuna()) && (pLinha != pIncremento.getLinha()) ||
                (pColuna != pIncremento.getColuna()) && (pLinha == pIncremento.getLinha()));
            
    }
    
}
