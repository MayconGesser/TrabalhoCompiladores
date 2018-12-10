import java.util.Iterator;
import java.util.LinkedList;


public class TabelaSimbolos implements SemanticConstants{
	
	private final LinkedList<Simbolo> tabela;
	private int primeiroId; 
	private int ultimoId; 
	
	public TabelaSimbolos() {
		tabela = new LinkedList<Simbolo>();
		primeiroId = 0; 
		ultimoId = 0; 
	}
	
	//TODO adicionar tratamento de excecao pra qdo nao encontrar o simbolo (precisa?)
	public Simbolo getSimbolo(int nivel, int deslocamento) {
		return retornaPonteiroPara(nivel, deslocamento); 
	}
	
	public void atualizarSimbolo(Simbolo s, int categoria, int subCategoria, int deslocamento) {
		Simbolo ss = retornaPonteiroPara(s);
		ss.setCategoria(categoria);
		ss.setSubCategoria(subCategoria);
		ss.setDeslocamento(deslocamento);
	}
	
	public void inserirSimbolo(Simbolo s) {
		tabela.add(s);
		ultimoId = tabela.indexOf(s);
	}
	
	//mesmo comportamento q getSimbolo, mas retira o simbolo da tabela
	public Simbolo retirarSimbolo(int nivel, int deslocamento) {
		Simbolo s = getSimbolo(nivel,deslocamento);
		tabela.remove(s);
		if(tabela.indexOf(s) == primeiroId && tabela.get(tabela.indexOf(s)+1) != null) {
			++primeiroId;
		}
		else if(tabela.indexOf(s) == ultimoId && tabela.get(tabela.indexOf(s)-1) != null) {
			--ultimoId;
		}
		return s;
	}	
	
	public boolean verificaSeExisteEmMesmoNivel(Simbolo s) {
		Simbolo ret = retornaSimboloPorLexema(s.getToken());
		return s.ehMesmoNivel(ret);
	}
	
	public int getPosicaoSimbolo(Simbolo s) {
		return tabela.indexOf(s);
	}
	
	public boolean ehIdVetor(Simbolo s) {
		return retornaPonteiroPara(s).getToken().getId() == t_vetor;
	}
	
	public int getPrimeiroId() {
		return primeiroId; 
	}
	
	public int getUltimoId() {
		return ultimoId;
	}
	
	public void limparTabela() {
		tabela.clear();
	}
	
	//https://stackoverflow.com/questions/27876260/finding-element-in-linkedlist-with-lambda
	public boolean existeID(Token t) {
		return tabela.stream().anyMatch(simbolo -> simbolo.getToken().getLexeme().equals(t.getLexeme()));
	}
	
	public int getCategoriaSimbolo(Token t) {
		return retornaSimboloPorLexema(t).getCategoria();
	}
	
	public void setTipoMetodo(Token t, int nivel, int tipo) {
		Iterator<Simbolo> iterador = tabela.iterator();
		Simbolo s = iterador.next();
		while(!(s.getToken().getLexeme().equals(t.getLexeme()) && s.getNivel() != nivel && s.getIdSemantico() != ID_METODO && iterador.hasNext())) {
			s = iterador.next();
		}
		s.setTipo(tipo);
	}
	
	private Simbolo retornaSimboloPorLexema(Token t) {
		Iterator<Simbolo> iterador = tabela.iterator();
		Simbolo retorno = iterador.next();
		//TODO: tratar pra/se nao achar
		while(!(retorno.getToken().getLexeme().equals(t)) && iterador.hasNext()) {
			retorno = iterador.next();
		}
		return retorno;
	}
	
	//metodo generico pra nao precisar escrever o msm codigo sempre
	private Simbolo retornaPonteiroPara(Simbolo s) {
		Iterator<Simbolo> iterador = tabela.iterator();
		Simbolo retorno = iterador.next();
		while(!(retorno.equals(s)) && iterador.hasNext()) {
			retorno = iterador.next();
		}
		return retorno;
	}
	
	private Simbolo retornaPonteiroPara(int nivel, int deslocamento) {
		Iterator<Simbolo> iterador = tabela.iterator();
		Simbolo retorno = iterador.next();
		while(retorno.getNivel() != nivel && retorno.getDeslocamento() != deslocamento && iterador.hasNext()) {
			retorno = iterador.next();
		}
		return retorno;
	}
}
