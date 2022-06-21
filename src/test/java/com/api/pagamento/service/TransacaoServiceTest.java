package com.api.pagamento.service;

import com.api.pagamento.builder.TransacaoDTOBuilder;
import com.api.pagamento.domain.dto.TransacaoDTO;
import com.api.pagamento.domain.dto.util.Mapper;
import com.api.pagamento.domain.enumeration.StatusEnum;
import com.api.pagamento.domain.exception.InsercaoNaoPermitidaException;
import com.api.pagamento.domain.exception.TransacaoInexistenteException;
import com.api.pagamento.domain.model.Transacao;
import com.api.pagamento.repository.DescricaoRepository;
import com.api.pagamento.repository.TransacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

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
public class TransacaoServiceTest {

    // Mock: cria uma instancia de uma classe, porém Mockada (simulada). Se você chamar um metodo ele não irá chamar
    // o metodo real, a não ser que você queira.
    @Mock
    private TransacaoRepository transacaoRepository;

    // InjectMocks: Cria uma intancia e injeta as dependências necessárias que estão anotadas com @Mock.
    @InjectMocks
    private TransacaoServiceImp transacaoService;

    @Mock
    private DescricaoRepository descricaoRepository;


    // @Test = A anotação de teste informa ao JUnit que o método void público ao qual está anexado pode ser executado
    // como um caso de teste . Para executar o método, JUnit primeiro constrói uma nova instância da classe e,
    // em seguida,  invoca o método anotado

    //When: Após um mock ser criado, você pode direcionar um retorno para um metodo dado um parametro de entrada.

    // Quando o pagamento é informado, ele deve ser criado
    @Test
    void whenPaymentInformedThenItShouldBeCreated() throws InsercaoNaoPermitidaException {

        // Dado

            //Gera um TransacaoDTO
            TransacaoDTO expectedTransacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

            //Tranforma o TransacaoDTO em um Transacao
            Transacao transacao = (Transacao) Mapper.convert(expectedTransacaoDTO, Transacao.class);

            transacao.setId(null);
            transacao.getDescricao().setId(null);
            transacao.getFormaPagamento().setId(null);

        //Quando

            //transacaoService.save( -> transacaoRepository.save(expectedTransacao) -> expectedTransacao
            when(transacaoRepository
                    .save(transacao))
                    .thenReturn((Transacao) Mapper.convert(expectedTransacaoDTO,Transacao.class));

        // Então

            //Cria um TransacaoDTO
            TransacaoDTO createdTransacaoDTO  = transacaoService.pagar(transacao);

            //Verifica se o atributo id do createdTransacaoDTO é igual ao atributo id do expectedDTO
            assertThat(createdTransacaoDTO.getId(), is(equalTo(expectedTransacaoDTO.getId())));
            assertThat(createdTransacaoDTO.getCartao(), is(equalTo(expectedTransacaoDTO.getCartao())));
            assertThat(createdTransacaoDTO.getDescricao().getId(), is(equalTo(expectedTransacaoDTO.getDescricao().getId())));
            assertThat(createdTransacaoDTO.getDescricao().getValor(), is(equalTo(expectedTransacaoDTO.getDescricao().getValor())));
            assertThat(createdTransacaoDTO.getDescricao().getDataHora(), is(equalTo(expectedTransacaoDTO.getDescricao().getDataHora())));
            assertThat(createdTransacaoDTO.getDescricao().getEstabelecimento(), is(equalTo(expectedTransacaoDTO.getDescricao().getEstabelecimento())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getId(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getId())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getTipo(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getTipo())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getParcelas(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getParcelas())));

    }

