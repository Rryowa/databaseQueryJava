import java.util.List;
import java.util.Map;

public class Main {

   public static void main(String... args){
        JavaSchoolStarter starter = new JavaSchoolStarter();
        
        List<Map<String,Object>> result0 = starter.execute("INSERT VALUES 'id'=1, 'lastName' = ' Петров ', 'age'=30, ' cost'=5.4, 'active'=true");
        List<Map<String,Object>> result1 = starter.execute("INSERT VALUES 'id'=2, 'lastName' = 'Иванов', 'age'=25, ' cost'=4.3, 'active'=false");
        List<Map<String,Object>> result2 = starter.execute("INSERT VALUES 'id'=3, 'lastName' = 'Федоров', 'age'=40, 'active'=true");
//        List<Map<String,Object>> result3 = starter.execute("UPDATE VALUES 'active'=false, 'cost'=10.1 where 'id'=3");
//        List<Map<String,Object>> result4 = starter.execute("UPDATE VALUES 'active'=true  where 'active'=false");
        List<Map<String,Object>> result5 = starter.execute("SELECT WHERE 'age'>=30 and 'age'<40");
        System.out.println("\nResult: ");

        for (Map<String, Object> map : result2) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.print(entry.getKey() + ":" + entry.getValue().toString() + ", ");
            }
            System.out.println();
        }
        
        
        
   }
}
