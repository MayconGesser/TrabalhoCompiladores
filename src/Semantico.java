import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.sun.org.apache.bcel.internal.generic.RET;

public class Semantico implements Constants {


    private final int ID_PROGRAMA = 1;
    private final int ID_CONSTANTE = 2;
    private final int ID_VARIAVEL = 3;
    private final int ID_METODO = 4;
    private final int ID_PARAMETRO = 5;

    private final int CONT_LID_DECL = 1;
    private final int CONT_LID_PAR_FORMAL = 2;
    private final int CONT_LID_LEITURA = 3;

    private final int CONT_EXPR_IMPRESSAO = 1;
    private final int CONT_EXPR_PARATUAL = 2;

    private final int TIPO_INTEIRO = 1;
    private final int TIPO_REAL = 2;
    private final int TIPO_BOOLEANO = 3;
    private final int TIPO_CARACTER = 4;
    private final int TIPO_CADEIA = 5;
    private final int TIPO_PREDEF = 6;
    private final int TIPO_NULO = 7;

    private final int CAT_VARIAVEL = 1;
    private final int CAT_CONSTANTE = 2;
    private final int CAT_METODO = 3;
    private final int CAT_PARAMETRO = 4;

    private final int SUB_CAT_VETOR = 1;
    private final int SUB_CAT_CADEIA = 2;
    private final int SUB_CAT_PREDEF = 3;

    private final int MPP_REFERENCIA = 1;
    private final int MPP_VALOR = 2;

    private final int OP_REL_IGUAL = 1;
    private final int OP_REL_MENOR = 2;
    private final int OP_REL_MAIOR = 3;
    private final int OP_REL_MAIOR_IGUAL = 4;
    private final int OP_REL_MENOR_IGUAL = 5;
    private final int OP_REL_DIFERENTE = 6;
    private final int OP_ADD_ADICAO = 7;
    private final int OP_ADD_SUBTRACAO = 8;
    private final int OP_ADD_OU = 9;
    private final int OP_MULT_VEZES = 10;
    private final int OP_MULT_DIV_BARRA = 11;
    private final int OP_MULT_DIV = 12;
    private final int OP_MULT_E = 13;

    private int nivelAtual;
    private int deslocamento;
    private int contextoLID;
    private int primeiroIdLista;
    private int ultimoIdLista;
    private int tipoAtual;
    private int tipoConst;
    private int valConst;
    private int subCategoriaAtual;
    private int numElementos;
    private int numParamFormais;
    private int categoriaAtual;
    private int mpp;
    private int posId;
    private int tipoExpr;
    private int contextoEXPR;
    private int tipoMetAtual;
    private int tipoLadoEsq;
    private int tipoVarIndexada;
    private int numParamAtuais;
    private int tipoExprSimples;
    private int operadorAtual;
    private int tipoTermo;
    private int tipoFator;
    private boolean opNega;
    private boolean opUnario;
    private int tipoVar;


