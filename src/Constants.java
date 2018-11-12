public interface Constants extends ParserConstants
{
	
	final String[] lexemes = {
		"programa","id","inteiro","real","booleano","caracter",
		"cadeia","metodo","ref","val","se","entao","senao","leia",
		"escreva","retorne","ou","div","e","nao","falso","verdadeiro","enquanto","faca",
		":","<","+","/","]","=",")","*",">",",","}","[","{",";",".","(","-","<=",">=",":=","<>"
	};
	
    int EPSILON  = 0;
    int DOLLAR   = 1;

    int t_programa = 2;
    int t_id = 3;
    int t_inteiro = 4;
    int t_real = 5;
    int t_booleano = 6;
    int t_caracter = 7;
    int t_cadeia = 8;
    int t_metodo = 9;
    int t_ref = 10;
    int t_val = 11;
    int t_se = 12;
    int t_entao = 13;
    int t_senao = 14;
    int t_leia = 15;
    int t_escreva = 16;
    int t_retorne = 17;
    int t_ou = 18;
    int t_div = 19;
    int t_e = 20;
    int t_nao = 21;
    int t_falso = 22;
    int t_verdadeiro = 23;
    int t_enquanto = 24;
    int t_faca = 25;
    int t_TOKEN_26 = 26; //":"
    int t_TOKEN_27 = 27; //"<"
    int t_TOKEN_28 = 28; //"+"
    int t_TOKEN_29 = 29; //"/"
    int t_TOKEN_30 = 30; //"]"
    int t_TOKEN_31 = 31; //"="
    int t_TOKEN_32 = 32; //")"
    int t_TOKEN_33 = 33; //"*"
    int t_TOKEN_34 = 34; //">"
    int t_TOKEN_35 = 35; //","
    int t_TOKEN_36 = 36; //"}"
    int t_TOKEN_37 = 37; //"["
    int t_TOKEN_38 = 38; //"{"
    int t_TOKEN_39 = 39; //";"
    int t_TOKEN_40 = 40; //"."
    int t_TOKEN_41 = 41; //"("
    int t_TOKEN_42 = 42; //"-"
    int t_TOKEN_43 = 43; //"<="
    int t_TOKEN_44 = 44; //">="
    int t_TOKEN_45 = 45; //":="
    int t_TOKEN_46 = 46; //"<>"
    int t_num_int = 47;
    int t_num_real = 48;
    int t_literal = 49;

}
