package com.algaworks.algafood.api.exceptionhandler;

import com.algaworks.algafood.api.exceptionhandler.enums.ProblemType;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioExpetion;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    public static final String MSG_ERRO_GENERICA_USUARIO_FINAL
            = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<Object> handleEntidadeNaoEncontradaException(EntidadeNaoEncontradaException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADA;
        Problem problem = createProblemBuilder(status, problemType, ex.getMessage()).build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NegocioExpetion.class)
    public ResponseEntity<Object> handleNegocioExpetion(NegocioExpetion ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ProblemType problemType = ProblemType.ERRO_NEGOCIO;
        Problem problem = createProblemBuilder(status, problemType, ex.getMessage())
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(EntidadeEmUsoException.class)
    public ResponseEntity<Object> handleEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ProblemType problemType = ProblemType.ENTIDADE_EM_USO;
        Problem problem = createProblemBuilder(status, problemType, ex.getMessage())
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();

        return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request){
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        ProblemType problemType = ProblemType.ERRO_SISTEMA;

        String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;

        Problem problem = createProblemBuilder(status,problemType,detail).build();
        ex.printStackTrace();
        return handleExceptionInternal(ex,problem,new HttpHeaders(),status,request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (body == null) {
            body = Problem.builder()
                    .timestamp(LocalDateTime.now())
                    .title(status.getReasonPhrase())
                    .status(status.value())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();
        } else if (body instanceof String) {
            body = Problem.builder()
                    .timestamp(LocalDateTime.now())
                    .title((String) body)
                    .status(status.value())
                    .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                    .build();
        }
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADA;

        String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", ex.getRequestURL());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex,problem,headers,status,request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        Throwable rootCause = ExceptionUtils.getRootCause(ex);

        if (rootCause instanceof InvalidFormatException) {
            return handleInvalidFormatException((InvalidFormatException) rootCause, headers, status, request);
        } else if (rootCause instanceof PropertyBindingException) {
            return handlePropertyBindingException((PropertyBindingException) rootCause, headers, status, request);
        }

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex instanceof MethodArgumentTypeMismatchException) {
            return handleMethodArgumentTypeMismatch(
                    (MethodArgumentTypeMismatchException) ex, headers, status, request);
        }

        return super.handleTypeMismatch(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.DADOS_INVALIDOS;

        String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";

        BindingResult bindingResult = ex.getBindingResult();
        List<Problem.Field> fields = bindingResult.getFieldErrors()
                .stream().map(fieldError -> {
                    String message = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                    return Problem.Field.builder()
                            .name(fieldError.getField())
                            .userMessage(message)
                            .build();
                }).collect(Collectors.toList());

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(detail)
                .fields(fields)
                .build();

        return handleExceptionInternal(ex,problem, headers, status, request);
    }

    private ResponseEntity<Object> handleInvalidFormatException(InvalidFormatException ex, HttpHeaders headers,
                                                                HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                path, ex.getValue(), ex.getTargetType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);

    }

    private ResponseEntity<Object> handlePropertyBindingException(PropertyBindingException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        String path = joinPath(ex.getPath());

        ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
        String detail = String.format("A propriedade '%s', não existe." +
                "Corrija ou remova essa propriedade e tente novamente.", path);

        Problem problem = createProblemBuilder(status, problemType, detail)
                .userMessage(MSG_ERRO_GENERICA_USUARIO_FINAL)
                .build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }


    private ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                    HttpHeaders headers, HttpStatus status, WebRequest request) {
        ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

        String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
                        + "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

        Problem problem = createProblemBuilder(status, problemType, detail).build();

        return handleExceptionInternal(ex, problem, headers, status, request);
    }

    private String joinPath(List<JsonMappingException.Reference> references) {
        return references.stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
    }

    private Problem.ProblemBuilder createProblemBuilder(HttpStatus status, ProblemType problemType, String detail) {
        return Problem.builder()
                .status(status.value())
                .type(problemType.getUri())
                .title(problemType.getTitle())
                .detail(detail)
                .timestamp(LocalDateTime.now());

    }
}