    public void executeAction(int action, Token token) throws SemanticError {
        switch (action) {
            case 101:
                nivelAtual = 0;
                deslocamento = 0;
                insertTS(token, ID_PROGRAMA);
                return;
            case 102:
                contextoLID = CONT_LID_DECL;
                primeiroIdLista = getUltimoIdTS() + 1;
                return;
            case 103:
                ultimoIdLista = getUltimoIdTS();
                return;
            case 104:
                updateIds();
                return;
            case 105:
                tipoAtual = TIPO_INTEIRO;
                return;
            case 106:
                tipoAtual = TIPO_REAL;
                return;
            case 107:
                tipoAtual = TIPO_BOOLEANO;
                return;
            case 108:
                tipoAtual = TIPO_CARACTER;
                return;
            case 109:
                if (tipoConst != TIPO_INTEIRO) {
                    throw new SemanticError("Esperava-se uma constante inteira", token.getPosition());
                } else if (valConst > 256) {
                    throw new SemanticError("Cadeia maior que o permitido", token.getPosition());
                } else {
                    tipoAtual = TIPO_CADEIA;
                }
                return;
            case 110:
                if (tipoAtual == TIPO_CADEIA) {
                    throw new SemanticError("Vetor do tipo cadeia não é permitido", token.getPosition());
                } else {
                    subCategoriaAtual = SUB_CAT_VETOR;
                }
                return;
            case 111:
                if (tipoConst != TIPO_INTEIRO) {
                    throw new SemanticError("A dimensão deve ser uma constante inteira", token.getPosition());
                } else {
                    numElementos = valConst;
                }
                return;
            case 112:
                if (tipoAtual == TIPO_CADEIA) {
                    subCategoriaAtual = SUB_CAT_CADEIA;
                } else {
                    subCategoriaAtual = SUB_CAT_PREDEF;
                }
                return;
            case 113:
                if (contextoLID == CONT_LID_DECL) {
                    if (doesIdExists(token)) {
                        throw new SemanticError("Id já declarado", token.getPosition());
                    } else {
                        insertTS(token, ID_VARIAVEL);
                    }
                } else if (contextoLID == CONT_LID_PAR_FORMAL) {
                    if (doesIdExists(token)) {
                        throw new SemanticError("Id de parâmetro repetido", token.getPosition());
                    } else {
                        numParamFormais++;
                        insertTS(token, ID_VARIAVEL);
                    }
                } else if (contextoLID == CONT_LID_LEITURA) {
                    if (!doesIdExists(token)) {
                        throw new SemanticError("Id não declarado", token.getPosition());
                    } else {
                        if (isCategoriaInvalidToRead() || isTipoInvalidToRead()) {
                            throw new SemanticError("Tipo inválido para leitura", token.getPosition());
                        } else {
                            //TODO gera codigo para leitura lul
                        }
                    }
                }
                return;
            case 114:
                if (subCategoriaAtual == SUB_CAT_CADEIA || subCategoriaAtual == SUB_CAT_VETOR) {
                    throw new SemanticError("Apenas id de tipo pré-definido pode ser declarado como constante", token.getPosition());
                } else {
                    categoriaAtual = CAT_CONSTANTE;
                }
                return;
            case 115:
                if (tipoConst != tipoAtual) {
                    throw new SemanticError("Tipo da constante incorreto", token.getPosition());
                }
                return;
            case 116:
                categoriaAtual = CAT_VARIAVEL;
                return;
            case 117:
                if (doesIdExistsOnThatLevel(token)) {
                    throw new SemanticError("Id já declarado", token.getPosition());
                } else {
                    insertTS(token, ID_METODO);
                    numParamFormais = 0;
                    nivelAtual++;
                }
                return;
            case 118:
                updateNPF();
                return;
            case 119:
                updateTipoMetodo();
                return;
            case 120:
                removeVarsTS();
                nivelAtual--;
                return;
            case 121:
                contextoLID = CONT_LID_PAR_FORMAL;
                primeiroIdLista = getUltimoIdTS() + 1;
                return;
            case 122:
                ultimoIdLista = getUltimoIdTS();
                return;
            case 123:
                if (tipoAtual != TIPO_PREDEF) {
                    throw new SemanticError("Parametros devem ser de tipo pré-definido", token.getPosition());
                } else {
                    updatePFs();
                }
                return;
            case 124:
                if (tipoAtual == TIPO_CADEIA) {
                    throw new SemanticError("Métodos devem ser de tipo pré-definido", token.getPosition());
                } else {
                    tipoMetAtual = tipoAtual;
                    setTipoMetodo();
                }
                return;
            case 125:
                tipoMetAtual = TIPO_NULO;
                setTipoMetodo();
                return;
            case 126:
                mpp = MPP_REFERENCIA;
                return;
            case 127:
                mpp = MPP_VALOR;
                return;
            case 128:
                if (!doesIdExists(token)) {
                    throw new SemanticError("Identificador não declarado", token.getPosition());
                } else {
                    posId = findPosicaoId(token);
                }
                return;
            case 129:
                if (tipoExpr != TIPO_BOOLEANO && tipoExpr != TIPO_INTEIRO) {
                    throw new SemanticError("Tipo inválido da expressão", token.getPosition());
                } else {
                    //TODO acao de geracao de codigo ?lul?
                }
                return;
            case 130:
                contextoLID = CONT_LID_LEITURA;
                return;
            case 131:
                contextoEXPR = CONT_EXPR_IMPRESSAO;
                return;
            case 132:
                if (!metodoHasTipo()) {
                    throw new SemanticError("'Retorne ' só pode ser usado em Método com tipo", token.getPosition());
                } else if (tipoExpr != tipoMetAtual) {
                    throw new SemanticError("Tipo de retorno inválido", token.getPosition());
                } else {
                    //TODO acao de geracao de codigo ?lul?
                }
                return;
            case 133:
                int categoriaId = getCategoriaId(token);
                if (categoriaId == CAT_VARIAVEL || categoriaId == CAT_CONSTANTE) {
                    if (isIdVetor(token)) {
                        throw new SemanticError("id deveria ser indexado", token.getPosition());
                    } else {
                        tipoLadoEsq = getTipoId(token);
                    }
                } else {
                    throw new SemanticError("id feveria ser var ou par");
                }
                return;
            case 134:
                if (!isCompativel(tipoExpr, tipoLadoEsq)) {
                    throw new SemanticError("tipos incompatíveis", token.getPosition());
                } else {
                    //TODO geracao codigo... ?
                }
                return;
            case 135:
                if (getCategoriaId(token) != CAT_VARIAVEL) {
                    throw new SemanticError("esperava-se uma variável", token.getPosition());
                } else {
                    if (getTipoId(token) != TIPO_CADEIA && !isIdVetor(token)) {
                        throw new SemanticError("Apenas vetores e cadeas podem ser indexados", token.getPosition());
                    } else {
                        tipoVarIndexada = getTipoId(token);
                    }
                }
                return;
            case 136:
                if (tipoExpr != TIPO_INTEIRO) {
                    throw new SemanticError("índice deveria ser inteiro", token.getPosition());
                } else if (tipoVarIndexada == TIPO_CADEIA) {
                    tipoLadoEsq = TIPO_CARACTER;
                } else {
                    tipoLadoEsq = tipoVarIndexada;
                }
                return;
            case 137:
                if (getCategoriaId(token) != CAT_METODO) {
                    throw new SemanticError("id deveria ser um método", token.getPosition());
                } else if (tipoMetAtual != TIPO_NULO) {
                    throw new SemanticError("Esperava-se método sem tipo", token.getPosition());
                }
                return;
            case 138:
                numParamAtuais = 0;
                contextoEXPR = CONT_EXPR_PARATUAL;
                return;
            case 139:
                if (numParamFormais != numParamAtuais) {
                    throw new SemanticError("erro na quantidade de parâmetros", token.getPosition());
                } else {
                    //TODO geracao de codigo para chamada de procedimento
                }
                return;
            case 140:
                if (getCategoriaId(token) != CAT_METODO) {
                    throw new SemanticError("id deveria ser um método", token.getPosition());
                } else if (tipoMetAtual != TIPO_NULO) {
                    throw new SemanticError("esperava-se método sem tipo", token.getPosition());
                } else if (numParamFormais != 0) {
                    throw new SemanticError("erro na quantidade de parametros", token.getPosition());
                } else {
                    //TODO geracao de codigo para chamada de metodo
                }
                return;
            case 141:
                if (contextoEXPR == CONT_EXPR_PARATUAL) {
                    numParamAtuais++;
                    if (!isParamAtuaisValidos()) {
                        throw new SemanticError("parametros atuais nao coincidem com parametros do metodo", token.getPosition());
                    }
                } else if (contextoEXPR == CONT_EXPR_IMPRESSAO) {
                    if (tipoExpr == TIPO_BOOLEANO) {
                        throw new SemanticError("tipo invalido para impressão", token.getPosition());
                    } else {
                        //TODO gera cod pra impressao
                    }
                }
                return;
            case 142:
                tipoExpr = tipoExprSimples;
                return;
            case 143:
                if (!isCompativel(tipoExprSimples, tipoExpr)) {
                    throw new SemanticError("Operandos incompatíveis", token.getPosition());
                } else {
                    tipoExpr = TIPO_BOOLEANO;
                }
                return;
            case 144:
                operadorAtual = OP_REL_IGUAL;
                return;
            case 145:
                operadorAtual = OP_REL_MENOR;
                return;
            case 146:
                operadorAtual = OP_REL_MAIOR;
                return;
            case 147:
                operadorAtual = OP_REL_MAIOR_IGUAL;
                return;
            case 148:
                operadorAtual = OP_REL_MENOR_IGUAL;
                return;
            case 149:
                operadorAtual = OP_REL_DIFERENTE;
                return;
            case 150:
                tipoExprSimples = tipoTermo;
                return;
            case 151:
                if (!isOperadorAndOperandoCompativeis(tipoExprSimples)) {
                    throw new SemanticError("Operador e operando incompatíveis", token.getPosition());
                }
                return;
            case 152:
                if (!isCompativel(tipoTermo, tipoExprSimples)) {
                    throw new SemanticError("Operando incompatíveis", token.getPosition());
                } else {
                    tipoExprSimples = getTipoResultadoOperacao();
                    //TODO gera codigo blabla
                }
                return;
            case 153:
                operadorAtual = OP_ADD_ADICAO;
                return;
            case 154:
                operadorAtual = OP_ADD_SUBTRACAO;
                return;
            case 155:
                operadorAtual = OP_ADD_OU;
                return;
            case 156:
                tipoTermo = tipoFator;
                return;
            case 157:
                if (!isOperadorAndOperandoCompativeis(tipoTermo)) {
                    throw new SemanticError("operador e operando incompatíveis", token.getPosition());
                }
                return;
            case 158:
                if (!isCompativel(tipoFator, tipoTermo)) {
                    throw new SemanticError("Operanod incomatíveis", token.getPosition());
                } else {
                    tipoTermo = getTipoResultadoOperacao();
                    //TODO geracao de cod...
                }
                return;
            case 159:
                operadorAtual = OP_MULT_VEZES;
                return;
            case 160:
                operadorAtual = OP_MULT_DIV_BARRA;
                return;
            case 161:
                operadorAtual = OP_MULT_E;
                return;
            case 162:
                operadorAtual = OP_MULT_DIV;
                return;
            case 163:
                if (opNega) {
                    throw new SemanticError("Operador `não` repetido não pode", token.getPosition());
                } else {
                    opNega = true;
                }
                return;
            case 164:
                if (tipoFator != TIPO_BOOLEANO) {
                    throw new SemanticError("Operador não exige operando booleano", token.getPosition());
                } else {
                    opNega = false;
                }
                return;
            case 165:
                if (opUnario) {
                    throw new SemanticError("Operador `unário` repetido", token.getPosition());
                } else {
                    opUnario = true;
                }
                return;
            case 166:
                if (tipoFator != TIPO_INTEIRO && tipoFator != TIPO_REAL) {
                    throw new SemanticError("Operador unário exige operando numérico", token.getPosition());
                } else {
                    opUnario = false;
                }
                return;
            case 167:
                opNega = false;
                opUnario = false;
                return;
            case 168:
                tipoFator = tipoExpr;
                return;
            case 169:
                tipoFator = tipoVar;
                return;
            case 170:
                tipoFator = tipoConst;
                return;
            case 171:
                if (getCategoriaId(token) != CAT_METODO) {
                    throw new SemanticError("id deveria ser um método", token.getPosition());
                } else if (getTipoId(token) != TIPO_NULO) {
                    throw new SemanticError("esperava-se método com tipo", token.getPosition());
                } else {
                    numParamAtuais = 0;
                    contextoEXPR = CONT_EXPR_PARATUAL;
                }
                return;
            case 172:
                if (numParamAtuais == numParamFormais) {
                    tipoVar = getTipoMetodo();
                    //TODO geracao de codigo ativ metodo
                } else {
                    throw new SemanticError("Erro na quantidade de parametros", token.getPosition());
                }
                return;
            case 173:
                if (tipoExpr != TIPO_INTEIRO) {
                    throw new SemanticError("Índice deveria ser inteiro", token.getPosition());
                } else if (tipoVarIndexada == TIPO_CADEIA) {
                    tipoVar = TIPO_CARACTER;
                } else {
                    tipoVar = getTipoVetor();
                }
                return;
            case 174:
                int categoria = getCategoriaId(token);
                int tipo = getTipoId(token);
                if (categoria == CAT_VARIAVEL || categoria == CAT_PARAMETRO) {
                    if (isIdVetor(token)) {
                        throw new SemanticError("Vetor deve ser indexado", token.getPosition());
                    } else {
                        tipoVar = tipo;
                    }
                } else if (categoria == CAT_METODO) {
                    if (tipo == TIPO_NULO) {
                        throw new SemanticError("Esperava-se método com tipo", token.getPosition());
                    } else if (numParamFormais != 0) {
                        throw new SemanticError("Erro na quantidade parâmetros", token.getPosition());
                    } else {
                        tipoVar = getTipoResultadoOperacao();
                        //TODO gerar codigo
                    }
                } else if (categoria == CAT_CONSTANTE) {
                    tipoVar = tipoConst;
                } else {
                    throw new SemanticError("Esperava-se var, id-método ou constante", token.getPosition());
                }
                return;
            case 175:
                if (!doesIdExists(token)) {
                    throw new SemanticError("Id não declarado", token.getPosition());
                } else if (getCategoriaId(token) != CAT_CONSTANTE) {
                    throw new SemanticError("id de constante esperado", token.getPosition());
                } else {
                    tipoConst = getTipoId(token);
                    valConst = geValorId(token);
                }
                return;
            case 176:
                tipoConst = TIPO_INTEIRO;
                valConst = getValor();
                return;
            case 177:
                tipoConst = TIPO_REAL;
                valConst = getValor();
                return;
            case 178:
                tipoConst = TIPO_BOOLEANO;
                valConst = getValor();
                return;
            case 179:
                tipoConst = TIPO_BOOLEANO;
                valConst = getValor();
                return;
            case 180:
                tipoConst = TIPO_CADEIA;
                valConst = getValor();
                return;
            default:
                throw new SemanticError("Erro nao identificado - acao semantica nao identificada -", token.getPosition());
        }

    }

