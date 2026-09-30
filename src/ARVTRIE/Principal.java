package ARVTRIE;

/** Demonstra busca por prefixo e ordenação de sugestões pelo uso. */
public class Principal {

    /** Executa um exemplo de sugestões ordenadas pelas frequências registradas. */
    public static void main(String[] argumentos) {
        Dicionario dicionario = new Dicionario();
        dicionario.inserir("casa");
        dicionario.inserir("casaco");
        dicionario.inserir("carro");
        dicionario.inserir("banana");
        dicionario.inserir("cachorro");

        dicionario.registrarUso("casa");
        dicionario.registrarUso("casa");
        dicionario.registrarUso("carro");

        System.out.println(dicionario.sugerir("ca"));
    }
}
