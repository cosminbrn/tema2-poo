package main.fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class FiltersInput {
    private String searchType;
    private String expertiseArea;
    private double performanceScoreAbove;
    private double performanceScoreBelow;
    private String seniority;
    private String businessPriority;
    private String type;
    private String createdAfter;
    private String[] keywords;
    private String createdBefore;
    private boolean availableForAssignment;
}

