import java.util.ArrayList;

public class Simbolo implements SemanticConstants {
    private int idSemantico;
    private Token token;
    private int nivel;
    private int deslocamento;
    private int categoria;
    private int subCategoria;
    private int tamanho;
    private int tipo;
    private int NPF;
    private int posParamInicial;
    private int posParamFinal;
    private int mpp;

    public Simbolo() {
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token t) {
        token = t;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getDeslocamento() {
        return deslocamento;
    }

    public void setDeslocamento(int deslocamento) {
        this.deslocamento = deslocamento;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getSubCategoria() {
        return subCategoria;
    }

    public void setSubCategoria(int subCategoria) {
        this.subCategoria = subCategoria;
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getIdSemantico() {
        return idSemantico;
    }

    public void setIdSemantico(int idSemantico) {
        this.idSemantico = idSemantico;
    }

    public boolean ehMesmaCategoria(Simbolo outro) {
        return this.categoria == outro.getCategoria();
    }

    public boolean ehMesmoNivel(Simbolo outro) {
        return this.nivel == outro.getNivel();
    }

    public boolean ehMesmoDeslocamento(Simbolo outro) {
        return this.deslocamento == outro.getDeslocamento();
    }

    public boolean ehMesmoTipo(Simbolo outro) {
        return this.tipo == outro.getTipo();
    }

    public boolean ehMetodo() {
        return this.idSemantico == ID_METODO;
    }

    public boolean ehVetor() {
        return this.subCategoria == SUB_CAT_VETOR;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Simbolo)) {
            return false;
        } else {
            Simbolo outro = (Simbolo) other;
            return (this.ehMesmoNivel(outro) &&
                    this.ehMesmoDeslocamento(outro) &&
                    this.ehMesmaCategoria(outro) &&
                    this.subCategoria == outro.getSubCategoria() &&
                    this.tamanho == outro.getTamanho() &&
                    this.ehMesmoTipo(outro));
        }

    }

    public int getNPF() {
        return NPF;
    }

    public void setNPF(int NPF) {
        this.NPF = NPF;
    }

    public void setParams(int primeiro, int ultimo) {
        posParamFinal = ultimo;
        posParamInicial = primeiro;
    }

    public int getMpp() {
        return mpp;
    }

    public void setMpp(int mpp) {
        this.mpp = mpp;
    }

    public void setTamanho(int tamanho) {
        this.tamanho = tamanho;
    }
}
