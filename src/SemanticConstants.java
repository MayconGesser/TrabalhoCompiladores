
public interface SemanticConstants extends Constants{
	final int ID_PROGRAMA = 1;
	final int ID_CONSTANTE = 2;
	final int ID_VARIAVEL = 3;
	final int ID_METODO = 4;
	final int ID_PARAMETRO = 5;

	final int CONT_LID_DECL = 1;
	final int CONT_LID_PAR_FORMAL = 2;
	final int CONT_LID_LEITURA = 3;

	final int CONT_EXPR_IMPRESSAO = 1;
	final int CONT_EXPR_PARATUAL = 2;

	final int TIPO_INTEIRO = 1;
	final int TIPO_REAL = 2;
	final int TIPO_BOOLEANO = 3;
	final int TIPO_CARACTER = 4;
	final int TIPO_CADEIA = 5;
	final int TIPO_PREDEF = 6;
	final int TIPO_NULO = 7;

	final int CAT_VARIAVEL = 1;
	final int CAT_CONSTANTE = 2;
	final int CAT_METODO = 3;
	final int CAT_PARAMETRO = 4;

	final int SUB_CAT_VETOR = 1;
	final int SUB_CAT_CADEIA = 2;
	final int SUB_CAT_PREDEF = 3;

	final int MPP_REFERENCIA = 1;
	final int MPP_VALOR = 2;

	final int OP_REL_IGUAL = 1;
	final int OP_REL_MENOR = 2;
	final int OP_REL_MAIOR = 3;
	final int OP_REL_MAIOR_IGUAL = 4;
	final int OP_REL_MENOR_IGUAL = 5;
	final int OP_REL_DIFERENTE = 6;
	final int OP_ADD_ADICAO = 7;
	final int OP_ADD_SUBTRACAO = 8;
	final int OP_ADD_OU = 9;
	final int OP_MULT_VEZES = 10;
	final int OP_MULT_DIV_BARRA = 11;
	final int OP_MULT_DIV = 12;
	final int OP_MULT_E = 13;
}
