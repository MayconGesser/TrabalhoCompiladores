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

    private final int TIPO_INTEIRO = 1;
    private final int TIPO_REAL = 2;
    private final int TIPO_BOOLEANO = 3;
    private final int TIPO_CARACTER = 4;
    private final int TIPO_CADEIA = 5;
    private final int TIPO_PREDEF = 6;
    private final int TIPO_NULO = 7;

    private final int CAT_VARIAVEL = 1;
    private final int CAT_CONSTANTE = 2;

    private final int SUB_CAT_VETOR = 1;
    private final int SUB_CAT_CADEIA = 2;
    private final int SUB_CAT_PREDEF = 3;

    private final int MPP_REFERENCIA = 1;
    private final int MPP_VALOR = 2;


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
                    setTipoMetodo(tipoAtual);
                }
                return;
            case 125:
                setTipoMetodo(TIPO_NULO);
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
                } else if (tipoExpr != tipoAtual) {
                    throw new SemanticError("Tipo de retorno inválido", token.getPosition());
                }
                else {
                    //TODO acao de geracao de codigo ?lul?
                }
                return;
            case 133:
                if (categoriaAtual == CAT_VARIAVEL || categoriaAtual == CAT_CONSTANTE){

                }
        }
    }

    private boolean metodoHasTipo() {
        //TODO verifica se o metodo atual tem tipo
        return tipoAtual != TIPO_NULO;
    }

    private int findPosicaoId(Token token) {
        //TODO retorna a posicao na tabela do id especifico
        return -1;
    }

    private void setTipoMetodo(int tipoAtual) {
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