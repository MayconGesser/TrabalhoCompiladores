import java.util.Objects;

public class Simbolo implements SemanticConstants {
    private int idSemantico = -1;
    private String lexeme;
    private int nivel = -1;
    private int deslocamento = -1;
    private int categoria = -1;
    private int subCategoria = -1;
    private int tamanho = -1;
    private int tipo = -1;
    private int NPF = 0;
    private int posParamFinal = -1;
    private int mpp = -1;
    private int id = -1;

    public Simbolo() {
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
        return this.id == ID_METODO;
    }

    public boolean ehVetor() {
        return this.subCategoria == SUB_CAT_VETOR;
    }

    public int getNPF() {
        return NPF;
    }

    public void setNPF(int NPF) {
        this.NPF = NPF;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexeme, nivel);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Simbolo simbolo = (Simbolo) o;
        return nivel == simbolo.nivel &&
                Objects.equals(lexeme, simbolo.lexeme);
    }

    public int getPosParamFinal() {
        return posParamFinal;
    }

    public void setPosParamFinal(int posParamFinal) {
        this.posParamFinal = posParamFinal;
    }
}
