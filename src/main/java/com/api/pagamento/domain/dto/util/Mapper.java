package com.api.pagamento.domain.dto.util;

import org.modelmapper.ModelMapper;

public class Mapper {

    public static Object convert(Object origem, Class<?> destino) {

        return new ModelMapper().map(origem, destino);
    }

}
