package ARVTRIE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

/** Integra leitura de arquivo, Trie e árvore de ranking por frequência. */
public class Dicionario {

    private final ArvoreTrie arvoreTrie = new ArvoreTrie();
    private final ArvoreRankingPalavras arvoreRanking = new ArvoreRankingPalavras();

    /** Carrega palavras em UTF-8, uma por linha, e zera as frequências anteriores. */
    public void carregar(String arquivo) throws IOException {
        List<String> linhas = Files.readAllLines(Path.of(arquivo), StandardCharsets.UTF_8);
        arvoreTrie.limpar();
        arvoreRanking.limpar();

        for (String linha : linhas) {
            for (String token : linha.split("\\s+")) {
                String palavra = ArvoreTrie.normalizarPalavra(token);
                if (!palavra.isEmpty()) {
                    inserir(palavra);
                }
            }
        }
    }

    /** Insere uma palavra nova nas duas estruturas com frequência zero. */
    public void inserir(String palavra) {
        String palavraNormalizada = ArvoreTrie.normalizarPalavra(palavra);
        if (!palavraNormalizada.isEmpty() && !arvoreTrie.buscar(palavraNormalizada)) {
            arvoreTrie.inserir(palavraNormalizada);
            arvoreRanking.adicionarOuAtualizar(palavraNormalizada, 0);
        }
    }

    /** Verifica se a palavra completa está cadastrada. */
    public boolean buscar(String palavra) {
        return arvoreTrie.buscar(palavra);
    }

    /** Verifica se há alguma palavra cadastrada com o prefixo informado. */
    public boolean buscarPrefixo(String prefixo) {
        return arvoreTrie.comecaComPrefixo(prefixo);
    }

    /** Lista palavras do prefixo por frequência decrescente e desempata alfabeticamente. */
    public List<String> sugerir(String prefixo) {
        List<String> sugestoes = arvoreTrie.sugerir(prefixo);
        sugestoes.sort(Comparator.comparingInt(
                (String palavra) -> arvoreRanking.obterFrequencia(palavra))
                .reversed()
                .thenComparing(Comparator.naturalOrder()));
        return sugestoes;
    }

    /** Incrementa o uso de uma palavra, inserindo-a antes se ainda não existir. */
    public void registrarUso(String palavra) {
        String palavraNormalizada = ArvoreTrie.normalizarPalavra(palavra);
        if (palavraNormalizada.isEmpty()) {
            return;
        }

        inserir(palavraNormalizada);
        int frequenciaNova = arvoreTrie.obterFrequencia(palavraNormalizada) + 1;
        arvoreTrie.atualizarFrequencia(palavraNormalizada, frequenciaNova);
        arvoreRanking.adicionarOuAtualizar(palavraNormalizada, frequenciaNova);
    }

    /** Devolve a Trie para operações que exijam acesso direto à estrutura. */
    public ArvoreTrie obterArvoreTrie() {
        return arvoreTrie;
    }

    /** Devolve a BST usada para consultas de ranking. */
    public ArvoreRankingPalavras obterArvoreRanking() {
        return arvoreRanking;
    }
}
