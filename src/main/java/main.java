import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import tasks.*;

import java.io.IOException;

public class main implements Runnable{

    public static void main(String[] args) throws InterruptedException  {
        // scrapeAngliaToolCentre.scrape("screw");
        scrapeToolstation.scrape("test");
        // scrapeScrewfix.scrape("test");
    }

    public void run() {

    }
}