import java.util.HashMap;

public class TradutorC extends atribuicaoBaseListener{
    HashMap<String, String[]> variaveis = new HashMap<String, String[]>();
    String escopo = "0";

    void adicionarVariavel(String nome, String tipo) {
        if (!variaveis.containsKey(nome)) {
            String[] variavel = {tipo, escopo};
            variaveis.put(nome, variavel);
        } 
        else{
            System.err.println("ID " + nome + " already exists");
            System.exit(0);
        }
    }

    void verificaVariavel(String nome, String valor){
        if(variaveis.containsKey(nome)){
            String tipo = variaveis.get(nome)[0];

            var valorSeparado = valor.split("[\\+ | - | / | \\*]");
            if (tipo.equals("mario")){
                for (String string : valorSeparado) {
                    if(string.contains(".") || string.contains("\"")){
                        System.err.println("mismatched type input luigi expecting " + tipo);
                        System.exit(0);
                        
                    }
                    if(string.contains("\"")){
                        System.err.println("mismatched type input peach expecting " + tipo);
                        System.exit(0);

                    }

                    if(string.matches("([a-zA-Z])([a-zA-Z0-9])*")){
                        if(variaveis.containsKey(string)){
                            var tipo2 = variaveis.get(string)[0];
                            if(!tipo2.equals(tipo)){
                                System.err.println("mismatched type input " + tipo2 + " expecting " + tipo);
                                System.exit(0);
                            }
                        }
                        else{
                            System.err.println("unknown ID inputed");
                            System.exit(0);
                        }
                    }
                }
            }
            else if (tipo.equals("luigi")){
                for (String string : valorSeparado) {
                    if(string.contains("\"")){
                        System.err.println("mismatched type input peach expecting " + tipo);
                        System.exit(0);
                    }

                    if(string.matches("([a-zA-Z])([a-zA-Z0-9])*")){
                        if(variaveis.containsKey(string)){
                            var tipo2 = variaveis.get(string)[0];
                            if(!tipo2.equals(tipo)){
                                System.err.println("mismatched type input " + tipo2 + " expecting " + tipo);
                                System.exit(0);
                            }
                        }
                        else{
                            System.err.println("unknown ID inputed");
                            System.exit(0);
                        }
                    }
                }
            }
            else if (tipo.equals("peach")){
                for (String string : valorSeparado) {
                    if(!string.contains("\"")){
                        System.out.println("mismatched type expecting " + tipo);
                        System.exit(0);
                    }

                    if(string.matches("([a-zA-Z])([a-zA-Z0-9])*")){
                        if(variaveis.containsKey(string)){
                            var tipo2 = variaveis.get(string)[0];
                            if(!tipo2.equals(tipo)){
                                System.err.println("mismatched type input " + tipo2 + " expecting " + tipo);
                                System.exit(0);
                            }
                        }
                        else{
                            System.err.println("unknown ID inputed");
                            System.exit(0);
                        }
                    }
                }
            }
        }
        else{
            System.err.println("unknown ID inputed");
            System.exit(0);
        }
    }

    String verificaTipo(String valor){
        if(valor.matches("([a-zA-Z])([a-zA-Z0-9])*")){
            if(variaveis.containsKey(valor)){
                return variaveis.get(valor)[0];
            }
            else{
                System.err.println("unknown ID inputed");
                System.exit(0);
            }
        }
        else if(valor.contains("\""))
            return "peach";
        else if(valor.contains("."))
            return "luigi";
        else if(valor.matches("[0-9]+")){
            return "mario";
        }
        return "";
    }

    public void enterDeclaracao(atribuicaoParser.DeclaracaoContext ctx) {
        atribuicaoParser.VarContext var = ctx.var();
        atribuicaoParser.TipoContext tipo = ctx.tipo();
        adicionarVariavel(var.getText(), tipo.getText());
    }

    public void enterAtribuicao(atribuicaoParser.AtribuicaoContext ctx) {
        atribuicaoParser.VarContext var = ctx.var();
        atribuicaoParser.ExpreContext expre = ctx.expre();
        verificaVariavel(var.getText(), expre.getText());
    }

    public void enterOperador(atribuicaoParser.OperadorContext ctx) {
        if(!ctx.getParent().getText().contains("wario")){
            System.out.print(" = ");
        }
    }
    
    public void enterTipo(atribuicaoParser.TipoContext ctx) {
        String tipo = ctx.getText();
        switch (tipo) {
            case "mario":
                System.out.print("int ");
                break;
            case "luigi":
                System.out.print("double ");
                break;
            case "peach":
                System.out.print("std::string ");
                break;
        };
    }