    private int getValor() {
        //TODO ver qualeh aqui q eu n entendi, eh noix
        //todo ACHO QUE N PRECISA PQ EH SOH PRA GER DE COD
        return -1;
    }

    private int geValorId(Token token) {
        //TODO pegar valor da constante id
        return -1;
    }

    private int getTipoVetor() {
        //TODO pegar tipo do vetor
        return -1;
    }

    private int getTipoMetodo() {
        //TODO pegar tipo do metodo que foi chamado, provavelmente vai ter que voltar e guardar qual foi
        return -1;
    }

    private int getTipoResultadoOperacao() {
        switch (operadorAtual) {
            case OP_REL_IGUAL:
            case OP_REL_MENOR:
            case OP_REL_MAIOR:
            case OP_REL_MAIOR_IGUAL:
            case OP_REL_MENOR_IGUAL:
            case OP_REL_DIFERENTE:
            case OP_ADD_OU:
            case OP_MULT_E:
                return TIPO_BOOLEANO;
            case OP_ADD_ADICAO:
            case OP_ADD_SUBTRACAO:
                return -1; //TODO verificar se inteiro ou real;
            case OP_MULT_VEZES:
            case OP_MULT_DIV_BARRA:
            case OP_MULT_DIV:
                return TIPO_REAL;
            default:
                return -1;
        }
    }

