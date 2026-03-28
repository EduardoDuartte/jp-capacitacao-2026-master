# API de Produtos, Estoque, Carrinho, Pedidos e Cupons

## Visão geral

Este projeto é uma API REST desenvolvida com **Java**, **Spring Boot** e **JPA/Hibernate**, com persistência em **Oracle**.

A aplicação começou como um CRUD de produtos e foi evoluindo com novas regras de negócio, incluindo:

- cadastro e manutenção de produtos
- histórico de alteração de preço
- categorias com hierarquia
- controle de estoque e transações de estoque
- carrinho de compras
- checkout e geração de pedidos
- cupons de desconto
- delete lógico de produtos

O objetivo do projeto é simular um fluxo básico de e-commerce/backoffice com regras de negócio de nível júnior/intermediário.

---

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Oracle Database
- Lombok
- Swagger / OpenAPI
- Maven

---

## Funcionalidades implementadas

### Produtos

- cadastrar produto
- listar produtos ativos
- buscar produto por id
- atualizar produto sem permitir alteração de preço
- atualizar preço em endpoint específico
- delete lógico de produto
- reativar produto
- buscar produtos por categoria
- buscar produtos de uma categoria incluindo subcategorias filhas diretas

### Histórico de preço

- registrar histórico sempre que o preço do produto for alterado
- consultar histórico por produto

### Categorias

- cadastrar categoria
- atualizar categoria
- listar categorias
- buscar categoria por id
- deletar categoria
- hierarquia pai → filho
- nome único no mesmo nível

### Estoque

- entrada de estoque
- saída de estoque
- ajuste de estoque
- devolução de estoque
- impedir saída com estoque insuficiente
- flag de estoque baixo
- histórico de movimentação por produto

### Carrinho

- usuário pode ter apenas 1 carrinho ativo
- criar ou buscar carrinho ativo do usuário
- adicionar item ao carrinho
- atualizar quantidade de item
- remover item
- listar itens do carrinho
- visualizar carrinho completo com total
- usar `priceSnapshot` nos itens
- recalcular total automaticamente

### Pedido

- checkout do carrinho para pedido
- gerar itens do pedido
- baixar estoque no checkout
- mudar status do carrinho para finalizado
- limpar itens do carrinho após checkout
- consultar pedido por id
- listar pedidos por usuário
- listar itens do pedido
- atualizar status do pedido
- cancelar pedido apenas em `CRIADO` ou `PAGO`
- devolver estoque no cancelamento

### Cupons

- cadastrar cupom
- listar cupons
- buscar cupom por id
- buscar cupom por código
- atualizar cupom
- deletar cupom
- cupons percentuais e fixos
- cupons aplicáveis ao pedido, categoria ou produto
- validar expiração
- validar limite de uso
- impedir reutilização pelo mesmo usuário
- aplicar cupom no checkout

---

## Regras de negócio principais

## Produtos

- produto novo nasce com estoque inicial igual a `0`
- produto possui delete lógico usando o campo `ativo`
- produto inativo não aparece na listagem padrão
- produto inativo não pode ser atualizado nem ter preço alterado
- preço só pode ser alterado pelo endpoint específico de atualização de preço

## Histórico de preço

- toda alteração de preço gera um registro em `HistoricoPreco`
- o histórico registra:
  - preço antigo
  - preço novo
  - data da alteração

## Categorias

- toda categoria pode ter uma categoria pai
- categorias podem formar hierarquia simples
- o nome de uma categoria deve ser único dentro do mesmo nível
- um produto deve obrigatoriamente pertencer a uma categoria

## Estoque

- toda movimentação de estoque gera uma `TransacaoEstoque`
- tipos de transação:
  - `ENTRADA`
  - `SAIDA`
  - `AJUSTE`
  - `DEVOLUCAO`
- venda/pedido reduz estoque
- não é permitido vender sem estoque suficiente
- produto pode ter flag de estoque baixo com base em estoque mínimo

## Carrinho

- cada usuário pode ter apenas 1 carrinho com status `ATIVO`
- itens do carrinho salvam o preço do momento em `priceSnapshot`
- total do carrinho é recalculado a cada alteração dos itens
- adicionar item ao carrinho não baixa estoque
- a baixa real de estoque acontece no checkout do pedido

## Pedido

- checkout transforma carrinho em pedido
- pedido nasce com status `CRIADO`
- status possíveis:
  - `CRIADO`
  - `PAGO`
  - `CANCELADO`
  - `ENVIADO`
  - `FINALIZADO`
- cancelamento só é permitido em `CRIADO` ou `PAGO`
- ao cancelar, o estoque dos itens é devolvido

## Cupons

- tipos de cupom:
  - `PERCENTUAL`
  - `FIXO`
- cupom pode ser aplicável a:
  - `PEDIDO`
  - `CATEGORIA`
  - `PRODUTO`
- cupom tem período de validade
- cupom pode ter limite de uso
- se `limiteUso` não for informado, o padrão é `1`
- cupom expirado é rejeitado
- cupom já utilizado pelo usuário é rejeitado
- cupom não aplicável aos itens do carrinho é rejeitado

---

## Modelo de entidades

### Produto

Campos principais:

- `id`
- `nome`
- `descricao`
- `preco`
- `codigoBarras`
- `ativo`
- `categoria`
- `quantidadeEstoque`
- `estoqueMinimo`
- `estoqueBaixo`
- `criadoEm`
- `atualizadoEm`

### HistoricoPreco

- `id`
- `produto`
- `precoAntigo`
- `precoNovo`
- `dataAlteracao`

### Categoria

- `idCategoria`
- `nome`
- `categoriaPai`
- `criadoEm`
- `atualizadoEm`

