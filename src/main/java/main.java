import objects.item;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import tasks.*;

import java.util.ArrayList;

public class main {
    private static SessionFactory factory;
    private static objects.item item;

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "C:\\GeckDriver\\geckodriver.exe");

        ArrayList<String> arr_HomeHardwareDirect = new ArrayList<String>();
        ArrayList<String> arr_RgcBuildingSupplies = new ArrayList<String>();
        ArrayList<String> arr_ScrewFix = new ArrayList<String>();
        ArrayList<String> arr_Toolstation = new ArrayList<String>();
        ArrayList<String> arr_Wickes = new ArrayList<String>();

        scrapeHomeHardwareDirect homeHardwareDirectThread = new scrapeHomeHardwareDirect();
        scrapeRgcBuildingSupplies rgcBuildingSuppliesThread = new scrapeRgcBuildingSupplies();
        scrapeScrewfix screwfixThread = new scrapeScrewfix();
        scrapeToolstation toolstationThread = new scrapeToolstation();
        scrapeWickes wickesThread = new scrapeWickes();

        wickesThread.start();
        homeHardwareDirectThread.start();
        rgcBuildingSuppliesThread.start();
        screwfixThread.start();
        toolstationThread.start();

        try {
            factory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

        //scrapeAngliaToolCentre.scrape("screw"); // SWITCH TO rgcbuildingsupplies.CO.UK
        main ME = new main();

        /* Add few employee records in database */
        Integer empID1 = ME.addItem("Zara", 100, "test", "tessst");
    }

    /* Method to CREATE an employee in the database */
    public Integer addItem(String item_name, int item_price, String item_image_link ,String item_link){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer employeeID = null;

        try {
            tx = session.beginTransaction();
            item employee = new item(item_name, item_price, item_image_link, item_link);
            employeeID = (Integer) session.save(employee);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return employeeID;
    }
}