    // Quando o nsu, codigo_pagamento ou o status é informado, uma exceção deve ser lançada
    @Test
    void whenIdsNsuCodPagStatusInformedThenAnExceptionShouldBeThrown() {

        // Dado

            //Gera um TransacaoDTO
            TransacaoDTO transacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

            //Tranforma o TransacaoDTO em um Transacao
            Transacao transacao = (Transacao) Mapper.convert(transacaoDTO, Transacao.class);

            transacao.setId(null);
            transacao.getDescricao().setId(null);
            transacao.getFormaPagamento().setId(null);

        //Quando

            transacao.getDescricao().setNsu("1234567890");
            transacao.getDescricao().setCodigoAutorizacao("147258369");
            transacao.getDescricao().setStatus(StatusEnum.AUTORIZADO);

        // Então

            //Verifica se transacaoService.pagar(transacao) lançou a exceção InsercaoNaoPermitidaException.class
            assertThrows(InsercaoNaoPermitidaException.class, () -> transacaoService.pagar(transacao));

    }

    // Quando uma transacao é informada pelo id e não é encontrada, uma exceção deve ser retornada
    @Test
    void whenTransactionIsInformedByIdAndNotFoundThenAnExceptionIsReturned()  {
        // Dado

        Long id = 1L;

        //Quando

        //transacaoRepository.procurarPeloId(id) -> TransacaoInexistenteException

        // Então

        //Verifica se transacaoService.procurarPeloId(id) lançou a exceção TransacaoInexistenteException.class
        assertThrows(TransacaoInexistenteException.class, () -> transacaoService.procurarPeloId(id));

    }

    // Quando um pagamento não é informado com todos os campos obrigatórios, uma exceção deve ser retornada
    @Test
    void whenPaymentWithoutAllFieldsIsInformedThenAnExceptionIsReturned()  {
        // Dado

            //Gera um TransacaoDTO
            TransacaoDTO transacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

            //Tranforma o TransacaoDTO em um Transacao
            Transacao transacao = (Transacao) Mapper.convert(transacaoDTO, Transacao.class);

            transacao.setId(null);
            transacao.getDescricao().setId(null);
            transacao.getFormaPagamento().setId(null);


        //Quando

        //transacaoRepository.save(transacao) -> ConstraintViolationException
            when(transacaoRepository.save(transacao))
                   .thenThrow(ConstraintViolationException.class);

        // Então

             //Verifica se transacaoService.pagar(transacao) lançou a exceção ConstraintViolationException.class
            assertThrows(ConstraintViolationException.class, () -> transacaoService.pagar(transacao));

    }

    // Quando estorno é chamado pelo id, o estorno é retornado
    @Test
    void whenReversalInformedByIdThenReversalIsReturned() throws Exception {

        // Dado

        Long id = 1L;

        //Gera um TransacaoDTO
        TransacaoDTO expectedTransacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

        expectedTransacaoDTO.getDescricao().setNsu("1234567890");
        expectedTransacaoDTO.getDescricao().setCodigoAutorizacao("147258369");
        expectedTransacaoDTO.getDescricao().setStatus(StatusEnum.NEGADO);

        //Tranforma o TransacaoDTO em um Transacao
        Transacao transacao = (Transacao) Mapper.convert(expectedTransacaoDTO, Transacao.class);

        //When

        //transacaoService.findById(id) -> transacao
        when(transacaoRepository.findById(id))
                .thenReturn(Optional.ofNullable(transacao));

        // Então

          //Cria um TransacaoDTO
            TransacaoDTO createdTransacaoDTO  = transacaoService.estornar(id);

            //Verifica se o atributo id do createdTransacaoDTO é igual ao atributo id do expectedDTO
            assertThat(createdTransacaoDTO.getId(), is(equalTo(expectedTransacaoDTO.getId())));
            assertThat(createdTransacaoDTO.getCartao(), is(equalTo(expectedTransacaoDTO.getCartao())));
            assertThat(createdTransacaoDTO.getDescricao().getId(), is(equalTo(expectedTransacaoDTO.getDescricao().getId())));
            assertThat(createdTransacaoDTO.getDescricao().getValor(), is(equalTo(expectedTransacaoDTO.getDescricao().getValor())));
            assertThat(createdTransacaoDTO.getDescricao().getDataHora(), is(equalTo(expectedTransacaoDTO.getDescricao().getDataHora())));
            assertThat(createdTransacaoDTO.getDescricao().getEstabelecimento(), is(equalTo(expectedTransacaoDTO.getDescricao().getEstabelecimento())));
            assertThat(createdTransacaoDTO.getDescricao().getNsu(), is(equalTo(expectedTransacaoDTO.getDescricao().getNsu())));
            assertThat(createdTransacaoDTO.getDescricao().getCodigoAutorizacao(), is(equalTo(expectedTransacaoDTO.getDescricao().getCodigoAutorizacao())));
            assertThat(createdTransacaoDTO.getDescricao().getStatus(), is(equalTo(expectedTransacaoDTO.getDescricao().getStatus())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getId(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getId())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getTipo(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getTipo())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getParcelas(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getParcelas())));

    }

