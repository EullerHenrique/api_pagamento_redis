package com.api.pagamento.builder;

import com.api.pagamento.domain.dto.FormaPagamentoDTO;
import com.api.pagamento.domain.enumeration.TipoEnum;
import lombok.Builder;

@Builder
public class FormaPagamentoDTOBuilder {

    @Builder.Default
    private static Long id = 1L;

    @Builder.Default
    private static TipoEnum tipo = TipoEnum.AVISTA;

    @Builder.Default
    private static String parcelas = "1";

    public static FormaPagamentoDTO toFormaPagamento() {
        return new FormaPagamentoDTO(id, tipo, parcelas);
    }

}
