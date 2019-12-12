import objects.item;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import tasks.*;
import utils.vals;

import java.util.ArrayList;

public class main {
    private static SessionFactory factory;
    private static objects.item item;
    private static boolean started = false;


    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "C:\\GeckDriver\\geckodriver.exe");


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
        main ME = new main();

        while (!started) {
            System.out.println("Waiting for scrapers to finish");
            Thread.sleep(4000);

            System.out.println("Status:");
            System.out.println("arr_HomeHardwareDirect   : " + vals.bool_HomeHardwareDirect);
            System.out.println("arr_RgcBuildingSupplies  : " + vals.bool_RgcBuildingSupplies);
            System.out.println("arr_ScrewFix             : " + vals.bool_ScrewFix);
            System.out.println("arr_Toolstation          : " + vals.bool_Toolstation);
            System.out.println("arr_Wickes               : " + vals.bool_Wickes);

                if(vals.bool_Wickes && vals.bool_HomeHardwareDirect && vals.bool_RgcBuildingSupplies && vals.bool_ScrewFix && vals.bool_Toolstation) {
                    for(objects.item item : vals.arr_itemList) {
                        ME.addItem(item);
                        System.out.println("Object added: " + item.getItemName());
                    }
                    started = true;
                }
            }



        //scrapeAngliaToolCentre.scrape("screw"); // SWITCH TO rgcbuildingsupplies.CO.UK
        /* Add few employee records in database */
        //Integer empID1 = ME.addItem("Zara", 100, "test", "tessst");
    }

    /* Method to CREATE an employee in the database */
    public Integer addItem(item item){
        Session session = factory.openSession();
        Transaction tx = null;
        Integer itemID = null;

        try {
            tx = session.beginTransaction();
            itemID = (Integer) session.save(item);
            tx.commit();
        } catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return itemID;
    }
}

