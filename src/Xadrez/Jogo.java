package Xadrez;

import Auxiliar.Consts;
import Auxiliar.InvalidValueException;
import Pecas.*;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Jogo extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Tabuleiro tTabuleiro;//atributo com a janela de desenho
    Conjunto cBrancas;
    Conjunto cPretas;
    boolean bEmJogada;
    Peca pecaEmMovimento;
    Peca pecaEmPromocao;
    int controleMouse;
    int nJogadas;
    boolean controleTecla;
    boolean turnoBranca;
    boolean fimDeJogo;
    boolean emPromocao;

    public enum CoresConjuntos {

        BRANCAS, PRETAS
    };

    public Jogo() {
        cBrancas = new Conjunto();
        cPretas = new Conjunto();
        tTabuleiro = new Tabuleiro();
        tTabuleiro.setFocusable(true);
        tTabuleiro.addMouseListener(this);//Adiciona evento de mouse ao Painel de desenho
        tTabuleiro.addKeyListener(this);
        bEmJogada = false;
        pecaEmMovimento = null;
        pecaEmPromocao = null;
        controleMouse = -1;
        nJogadas = 0;
        controleTecla = false;
        turnoBranca = true;
        fimDeJogo = false;
        emPromocao = false;
        initComponents();
    }

    // Inicializa pecas e suas posicoes na main
    public void addPeca(Peca aPeca, CoresConjuntos aCorConjunto) {
        if (aCorConjunto == CoresConjuntos.BRANCAS) {
            cBrancas.add(aPeca);
        } else {
            cPretas.add(aPeca);
        }
    }

    // Retorna objeto que foi selecionado pelo jogador
    public Peca getPecaClicada(Posicao aPosicao) {
        Peca pTemp = cBrancas.getPecaClicada(aPosicao);
        if (pTemp != null) {
            return pTemp;
        }
        pTemp = cPretas.getPecaClicada(aPosicao);
        if (pTemp != null) {
            return pTemp;
        }
        return null;
    }
    
    // Verifica se existe qualquer peca em dada posicao
    protected boolean posicaoOcupada(Posicao pPosicao){
        return (this.cBrancas.PecaPosicao(pPosicao) || this.cPretas.PecaPosicao(pPosicao));
    }

    // Retorna, com base nas condicoes atuais, as posicoes possiveis para a peca selecionada
    // Atualiza a lista de posicao de adversarios
    protected ArrayList<Posicao> posicaoValida(ArrayList<Posicao> pAdversario){
        try{
            Posicao pAtual = pecaEmMovimento.getPosicaoPeca();
            ArrayList<Posicao> posicaoPossivel = new ArrayList<>();
            posicaoPossivel.add(pAtual);
            
            // Checa os movimentos nas 8 direcoes & atualiza possiveis ataques
            for (int i = -1; i <= 1; i++)
                for (int j = -1; j <= 1; j++)
                    if (i != 0 || j != 0)
                        this.checaIncremento(posicaoPossivel, pAdversario, pAtual, i, j);

            return posicaoPossivel;
        }catch(NullPointerException e){
            System.out.println("Atenção -> tentativa de ler peça inválida!!");
        }
        return null;
    }
    
    // Funcao auxiliar da posicaoValida() - utiliza as funcoes da interface Movimentos implementada na classe Peca
    // Incrementa, de 1 em 1 casa, nas 8 direcoes e aplica as regras
    protected void checaIncremento(ArrayList<Posicao> ondePode, ArrayList<Posicao> pAdversario, Posicao pAtual, // Sempre igual
                                    int deltaLinha, int deltaColuna){                                        // Caso a caso
        // Define parametros iniciais referentes a peca em movimento
        int distAtual = 0;
        int colAtual = pAtual.getColuna();
        int linAtual = pAtual.getLinha();
        String flagPeca = pecaEmMovimento.toString();
        Posicao pIncremento;
        
        // Caso especial cavalo -> unico que pode se mover sobre outras pecas
        // Definido so um caso (1,1) para não repetir checagem 8 vezes
        if(flagPeca.equals("Cavalo") && deltaLinha == 1 && deltaColuna == 1){               
            for(int i = linAtual - 2; i <= linAtual + 2; i++){               
                for(int j = colAtual - 2; j <= colAtual + 2; j++){
                    if(i > 7 || i < 0) continue;
                    if(j > 7 || j < 0) continue;
                    pIncremento = new Posicao(i, j);
                    if(pecaEmMovimento.direcaoMovimento(pIncremento)){
                        if(!this.posicaoOcupada(pIncremento))
                            ondePode.add(pIncremento);
                        else
                            if(!pecaEmMovimento.temAMesmaCorQue(this.getPecaClicada(pIncremento)))
                                pAdversario.add(pIncremento);      
                    }   
                }
            }
        }
   
        // Enquanto estiver no range de movimentos possiveis e na direcao correta adiciona na lista
        while(++distAtual <= pecaEmMovimento.limiteMovimento() &&
                pecaEmMovimento.direcaoMovimento(new Posicao(pAtual.getLinha() + deltaLinha, pAtual.getColuna() + deltaColuna))){
            linAtual += deltaLinha;
            colAtual += deltaColuna;
            
            if(linAtual < 0 || linAtual > 7) break;
            if(colAtual < 0 || colAtual > 7) break;
            
            pIncremento = new Posicao(linAtual, colAtual);
            
            // Posicao sem pecas
            if(!this.posicaoOcupada(pIncremento)){
                // Caso especial - peao
                if(flagPeca.equals("Peao") && pecaEmMovimento.ehDiagonal(pIncremento)) break;
                
                // Caso especial - rei
                if(flagPeca.equalsIgnoreCase("Rei")){
                    ArrayList<Posicao> aquiNaoMeuRei;
                    
                    // Condicao de checagem posicao de xeque - rei nao pode se colocar em xeque
                    if(this.pecaEmMovimento.converterBoolCoordenada() == 1){
                        aquiNaoMeuRei = this.estaraEmXeque(pIncremento, CoresConjuntos.BRANCAS);   
                    }else{
                        aquiNaoMeuRei = this.estaraEmXeque(pIncremento, CoresConjuntos.PRETAS);
                    }
                    
                    // Existem posicoes possiveis de xeque - destaca pecas adversarias
                    if(aquiNaoMeuRei != null){
                        for(Posicao p : aquiNaoMeuRei){
                            System.out.println(this.getPecaClicada(p) + " em " + p);
                            this.tTabuleiro.destaqueXeque(p);
                        }
                        break; 
                    }
                }

                // Posicao possivel de movimento
                ondePode.add(pIncremento);
                
            // Posicao tem peca
            }else{
                // Verifica se eh peca adversaria
                if(!pecaEmMovimento.temAMesmaCorQue(this.getPecaClicada(pIncremento))){
                    // Caso especial - peao
                    if(flagPeca.equals("Peao") &&
                        (!pecaEmMovimento.ehDiagonal(pIncremento) || ((pecaEmMovimento.converterBoolCoordenada()*deltaLinha) == -1)))
                        break;
                    
                    // Posicao possivel de ataque
                    pAdversario.add(pIncremento);
                }
                // Caso seja peca da mesma cor interrompe movimento
                break;
            }
        }
    }
    
    // Funcao que verifica se rei estara em situacao de xeque
    public ArrayList<Posicao> estaraEmXeque(Posicao pPosicao, CoresConjuntos aCorConjunto) {
        boolean flag = false;
        ArrayList<Posicao> osMatadores = new ArrayList<>();
        Peca reiBKP = this.pecaEmMovimento;
        
        if(aCorConjunto == CoresConjuntos.BRANCAS) {  
            for(Peca vaiMataORei : cBrancas){
                // Caso especial - rei
                if(vaiMataORei.toString().equals("Rei")) continue;
                
                // Caso especial - peao
                if(vaiMataORei.toString().equals("Peao")){
                    if((vaiMataORei.ehDiagonal(pPosicao) && (pPosicao.getLinha() - vaiMataORei.getPosicaoPeca().getLinha() == vaiMataORei.converterBoolCoordenada()))){
                        flag = true;
                        osMatadores.add(vaiMataORei.getPosicaoPeca());
                    }
                // Caso geral 
                }else{
                    this.pecaEmMovimento = vaiMataORei;
                    ArrayList<Posicao> osAlemaoPeca = new ArrayList<>();
                    ArrayList<Posicao> ondePodePeca = posicaoValida(osAlemaoPeca);

                    for(Posicao pos : ondePodePeca){
                        if(pPosicao.igual(pos)){
                            flag = true;
                            osMatadores.add(vaiMataORei.getPosicaoPeca());
                        }
                    }
                    this.pecaEmMovimento = reiBKP;
                }
            }
        }else{
            for(Peca vaiMataORei : cPretas){
                if(vaiMataORei.toString().equals("Rei")) continue;
                if(vaiMataORei.toString().equals("Peao")){
                    if((vaiMataORei.ehDiagonal(pPosicao) && (pPosicao.getLinha() - vaiMataORei.getPosicaoPeca().getLinha() == vaiMataORei.converterBoolCoordenada()))){
                        //System.out.println("condicao certa!!!!!!!!!!!!!!!");
                        flag = true;
                        osMatadores.add(vaiMataORei.getPosicaoPeca());
                    }
                }else{
                    this.pecaEmMovimento = vaiMataORei;
                    ArrayList<Posicao> osAlemaoPeca = new ArrayList<>();
                    ArrayList<Posicao> ondePodePeca = posicaoValida(osAlemaoPeca);

                    for(Posicao pos : ondePodePeca){
                        if(pPosicao.igual(pos)){
                            flag = true;
                            //System.out.println("Posicao de xeque para " + vaiMataORei);
                            osMatadores.add(vaiMataORei.getPosicaoPeca());
                        }
                    }
                    this.pecaEmMovimento = reiBKP;
                }
            }
        }

        return flag ? osMatadores : null;
    }
    
    
   
    protected void destacarMovimentacao(){
        if(this.pecaEmMovimento != null){
            // Destaque peca selecionada
            Posicao atual = pecaEmMovimento.getPosicaoPeca();
            this.tTabuleiro.destacarPosicao(atual, true);

            // Listas dos movimentos e ataques possiveis
            ArrayList<Posicao> ataques = new ArrayList<>();
            ArrayList<Posicao> movimentos = this.posicaoValida(ataques);
            

            // Mostra movimentos possiveis - botao direito
            if(this.controleMouse == 3){
                for(Posicao pMove : movimentos){
                    this.tTabuleiro.preencherPosicao(pMove, true);
                }
            }

            // Mostra ataques possiveis - botao scroll (meio)
            if(this.controleMouse == 2){
                // Verificacao ataques possiveis
                for(Posicao pAtaque : ataques){
                    this.tTabuleiro.preencherPosicao(pAtaque, false);
                }
            }
            
            if(this.controleTecla){
                for(Posicao pAtaque : ataques){
                    this.tTabuleiro.preencherPosicao(pAtaque, false);
                }
                
                for(Posicao pMove : movimentos){
                    this.tTabuleiro.preencherPosicao(pMove, true);
                }
            }
            
           
        }
    }

  
    
    public void paint(Graphics g) {
        super.paint(g);
        cBrancas.AutoDesenho(tTabuleiro);
        cPretas.AutoDesenho(tTabuleiro);    
        /*
        Posicao teste = new Posicao(5,5);
        this.tTabuleiro.destacarPosicao(teste);
        
        Posicao teste2 = new Posicao(4,3);
        this.tTabuleiro.preencherPosicao(teste2);
        */
        
        destacarMovimentacao();
        
        //destaquePecaAtual();
        //mostraMovimento();
        
        
    }

    public void go() {       
        TimerTask task = new TimerTask() {
            public void run() {
                if(!fimDeJogo)
                    repaint();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, Consts.DELAY);
        
    }

    public Posicao getPosicaoDoClique(MouseEvent aMouseEvent) {
        return new Posicao(aMouseEvent.getY() / Consts.SIZE,
                aMouseEvent.getX() / Consts.SIZE);
    }
    

    public void mousePressed(MouseEvent e) {
        int x = e.getX();//pega as coordenadas do mouse
        int y = e.getY();
        this.clickLabel.setText("x:" + x + "  y:" + y + "   -   Quadrante: [" + y / Consts.SIZE + "," + x / Consts.SIZE + "]");     

        if(!this.fimDeJogo && !this.emPromocao){
            // Controle para mostrar dica de ataque/movimento possivel
            this.controleMouse = e.getButton();

            // Objeto peca clicada
            Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e));       

            // Output tela sem jogada ativa
            if (!bEmJogada){
                if(pecaClicada == null)
                    System.out.println("Nenhuma peca selecionada");
                else
                    System.out.println("Peca " + pecaClicada + " selecionada");
            }

            // Peca que vai executar movimento ja selecionada
            if(bEmJogada){
                // Cria lista de ataques e movimentos possiveis para a peca de acordo com a condicao do tabuleiro
                ArrayList<Posicao> ataqueValido  = new ArrayList<>();
                if(pecaClicada != null){  
                    for(Posicao p : this.posicaoValida(ataqueValido));    
                }


                // Selecionou uma posicao vazia
                if (pecaClicada == null){
                    // Condicao acabou movimento ou esta em movimento
                    System.out.println(pecaEmMovimento);

                    // Verifica se posicao eh valida
                    if(this.posicaoValida(ataqueValido).contains(getPosicaoDoClique(e))){
                        try{
                            pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), tTabuleiro);
                            this.checaPromocao(pecaEmMovimento);
                        }catch(InvalidValueException excecao){
                            System.out.println(excecao.getMessage());
                        }
                        this.nJogadas++;
                        pecaEmMovimento = null;
                        bEmJogada = false;
                    }else{
                        System.out.println("AVISO 1: Jogada ainda em movimento, selecione uma posicao valida");
                    }

                // Selecionou uma peca
                }else{     
                    // Verifica se ataque eh valido / deselecionou peca
                    if(ataqueValido.contains(pecaClicada.getPosicaoPeca()) || pecaEmMovimento == pecaClicada){
                        try{
                            pecaEmMovimento.setPosicao(this.getPosicaoDoClique(e), tTabuleiro);
                            this.checaPromocao(pecaEmMovimento);
                        }catch(InvalidValueException excecao){
                            System.out.println(excecao.getMessage());
                        }

                        // Selecionou mesma peca
                        if(pecaEmMovimento == pecaClicada){
                            System.out.println("A peca foi deselecionada, escolha outra peca");
                        }

                        // Executa ataque
                        if(!pecaEmMovimento.temAMesmaCorQue(pecaClicada)){
                            if(pecaClicada.toString().equalsIgnoreCase("rei")){
                                this.fimDeJogo = true;
                                this.emPromocao = false;
                                this.telaFim(); 
                            }
                            this.nJogadas++;
                            cBrancas.pecaFora(pecaClicada);
                            cPretas.pecaFora(pecaClicada);                        
                    }

                    // Fim da jogada        
                    pecaEmMovimento = null;
                    bEmJogada = false;

                    // Selecionou uma peca invalida
                    }else{
                        System.out.println("aviso 2: Jogada ainda em movimento, selecione uma posicao valida");
                    }
                }
            // Selecao da peca que vai executar a jogada
            }else{
                if (pecaClicada != null) {   
                    if(pecaClicada.pecaBranca() != (this.nJogadas % 2 == 0)){
                        if(pecaClicada.pecaBranca()){
                            System.out.println("ATENCAO: eh a vez das pecas PRETAS!");
                        }else{
                            System.out.println("ATENCAO: eh a vez das pecas BRANCAS!");
                        }
                        pecaEmMovimento = null;
                        bEmJogada = false;
                    }else{
                        System.out.println("Movimentacao em andamento, selecione o destino da peca");
                        pecaEmMovimento = pecaClicada;
                        bEmJogada = true;
                    }
                }
            }
            if(!fimDeJogo && !emPromocao)
                repaint();
        }

    }
    
    public void checaPromocao(Peca pPeca){
        if(pPeca.toString() == "Peao"){
            if(pPeca.pecaBranca() && pPeca.getPosicaoPeca().getLinha() == 0){
                this.telaPromocao();
                this.emPromocao = true;
            }

            if(!pPeca.pecaBranca() && pPeca.getPosicaoPeca().getLinha() == 7){
                this.telaPromocao();
                this.emPromocao = true;
            }

            if(this.emPromocao){
                this.pecaEmPromocao = pPeca;
                System.out.println("ESCOLHA A PROMOCAO DO PEAO:");
                System.out.println("C --> CAVALO");
                System.out.println("T --> TORRE");
                System.out.println("B --> BISPO");
                System.out.println("R --> RAINHA");
            }
        }
 
    }
    
    public void telaFim(){
        ImageIcon iImage;
        try {
            String imagePath = new File(".").getCanonicalPath() + Consts.PATH + "GameOver.png";
            Image originalImage = ImageIO.read(new File(imagePath));
            int newWidth = 8 * Consts.SIZE;
            int newHeight = 8 * Consts.SIZE;
            Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(resizedImage);
            Graphics2D g2d = (Graphics2D) tTabuleiro.getGraphics();
            imageIcon.paintIcon(tTabuleiro, g2d, 0, 0);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }   
    }
    
    public void telaPromocao(){
        ImageIcon iImage;
        try {
            String imagePath = new File(".").getCanonicalPath() + Consts.PATH + "Promotion.png";
            Image originalImage = ImageIO.read(new File(imagePath));
            int newWidth = 8 * Consts.SIZE;
            int newHeight = 8 * Consts.SIZE;
            Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(resizedImage);
            Graphics2D g2d = (Graphics2D) tTabuleiro.getGraphics();
            imageIcon.paintIcon(tTabuleiro, g2d, 0, 0);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } 
    }
    
    public void gerarTabuleiroRandomizado() {
        // Criar um array para armazenar as posições iniciais das peças
        Posicao[] posicoesIniciais = new Posicao[32];

        // Gerar posições aleatórias para as peças brancas
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            // Caso peao
            int row = 1;
            if(i < 8){
                row = 1 + random.nextInt(6); // Gera um número aleatório entre 1 e 6 (inclusive)
            }else{
                row = random.nextInt(8);  // Gera um número aleatório entre 0 e 7 (inclusive)
            }
                
            int col = random.nextInt(8); // Gera um número aleatório entre 0 e 7 (inclusive)
            posicoesIniciais[i] = new Posicao(row, col);
        }

        // Gerar posições aleatórias para as peças pretas
        for (int i = 16; i < 32; i++) {
            // Caso peao
            int row = 1;
            if(i < 24){
                row = 1 + random.nextInt(6); // Gera um número aleatório entre 1 e 6 (inclusive)
            }else{
                row = random.nextInt(8);  // Gera um número aleatório entre 0 e 7 (inclusive)
            }
             
            int col = random.nextInt(8); // Gera um número aleatório entre 0 e 7 (inclusive)
            posicoesIniciais[i] = new Posicao(row, col);
        }

        // Criar as peças brancas e pretas e adicionar ao conjunto correspondente
        this.cBrancas = new Conjunto();
        this.cPretas = new Conjunto();
        
        if(this.getPecaClicada(posicoesIniciais[0]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[0], true));
            
        
        if(this.getPecaClicada(posicoesIniciais[1]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[1], true));
        if(this.getPecaClicada(posicoesIniciais[2]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[2], true));
        if(this.getPecaClicada(posicoesIniciais[3]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[3], true));                
        if(this.getPecaClicada(posicoesIniciais[4]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[4], true));
        if(this.getPecaClicada(posicoesIniciais[5]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[5], true));
        if(this.getPecaClicada(posicoesIniciais[6]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[6], true));
        if(this.getPecaClicada(posicoesIniciais[7]) == null) cBrancas.add(new Peao("PeaoBranco.png", posicoesIniciais[7], true));  
        if(this.getPecaClicada(posicoesIniciais[8]) == null) cBrancas.add(new Torre("TorreBranca.png", posicoesIniciais[8], true));                  
        if(this.getPecaClicada(posicoesIniciais[9]) == null) cBrancas.add(new Torre("TorreBranca.png", posicoesIniciais[9], true));                  
        if(this.getPecaClicada(posicoesIniciais[10]) == null) cBrancas.add(new Cavalo("CavaloBranco.png", posicoesIniciais[10], true));                  
        if(this.getPecaClicada(posicoesIniciais[1]) == null) cBrancas.add(new Cavalo("CavaloBranco.png", posicoesIniciais[11], true));                                  
        if(this.getPecaClicada(posicoesIniciais[12]) == null) cBrancas.add(new Bispo("BispoBranco.png", posicoesIniciais[12], true));                                  
        if(this.getPecaClicada(posicoesIniciais[13]) == null) cBrancas.add(new Bispo("BispoBranco.png", posicoesIniciais[13], true));                                  
        if(this.getPecaClicada(posicoesIniciais[14]) == null) cBrancas.add(new Rainha("RainhaBranca.png", posicoesIniciais[14], true));                                  
        
        // Forcando que tenha rei no tabuleiro
        if(this.getPecaClicada(posicoesIniciais[15]) != null) cBrancas.pecaFora(this.getPecaClicada(posicoesIniciais[15]));
        cBrancas.add(new Rei("ReiBranco.png", posicoesIniciais[15], true));                                        
        
        if(this.getPecaClicada(posicoesIniciais[16]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[16], false));                 
        if(this.getPecaClicada(posicoesIniciais[17]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[17], false));                                 
        if(this.getPecaClicada(posicoesIniciais[18]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[18], false));                 
        if(this.getPecaClicada(posicoesIniciais[19]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[19], false));                                 
        if(this.getPecaClicada(posicoesIniciais[20]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[20], false));                 
        if(this.getPecaClicada(posicoesIniciais[21]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[21], false));                                 
        if(this.getPecaClicada(posicoesIniciais[22]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[22], false));                 
        if(this.getPecaClicada(posicoesIniciais[23]) == null) cPretas.add(new Peao("PeaoPreto.png", posicoesIniciais[23], false));                                 
        if(this.getPecaClicada(posicoesIniciais[24]) == null) cPretas.add(new Torre("TorrePreta.png", posicoesIniciais[24], false));                  
        if(this.getPecaClicada(posicoesIniciais[25]) == null) cPretas.add(new Torre("TorrePreta.png", posicoesIniciais[25], false));                  
        if(this.getPecaClicada(posicoesIniciais[26]) == null) cPretas.add(new Cavalo("CavaloPreto.png", posicoesIniciais[26], false));                  
        if(this.getPecaClicada(posicoesIniciais[27]) == null) cPretas.add(new Cavalo("CavaloPreto.png", posicoesIniciais[27], false));                                  
        if(this.getPecaClicada(posicoesIniciais[28]) == null) cPretas.add(new Bispo("BispoPreto.png", posicoesIniciais[28], false));                                  
        if(this.getPecaClicada(posicoesIniciais[29]) == null) cPretas.add(new Bispo("BispoPreto.png", posicoesIniciais[29], false));                                  
        if(this.getPecaClicada(posicoesIniciais[30]) == null) cPretas.add(new Rainha("RainhaPreta.png", posicoesIniciais[30], false));                                  
  
        // Forcando que tenha rei no tabuleiro
        if(this.getPecaClicada(posicoesIniciais[31]) != null){
            cBrancas.pecaFora(this.getPecaClicada(posicoesIniciais[31]));
            cPretas.pecaFora(this.getPecaClicada(posicoesIniciais[31]));
        }
        cPretas.add(new Rei("ReiPreto.png", posicoesIniciais[31], false)); 
        
    }
    
    public static void newGame(Jogo tMeuJogo){
        tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,0), true), Jogo.CoresConjuntos.BRANCAS);
        tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,1), true), Jogo.CoresConjuntos.BRANCAS);
        tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,2), true), Jogo.CoresConjuntos.BRANCAS);
        tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,3), true), Jogo.CoresConjuntos.BRANCAS);                
        tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,4), true), Jogo.CoresConjuntos.BRANCAS);
        tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,5), true), Jogo.CoresConjuntos.BRANCAS);
        tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,6), true), Jogo.CoresConjuntos.BRANCAS);
        tMeuJogo.addPeca(new Peao("PeaoBranco.png", new Posicao(6,7), true), Jogo.CoresConjuntos.BRANCAS);  
        tMeuJogo.addPeca(new Torre("TorreBranca.png", new Posicao(7,7), true), Jogo.CoresConjuntos.BRANCAS);                  
        tMeuJogo.addPeca(new Torre("TorreBranca.png", new Posicao(7,0), true), Jogo.CoresConjuntos.BRANCAS);                  
        tMeuJogo.addPeca(new Cavalo("CavaloBranco.png", new Posicao(7,1), true), Jogo.CoresConjuntos.BRANCAS);                  
        tMeuJogo.addPeca(new Cavalo("CavaloBranco.png", new Posicao(7,6), true), Jogo.CoresConjuntos.BRANCAS);                                  
        tMeuJogo.addPeca(new Bispo("BispoBranco.png", new Posicao(7,5), true), Jogo.CoresConjuntos.BRANCAS);                                  
        tMeuJogo.addPeca(new Bispo("BispoBranco.png", new Posicao(7,2), true), Jogo.CoresConjuntos.BRANCAS);                                  
        tMeuJogo.addPeca(new Rainha("RainhaBranca.png", new Posicao(7,3), true), Jogo.CoresConjuntos.BRANCAS);                                  
        tMeuJogo.addPeca(new Rei("ReiBranco.png", new Posicao(7,4), true), Jogo.CoresConjuntos.BRANCAS);                                                  

        tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,0), false), Jogo.CoresConjuntos.PRETAS);                 
        tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,1), false), Jogo.CoresConjuntos.PRETAS);                                 
        tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,2), false), Jogo.CoresConjuntos.PRETAS);                 
        tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,3), false), Jogo.CoresConjuntos.PRETAS);                                 
        tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,4), false), Jogo.CoresConjuntos.PRETAS);                 
        tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,5), false), Jogo.CoresConjuntos.PRETAS);                                 
        tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,6), false), Jogo.CoresConjuntos.PRETAS);                 
        tMeuJogo.addPeca(new Peao("PeaoPreto.png", new Posicao(1,7), false), Jogo.CoresConjuntos.PRETAS);                                 
        tMeuJogo.addPeca(new Torre("TorrePreta.png", new Posicao(0,7), false), Jogo.CoresConjuntos.PRETAS);                  
        tMeuJogo.addPeca(new Torre("TorrePreta.png", new Posicao(0,0), false), Jogo.CoresConjuntos.PRETAS);                  
        tMeuJogo.addPeca(new Cavalo("CavaloPreto.png", new Posicao(0,1), false), Jogo.CoresConjuntos.PRETAS);                  
        tMeuJogo.addPeca(new Cavalo("CavaloPreto.png", new Posicao(0,6), false), Jogo.CoresConjuntos.PRETAS);                                  
        tMeuJogo.addPeca(new Bispo("BispoPreto.png", new Posicao(0,5), false), Jogo.CoresConjuntos.PRETAS);                                  
        tMeuJogo.addPeca(new Bispo("BispoPreto.png", new Posicao(0,2), false), Jogo.CoresConjuntos.PRETAS);                                  
        tMeuJogo.addPeca(new Rainha("RainhaPreta.png", new Posicao(0,3), false), Jogo.CoresConjuntos.PRETAS);                                  
        tMeuJogo.addPeca(new Rei("ReiPreto.png", new Posicao(0,4), false), Jogo.CoresConjuntos.PRETAS);                                                  


        tMeuJogo.setVisible(true);                
        tMeuJogo.go();
        
    }


    
    @Override
    public void keyPressed(KeyEvent e) {
        if(!this.fimDeJogo && !this.emPromocao){
            if(e.getKeyCode() == KeyEvent.VK_S){
                /* "jogoSalvos"+File.pathSeparator+"Nome" */
                File tanque = new File("Teste_Save_Peca.dat");
                try {
                    tanque.createNewFile();
                    FileOutputStream canoOut = new FileOutputStream(tanque);
                    GZIPOutputStream compactador = new GZIPOutputStream(canoOut);
                    ObjectOutputStream serializador = new ObjectOutputStream(compactador);
                    serializador.writeObject(this.cBrancas);
                    serializador.writeObject(this.cPretas);
                    serializador.flush();
                    serializador.close();
                    System.out.println("Jogo salvo!");
                } catch (IOException ex) {
                    System.out.println("Ocorreu o seguinte erro:" + ex.getMessage());
                }

            }

            if(e.getKeyCode() == KeyEvent.VK_O){
                File tanque = new File("Teste_Save_Peca.dat");
                try {
                    FileInputStream canoIn = new FileInputStream(tanque);
                    GZIPInputStream descompactador = new GZIPInputStream(canoIn);
                    ObjectInputStream deserializador = new ObjectInputStream(descompactador);
                    this.cBrancas = (Conjunto) deserializador.readObject();    
                    this.cPretas = (Conjunto) deserializador.readObject();
                    deserializador.close();
                    System.out.println("Jogo carregado!");  
                } catch (IOException ex) {
                    System.out.println("Ocorreu o seguinte erro:" + ex.getMessage());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Jogo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if(e.getKeyCode() == KeyEvent.VK_R){
                this.gerarTabuleiroRandomizado();
            }

            if(e.getKeyCode() == KeyEvent.VK_H){
                this.controleTecla = true;
            }

            if(e.getKeyCode() == KeyEvent.VK_G){
                this.controleTecla = false;
            }
        }
        
        if(this.fimDeJogo && (e.getKeyCode() == KeyEvent.VK_SPACE)){
            if(this.fimDeJogo){
                // Criar as peças brancas e pretas e adicionar ao conjunto correspondente
                this.cBrancas = new Conjunto();
                this.cPretas = new Conjunto();
                bEmJogada = false;
                pecaEmMovimento = null;
                controleMouse = -1;
                nJogadas = 0;
                controleTecla = false;
                turnoBranca = true;
                fimDeJogo = false;
                this.newGame(this);
            }
        }
        
        if(this.emPromocao){
            Posicao atual = this.pecaEmPromocao.getPosicaoPeca();
            boolean ehBranca = this.pecaEmPromocao.pecaBranca();
            Peca pNova;
            
            if(e.getKeyCode() == KeyEvent.VK_C){
                if(ehBranca){
                    pNova = new Cavalo("CavaloBranco.png", atual, true);
                    this.addPeca(pNova, Jogo.CoresConjuntos.BRANCAS); 
                }else{
                    pNova = new Cavalo("CavaloPreto.png", atual, false);
                    this.addPeca(pNova, Jogo.CoresConjuntos.PRETAS); 
                }
                this.removePeao();
                this.emPromocao = false;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_T){
                if(ehBranca){
                    pNova = new Torre("TorreBranca.png", atual, true);
                    this.addPeca(pNova, Jogo.CoresConjuntos.BRANCAS); 
                }else{
                    pNova = new Torre("TorrePreta.png", atual, false);
                    this.addPeca(pNova, Jogo.CoresConjuntos.PRETAS); 
                }
                this.removePeao();
                this.emPromocao = false;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_B){
                if(ehBranca){
                    pNova = new Bispo("BispoBranco.png", atual, true);
                    this.addPeca(pNova, Jogo.CoresConjuntos.BRANCAS); 
                }else{
                    pNova = new Bispo("BispoPreto.png", atual, false);
                    this.addPeca(pNova, Jogo.CoresConjuntos.PRETAS); 
                }
                this.removePeao();
                this.emPromocao = false;
            }
            
            if(e.getKeyCode() == KeyEvent.VK_R){
                if(ehBranca){
                    pNova = new Rainha("RainhaBranca.png", atual, true);
                    this.addPeca(pNova, Jogo.CoresConjuntos.BRANCAS); 
                }else{
                    pNova = new Rainha("RainhaPreta.png", atual, false);
                    this.addPeca(pNova, Jogo.CoresConjuntos.PRETAS); 
                }
                this.removePeao();
                this.emPromocao = false;
            }
        }

        if(!fimDeJogo && !emPromocao)
            repaint();

    }
    
    void removePeao(){
        if(this.pecaEmPromocao.pecaBranca()){
            this.cBrancas.pecaFora(pecaEmPromocao);
        }else{
            this.cPretas.pecaFora(pecaEmPromocao);
        }
    }

    public void mouseClicked(MouseEvent e) {
       
    }
    
    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        /*
        Peca pecaClicada = this.getPecaClicada(this.getPosicaoDoClique(e)); 
        if(pecaClicada != null){
            this.tTabuleiro.destacarPosicao(pecaClicada.getPosicaoPeca());
        }
        */
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanelCenario = tTabuleiro;
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        coordenadaLabel = new javax.swing.JLabel();
        clickLabel = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SCC0204 - Xadrez");
        setResizable(false);

        jPanelCenario.setMaximumSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setMinimumSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setPreferredSize(new java.awt.Dimension(800, 800));
        jPanelCenario.setVerifyInputWhenFocusTarget(false);

        javax.swing.GroupLayout jPanelCenarioLayout = new javax.swing.GroupLayout(jPanelCenario);
        jPanelCenario.setLayout(jPanelCenarioLayout);
        jPanelCenarioLayout.setHorizontalGroup(
            jPanelCenarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanelCenarioLayout.setVerticalGroup(
            jPanelCenarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel2.setText("Coordenada:");

        jLabel3.setText("click:");

        coordenadaLabel.setText("10");

        clickLabel.setText("quadrante");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanelCenario, javax.swing.GroupLayout.PREFERRED_SIZE, 620, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(clickLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(coordenadaLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(293, 293, 293))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanelCenario, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(clickLabel)
                        .addComponent(coordenadaLabel)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel clickLabel;
    private javax.swing.JLabel coordenadaLabel;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelCenario;
    // End of variables declaration//GEN-END:variables

}