    //Quando a transacao é informada pelo id, a transação é retornada
    @Test
    void whenTransactionByIdIsInformedThenIsReturned() throws Exception {

        // Dado

        Long id = 1L;

        //Gera um TransacaoDTO
        TransacaoDTO expectedTransacaoDTO = TransacaoDTOBuilder.builder().build().toTransacaoDTO();


        expectedTransacaoDTO.getDescricao().setNsu("1234567890");
        expectedTransacaoDTO.getDescricao().setCodigoAutorizacao("147258369");
        expectedTransacaoDTO.getDescricao().setStatus(StatusEnum.AUTORIZADO);

        //Tranforma o TransacaoDTO em um Transacao
        Transacao transacao = (Transacao) Mapper.convert(expectedTransacaoDTO, Transacao.class);

        //When

        //transacaoService.findById(id) -> transacao
        when(transacaoRepository.findById(id))
                .thenReturn(Optional.ofNullable(transacao));

        // Então

            //Cria um TransacaoDTO
            TransacaoDTO createdTransacaoDTO  = transacaoService.procurarPeloId(id);

            //Verifica se o atributo id do createdTransacaoDTO é igual ao atributo id do expectedDTO
            assertThat(createdTransacaoDTO.getId(), is(equalTo(expectedTransacaoDTO.getId())));
            assertThat(createdTransacaoDTO.getCartao(), is(equalTo(expectedTransacaoDTO.getCartao())));
            assertThat(createdTransacaoDTO.getDescricao().getId(), is(equalTo(expectedTransacaoDTO.getDescricao().getId())));
            assertThat(createdTransacaoDTO.getDescricao().getValor(), is(equalTo(expectedTransacaoDTO.getDescricao().getValor())));
            assertThat(createdTransacaoDTO.getDescricao().getDataHora(), is(equalTo(expectedTransacaoDTO.getDescricao().getDataHora())));
            assertThat(createdTransacaoDTO.getDescricao().getEstabelecimento(), is(equalTo(expectedTransacaoDTO.getDescricao().getEstabelecimento())));
            assertThat(createdTransacaoDTO.getDescricao().getNsu(), is(equalTo(expectedTransacaoDTO.getDescricao().getNsu())));
            assertThat(createdTransacaoDTO.getDescricao().getCodigoAutorizacao(), is(equalTo(expectedTransacaoDTO.getDescricao().getCodigoAutorizacao())));
            assertThat(createdTransacaoDTO.getDescricao().getStatus(), is(equalTo(expectedTransacaoDTO.getDescricao().getStatus())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getId(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getId())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getTipo(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getTipo())));
            assertThat(createdTransacaoDTO.getFormaPagamento().getParcelas(), is(equalTo(expectedTransacaoDTO.getFormaPagamento().getParcelas())));

    }