### Carrinho

- `idCarrinho`
- `idUsuario`
- `status`
- `valorTotal`

### ItemCarrinho

- `idItem`
- `carrinho`
- `produto`
- `quantidade`
- `priceSnapshot`

### TransacaoEstoque

- `idTransacao`
- `produto`
- `delta`
- `reason`
- `referenceId`
- `criadoPor`
- `criadoEm`

### Pedido

- `idPedido`
- `idUsuario`
- `total`
- `desconto`
- `frete`
- `status`
- `criadoEm`
- `endereco`

### ItemPedido

- `idItem`
- `pedido`
- `produto`
- `quantidade`
- `priceSnapshot`

### Cupom

- `idCupom`
- `codigo`
- `tipo`
- `valor`
- `dataCriacao`
- `dataExpiracao`
- `limiteUso`
- `contagemUso`
- `aplicavelA`
- `idCategoria`
- `idProduto`

### CupomUsoUsuario

- `idUso`
- `cupom`
- `idUsuario`
- `usadoEm`

---

## Estrutura sugerida do projeto

```text
src/main/java/br/com/indra/eduardo_duarte
├── controller
├── model
├── repository
└── service
    ├── dto

```

---

## Endpoints principais

## Produtos

### Listar produtos
`GET /produtos`

### Buscar produto por id
`GET /produtos/{id}`

### Criar produto
`POST /produtos`

### Atualizar produto
`PUT /produtos/{id}`

> Não altera o preço.

### Atualizar preço do produto
`PUT /produtos/{id}/preco?preco=10.50`

### Deletar logicamente produto
`DELETE /produtos/{id}`

### Reativar produto
`PATCH /produtos/{id}/reativar`

### Buscar produtos por categoria
`GET /produtos/categoria/{idCategoria}`

---

## Categorias

### Listar categorias
`GET /categorias`

### Buscar categoria por id
`GET /categorias/{id}`

### Criar categoria
`POST /categorias`

### Atualizar categoria
`PUT /categorias/{id}`

### Deletar categoria
`DELETE /categorias/{id}`

---

## Estoque

### Entrada de estoque
`POST /estoque/entrada`

### Saída de estoque
`POST /estoque/saida`

### Ajuste de estoque
`POST /estoque/ajuste`

### Devolução de estoque
`POST /estoque/devolucao`

### Buscar transações de estoque por produto
`GET /estoque/produto/{idProduto}/transacoes`

---

## Carrinho

### Criar ou buscar carrinho ativo do usuário
`POST /carrinhos/usuario/{idUsuario}`

### Buscar carrinho ativo do usuário
`GET /carrinhos/usuario/{idUsuario}`

### Adicionar item ao carrinho
`POST /carrinhos/usuario/{idUsuario}/itens?idProduto=1&quantidade=2`

### Atualizar quantidade de item
`PUT /carrinhos/{idCarrinho}/itens/{idProduto}?quantidade=3`

### Remover item do carrinho
`DELETE /carrinhos/{idCarrinho}/itens/{idProduto}`

### Listar itens do carrinho
`GET /carrinhos/{idCarrinho}/itens`

### Visualizar carrinho completo
`GET /carrinhos/{idCarrinho}`

---

## Pedidos

### Checkout do carrinho
`POST /pedidos/checkout`

### Buscar pedido por id
`GET /pedidos/{idPedido}`

### Listar pedidos por usuário
`GET /pedidos/usuario/{idUsuario}`

### Listar itens do pedido
`GET /pedidos/{idPedido}/itens`

### Atualizar status do pedido
`PATCH /pedidos/{idPedido}/status`

---

## Cupons

### Listar cupons
`GET /cupons`

### Buscar cupom por id
`GET /cupons/{id}`

### Buscar cupom por código
`GET /cupons/codigo/{codigo}`

### Criar cupom
`POST /cupons`

### Atualizar cupom
`PUT /cupons/{id}`

### Deletar cupom
`DELETE /cupons/{id}`

---

## Exemplos de payloads

## Criar produto

```json
{
  "nome": "Limão",
  "descricao": "Limão siciliano",
  "preco": 3.99,
  "codigoBarras": "55584586952656895",
  "idCategoria": 2
}
```

## Criar categoria

```json
{
  "nome": "Cítricos",
  "idPai": 1
}
```

## Entrada de estoque

```json
{
  "idProduto": 1,
  "quantidade": 50,
  "referenceId": 1001,
  "criadoPor": "eduardo"
}
```

## Criar cupom percentual para pedido

```json
{
  "codigo": "DESC10",
  "tipo": "PERCENTUAL",
  "valor": 10,
  "dataExpiracao": "2026-12-31T23:59:59",
  "limiteUso": 5,
  "aplicavelA": "PEDIDO"
}
```

## Checkout com cupom

```json
{
  "idUsuario": 1,
  "frete": 5.00,
  "endereco": "Rua Exemplo, 123",
  "criadoPor": "eduardo",
  "codigoCupom": "DESC10"
}
```

---

## Como executar o projeto

### Pré-requisitos

- Java instalado
- Maven instalado
- Oracle Database configurado

### Passos gerais

1. configurar o banco Oracle
2. ajustar `application.yml` com usuário, senha e URL
3. criar/ajustar as tabelas necessárias
4. rodar a aplicação Spring Boot
5. acessar Swagger para testar os endpoints

Exemplo de acesso ao Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## Observações finais

Este projeto foi evoluído gradualmente, com foco em aprendizado prático de:

- modelagem de entidades
- CRUD REST
- regras de negócio
- relacionamentos JPA
- transações
- DTOs
- controle de estoque
- carrinho e pedido
- cupons e validações

