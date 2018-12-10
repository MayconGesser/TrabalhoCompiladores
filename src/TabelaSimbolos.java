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
		Iterator<Simbolo> iterador = tabela.iterator();
		Simbolo s = iterador.next();
		while(s.getNivel() != nivel && s.getDeslocamento() != deslocamento && iterador.hasNext()) {
			s = iterador.next();
		}
		return s; 
	}
	
	public void atualizarSimbolo(Simbolo s, int categoria, int subCategoria, int deslocamento) {
		Iterator<Simbolo> iterador = tabela.iterator();
		Simbolo ss = iterador.next();
		while(!(s.equals(ss))) {
			ss = iterador.next();
		}
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
		if(tabela.indexOf(s) == primeiroId) {
			++primeiroId;
		}
		else if(tabela.indexOf(s) == ultimoId) {
			--ultimoId;
		}
		return s;
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
}
