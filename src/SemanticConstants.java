
public interface SemanticConstants extends Constants{
	int ID_PROGRAMA = 1;
	int ID_CONSTANTE = 2;
	int ID_VARIAVEL = 3;
	int ID_METODO = 4;
	int ID_PARAMETRO = 5;

	int CONT_LID_DECL = 1;
	int CONT_LID_PAR_FORMAL = 2;
	int CONT_LID_LEITURA = 3;

	int CONT_EXPR_IMPRESSAO = 1;
	int CONT_EXPR_PARATUAL = 2;

	int TIPO_INTEIRO = 1;
	int TIPO_REAL = 2;
	int TIPO_BOOLEANO = 3;
	int TIPO_CARACTER = 4;
	int TIPO_CADEIA = 5;
	int TIPO_NULO = 6;

	int CAT_VARIAVEL = 1;
	int CAT_CONSTANTE = 2;
	int CAT_METODO = 3;
	int CAT_PARAMETRO = 4;

	int SUB_CAT_VETOR = 1;
	int SUB_CAT_CADEIA = 2;
	int SUB_CAT_PREDEF = 3;

	int MPP_REFERENCIA = 1;
	int MPP_VALOR = 2;

	int OP_REL_IGUAL = 1;
	int OP_REL_MENOR = 2;
	int OP_REL_MAIOR = 3;
	int OP_REL_MAIOR_IGUAL = 4;
	int OP_REL_MENOR_IGUAL = 5;
	int OP_REL_DIFERENTE = 6;
	int OP_ADD_ADICAO = 7;
	int OP_ADD_SUBTRACAO = 8;
	int OP_ADD_OU = 9;
	int OP_MULT_VEZES = 10;
	int OP_MULT_DIV_BARRA = 11;
	int OP_MULT_DIV = 12;
	int OP_MULT_E = 13;
}