    //Quando a transacao não é informada, todas as transações são retornadas
    @Test
    void whenTransactionIsCalledThenAllIsReturned() throws Exception {

        //Dado

        //Gera um TransacaoDTO
        TransacaoDTO transacaoDTO1 = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

        transacaoDTO1.getDescricao().setNsu("1234567890");
        transacaoDTO1.getDescricao().setCodigoAutorizacao("147258369");
        transacaoDTO1.getDescricao().setStatus(StatusEnum.AUTORIZADO);

        //Tranforma o TransacaoDTO em um Transacao
        Transacao transacao1 = (Transacao) Mapper.convert(transacaoDTO1, Transacao.class);

        //Gera um TransacaoDTO
        TransacaoDTO transacaoDTO2 = TransacaoDTOBuilder.builder().build().toTransacaoDTO();

        transacaoDTO2.getDescricao().setNsu("1234567890");
        transacaoDTO2.getDescricao().setCodigoAutorizacao("147258369");
        transacaoDTO2.getDescricao().setStatus(StatusEnum.AUTORIZADO);

        //Tranforma o TransacaoDTO em um Transacao
        Transacao transacao2 = (Transacao) Mapper.convert(transacaoDTO2, Transacao.class);

        List<TransacaoDTO> transacaoDTOList = new ArrayList<>();
        transacaoDTOList.add(transacaoDTO1);
        transacaoDTOList.add(transacaoDTO2);

        List<Transacao> transacaoList = new ArrayList<>();
        transacaoList.add(transacao1);
        transacaoList.add(transacao2);

        //Quando

        //transacaoService.procurarTodos() -> transacaoDTOList
        when(transacaoRepository.findAll())
                .thenReturn(transacaoList);


        //Cria um TransacaoDTO
        List<TransacaoDTO> createdTransacaoDTOList  = transacaoService.procurarTodos();

        //Verifica se o atributo id do createdTransacaoDTO é igual ao atributo id do expectedDTO

        for(int i=0; i < transacaoDTOList.size(); i++){
            assertThat(createdTransacaoDTOList.get(i).getId(), is(equalTo(transacaoDTOList.get(i).getId())));
            assertThat(createdTransacaoDTOList.get(i).getCartao(), is(equalTo(transacaoDTOList.get(i).getCartao())));
            assertThat(createdTransacaoDTOList.get(i).getDescricao().getId(), is(equalTo(transacaoDTOList.get(i).getDescricao().getId())));
            assertThat(createdTransacaoDTOList.get(i).getDescricao().getValor(), is(equalTo(transacaoDTOList.get(i).getDescricao().getValor())));
            assertThat(createdTransacaoDTOList.get(i).getDescricao().getDataHora(), is(equalTo(transacaoDTOList.get(i).getDescricao().getDataHora())));
            assertThat(createdTransacaoDTOList.get(i).getDescricao().getEstabelecimento(), is(equalTo(transacaoDTOList.get(i).getDescricao().getEstabelecimento())));
            assertThat(createdTransacaoDTOList.get(i).getDescricao().getNsu(), is(equalTo(transacaoDTOList.get(i).getDescricao().getNsu())));
            assertThat(createdTransacaoDTOList.get(i).getDescricao().getCodigoAutorizacao(), is(equalTo(transacaoDTOList.get(i).getDescricao().getCodigoAutorizacao())));
            assertThat(createdTransacaoDTOList.get(i).getDescricao().getStatus(), is(equalTo(transacaoDTOList.get(i).getDescricao().getStatus())));
            assertThat(createdTransacaoDTOList.get(i).getFormaPagamento().getId(), is(equalTo(transacaoDTOList.get(i).getFormaPagamento().getId())));
            assertThat(createdTransacaoDTOList.get(i).getFormaPagamento().getTipo(), is(equalTo(transacaoDTOList.get(i).getFormaPagamento().getTipo())));
            assertThat(createdTransacaoDTOList.get(i).getFormaPagamento().getParcelas(), is(equalTo(transacaoDTOList.get(i).getFormaPagamento().getParcelas())));

        }

    }

}
