package proglang;
import java.util.Map;
public final class SequentialProgram {
    private final Stmt field;
    SequentialProgram(Stmt f){
      this.field = f;
    }
    Map<String,Integer> execute(Map<String,Integer> initStore){
        Map<String,Integer> workingstore = initStore;
        Stmt current=field;
        while(current!=null)
            current= StmtKt.step(current,workingstore);
        return workingstore;
    }
    @Override
    public String toString(){
        return field.toString();
    }
}
