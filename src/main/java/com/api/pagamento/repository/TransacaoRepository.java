package com.api.pagamento.repository;

import com.api.pagamento.domain.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;

//JPA
//Java Persistence API (ou simplesmente JPA) é uma API padrão da linguagem Java que descreve uma interface comum
//para frameworks de persistência de dados. A JPA define um meio de mapeamento objeto-relacional para objetos Java
//simples e comuns (POJOs), denominados beans de entidade. Diversos frameworks de mapeamento objeto/relacional como
//o Hibernate implementam a JPA.

//JpaRepository

//É uma extensão específica do repositório JPA. Ele contém a API completa de CrudRepository e
//PagingAndSortingRepository. Portanto, ele contém API para operações CRUD básicas e também API para paginação
//e classificação.

//Hibernate

//O Hibernate é uma ferramenta de mapeamento objeto-relacional (ou ORM) para Java. Basicamente como todo o ORM,
//o Hibernate transforma os dados da estrutura lógica de um banco de dados em objetos relacionais.
//Com a utilização do Hibernate, não há necessidade de escrever SQL “puro”, pois ele utiliza seu próprio código,
//chamado de HQL (Hibernate Query Language).

public interface TransacaoRepository extends JpaRepository<Transacao, Long> { }
