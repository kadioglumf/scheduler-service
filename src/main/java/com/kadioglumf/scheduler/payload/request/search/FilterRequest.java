package com.kadioglumf.scheduler.payload.request.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kadioglumf.scheduler.service.search.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FilterRequest implements Serializable {

    private static final long serialVersionUID = 6293344849078612450L;

    @NotBlank
    private String key;

    @NotNull
    private List<ConditionRequest> conditions;

    @NotNull
    private FieldType fieldType;

    @NotNull
    private Operator operator;


    public List<ConditionRequest> getConditions() {
        if (Objects.isNull(this.conditions)) return new ArrayList<>();
        return this.conditions;
    }
}
