package az.orient.bankdemoboot.schedule;

public class MyThread extends Thread{

    @Override
    public void run() {
       try {
           while (true) {
               getAllCustomer();
               Thread.sleep(3000);
           }
       } catch (Exception ex) {
           ex.printStackTrace();
       }
    }

    private void getAllCustomer() {
        System.out.println("Hello Qrup57!!!");
    }
}