    public void enterVar(atribuicaoParser.VarContext ctx) {
        System.out.print(ctx.getText());
    }

    public void enterNum(atribuicaoParser.NumContext ctx) {
        System.out.print(ctx.getText());
    }

    public void enterFim(atribuicaoParser.FimContext ctx) {
        System.out.println(";");
    }

    public void enterInit(atribuicaoParser.InitContext ctx) {
        System.out.println("#include <iostream>");
        System.out.println("int main() {");
        
    }

    public void exitInit(atribuicaoParser.InitContext ctx) {
        System.out.println("}");
    }

    public void enterSe(atribuicaoParser.SeContext ctx) {
        System.out.print("if(");
    }

    public void enterComp(atribuicaoParser.CompContext ctx) {
        System.out.print(" " + ctx.getText() + " ");
    }

    public void enterAbreColc(atribuicaoParser.AbreColcContext ctx) {
        System.out.println("{");
        int valor = Integer.parseInt(escopo) + 1;
        escopo = String.valueOf(valor);
    }

    public void enterFechaColc(atribuicaoParser.FechaColcContext ctx) {
        System.out.println("}");

        for(String chave : variaveis.keySet()){
            if(variaveis.get(chave)[1].equals(escopo)){
                variaveis.remove(chave);
            }
        }

        int valor = Integer.parseInt(escopo) - 1;
        escopo = Integer.toString(valor);
    }

    public void exitCondicao(atribuicaoParser.CondicaoContext ctx) {
        System.out.print(")");
        var expre1 = ctx.expre().get(0).getText().split("[\\+ | - | / | \\*]");
        var expre2 = ctx.expre().get(1).getText().split("[\\+ | - | / | \\*]");
        var tipo1 = verificaTipo(expre1[0]);
        var tipo2 = verificaTipo(expre2[0]);

        if(tipo1.equals("")){
            System.err.println("token recognition error at " + expre1[0]);
            System.exit(0);
        }

        if(tipo2.equals("")){
            System.err.println("token recognition error at " + expre2[0]);
            System.exit(0);
        }

        if(!tipo1.equals(tipo2)){
            System.err.println("mismatched type input " + tipo1 + " expecting " + tipo2);
            System.exit(0);
        }

        String tipoAux;

        for (String string : expre1) {
            tipoAux = verificaTipo(string);
            if(!tipo1.equals(tipoAux)){
                System.err.println("mismatched type input " + tipoAux + " expecting " + tipo1);
                System.exit(0);
            }
        }

        for (String string : expre2) {
            tipoAux = verificaTipo(string);
            if(!tipo2.equals(tipoAux)){
                System.err.println("mismatched type input " + tipoAux + " expecting " + tipo2);
                System.exit(0);
            }
        }
        
    }

    public void enterSenao(atribuicaoParser.SenaoContext ctx) {
        System.out.print("else");
    }

    public void enterSenaose(atribuicaoParser.SenaoseContext ctx) {
        System.out.print("else if(");
    }

    public void enterAbrePar(atribuicaoParser.AbreParContext ctx) {

        if(!ctx.getParent().getText().contains("wario") && !ctx.getParent().getText().contains("waluigi")){
            System.out.print("(");
        }
    }

    public void enterFechaPar(atribuicaoParser.FechaParContext ctx) {
        if(!ctx.getParent().getText().contains("wario") && !ctx.getParent().getText().contains("waluigi")){
            System.out.print(")");
        }
    }

    public void enterMais(atribuicaoParser.MaisContext ctx) {
        System.out.print("+");
    }

    public void enterMenos(atribuicaoParser.MenosContext ctx) {
        System.out.print("-");
    }

    public void enterDiv(atribuicaoParser.DivContext ctx) {
        System.out.print("/");
    }

    public void enterMult(atribuicaoParser.MultContext ctx) {
        System.out.print("*");
    }

    public void enterVirg(atribuicaoParser.VirgContext ctx) {
        System.out.print(";");
    }

    public void enterInput(atribuicaoParser.InputContext ctx) {
        System.out.print("cin >> ");
    }

    public void enterEscreva(atribuicaoParser.EscrevaContext ctx) {
        System.out.print("cout << ");
    }

    public void enterEnquanto(atribuicaoParser.EnquantoContext ctx) {
        System.out.print("while(");
    }

    public void enterPara(atribuicaoParser.ParaContext ctx) {
        System.out.print("for");
    }

    public void enterLetras(atribuicaoParser.LetrasContext ctx) {
        System.out.print(ctx.getText());
    }
}