package rw.ac.rca.spring_boot_template.mailHandler;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@NoArgsConstructor
public class Mail {
    private String appName;
    private String subject;
    private String fullNames;
    private String toEmail;
    private String template;
    private String data;
    private Object otherData;

    public Mail(String appName, String subject, String fullNames, String toEmail,String data,String template) {
        this.appName = appName;
        this.subject = subject;
        this.fullNames = fullNames;
        this.toEmail = toEmail;
        this.template = template;
        this.data = data;
    }

    public Mail(String username, String courseName, String s) {
        this.appName=username;
        this.subject=courseName;
        this.template=s;

    }
}
