package com.api.pagamento.controller;

import com.api.pagamento.domain.dto.ResponseErrorDTO;
import com.api.pagamento.domain.dto.TransacaoDTO;
import com.api.pagamento.domain.exception.InsercaoNaoPermitidaException;
import com.api.pagamento.domain.exception.TransacaoInexistenteException;
import com.api.pagamento.domain.model.Transacao;
import com.api.pagamento.service.TransacaoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

//@RestController: @Controller + @ResponseBody

//@Controller

//A anotação @Controller é uma anotação usada no framework Spring MVC (o componente do Spring Framework
//usado para implementar o aplicativo da Web). A anotação @Controller indica que uma classe particular serve como
//controlador. A anotação @Controller atua como um estereótipo para a classe anotada, indicando sua função.
//O despachante verifica essas classes anotadas em busca de métodos mapeados e detecta as anotações @RequestMapping.

//@ResponseBody em cada metódo
//A anotação @ResponseBody informa a um controlador que o objeto retornado é serializado automaticamente
//em JSON e passado de volta para o objeto HttpResponse .

@RestController

//@RequestMapping

//A anotação @RequestMapping mapeia uma classe, ou seja, associa uma URI a uma classe. Ao acessar a URL,
//os metódos mapeados da classe podem ser acessados.

@RequestMapping("/transacao/v1")

//@RequiredArgsConstructor
//Gera um construtor com argumentos necessários. Os argumentos obrigatórios são campos finais e campos com restrições como @NonNull.

@RequiredArgsConstructor
public class TransacaoController {
    private final TransacaoService transacaoService;

    //ResponseEntity vs ResponseStatus: https://www.youtube.com/watch?v=D1TiEm956WE

    @ApiOperation(value = "Procura uma transação pelo id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "A transação foi encontrada"),
            @ApiResponse(code = 404, message = "A transação com o id em questão não foi encontrada"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<TransacaoDTO> procurarPeloId(@PathVariable Long id) throws TransacaoInexistenteException {

       return ResponseEntity.ok().body(transacaoService.procurarPeloId(id));

    }

    @ApiOperation(value = "Procura todas as transações")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pelo menos uma transação foi encontrada"),
            @ApiResponse(code = 404, message = "Nenhuma transação foi encontrada"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<TransacaoDTO>> procurarTodos() throws TransacaoInexistenteException {

        return ResponseEntity.ok().body(transacaoService.procurarTodos());

    }

    @ApiOperation(value = "Realiza um pagamento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "O pagamento foi realizado"),
            @ApiResponse(code = 404, message = "O código de autorização, o nsu e o status não podem ser inseridos pelo usuário"),
            @ApiResponse(code = 400, message = "Há campos obrigatórios que não foram preenchidos"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PostMapping(value = "/pagamento", produces = "application/json", consumes = "application/json")
    public ResponseEntity<TransacaoDTO> pagar(@RequestBody @Valid Transacao transacao) throws InsercaoNaoPermitidaException {

        return ResponseEntity.ok().body(transacaoService.pagar(transacao));

    }

    @ApiOperation(value = "Solicita um estorno pelo id da transação")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "O estorno foi realizado"),
            @ApiResponse(code = 404, message = "Nenhuma transação foi encontrada"),
            @ApiResponse(code = 400, message = "Há campos obrigatórios que não foram preenchidos"),
            @ApiResponse(code = 500, message = "Internal Server Error")
    })
    @PutMapping(value = "/estorno/{id}", produces = "application/json")
    public ResponseEntity<TransacaoDTO> estornar(@PathVariable Long id) throws TransacaoInexistenteException {

        return ResponseEntity.ok().body(transacaoService.estornar(id));
    }

    @ExceptionHandler(InsercaoNaoPermitidaException.class)
    public ResponseEntity<ResponseErrorDTO> InsercaoNaoPermitidaException(InsercaoNaoPermitidaException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getResponseError());

    }

    @ExceptionHandler(TransacaoInexistenteException.class)
    public ResponseEntity<ResponseErrorDTO> TranscaoInexistenteException(TransacaoInexistenteException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getResponseError());

    }


}
