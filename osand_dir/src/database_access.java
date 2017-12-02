import java.sql.*;

public class database_access {
    /** database url - should not change*/
    private static final String DATABASE_URL = "jbdc:mysql://localhost:3306/s-l112.engr.uiowa.edu";
    /** username connection*/
    private static final String USER = "engr_class015";
    /** password for our team*/
    private static final String PASS = "engr_class015-xyz";

    public static void main(String[] args){
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            connection = DriverManager.getConnection(DATABASE_URL,USER,PASS);

            statement = connection.createStatement();

            resultSet = statement.executeQuery("");



        }catch(SQLException sqlerr){

        }
    }
}
