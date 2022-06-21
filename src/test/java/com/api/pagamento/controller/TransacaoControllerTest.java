package com.api.pagamento.controller;

import com.api.pagamento.builder.TransacaoDTOBuilder;
import com.api.pagamento.domain.dto.TransacaoDTO;
import com.api.pagamento.domain.dto.util.Mapper;
import com.api.pagamento.domain.enumeration.StatusEnum;
import com.api.pagamento.domain.exception.InsercaoNaoPermitidaException;
import com.api.pagamento.domain.exception.TransacaoInexistenteException;
import com.api.pagamento.domain.model.Transacao;
import com.api.pagamento.service.TransacaoService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*

    TDD é uma sigla para Test Driven Development, ou Desenvolvimento Orientado a Testes. A ideia do TDD é que
    você trabalhe em ciclos. Estes ciclos ocorrem na seguinte ordem:

    Primeiro, escreva um teste unitário que inicialmente irá falhar, tendo em vista que o código ainda não foi implementado;
    Crie o código que satisfaça esse teste, ou seja: implemente a funcionalidade em questão. Essa primeira implementação
    deverá satisfazer imediatamente o teste que foi escrito no ciclo anterior;
    Quando o código estiver implementado e o teste satisfeito, refatore o código para melhorar pontos como legibilidade.
    Logo após, execute o teste novamente. A nova versão do código também deverá passar sem que seja necessário modificar o teste escrito inicialmente.


 */

/*

        Testes de Unidade ou teste unitário é a fase de testes onde cada unidade do sistema é testada individualmente.
        O objetivo é isolar cada parte do sistema para garantir que elas estejam funcionando conforme especificado.


        */

/*
    Junit

        Esse framework facilita a criação e manutenção do código para a automação de testes com apresentação dos
        resultados.
        Com ele, pode ser verificado se cada método de uma classe funciona da forma esperada, exibindo possíveis
        erros ou falhas podendo ser utilizado tanto para a execução de baterias de testes como para extensão.

        Com JUnit, o programador tem a possibilidade de usar esta ferramenta para criar um modelo padrão de testes,
        muitas vezes de forma automatizada.

        O teste de unidade testa o menor dos componentes de um sistema de maneira isolada. Cada uma dessas unidades
        define um conjunto de estímulos (chamada de métodos), e de dados de entrada e saída associados a cada estímulo.
        As entradas são parâmetros e as saídas são o valor de retorno, exceções ou o estado do objeto. Tipicamente
        um teste unitário executa um método individualmente e compara uma saída conhecida após o processamento da mesma.

*/

/* Mockito

    O Mockito é um framework de testes unitários e o seu principal objetivo é instanciar classes e controlar o
    comportamento dos métodos. Isso é chamado de mock, na tradução livre quer dizer zombar, e talvez seja mesmo o termo
    que melhor o define.
    Pois ao mockar a dependencia de uma classe, eu faço com que a classe que estou testando pense estar invocando o metodo
    realmente, mas de fato não está.

*/


/* hamcrest

    O Hamcrest é um framework que possibilita a criação de regras de verificação (matchers) de forma declarativa.
    Como dito no próprio site do Hamcrest, Matchers that can be combined to create flexible expressions of intent.

    Portanto a ideia é que com os matchers Hamcrest as asserções utilizadas expressem melhor a sua intenção,
    ficando mais legíveis e mais expressivas.

    Um matcher Hamcrest é um objeto que
        reporta se um dado objeto satisfaz um determinado critério;
        pode descrever este critério; e
        é capaz de descrever porque um objeto não satisfaz um determinado critério.


*/

//MockitoExtension.class = Extensão necessária para as anotações do mockito serem utilizadas
@ExtendWith(MockitoExtension.class)
public class TransacaoControllerTest {

    //MockMvc: fornece suporte para teste Spring MVC.
    // Ele encapsula todos os beans de aplicativo da we e os disponibiliza para teste.
    private MockMvc mockMvc;

    // Mock: cria uma instancia de uma classe, porém Mockada (simulada). Se você chamar um metodo ele não irá chamar
    // o metodo real, a não ser que você queira.
    @Mock
    private TransacaoService transacaoService;

    // InjectMocks: Cria uma intancia e injeta as dependências necessárias que estão anotadas com @Mock.
    @InjectMocks
    private TransacaoController transacaoController;

