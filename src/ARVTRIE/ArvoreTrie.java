package ARVTRIE;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Trie para inserção, busca exata e sugestões por prefixo. */
public class ArvoreTrie {

    private NoTrie raiz;

    public ArvoreTrie() {
        raiz = new NoTrie();
    }

    /** Converte a palavra para minúsculas sem acentos e aceita somente a-z. */
    public static String normalizarPalavra(String palavra) {
        if (palavra == null) {
            return "";
        }

        String palavraNormalizada = Normalizer.normalize(palavra, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
        return palavraNormalizada.matches("[a-z]+") ? palavraNormalizada : "";
    }

    /** Insere uma palavra; palavras repetidas não alteram a frequência existente. */
    public void inserir(String palavra) {
        String palavraNormalizada = normalizarPalavra(palavra);
        if (palavraNormalizada.isEmpty()) {
            return;
        }

        NoTrie noAtual = raiz;
        for (int posicao = 0; posicao < palavraNormalizada.length(); posicao++) {
            int indice = palavraNormalizada.charAt(posicao) - 'a';
            if (noAtual.filhos[indice] == null) {
                noAtual.filhos[indice] = new NoTrie();
            }
            noAtual = noAtual.filhos[indice];
        }
        noAtual.ehFimDaPalavra = true;
    }

    /** Retorna verdadeiro somente se a palavra completa existir. */
    public boolean buscar(String palavra) {
        NoTrie no = atravessarAte(palavra);
        return no != null && no.ehFimDaPalavra;
    }

    /** Percorre o caminho da palavra e retorna seu nó final, se o caminho existir. */
    public NoTrie atravessarAte(String palavra) {
        String palavraNormalizada = normalizarPalavra(palavra);
        if (palavraNormalizada.isEmpty()) {
            return null;
        }

        NoTrie noAtual = raiz;
        for (int posicao = 0; posicao < palavraNormalizada.length(); posicao++) {
            int indice = palavraNormalizada.charAt(posicao) - 'a';
            if (noAtual.filhos[indice] == null) {
                return null;
            }
            noAtual = noAtual.filhos[indice];
        }
        return noAtual;
    }

    /** Retorna a frequência da palavra, ou zero se ela não estiver na Trie. */
    public int obterFrequencia(String palavra) {
        NoTrie no = atravessarAte(palavra);
        return no != null && no.ehFimDaPalavra ? no.frequencia : 0;
    }

    /** Atualiza a frequência de uma palavra existente na Trie. */
    public void atualizarFrequencia(String palavra, int frequencia) {
        if (frequencia < 0) {
            throw new IllegalArgumentException("A frequência não pode ser negativa.");
        }

        NoTrie no = atravessarAte(palavra);
        if (no != null && no.ehFimDaPalavra) {
            no.frequencia = frequencia;
        }
    }

    /** Descarta todas as palavras armazenadas. */
    public void limpar() {
        raiz = new NoTrie();
    }

    /** Informa se existe ao menos uma palavra que começa pelo prefixo. */
    public boolean comecaComPrefixo(String prefixo) {
        if (prefixo == null) {
            return false;
        }
        if (prefixo.isEmpty()) {
            return true;
        }

        String prefixoNormalizado = normalizarPalavra(prefixo);
        if (prefixoNormalizado.isEmpty()) {
            return false;
        }

        NoTrie noAtual = raiz;
        for (int posicao = 0; posicao < prefixoNormalizado.length(); posicao++) {
            int indice = prefixoNormalizado.charAt(posicao) - 'a';
            if (noAtual.filhos[indice] == null) {
                return false;
            }
            noAtual = noAtual.filhos[indice];
        }
        return true;
    }

    /** Devolve em ordem alfabética todas as palavras que começam pelo prefixo. */
    public List<String> sugerir(String prefixo) {
        List<String> sugestoes = new ArrayList<>();
        if (prefixo == null) {
            return sugestoes;
        }

        String prefixoNormalizado = normalizarPalavra(prefixo);
        if (!prefixo.isEmpty() && prefixoNormalizado.isEmpty()) {
            return sugestoes;
        }

        NoTrie noAtual = raiz;
        for (int posicao = 0; posicao < prefixoNormalizado.length(); posicao++) {
            int indice = prefixoNormalizado.charAt(posicao) - 'a';
            if (noAtual.filhos[indice] == null) {
                return sugestoes;
            }
            noAtual = noAtual.filhos[indice];
        }

        coletarPalavras(noAtual, new StringBuilder(prefixoNormalizado), sugestoes);
        return sugestoes;
    }

    /** Percorre a subárvore em profundidade e acumula palavras completas. */
    private void coletarPalavras(NoTrie no, StringBuilder prefixo, List<String> palavras) {
        if (no.ehFimDaPalavra) {
            palavras.add(prefixo.toString());
        }

        for (int indice = 0; indice < no.filhos.length; indice++) {
            if (no.filhos[indice] != null) {
                prefixo.append((char) ('a' + indice));
                coletarPalavras(no.filhos[indice], prefixo, palavras);
                prefixo.deleteCharAt(prefixo.length() - 1);
            }
        }
    }
}
