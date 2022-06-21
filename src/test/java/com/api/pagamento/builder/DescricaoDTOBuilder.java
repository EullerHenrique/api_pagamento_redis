package com.api.pagamento.builder;


import com.api.pagamento.domain.dto.DescricaoDTO;
import com.api.pagamento.domain.enumeration.StatusEnum;
import lombok.Builder;

@Builder
public class DescricaoDTOBuilder {

    @Builder.Default
    private static Long id = 1L;

    @Builder.Default
    private static String valor = "500.50";

    @Builder.Default
    private static String dataHora = "01/05/2021 18:00:00";

    @Builder.Default
    private static String estabelecimento = "PetShop Mundo cão";

    @Builder.Default
    private static String nsu = null;

    @Builder.Default
    private static String codigoAutorizacao = null;

    @Builder.Default
    private static StatusEnum status = null;

    public static DescricaoDTO toDescricaoDTO() {
        return new DescricaoDTO(id, valor, dataHora, estabelecimento, nsu, codigoAutorizacao, status);
    }

}
