package com.kadioglumf.scheduler.payload.request.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kadioglumf.scheduler.service.search.FieldType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SearchRequest implements Serializable {

    private static final long serialVersionUID = 8514625832019794838L;

    private Operator operator;

    private List<FilterRequest> filters;

    private List<SortRequest> sorts;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

    public List<FilterRequest> getFilters() {
        if (Objects.isNull(this.filters)) return new ArrayList<>();
        return this.filters;
    }

    public List<SortRequest> getSorts() {
        if (Objects.isNull(this.sorts)) return new ArrayList<>();
        return this.sorts;
    }

    public void addFilterByParameter(@NonNull String key, @NonNull List<ConditionRequest> conditionRequestList, @NonNull FieldType fieldType, @Nullable Operator operator) {
        FilterRequest filter = new FilterRequest();

        filter.setKey(key);
        filter.setConditions(conditionRequestList);
        filter.setFieldType(fieldType);
        filter.setOperator(operator);

        if (Objects.isNull(this.filters)) {
            this.filters = new ArrayList<>();
        }
        filters.add(filter);
    }
}
