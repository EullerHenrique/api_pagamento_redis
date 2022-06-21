package com.api.pagamento.domain.exception;

import com.api.pagamento.domain.dto.ResponseErrorDTO;

public class TransacaoInexistenteException extends Exception{
    public TransacaoInexistenteException() {}
    public ResponseErrorDTO getResponseError(){
        ResponseErrorDTO rmDTO = new ResponseErrorDTO();
        rmDTO.setStatus(404);
        rmDTO.setError("Not Found");
        rmDTO.setMessage("Transação(ões) inexistente(s)");
        return rmDTO;
    }
}