    private boolean isOperadorAndOperandoCompativeis(int operando) throws SemanticError {
        switch (operadorAtual) {
            case OP_ADD_ADICAO:
            case OP_ADD_SUBTRACAO:
                return operando == TIPO_INTEIRO || operando == TIPO_REAL;
            case OP_MULT_DIV:
            case OP_MULT_DIV_BARRA:
            case OP_MULT_VEZES:
                return operando == TIPO_INTEIRO || operando == TIPO_REAL; //todo ver se realmente deixa separado ou junta com primeiro if
            case OP_ADD_OU:
            case OP_MULT_E:
                return operando == TIPO_BOOLEANO;
            case OP_REL_DIFERENTE:
            case OP_REL_IGUAL:
            case OP_REL_MAIOR:
            case OP_REL_MAIOR_IGUAL:
            case OP_REL_MENOR:
            case OP_REL_MENOR_IGUAL:
                return operando == TIPO_INTEIRO || operando == TIPO_REAL;
            default:
                throw new SemanticError("Operador e Operando incompativeis", 0);
        }
    }


    private boolean isParamAtuaisValidos() {
        //TODO verificar se existe PF correspondente e se o tipo e o MPP sao compativeis
        return false;
    }

    private boolean isCompativel(int tipoExpr, int tipoLadoEsq) {
        //TODO verifica se o tipoExpr pode ser "castado" para o tipoLadoEsq
        return false;
    }

