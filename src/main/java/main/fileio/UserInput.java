package main.fileio;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserInput {
    private String username;
    private String role;
    private String email;
    private String hireDate;
    private String seniority;
    private String expertiseArea;
    private String[] subordinates;
}
