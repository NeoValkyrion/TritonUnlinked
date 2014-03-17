package triton.unlinked;

/**
 * Created by Grim on 3/16/14.
 */
public class ProfessorObject {

    private String code;
    private String title;
    private String section;

    public ProfessorObject(String title, String code, String section){
        this.code = code;
        this.title = title;
        this.section = section;
    }
    public String getCode(){
        return this.code;
    }
    public String getTitle(){  return this.title; }
    public String getSection(){ return this.section; }

    public void setCode(String newCode){ this.code = newCode; }
    public void setTitle(String newTitle){ this.title = newTitle; }
    public void setSection(String newSection){ this.section = newSection; }

}


