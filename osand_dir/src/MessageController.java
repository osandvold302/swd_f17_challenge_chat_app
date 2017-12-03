public class MessageController extends GeneralController {


    public static void initMessages(){
        getClient().requestMessageHistory(getClient().getCurrentChannel());
        
    }
}
