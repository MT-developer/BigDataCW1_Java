package tasks;

import objects.item;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.vals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class scrapeScrewfix extends Thread {
    public void run() {
        String query = "power";
        boolean breakCondition = true;
        String itemClassname = "product_box_";
        String xpath = "/html/body/div[8]/div[1]/div/div[2]/div[2]/a[1]";
        ArrayList<WebElement> listing = new ArrayList<WebElement>();

        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("permissions.default.image", 2);
        options.setHeadless(true);
        FirefoxDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver,5);

        driver.navigate().to("https://www.screwfix.com/search?search=power#page_size=100");
        System.out.println("Navigated correctly");
        System.out.println("Checking for cookie message");
        driver.switchTo().frame(driver.findElement(By.xpath("//iframe[@title='TrustArc Cookie Consent Manager']")));

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));

        try {
            WebElement acceptCookieID = driver.findElement(By.xpath(xpath));
            if(driver.findElement(By.xpath(xpath)).isDisplayed()) {
                driver.findElement(By.xpath(xpath)).click();
                System.out.println("Cookie button clicked");
            } else {
                System.out.println("No cookie button");
            }

        } catch (TimeoutException t) {
            System.out.println("Cookie message not found");
        }
        driver.switchTo().defaultContent();

        do {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(itemClassname + "1")));
            System.out.println("Found element");

        try {
            for(int i = 1; i < 21; i++) {
                listing.add(driver.findElement((By.id(itemClassname + i))));
            }
            System.out.println("Found items: " + listing.size());
            if (listing.size() == 0) {
                break;
            }
        } catch (NoSuchElementException e) {
            System.out.println("Finished adding to array");
            breakCondition = false;
        }



            int count = 1;
            for (WebElement item : listing) {

                System.out.println("Scraping for info");

                WebElement priceVatString = item.findElement(By.cssSelector("#product_list_price_" + count));
                WebElement nameString = item.findElement(By.cssSelector("#product_description_" + count));
                String linkString = item.findElement(By.cssSelector("a")).getAttribute("href");
                WebElement imageLink = item.findElement(By.id("product_image"));

                System.out.println("Product name: " + nameString.getText());
                System.out.println("Product price: " + priceVatString.getText());
                System.out.println("Product link: " + linkString);
                System.out.println("Product image: " + imageLink.getAttribute("src"));
                count++;

                int priceInt = Integer.parseInt(priceVatString.getText().replaceAll("[\\D]", ""));
                objects.item tempItem = new item(nameString.getText().substring(nameString.getText().indexOf("\n") + 1), priceInt, imageLink.getAttribute("src"), linkString, "ScrewFix");
                vals.arr_itemList.add(tempItem);

            }
            count = 1;
            System.out.println("Finished scraping, navigating to next page....");
            listing.clear();



            String currentPage = driver.findElement(By.xpath("/html/body/main/div[2]/div/div/div[2]/div/div[1]/div/div/div/span[2]/label")).getText();
            int maxPage = driver.findElement(By.xpath("/html/body/main/div[2]/div/div/div[2]/div/div[1]/div/div/div/span[2]/label")).getText().charAt(10);

            String numbersLine = currentPage.replaceAll("[^0-9]+", " ");

            String[] strArray = numbersLine.split(" ");

            List<Integer> intArrayList = new ArrayList<Integer>();
            System.out.println(currentPage);

            if(strArray[0] == strArray[1]) {
                breakCondition = false;
                vals.set_bool_ScrewFix_true();
            }
            try {
                WebElement nextButtonele = driver.findElement(By.cssSelector(".md-11 > div:nth-child(1) > div:nth-child(1) > span:nth-child(2) > a:nth-child(3)"));
                // WebElement element = driver.findElementByClassName(nextButtonClass);
                driver.executeScript("arguments[0].click();", nextButtonele);
                System.out.println("clicked element");
                Thread.sleep(3500);

            } catch (NoSuchElementException e) {
                System.out.println("No next page found. Finished scraping");
                breakCondition = false;

                vals.set_bool_ScrewFix_true();

            } catch (InterruptedException e) {
                e.printStackTrace();
                vals.set_bool_ScrewFix_true();

            }
        } while(breakCondition);
        vals.set_bool_ScrewFix_true();
        System.out.println("DONE");

    }
}