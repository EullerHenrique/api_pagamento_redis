package com.api.pagamento.repository;

import com.api.pagamento.domain.model.Descricao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescricaoRepository extends JpaRepository<Descricao, Long> { }
