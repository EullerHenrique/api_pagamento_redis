package com.api.pagamento.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

//@Data = @Data é uma anotação que gera o código padronizado para classes Java: getters para todos os campos,
//setters para todos os campos não-finais e o toString apropriado, equals e implementações hashCode
//que envolvem os campos da classe.

@Data

//@Entity = A anotação @Entity é utilizada para informar que uma classe também é uma entidade.
//A partir disso, a JPA estabelecerá a ligação entre a entidade e uma tabela de mesmo nome no banco de dados,
//onde os dados de objetos desse tipo poderão ser persistidos.
//Uma entidade representa, na Orientação a Objetos, uma tabela do banco de dados, e cada instância dessa entidade
//representa uma linha dessa tabela.

@Entity

//@Builder = Builder é um padrão de projeto de software criacional que permite a separação da construção de
//um objeto complexo da sua representação, de forma que o mesmo processo de construção possa criar diferentes representações.

@Builder

//@DynamicUpdate
//É uma anotação de nível de classe que pode ser aplicada a uma entidade JPA.
// Ela garante que o Hibernate utilize apenas as colunas modificadas na instrução SQL que gera para a atualização de uma entidade.

@DynamicUpdate

//@NoArgsConstructor = essa anotação é responsável por gerar um construtor sem parâmetros,
//vale ressaltar que se tiver campos final na sua classe deverá usar um atributo force = true em sua anotação.

@NoArgsConstructor

//@AllArgsConstructor = essa anotação é responsável por gerar um construtor com um parâmetro para cada atributo de sua classe.

@AllArgsConstructor

//Obs: A especificação do JPA diz: "A especificação JPA requer que todas as classes persistentes tenham um construtor no-arg.
//Este construtor pode ser público ou protegido. Como o compilador cria automaticamente um construtor no-arg padrão
//quando nenhum outro construtor é definido, apenas as classes que definem os construtores também deve incluir um construtor sem argumentos."

//@Table

//Essa anotação do JPA é usada para adicionar o nome da tabela no banco de dados

@Table(name = "transacao")
public class Transacao implements Serializable {

    //@Id = Id da tabela

    //GeneratedValue(strategy = GenerationType.IDENTITY)

    // A anotação @GeneratedValue é utilizada para informar que a geração do valor do identificador único da entidade
    //será gerenciada pelo provedor de persistência. Essa anotação deve ser adicionada logo após a anotação @Id.
    //Quando não anotamos o campo com essa opção, significa que a responsabilidade de gerar e gerenciar as chaves
    //primárias será da aplicação, em outras palavras, do nosso código.

    //Opções:

    //GenerationType.AUTO = Valor padrão, deixa com o provedor de persistência a escolha da estratégia
    //mais adequada de acordo com o banco de dados.

    //GenerationType.IDENTITY = Informamos ao provedor de persistência que os valores a serem atribuídos ao
    //identificador único serão gerados pela coluna de auto incremento do banco de dados. Assim, um valor para
    //o identificador é gerado para cada registro inserido no banco. Alguns bancos de dados podem não suportar
    //essa opção.

    //GenerationType.SEQUENCE = Informamos ao provedor de persistência que os valores serão gerados a partir
    //de uma sequence. Caso não seja especificado um nome para a sequence, será utilizada uma sequence padrão,
    //a qual será global, para todas as entidades. Caso uma sequence seja especificada, o provedor passará a
    //adotar essa sequence para criação das chaves primárias. Alguns bancos de dados podem não suportar essa opção.

    //GenerationType.TABLE = Com a opção TABLE é necessário criar uma tabela para gerenciar as chaves primárias.
    //Por causa da sobrecarga de consultas necessárias para manter a tabela atualizada, essa opção é pouco recomendada.

    //@SequenceGenerator: https://stackoverflow.com/questions/54866321/hibernate-sequence-for-each-enitity-or-table

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "seq_transacao")
    @SequenceGenerator(name = "seq_transacao", sequenceName = "seq_transacao", allocationSize=1)

    private Long id;

    @NotBlank
    private String cartao;

    //O atributo mappedBy é obrigatório numa associação bidirecional e opcional numa associação unidirecional.
    //O atributo cascade também é opcional, possuindo um membro da enumeração javax.persistence.CascadeType.
    //O atributo targetEntity também é opcional.
    //Por fim, fetch também é opcional permitindo LAZY ou EAGER.

    //LAZY:  Lazy Loading faz com que determinados objetos não sejam carregados do banco até que você precise deles,
    //ou seja, são carregados 'on demand' (apenas quando você solicitar explicitamente o carregamento destes).

    //EAGER: Eager Loading carrega os dados mesmo que você não vá utilizá-los, mas é óbvio que você só utilizará
    //esta técnica se de fato você for precisar com muita frequência dos dados carregados.

    //CascadeType.ALL: propaga todas as operações - incluindo aquelas específicas do Hibernate
    //- de uma entidade pai para uma entidade filha.

    //CascadeType.PERSIST: Propaga a operação de persistência de uma entidade pai para uma entidade filha.

    //CascadeType.MERGE: Propaga a operação de mesclagem de uma entidade pai para uma entidade filha.

    //CascadeType.REMOVE: Propaga a operação de remoção da entidade pai para a entidade filha.
    //Semelhante ao CascadeType.REMOVE do JPA  , temos o CascadeType.DELETE , que é específico do Hibernate.
    //Não há diferença entre os dois.

    //CascadeType.DETACH: a entidade filha também é removida do contexto persistente.

    //CascadeType.LOCK: De forma não intuitiva, CascadeType.LOCK reanexa a entidade e sua entidade
    //filha associada ao contexto persistente novamente.

    //CascadeType.REFRESH: As operações de atualização relêem o valor de uma determinada instância do banco de dados.
    //Em alguns casos, podemos alterar uma instância após persistir no banco de dados, mas depois precisamos desfazer
    //essas alterações.

    //Nesse tipo de cenário, isso pode ser útil. Quando usamos esta operação com Cascade Type REFRESH,
    //a entidade filha também é recarregada do banco de dados sempre que a entidade pai é atualizada.

    //CascadeType.REPLICATE: A operação de replicação é usada quando temos mais de uma fonte de dados e queremos os
    //dados sincronizados.
    //Com CascadeType.REPLICATE , uma operação de sincronização também se propaga para entidades filhas sempre que
    //executada na entidade pai.

    //CascadeType.SAVE_UPDATE: Propaga a mesma operação para a entidade filha associada. É útil quando usamos
    //operações específicas do Hibernate, como salvar , atualizar  e salvarOrUpdate .

    //@NotNull: https://www.baeldung.com/java-bean-validation-not-null-empty-blank

    @Valid
    @NotNull
    @OneToOne(cascade= CascadeType.PERSIST)
    private Descricao descricao;

    @Valid
    @NotNull
    @OneToOne(cascade=CascadeType.PERSIST)
    private FormaPagamento formaPagamento;

}