import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


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
		if(tabela.indexOf(s) == primeiroId && tabela.get(tabela.indexOf(s)+1) != null) {
			++primeiroId;
		}
		else if(tabela.indexOf(s) == ultimoId && tabela.get(tabela.indexOf(s)-1) != null) {
			--ultimoId;
		}
		tabela.remove(s);
		return s;
	}	
	
	public boolean verificaSeExisteEmMesmoNivel(Simbolo s) {
		Simbolo ret = retornaSimboloPorLexema(s.getToken());
		return s.ehMesmoNivel(ret);
	}
	
	public int getPosicaoSimbolo(Simbolo s) {
		return tabela.indexOf(s);
	}
	
	public int getPosicaoSimbolo(Token t) {
		return tabela.indexOf(retornaSimboloPorLexema(t));
	}
	
	public boolean ehIdVetor(Token t, int nivel) {
		return retornaSimboloPorLexemaENivel(t,nivel).ehVetor();
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
	
	public int getIdSemanticoSimbolo(Token t) {
		return retornaSimboloPorLexema(t).getIdSemantico();
	}
	
	public int getTipoSimbolo(Token t) {
		return retornaSimboloPorLexema(t).getTipo();
	}
	
	public int getTipoMetodo(Token t, int nivel) {
		Simbolo s = getSimboloMetodo(t,nivel);
		return s.getTipo();		
	}
	
	public void setTipoMetodo(Token t, int nivel, int tipo) {
		Simbolo s = getSimboloMetodo(t, nivel);
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
	
	private Simbolo retornaSimboloPorLexemaENivel(Token t, int nivel) {
		List<Simbolo> filtrada = filtrarPorNivel(nivel);
		Iterator<Simbolo> iterador = filtrada.iterator();
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
		List<Simbolo> filtrada = filtrarPorNivel(nivel);
		Iterator<Simbolo> iterador = filtrada.iterator();
		Simbolo retorno = iterador.next();
		while(retorno.getDeslocamento() != deslocamento && iterador.hasNext()) {
			retorno = iterador.next();
		}
		return retorno;
	}
	
	private Simbolo getSimboloMetodo(Token t, int nivel) {
		List<Simbolo> filtrada = filtrarPorNivel(nivel);
		Iterator<Simbolo> iterador = filtrada.iterator();
		Simbolo s = iterador.next();
		while(!(s.getToken().getLexeme().equals(t.getLexeme()) && !(s.ehMetodo()) && iterador.hasNext())) {
			s = iterador.next();
		}
		return s;
	}
	
	private List<Simbolo> filtrarPorNivel(int nivel){
		return tabela.stream().filter(simbolo -> simbolo.getNivel() == nivel).collect(Collectors.toList());
	}
}
