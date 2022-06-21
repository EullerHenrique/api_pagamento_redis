package com.api.pagamento.domain.dto;

import com.api.pagamento.domain.enumeration.TipoEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

//@Data = @Data é uma anotação que gera o código padronizado para classes Java: getters para todos os campos,
//setters para todos os campos não-finais e o toString apropriado, equals e implementações hashCode
//que envolvem os campos da classe.

@Data

//@Builder = Builder é um padrão de projeto de software criacional que permite a separação da construção de
//um objeto complexo da sua representação, de forma que o mesmo processo de construção possa criar diferentes representações.

@Builder

// @AllArgsConstructor = essa anotação é responsável por gerar um construtor com um parâmetro para cada atributo de sua classe.

@AllArgsConstructor

//@NoArgsConstructor = essa anotação é responsável por gerar um construtor sem parâmetros,
//vale ressaltar que se tiver campos final na sua classe deverá usar um atributo force = true em sua anotação.

@NoArgsConstructor

//Obs: A especificação do JPA diz: "A especificação JPA requer que todas as classes persistentes tenham um construtor no-arg.
//Este construtor pode ser público ou protegido. Como o compilador cria automaticamente um construtor no-arg padrão
//quando nenhum outro construtor é definido, apenas as classes que definem os construtores também deve incluir um construtor sem argumentos."

/*
    DTO
    Objeto de Transferência de Dados (do inglês, Data transfer object, ou simplesmente DTO), é um padrão de projeto
    de software usado para transferir dados entre subsistemas de um software. DTOs são frequentemente usados em
    conjunção com objetos de acesso a dados para obter dados de um banco de dados.
    A diferença entre objetos de transferência de dados e objetos de negócio ou objetos de acesso a dados é que um DTO
    não possui comportamento algum, exceto o de armazenamento e obtenção de seus próprios dados. DTOs são objetos
    simples que não contêm qualquer lógica de negócio que requeira testes…
 */

public class FormaPagamentoDTO  implements Serializable {

    @JsonIgnore
    private Long id;
    private TipoEnum tipo;
    private String parcelas;

}