    // @BeforeEach: Execute antes de cada método de teste.
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transacaoController).build();
    }

    // @Test = A anotação de teste informa ao JUnit que o método void público ao qual está anexado pode ser executado
    // como um caso de teste . Para executar o método, JUnit primeiro constrói uma nova instância da classe e,
    // em seguida,  invoca o método anotado

    //When: Após um mock ser criado, você pode direcionar um retorno para um metodo dado um parametro de entrada.

    // Quando o pagamento é chamado, ele deve ser criado
    @Test
    void whenPaymentIsCalledThenItShouldBeCreated() throws Exception {

        //Dado

        //Gera um TransacaoDTO
        TransacaoDTO transacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

        transacaoDTO.getDescricao().setNsu("1234567890");
        transacaoDTO.getDescricao().setCodigoAutorizacao("147258369");
        transacaoDTO.getDescricao().setStatus(StatusEnum.AUTORIZADO);

        //Tranforma o TransacaoDTO em um Transacao
        Transacao transacao = (Transacao) Mapper.convert(transacaoDTO, Transacao.class);

        transacao.setId(null);
        transacao.getDescricao().setId(null);
        transacao.getFormaPagamento().setId(null);

        //Quando

        //transacaoService.pagar(transacao) -> transacaoDTO
        when(transacaoService.pagar(transacao))
                .thenReturn(transacaoDTO);

        // Então

        //perform: Executa o post /transacao/v1/pagamento
        //contentType: Define que o tipo do conteúdo é JSON
        //content: Define que o conteúdo é o Json de transacaoDTO
        //andExpect: Espera-se que o post retorne o status OK
        //andExpect: Espera-se que $.id seja igual a transacaoDTO.getId()

        mockMvc.perform(post("/transacao/v1/pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(transacao)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Math.toIntExact(transacaoDTO.getId()))))
                .andExpect(jsonPath("$.cartao", is(transacaoDTO.getCartao())))
                .andExpect(jsonPath("$.descricao.valor", is(transacaoDTO.getDescricao().getValor())))
                .andExpect(jsonPath("$.descricao.dataHora", is(transacaoDTO.getDescricao().getDataHora())))
                .andExpect(jsonPath("$.descricao.estabelecimento", is(transacaoDTO.getDescricao().getEstabelecimento())))
                .andExpect(jsonPath("$.descricao.nsu", is(transacaoDTO.getDescricao().getNsu())))
                .andExpect(jsonPath("$.descricao.codigoAutorizacao", is(transacaoDTO.getDescricao().getCodigoAutorizacao())))
                .andExpect(jsonPath("$.descricao.status", is(transacaoDTO.getDescricao().getStatus().toString())))
                .andExpect(jsonPath("$.formaPagamento.tipo", is(transacaoDTO.getFormaPagamento().getTipo().toString())))
                .andExpect(jsonPath("$.formaPagamento.parcelas", is(transacaoDTO.getFormaPagamento().getParcelas())));

    }

    // Quando o nsu, codigo_pagamento ou o status é informado ao chamar o pagamento, uma exceção deve ser retornada
    @Test
    void whenPaymentInformedIdsNsuCodPagStatusInformedThenThenAnExceptionIsReturned() throws Exception {

        // Dado

        //Gera um BeerDTO
        TransacaoDTO transacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

        //Tranforma o TransacaoDTO em um Transacao
        Transacao transacao = (Transacao) Mapper.convert(transacaoDTO, Transacao.class);

        transacao.setId(null);
        transacao.getDescricao().setId(null);
        transacao.getFormaPagamento().setId(null);

        //When

        //transacao for inválida

        //transacaoService.pagar(transacao) -> InsercaoNaoPermitidaException()
        when(transacaoService.pagar(transacao))
                .thenThrow(InsercaoNaoPermitidaException.class);

        // Então

        //perform: Executa o post /transacao/v1/pagamento
        //contentType: Define que o tipo do conteúdo é JSON
        //andExpect: Espera-se que o post retorne o status BadRequest

        mockMvc.perform(post("/transacao/v1/pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(transacao)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof InsercaoNaoPermitidaException))
        ;

    }

    // Quando uma transacao é chamada pelo id e não é encontrada, uma exceção deve ser retornada
    @Test
    void whenTransactionIsCalledByIdAndNotFoundThenAnExceptionIsReturned() throws Exception {

        // Dado

            Long id = 1L;

        //When

            //transacaoService.procurarPeloId(id) -> TransacaoInexistenteException()
            when(transacaoService.procurarPeloId(id))
                    .thenThrow(TransacaoInexistenteException.class);

        // Então

            //perform: Executa o get /transacao/v1/1
            //contentType: Define que o tipo do conteúdo é JSON
            //andExpect: Espera-se que o post retorne o status BadRequest

            mockMvc.perform(get("/transacao/v1/1")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(result -> assertTrue(result.getResolvedException() instanceof TransacaoInexistenteException))
        ;

    }

    // Quando um pagamento não é chamado com todos os campos obrigatórios, uma exceção deve ser retornada
    @Test
    void whenPaymentWithoutAllFieldsIsCalledThenAnExceptionIsReturned() throws Exception {

        // Dado

        //Gera um TransacaoDTO
        TransacaoDTO transacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

        transacaoDTO.setCartao(null);
        transacaoDTO.getDescricao().setEstabelecimento(null);

        //Tranforma o TransacaoDTO em um Transacao
        Transacao transacao = (Transacao) Mapper.convert(transacaoDTO, Transacao.class);

        transacao.setId(null);
        transacao.getDescricao().setId(null);
        transacao.getFormaPagamento().setId(null);

        //When

        //transacao for inválido
        //A anotação @Valid beerDTO presente no metódo createBeer que realiza tal verificação

        // Então

        //perform: Executa o post /transacao/v1/pagamento
        //contentType: Define que o tipo do conteúdo é JSON
        //andExpect: Espera-se que o post retorne o status BadRequest

        mockMvc.perform(post("/transacao/v1/pagamento")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(transacao)))
                .andExpect(status().isBadRequest());

    }

    // Quando estorno é chamado pelo id, o estorno é retornado
    @Test
    void whenReversalCallByIdThenReversalIsReturned() throws Exception {

        // Dado

        //Gera um TransacaoDTO
        TransacaoDTO transacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

        transacaoDTO.getDescricao().setNsu("1234567890");
        transacaoDTO.getDescricao().setCodigoAutorizacao("147258369");
        transacaoDTO.getDescricao().setStatus(StatusEnum.NEGADO);

        //When

        //transacaoService.estornar() -> transacaoDTO
        when(transacaoService.estornar(1L))
                .thenReturn(transacaoDTO);

        // Então

        //perform: Executa o post /transacao/v1/pagamento
        //contentType: Define que o tipo do conteúdo é JSON
        //content: Define que o conteúdo é o Json de transacaoDTO
        //andExpect: Espera-se que o post retorne o status Ok

        mockMvc.perform(put("/transacao/v1/estorno/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Math.toIntExact(transacaoDTO.getId()))))
                .andExpect(jsonPath("$.cartao", is(transacaoDTO.getCartao())))
                .andExpect(jsonPath("$.descricao.valor", is(transacaoDTO.getDescricao().getValor())))
                .andExpect(jsonPath("$.descricao.dataHora", is(transacaoDTO.getDescricao().getDataHora())))
                .andExpect(jsonPath("$.descricao.estabelecimento", is(transacaoDTO.getDescricao().getEstabelecimento())))
                .andExpect(jsonPath("$.descricao.nsu", is(transacaoDTO.getDescricao().getNsu())))
                .andExpect(jsonPath("$.descricao.codigoAutorizacao", is(transacaoDTO.getDescricao().getCodigoAutorizacao())))
                .andExpect(jsonPath("$.descricao.status", is(transacaoDTO.getDescricao().getStatus().toString())))
                .andExpect(jsonPath("$.formaPagamento.tipo", is(transacaoDTO.getFormaPagamento().getTipo().toString())))
                .andExpect(jsonPath("$.formaPagamento.parcelas", is(transacaoDTO.getFormaPagamento().getParcelas())));

    }

    //Quando a transacao é chamada pelo id, a transação é retornada
    @Test
    void whenTransactionByIdIsCalledThenIsReturned() throws Exception {

        //Dado

        //Gera um TransacaoDTO
        TransacaoDTO transacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

        transacaoDTO.getDescricao().setNsu("1234567890");
        transacaoDTO.getDescricao().setCodigoAutorizacao("147258369");
        transacaoDTO.getDescricao().setStatus(StatusEnum.AUTORIZADO);

        //Quando

        //transacaoService.procurarPeloId(1) -> transacaoDTO
        when(transacaoService.procurarPeloId(1L))
                .thenReturn(transacaoDTO);

        // Então

        //perform: Executa o post /transacao/v1/pagamento
        //contentType: Define que o tipo do conteúdo é JSON
        //content: Define que o conteúdo é o Json de transacaoDTO
        //andExpect: Espera-se que o post retorne o status OK
        //andExpect: Espera-se que $.id seja igual a transacaoDTO.getId()


        mockMvc.perform(get("/transacao/v1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(Math.toIntExact(transacaoDTO.getId()))))
                .andExpect(jsonPath("$.cartao", is(transacaoDTO.getCartao())))
                .andExpect(jsonPath("$.descricao.valor", is(transacaoDTO.getDescricao().getValor())))
                .andExpect(jsonPath("$.descricao.dataHora", is(transacaoDTO.getDescricao().getDataHora())))
                .andExpect(jsonPath("$.descricao.estabelecimento", is(transacaoDTO.getDescricao().getEstabelecimento())))
                .andExpect(jsonPath("$.descricao.nsu", is(transacaoDTO.getDescricao().getNsu())))
                .andExpect(jsonPath("$.descricao.codigoAutorizacao", is(transacaoDTO.getDescricao().getCodigoAutorizacao())))
                .andExpect(jsonPath("$.descricao.status", is(transacaoDTO.getDescricao().getStatus().toString())))
                .andExpect(jsonPath("$.formaPagamento.tipo", is(transacaoDTO.getFormaPagamento().getTipo().toString())))
                .andExpect(jsonPath("$.formaPagamento.parcelas", is(transacaoDTO.getFormaPagamento().getParcelas())));

    }

    //Quando a transacao é chamada, todas as transações são retornadas
    @Test
    void whenTransactionIsCalledThenAllIsReturned() throws Exception {

        //Dado

            //Gera um TransacaoDTO
            TransacaoDTO transacaoDTO1 = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

            transacaoDTO1.getDescricao().setNsu("1234567890");
            transacaoDTO1.getDescricao().setCodigoAutorizacao("147258369");
            transacaoDTO1.getDescricao().setStatus(StatusEnum.AUTORIZADO);

            //Gera um TransacaoDTO
            TransacaoDTO transacaoDTO2 = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

            transacaoDTO2.getDescricao().setNsu("1234567890");
            transacaoDTO2.getDescricao().setCodigoAutorizacao("147258369");
            transacaoDTO2.getDescricao().setStatus(StatusEnum.AUTORIZADO);

            List<TransacaoDTO> transacaoDTOList = new ArrayList<>();
            transacaoDTOList.add(transacaoDTO1);
            transacaoDTOList.add(transacaoDTO2);

        //Quando

            //transacaoService.procurarTodos() -> transacaoDTOList
            when(transacaoService.procurarTodos())
                    .thenReturn(transacaoDTOList);

        // Então

            //perform: Executa o post /transacao/v1/pagamento
            //contentType: Define que o tipo do conteúdo é JSON
            //content: Define que o conteúdo é o Json de transacaoDTO
            //andExpect: Espera-se que o post retorne o status OK
            //andExpect: Espera-se que $.id seja igual a transacaoDTO.getId()

           mockMvc.perform(get("/transacao/v1")
                        .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.[0].id", is(Math.toIntExact(transacaoDTO1.getId()))))
                    .andExpect(jsonPath("$.[0].cartao", is(transacaoDTO1.getCartao())))
                    .andExpect(jsonPath("$.[0].descricao.valor", is(transacaoDTO1.getDescricao().getValor())))
                    .andExpect(jsonPath("$.[0].descricao.dataHora", is(transacaoDTO1.getDescricao().getDataHora())))
                    .andExpect(jsonPath("$.[0].descricao.estabelecimento", is(transacaoDTO1.getDescricao().getEstabelecimento())))
                    .andExpect(jsonPath("$.[0].descricao.nsu", is(transacaoDTO1.getDescricao().getNsu())))
                    .andExpect(jsonPath("$.[0].descricao.codigoAutorizacao", is(transacaoDTO1.getDescricao().getCodigoAutorizacao())))
                    .andExpect(jsonPath("$.[0].descricao.status", is(transacaoDTO1.getDescricao().getStatus().toString())))
                    .andExpect(jsonPath("$.[0].formaPagamento.tipo", is(transacaoDTO1.getFormaPagamento().getTipo().toString())))
                    .andExpect(jsonPath("$.[0].formaPagamento.parcelas", is(transacaoDTO1.getFormaPagamento().getParcelas())))
                    .andExpect(jsonPath("$.[1].id", is(Math.toIntExact(transacaoDTO2.getId()))))
                    .andExpect(jsonPath("$.[1].cartao", is(transacaoDTO2.getCartao())))
                    .andExpect(jsonPath("$.[1].descricao.valor", is(transacaoDTO2.getDescricao().getValor())))
                    .andExpect(jsonPath("$.[1].descricao.dataHora", is(transacaoDTO2.getDescricao().getDataHora())))
                    .andExpect(jsonPath("$.[1].descricao.estabelecimento", is(transacaoDTO2.getDescricao().getEstabelecimento())))
                    .andExpect(jsonPath("$.[1].descricao.nsu", is(transacaoDTO2.getDescricao().getNsu())))
                    .andExpect(jsonPath("$.[1].descricao.codigoAutorizacao", is(transacaoDTO2.getDescricao().getCodigoAutorizacao())))
                    .andExpect(jsonPath("$.[1].descricao.status", is(transacaoDTO2.getDescricao().getStatus().toString())))
                    .andExpect(jsonPath("$.[1].formaPagamento.tipo", is(transacaoDTO2.getFormaPagamento().getTipo().toString())))
                    .andExpect(jsonPath("$.[1].formaPagamento.parcelas", is(transacaoDTO2.getFormaPagamento().getParcelas())));
    }
}
