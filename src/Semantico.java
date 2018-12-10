public class Semantico implements Constants, SemanticConstants {
    private int nivelAtual = -1;
    private int deslocamento = -1;
    private int contextoLID = -1;
    private int primeiroIdLista = -1;
    private int ultimoIdLista = -1;
    private int tipoAtual = -1;
    private int tipoConst = -1;
    private int valConst = -1;
    private int subCategoriaAtual = -1;
    private int numElementos = -1;
    private int numParamFormais = -1;
    private int categoriaAtual = -1;
    private int mpp = -1;
    private int posId = -1;
    private int tipoExpr = -1;
    private int contextoEXPR = -1;
    private int tipoMetAtual = -1;
    private int tipoLadoEsq = -1;
    private int tipoVarIndexada = -1;
    private int numParamAtuais = -1;
    private int tipoExprSimples = -1;
    private int operadorAtual = -1;
    private int tipoTermo = -1;
    private int tipoFator = -1;
    private boolean opNega = false;
    private boolean opUnario = false;
    private int tipoVar = -1;

    private TabelaSimbolos TS = new TabelaSimbolos();

    public void executeAction(int action, Token token) throws SemanticError {
        switch (action) {
            case 101:
                nivelAtual = 0;
                deslocamento = 0;
                insertTSProg(token);
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
                    if (doesIdExistsOnThatLevel(token)) {
                        throw new SemanticError("Id já declarado", token.getPosition());
                    } else {
                        insertTSVar(token);
                    }
                } else if (contextoLID == CONT_LID_PAR_FORMAL) {
                    if (doesIdExistsOnThatLevel(token)) {
                        throw new SemanticError("Id de parâmetro repetido", token.getPosition());
                    } else {
                        numParamFormais++;
                        insertTSPF(token);
                    }
                } else if (contextoLID == CONT_LID_LEITURA) {
                    if (!doesIdExistsOnThatLevel(token)) {
                        throw new SemanticError("Id não declarado", token.getPosition());
                    } else {
                        if (isCategoriaInvalidToRead(token) || isTipoInvalidToRead(token)) {
                            throw new SemanticError("Tipo inválido para leitura", token.getPosition());
                        } else {
                            //gera codigo para leitura; nao implementado pois nao eh o foco do trabalho 
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
                    categoriaAtual = CAT_METODO;
                    insertTSMet(token);
                    posId = getUltimoIdTS();
                    numParamFormais = 0;
                    nivelAtual++;
                }
                return;
            case 118:
                updateNPF();
                return;
            case 119:
                updateTipoMetodo(tipoAtual);
                return;
            case 120:
                removeVarsTS();
                nivelAtual--;
                return;
            case 121:
                contextoLID = CONT_LID_PAR_FORMAL;
                categoriaAtual = CAT_PARAMETRO;
                primeiroIdLista = getUltimoIdTS() + 1;
                return;
            case 122:
                ultimoIdLista = getUltimoIdTS();
                return;
            case 123:
                if (tipoAtual == -1) {
                    throw new SemanticError("Parametros devem ser de tipo pré-definido", token.getPosition());
                } else {
                    updatePF();
                }
                return;
            case 124:
                if (tipoAtual == TIPO_CADEIA) {
                    throw new SemanticError("Métodos devem ser de tipo pré-definido", token.getPosition());
                } else {
                    tipoMetAtual = tipoAtual;
//                    setTipoMetodo(token, nivelAtual, tipoAtual);
                }
                return;
            case 125:
                tipoMetAtual = TIPO_NULO;
//                setTipoMetodo(token, nivelAtual, tipoAtual);
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
                    //geracao de cod
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
                    //ger cod
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
                    throw new SemanticError("id deveria ser var ou par");
                }
                return;
            case 134:
                if (!isCompativel(tipoExpr, tipoLadoEsq)) {
                    throw new SemanticError("tipos incompatíveis", token.getPosition());
                } else {
                    //ger cod
                }
                return;
            case 135:
                if (getCategoriaId(token) != CAT_VARIAVEL) {
                    throw new SemanticError("esperava-se uma variável", token.getPosition());
                } else {
                    if (getTipoId(token) != TIPO_CADEIA && !isIdVetor(token)) {
                        throw new SemanticError("Apenas vetores e cadeias podem ser indexados", token.getPosition());
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
                if (getCategoriaId() != CAT_METODO) {
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
                    //ger cod
                }
                return;
            case 140:
                if (getCategoriaId() != CAT_METODO) {
                    throw new SemanticError("id deveria ser um método", token.getPosition());
                } else if (tipoMetAtual != TIPO_NULO) {
                    throw new SemanticError("esperava-se método sem tipo", token.getPosition());
                } else if (numParamFormais != 0) {
                    throw new SemanticError("erro na quantidade de parametros", token.getPosition());
                } else {
                    //ger cod
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
                        //ger cod
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
                    //ger cod
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
                    throw new SemanticError("Operandos incompatíveis", token.getPosition());
                } else {
                    tipoTermo = getTipoResultadoOperacao();
                    //ger cod
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
                if (getCategoriaId() != CAT_METODO) {
                    throw new SemanticError("id deveria ser um método", token.getPosition());
                } else if (getTipoId() == TIPO_NULO) {
                    throw new SemanticError("esperava-se método com tipo", token.getPosition());
                } else {
                    numParamAtuais = 0;
                    contextoEXPR = CONT_EXPR_PARATUAL;
                }
                return;
            case 172:
                if (numParamAtuais == numParamFormais) {
                    tipoVar = getTipoMetodo();
                    //ger cod
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
                    tipoVar = getTipoVetor(token);
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
                        //ger cod
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
                valConst = getValor(token);
                return;
            case 177:
                tipoConst = TIPO_REAL;
//                valConst = getValor(token);
                return;
            case 178:
                tipoConst = TIPO_BOOLEANO;
//                valConst = getValor(token);
                return;
            case 179:
                tipoConst = TIPO_BOOLEANO;
//                valConst = getValor(token);
                return;
            case 180:
                tipoConst = TIPO_CADEIA;
//                valConst = getValor(token);
                return;
            default:
                throw new SemanticError("Erro nao identificado - acao semantica nao identificada -", token.getPosition());
        }

    }

    private int getTipoId() {
        return TS.getTipoMetodo(posId);
    }

    private int getCategoriaId() {
        return TS.getCategoriaSimbolo(posId);
    }

    private int getValor(Token token) {
        //TODO pra geracao de codigo tem que arrumar pra casos que nao seja inteiro
        return Integer.parseInt(token.getLexeme());
    }

    private int geValorId(Token token) {
        //TODO pra geracao de codigo tem que arrumar
        return -1;
    }

    private int getTipoVetor(Token token) {
        return TS.getTipoVetor(token, nivelAtual);
    }

    private int getTipoMetodo() {
        return TS.getTipoMetodo(posId);
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
                return (tipoAtual == TIPO_INTEIRO ? TIPO_INTEIRO : (tipoAtual == TIPO_REAL ? TIPO_REAL : -1)); //TODO verificar se inteiro ou real;
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
            case OP_MULT_DIV:
            case OP_MULT_DIV_BARRA:
            case OP_MULT_VEZES:
                return operando == TIPO_INTEIRO || operando == TIPO_REAL;
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
        return true;
    }

    private boolean isCompativel(int tipoExpr, int tipoLadoEsq) {
        switch (tipoExpr) {
            case TIPO_INTEIRO:
                return tipoLadoEsq == TIPO_INTEIRO || tipoLadoEsq == TIPO_REAL;
            case TIPO_REAL:
                return tipoLadoEsq == TIPO_REAL;
            case TIPO_BOOLEANO:
                return tipoLadoEsq == TIPO_BOOLEANO;
            case TIPO_CARACTER:
                return tipoLadoEsq == TIPO_CARACTER || tipoLadoEsq == TIPO_CADEIA;
            case TIPO_CADEIA:
                return tipoLadoEsq == TIPO_CADEIA;
            default:
                return false;
        }
    }

    private int getTipoId(Token token) {
        return TS.getTipoSimbolo(token, nivelAtual);
    }

    private int getCategoriaId(Token token) {
        return TS.getCategoriaSimbolo(token, nivelAtual);
    }

    private boolean isIdVetor(Token token) {
        return TS.ehIdVetor(token, nivelAtual);
    }

    private boolean metodoHasTipo() {
        return TS.getTipoMetodo(posId) != TIPO_NULO;
    }

    private int findPosicaoId(Token token) {
        return TS.getPosicaoSimbolo(token, nivelAtual);
    }

    private void updatePF() {
        TS.updateParam(ultimoIdLista, tipoAtual);
    }

    private void updateNPF() {
        TS.updateMetodo(posId, numParamFormais);
        TS.updatePFsMetodo(posId, primeiroIdLista, ultimoIdLista);
    }

    private void updateIds() {
        int primeiro = primeiroIdLista;
        int ultimo = ultimoIdLista;
        int deslocamentoLocal = 0;
        while (primeiro < ultimo) {
            Simbolo ID = TS.getSimbolo(primeiro);
            TS.atualizarSimbolo(ID, categoriaAtual, subCategoriaAtual, deslocamentoLocal);
            primeiro++;
            deslocamentoLocal++;
        }
    }

    private void removeVarsTS() {
        int d = deslocamento;
        while (d > 0) {
            TS.retirarSimbolo(nivelAtual, d);
            --d;
        }
    }

    private void updateTipoMetodo(int tipo) {
        TS.setTipoMetodo(posId, tipo);
    }

    private boolean doesIdExistsOnThatLevel(Token token) {
        return TS.verificaSeExisteEmMesmoNivel(token, nivelAtual);
    }

    private boolean doesIdExists(Token token) {
        return TS.existeID(token);
    }

    private boolean isTipoInvalidToRead(Token token) {
        int tipo = getTipoId(token);
        return tipo == TIPO_CADEIA;
    }

    private boolean isCategoriaInvalidToRead(Token token) {
        int subCat = getSubCatId(token);
        return subCat == SUB_CAT_VETOR || subCat == SUB_CAT_CADEIA;
    }

    private int getSubCatId(Token token) {
        return TS.getSubCategoriaSimbolo(token, nivelAtual);
    }

    private int getUltimoIdTS() {
        return TS.getUltimoId();
    }

    private void insertTSPF(Token token) {
        int tamanho = 1;
        Simbolo s = new Simbolo();
        s.setLexeme(token.getLexeme());
        s.setNivel(nivelAtual);
        s.setDeslocamento(deslocamento);
        s.setCategoria(CAT_PARAMETRO);
        s.setTamanho(tamanho);
        s.setTipo(tipoAtual);
        s.setMpp(mpp);
        s.setId(ID_PARAMETRO);
        TS.inserirSimbolo(s);
    }

    private void insertTSVar(Token token) {
        int tamanho = 1;
        if (subCategoriaAtual == SUB_CAT_VETOR || tipoAtual == TIPO_CADEIA) {
            tamanho = numElementos;
        }
        Simbolo s = new Simbolo();
        s.setLexeme(token.getLexeme());
        s.setNivel(nivelAtual);
        s.setDeslocamento(deslocamento);
        s.setCategoria(categoriaAtual);
        s.setSubCategoria(subCategoriaAtual);
        s.setTamanho(tamanho);
        s.setTipo(tipoAtual);
        s.setId(ID_VARIAVEL);
        TS.inserirSimbolo(s);
    }

    private void insertTSProg(Token token) {
        int tamanho = 1;
        Simbolo s = new Simbolo();
        s.setLexeme(token.getLexeme());
        s.setTamanho(tamanho);
        s.setId(ID_PROGRAMA);
        TS.inserirSimbolo(s);
    }

    private void insertTSMet(Token token) {
        int tamanho = 1;
        Simbolo s = new Simbolo();
        s.setLexeme(token.getLexeme());
        s.setNivel(nivelAtual);
        s.setDeslocamento(deslocamento);
        s.setCategoria(CAT_METODO);
        s.setTamanho(tamanho);
        s.setTipo(tipoAtual);
        s.setId(ID_METODO);
        TS.inserirSimbolo(s);
    }
}