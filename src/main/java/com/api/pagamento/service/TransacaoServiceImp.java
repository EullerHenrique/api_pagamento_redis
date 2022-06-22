package com.api.pagamento.service;

import com.api.pagamento.domain.dto.TransacaoDTO;
import com.api.pagamento.domain.dto.util.Mapper;
import com.api.pagamento.domain.enumeration.StatusEnum;
import com.api.pagamento.domain.exception.InsercaoNaoPermitidaException;
import com.api.pagamento.domain.exception.TransacaoInexistenteException;
import com.api.pagamento.domain.model.Transacao;
import com.api.pagamento.repository.DescricaoRepository;
import com.api.pagamento.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

//@Service

//A anotação @Service é usada em sua camada de serviço e anota classes que realizam tarefas de serviço, muitas vezes
//você não a usa, mas em muitos casos você usa essa anotação para representar uma prática recomendada. Por exemplo,
//você poderia chamar diretamente uma classe DAO para persistir um objeto em seu banco de dados, mas isso é horrível.
//É muito bom chamar uma classe de serviço que chama um DAO. Isso é uma boa coisa para executar o padrão de separação
//de interesses.

@Service

//@Transactional
//https://www.devmedia.com.br/conheca-o-spring-transactional-annotations/32472
//"A boa prática é sempre colocar o @Transactional nos métodos que precisam de transação, por exemplo: salvar, alterar,
//excluir, etc., pois assim você garante que eles vão ser executados dentro um contexto transacional e o rollback
//será feito caso ocorra algum erro."

@Transactional

//@RequiredArgsConstructor
//Gera um construtor com argumentos necessários. Os argumentos obrigatórios são campos finais e campos com restrições como @NonNull.

@RequiredArgsConstructor
public class TransacaoServiceImp implements TransacaoService {

    private final TransacaoRepository transacaoRepository;

    private final DescricaoRepository descricaoRepository;


    //@Cacheable: Essa anotação indica que o método aciona uma operação de criação de cache.
    //Cada vez que um método anotado é invocado, o comportamento de cache será aplicado, verificando se o método
    //já foi invocado para os argumentos fornecidos.
    //Se nenhum valor for encontrado no cache para a chave computada, o método de destino será invocado e o valor
    //retornado será armazenado no cache associado.
    //cacheNames: Nome do cache.
    //key: Chave única para o cache.

    @Override
    @Cacheable(cacheNames = "transacao", key="#id")
    public TransacaoDTO procurarPeloId(Long id) throws TransacaoInexistenteException {
        TransacaoDTO transacaoDTO = (TransacaoDTO) transacaoRepository.findById(id).map(t -> Mapper.convert(t, TransacaoDTO.class)).orElse(null);
        if(transacaoDTO != null){
            return transacaoDTO;
        }else{
            throw new TransacaoInexistenteException();
        }
    }

    @Override
    @Cacheable(cacheNames = "transacao", key="#root.method.name")
    public List<TransacaoDTO> procurarTodos() throws TransacaoInexistenteException {
        List<TransacaoDTO> transacaoDTO = transacaoRepository.findAll().stream().map(t -> (TransacaoDTO) Mapper.convert(t, TransacaoDTO.class)).collect(Collectors.toList());
        if(transacaoDTO.size() != 0){
            return transacaoDTO;
        }else{
            throw new TransacaoInexistenteException();
        }
    }

    //@CacheEvict: Essa anotação indica que o método aciona uma operação de remoção de cache.
    //allEntries = true: Remove todos os valores do cache associado ao nome especificado.
    @Override
    @CacheEvict(cacheNames = "transacao", allEntries = true)
    public TransacaoDTO pagar(Transacao transacao) throws InsercaoNaoPermitidaException {

        if(transacao.getDescricao().getStatus() == null && transacao.getDescricao().getNsu() == null && transacao.getDescricao().getCodigoAutorizacao() == null && transacao.getId() == null && transacao.getDescricao().getId() == null && transacao.getFormaPagamento().getId() == null) {
            transacao.getDescricao().setNsu("1234567890");
            transacao.getDescricao().setCodigoAutorizacao("147258369");
            transacao.getDescricao().setStatus(StatusEnum.AUTORIZADO);
            return (TransacaoDTO) Mapper.convert(transacaoRepository.save(transacao), TransacaoDTO.class);
        }else{
            throw new InsercaoNaoPermitidaException();
        }

    }

    @Override
    //@CachePut: Essa anotação indica que o método aciona uma operação de atualização de cache.
    //Ao contrário da anotação @Cacheable, essa anotação não faz com que o método recomendado seja ignorado.
    //Em vez disso, sempre faz com que o método seja invocado e seu resultado seja armazenado no cache associado,
    //ou seja, permite que o cache associado seja atualizado.

    @CachePut(cacheNames = "transacao", key="#id")
    public TransacaoDTO estornar(Long id) throws TransacaoInexistenteException {

        try{

            Transacao transacao = (Transacao) Mapper.convert(procurarPeloId(id), Transacao.class);
            transacao.getDescricao().setStatus(StatusEnum.NEGADO);

            descricaoRepository.save(transacao.getDescricao());

            return (TransacaoDTO) Mapper.convert(transacao, TransacaoDTO.class);
        }catch (TransacaoInexistenteException ex){
            throw new TransacaoInexistenteException();
        }

    }

}
