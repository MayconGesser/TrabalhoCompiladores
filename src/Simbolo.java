
public class Simbolo implements SemanticConstants{
	private int idSemantico; 
	private Token token; 
	private int nivel; 
	private int deslocamento; 
	private int categoria; 
	private int subCategoria; 
	private int tamanho;
	private int tipo;
	
	//TODO: refatorar essa bagunça...
	
	public Simbolo(Token token, int nivel, int deslocamento, int categoria, int subCategoria, int tamanho) {
		this.token = token; 
		this.nivel = nivel; 
		this.deslocamento = deslocamento; 
		this.categoria = categoria; 
		this.subCategoria = subCategoria; 
		this.tamanho = tamanho; 
	}
	
	public Simbolo(Token token, int nivel, int deslocamento, int categoria, int subCategoria, int tamanho, int tipo) {
		this.token = token; 
		this.nivel = nivel; 
		this.deslocamento = deslocamento; 
		this.categoria = categoria; 
		this.subCategoria = subCategoria; 
		this.tamanho = tamanho; 
		this.tipo = tipo;
	}
	
	public Simbolo(int idSemantico, Token token, int nivel, int deslocamento, int categoria, int subCategoria, int tamanho, int tipo) {
		this.idSemantico = idSemantico;
		this.token = token; 
		this.nivel = nivel; 
		this.deslocamento = deslocamento; 
		this.categoria = categoria; 
		this.subCategoria = subCategoria; 
		this.tamanho = tamanho; 
		this.tipo = tipo;
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
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Simbolo)) {
			return false;
		}
		else {
			Simbolo outro = (Simbolo)other;
			return  (this.ehMesmoNivel(outro) &&
					 this.ehMesmoDeslocamento(outro) &&
					 this.ehMesmaCategoria(outro) && 
					 this.subCategoria == outro.getSubCategoria() && 
					 this.tamanho == outro.getTamanho() &&
					 this.ehMesmoTipo(outro));
		}
		
	}
}