    private int getTipoId(Token token) {
        //TODO retorna o tipo do id
        return -1;
    }

    private int getCategoriaId(Token token) {
        //TODO retorna categoria do Id
        return -1;
    }

    private boolean isIdVetor(Token token) {
        //TODO verifica se o id eh vetor ou nao
        return false;
    }

    private boolean metodoHasTipo() {
        //TODO verifica se o metodo atual tem tipo
        return tipoAtual != TIPO_NULO;
    }

    private int findPosicaoId(Token token) {
        //TODO retorna a posicao na tabela do id especifico
        return -1;
    }

    private void setTipoMetodo() {
        //TODO seta o tipo do metodo na TS
    }

    private void updatePFs() {
        //TODO atualiza parametros formais usando primeiro e ultimoIdLista
        //TODO categoria Parametro, tipoAtual e MPP(?)
        //TODO insere os pares em uma lista auxiliar, quais parametros possui...
    }

    private void removeVarsTS() {
        //TODO remove as variaveis declaradas localmente, as do nivel atual
    }

    private void updateTipoMetodo() {
        //TODO atualiza tipometodo na TS
    }

    private void updateNPF() {
        //TODO atualiza numParFormais na TS
    }

    private boolean doesIdExistsOnThatLevel(Token token) {
        //TODO verifica se o id jah existe no nivel atual
        return false;
    }

    private boolean isTipoInvalidToRead() {
        //TODO verifica se o tipoAtual eh invalido para leitura
        return false;
    }

    private boolean isCategoriaInvalidToRead() {
        //TODO verifica se a categoriaAtual eh invalida para leitura
        return false;
    }

    private boolean doesIdExists(Token token) {
        //TODO verificar se id jah foi declarado
        return false;
    }

    private void updateIds() {
        //TODO atualiza os ids comecando pelo primeiroIdLista e terminando no ultimo...
        //TODO atualiza colocando CategoriaAtual, SubCategoria e Deslocamento
    }

    private int getUltimoIdTS() {
        //TODO retorna a posicao do ultimo id da TS
        return -1;
    }

    private void insertTS(Token token, int tipoId) {
        //TODO inserir na tabela de simbolos
        //TODO nao esquecer de nivelAtual, categoria etc...
    }
}