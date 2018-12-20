import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


public class TabelaSimbolos implements SemanticConstants {

    private final LinkedList<Simbolo> tabela;
    private int primeiroId;
    private int ultimoId;

    public TabelaSimbolos() {
        tabela = new LinkedList<>();
        primeiroId = -1;
        ultimoId = -1;
    }

    //TODO adicionar tratamento de excecao pra qdo nao encontrar o simbolo (precisa?)
    public Simbolo getSimbolo(int nivel, int deslocamento) {
        return retornaPonteiroPara(nivel, deslocamento);
    }

    public Simbolo getSimbolo(int pos) {
        return tabela.get(pos);
    }

    public void atualizarSimbolo(Simbolo s, int categoria, int subCategoria, int deslocamento) {
        Simbolo ss = retornaPonteiroPara(s);
        ss.setCategoria(categoria);
        ss.setSubCategoria(subCategoria);
        ss.setDeslocamento(deslocamento);
    }

    public void updateMetodo(int posMetodoAtual, int numParamFormais, int ultimoId) {
        Simbolo s = tabela.get(posMetodoAtual);
        s.setNPF(numParamFormais);
        s.setPosParamFinal(ultimoId);
    }

    public void inserirSimbolo(Simbolo s) {
        tabela.add(s);
        ultimoId = tabela.indexOf(s);
    }

    //mesmo comportamento q getSimbolo, mas retira o simbolo da tabela
    public Simbolo retirarSimbolo(int nivel, int deslocamento) { //TODO VER ISSO AQUI
        Simbolo s = getSimbolo(nivel, deslocamento);
        if (tabela.indexOf(s) == primeiroId && tabela.get(tabela.indexOf(s) + 1) != null) {
            ++primeiroId;
        } else if (tabela.indexOf(s) == ultimoId && tabela.get(tabela.indexOf(s) - 1) != null) {
            --ultimoId;
        }
        tabela.remove(s);
        return s;
    }

    public boolean verificaSeExisteEmMesmoNivel(Token t, int nivel) {
        return filtrarPorNivel(nivel).stream().anyMatch(simbolo -> simbolo.getLexeme().equals(t.getLexeme()));
    }

    public int getPosicaoSimbolo(Token t, int nivel) {
        return tabela.indexOf(getSimboloMaiorNivel(t, nivel));
    }

    private Simbolo getSimboloMaiorNivel(Token t, int nivel) {
        Simbolo simbolo = retornaSimboloPorLexemaENivel(t, nivel);
        while (simbolo == null && nivel > 0) {
            nivel--;
            simbolo = retornaSimboloPorLexemaENivel(t, nivel);
        }
        return simbolo;
    }

    public boolean ehIdVetor(int posid) {
        return tabela.get(posid).ehVetor();
    }

    public int getUltimoId() {
        return ultimoId;
    }

    public void limparTabela() {
        tabela.clear();
    }

    //https://stackoverflow.com/questions/27876260/finding-element-in-linkedlist-with-lambda
    public boolean existeID(Token t) {
        return tabela.stream().anyMatch(simbolo -> simbolo.getLexeme().equals(t.getLexeme()));
    }

    public int getCategoriaSimbolo(Token t, int nivel) {
        return getSimboloMaiorNivel(t, nivel).getCategoria();
    }

    public int getSubCategoriaSimbolo(Token token, int nivelAtual) {
        return getSimboloMaiorNivel(token, nivelAtual).getSubCategoria();
    }

    public int getTipoSimbolo(Token t, int nivel) {
        return getSimboloMaiorNivel(t, nivel).getTipo();
    }

    public int getTipoMetodo(int posMetodo) {
        Simbolo s = tabela.get(posMetodo);
        return s.getTipo();
    }

    public void setTipoMetodo(int posicaoMetodo, int tipo) {
        Simbolo s = tabela.get(posicaoMetodo);
        s.setTipo(tipo);
    }

    public int getTipoVetor(int posid) {
        Simbolo s = tabela.get(posid);
        return s.getTipo();
    }

    private Simbolo retornaSimboloPorLexemaENivel(Token t, int nivel) {
        Simbolo simbolo = new Simbolo();
        simbolo.setLexeme(t.getLexeme());
        simbolo.setNivel(nivel);

        Iterator<Simbolo> iterador = tabela.iterator();
        Simbolo retorno;
        while (iterador.hasNext()) {
            retorno = iterador.next();
            if (retorno.equals(simbolo)) {
                return retorno;
            }
        }
        return null;
    }

    //metodo generico pra nao precisar escrever o msm codigo sempre
    private Simbolo retornaPonteiroPara(Simbolo s) {
        Iterator<Simbolo> iterador = tabela.iterator();
        Simbolo retorno = iterador.next();
        while (!(retorno.equals(s)) && iterador.hasNext()) {
            retorno = iterador.next();
        }
        return retorno;
    }

    private Simbolo retornaPonteiroPara(int nivel, int deslocamento) {
        List<Simbolo> filtrada = filtrarPorNivel(nivel);
        Iterator<Simbolo> iterador = filtrada.iterator();
        Simbolo retorno = iterador.next();
        while (retorno.getDeslocamento() != deslocamento && iterador.hasNext()) {
            retorno = iterador.next();
        }
        return retorno;
    }

    private List<Simbolo> filtrarPorNivel(int nivel) {
        return tabela.stream().filter(simbolo -> simbolo.getNivel() == nivel).collect(Collectors.toList());
    }

    public void updateParam(int id, int tipoAtual, int mpp) {
        Simbolo s = tabela.get(id);
        s.setTipo(tipoAtual);
        s.setMpp(mpp);
    }

    public int getCategoriaSimbolo(int posId) {
        Simbolo s = tabela.get(posId);
        return s.getCategoria();
    }

    public int getNPF(int posId) {
        Simbolo s = tabela.get(posId);
        return s.getNPF();
    }
}