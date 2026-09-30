# Dicionário com Trie e Ranking por Frequência

Projeto Java de autocomplete que combina uma Trie para busca por palavra e prefixo com uma árvore binária de busca (BST) para manter o ranking de uso.

## Estrutura

- `Dicionario`: integra leitura do arquivo, busca, sugestões, inserção e registro de uso.
- `ArvoreTrie`: armazena palavras, percorre prefixos e coleta sugestões por DFS.
- `NoTrie`: nó da Trie; mantém filhos, marcação de fim de palavra e frequência.
- `ArvoreRankingPalavras`: BST lexicográfica que armazena e ordena palavras pela frequência.
- `Principal`: demonstra a inserção e o ranking de sugestões.

As palavras são normalizadas para minúsculas e sem acentos. São aceitas palavras formadas apenas pelas letras `a` a `z`.

## Funções principais

### `Dicionario`

- `carregar(String arquivo)`: lê um arquivo UTF-8 e insere suas palavras nas duas estruturas. Espera uma palavra por linha; espaços em branco também são aceitos como separadores. A carga limpa os dados anteriores e inicia frequências em zero.
- `inserir(String palavra)`: cadastra uma palavra nova com frequência zero.
- `buscar(String palavra)`: verifica a existência da palavra completa.
- `buscarPrefixo(String prefixo)`: verifica se existe alguma palavra começando pelo prefixo.
- `sugerir(String prefixo)`: retorna as palavras do prefixo ordenadas pela frequência, da maior para a menor; em caso de empate, usa ordem alfabética.
- `registrarUso(String palavra)`: insere a palavra se necessário e incrementa sua frequência na Trie e na BST.
- `obterArvoreTrie()` e `obterArvoreRanking()`: disponibilizam as estruturas para consultas diretas.

### `ArvoreTrie`

- `inserir(String palavra)`: cria os nós necessários e marca o nó final da palavra.
- `buscar(String palavra)`: retorna `true` somente quando o caminho existe e termina em uma palavra cadastrada.
- `comecaComPrefixo(String prefixo)`: verifica se o caminho do prefixo existe.
- `sugerir(String prefixo)`: percorre em profundidade a subárvore do prefixo e retorna palavras em ordem alfabética.
- `atravessarAte(String palavra)`: percorre os nós correspondentes à palavra e devolve o nó final do caminho, mesmo que ele não marque uma palavra completa.
- `obterFrequencia(String palavra)`: consulta a frequência de uma palavra completa; retorna zero quando ela não existe.
- `atualizarFrequencia(String palavra, int frequencia)`: atualiza o nó final de uma palavra existente.
- `limpar()`: remove todas as palavras da Trie.
- `normalizarPalavra(String palavra)`: normaliza letras maiúsculas e acentos; entrada inválida resulta em texto vazio.

### `ArvoreRankingPalavras`

- `adicionarOuAtualizar(String palavra, int frequencia)`: insere a palavra em ordem lexicográfica ou atualiza sua frequência.
- `obterFrequencia(String palavra)`: consulta a frequência na BST; retorna zero se a palavra não estiver cadastrada.
- `remover(String palavra)`: remove a palavra e reorganiza os filhos para manter a propriedade da BST.
- `obterMaisFrequentes(int quantidade)`: retorna até a quantidade solicitada, em frequência decrescente e com desempate alfabético.
- `limpar()`: esvazia a BST.

### `NoTrie`

Cada nó contém o vetor `filhos` para as letras de `a` a `z`, `ehFimDaPalavra` para identificar palavras completas e `frequencia` para registrar o uso.

## Compilar e executar

No diretório do projeto:

```sh
javac -d bin src/ARVTRIE/*.java
java -cp bin ARVTRIE.Principal
```

O exemplo insere palavras, registra usos e imprime as sugestões para o prefixo `ca`.

Para carregar um dicionário próprio:

```java
import ARVTRIE.Dicionario;

Dicionario dicionario = new Dicionario();
dicionario.carregar("palavras.txt");
dicionario.registrarUso("computador");
System.out.println(dicionario.sugerir("comp"));
```

O arquivo deve estar codificado em UTF-8. `carregar` recebe o caminho informado e não depende de um `palavras.txt` incluído no repositório.

## Arquivos legados

`BST.java` e `BTNode.java`, localizados na raiz, pertencem ao pacote separado `ARVBIN` e não estão dentro da pasta `src` configurada como fonte Java do projeto Eclipse. Eles não fazem parte da implementação integrada documentada acima.