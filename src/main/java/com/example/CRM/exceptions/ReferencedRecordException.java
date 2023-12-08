package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ReferencedRecordException extends RuntimeException {

    private final transient ApiResponse apiResponse;

    private final HttpStatus status = HttpStatus.CONFLICT;

    public ReferencedRecordException(String recordTable, Object fieldValue, String referenceTable) {
        super();
        this.apiResponse = new ApiResponse(false, String.format("%s record with value '%s' has a reference in %s table", recordTable, fieldValue, referenceTable));
    }
}
