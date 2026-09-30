package ARVTRIE;

/** Nó da Trie, com um caminho para cada letra do alfabeto. */
public class NoTrie {

    final NoTrie[] filhos;
    boolean ehFimDaPalavra;
    int frequencia;

    public NoTrie() {
        filhos = new NoTrie[26];
        ehFimDaPalavra = false;
        frequencia = 0;
    }
}
