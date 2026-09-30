package ARVTRIE;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** BST lexicográfica que associa cada palavra à sua frequência de uso. */
public class ArvoreRankingPalavras {

    private No raiz;

    private static class No {
        private final String palavra;
        private int frequencia;
        private No esquerda;
        private No direita;

        private No(String palavra, int frequencia) {
            this.palavra = palavra;
            this.frequencia = frequencia;
        }
    }

    /** Insere uma palavra ou substitui sua frequência atual. */
    public void adicionarOuAtualizar(String palavra, int frequenciaNova) {
        if (palavra == null || palavra.isEmpty()) {
            throw new IllegalArgumentException("A palavra não pode ser vazia.");
        }
        if (frequenciaNova < 0) {
            throw new IllegalArgumentException("A frequência não pode ser negativa.");
        }
        raiz = adicionarOuAtualizar(raiz, palavra, frequenciaNova);
    }

    private No adicionarOuAtualizar(No no, String palavra, int frequencia) {
        if (no == null) {
            return new No(palavra, frequencia);
        }

        int comparacao = palavra.compareTo(no.palavra);
        if (comparacao < 0) {
            no.esquerda = adicionarOuAtualizar(no.esquerda, palavra, frequencia);
        } else if (comparacao > 0) {
            no.direita = adicionarOuAtualizar(no.direita, palavra, frequencia);
        } else {
            no.frequencia = frequencia;
        }
        return no;
    }

    /** Retorna a frequência cadastrada ou zero quando a palavra não existe. */
    public int obterFrequencia(String palavra) {
        if (palavra == null) {
            return 0;
        }

        No noAtual = raiz;
        while (noAtual != null) {
            int comparacao = palavra.compareTo(noAtual.palavra);
            if (comparacao == 0) {
                return noAtual.frequencia;
            }
            noAtual = comparacao < 0 ? noAtual.esquerda : noAtual.direita;
        }
        return 0;
    }

    /** Remove uma palavra, reorganizando os nós necessários para manter a BST. */
    public void remover(String palavra) {
        if (palavra != null) {
            raiz = remover(raiz, palavra);
        }
    }

    private No remover(No no, String palavra) {
        if (no == null) {
            return null;
        }

        int comparacao = palavra.compareTo(no.palavra);
        if (comparacao < 0) {
            no.esquerda = remover(no.esquerda, palavra);
        } else if (comparacao > 0) {
            no.direita = remover(no.direita, palavra);
        } else if (no.esquerda == null) {
            return no.direita;
        } else if (no.direita == null) {
            return no.esquerda;
        } else {
            No sucessor = encontrarMinimo(no.direita);
            No substituto = new No(sucessor.palavra, sucessor.frequencia);
            substituto.esquerda = no.esquerda;
            substituto.direita = removerMinimo(no.direita);
            return substituto;
        }
        return no;
    }

    private No encontrarMinimo(No no) {
        while (no.esquerda != null) {
            no = no.esquerda;
        }
        return no;
    }

    private No removerMinimo(No no) {
        if (no.esquerda == null) {
            return no.direita;
        }
        no.esquerda = removerMinimo(no.esquerda);
        return no;
    }

    /** Retorna até a quantidade indicada, por frequência e depois por ordem alfabética. */
    public List<String> obterMaisFrequentes(int quantidade) {
        List<No> nos = new ArrayList<>();
        coletarEmOrdem(raiz, nos);
        nos.sort(Comparator.comparingInt((No no) -> no.frequencia).reversed()
                .thenComparing(no -> no.palavra));

        List<String> palavras = new ArrayList<>();
        for (int indice = 0; indice < Math.min(Math.max(quantidade, 0), nos.size()); indice++) {
            palavras.add(nos.get(indice).palavra);
        }
        return palavras;
    }

    private void coletarEmOrdem(No no, List<No> nos) {
        if (no == null) {
            return;
        }
        coletarEmOrdem(no.esquerda, nos);
        nos.add(no);
        coletarEmOrdem(no.direita, nos);
    }

    /** Esvazia a árvore de ranking. */
    public void limpar() {
        raiz = null;
    }
}
