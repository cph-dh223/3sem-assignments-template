package school.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class StudentInfo {
    String fullName;
    int studentId;
    String thisSemesterName;
    String thisSemesterDescription;
}
