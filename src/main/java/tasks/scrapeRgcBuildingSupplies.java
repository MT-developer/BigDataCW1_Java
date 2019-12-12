package tasks;

import objects.item;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.vals;

import java.util.List;

public class scrapeRgcBuildingSupplies extends Thread {

    public void run() {
        String query = "power/";
        boolean breakCondition = true;
        String itemClassname = "pg_row";

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);

        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);
        WebDriverWait wait = new WebDriverWait(driver, 5);

        driver.navigate().to("https://www.rgcbuildingsupplies.co.uk/online-store/search/" + query);
        System.out.println("Navigated correctly");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(itemClassname)));
        System.out.println("Found element");
        try {
            do {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(itemClassname)));

                List<WebElement> listing = driver.findElements(By.className(itemClassname));
                System.out.println("Found items: " + listing.size());

                if (listing.size() == 0) {
                    break;
                }

                for (WebElement item : listing) {
                    System.out.println("Scraping for info");
                    WebElement priceString = item.findElement(By.cssSelector("div.pg_row_buy > div.product_price"));
                    WebElement nameString = item.findElement(By.cssSelector("div.pg_row_desc > h3 > a"));
                    String linkString = item.findElement(By.cssSelector("a")).getAttribute("href");
                    WebElement imageLink = item.findElement(By.cssSelector("div.pg_row_image > a > img"));

                    int priceInt;
                    if(priceString.getText().equals("")) {
                        priceInt = 0;
                    } else {
                        priceInt = Integer.parseInt(priceString.getText().replaceAll("[\\D]", ""));
                    }

                    System.out.println("Product name: " + nameString.getText());
                    System.out.println("Product price(VAT): " + priceInt);
                    System.out.println("Product link : " + linkString);
                    System.out.println("Image link: " + imageLink.getAttribute("src"));


                    objects.item tempItem = new item(nameString.getText().substring(nameString.getText().indexOf("\n") + 1), priceInt, imageLink.getAttribute("src"), linkString, "RGC Building supplies");
                    vals.arr_itemList.add(tempItem);


                }
                System.out.println("Finished scraping, navigating to next page....");

                try {
                    WebElement nextButton = driver.findElementByLinkText("Next Page >");
                    // WebElement element = driver.findElementByClassName(nextButtonClass);
                    driver.executeScript("arguments[0].click();", nextButton);
                    System.out.println("clicked element");
                    Thread.sleep(4000);
                } catch (NoSuchElementException e) {
                    System.out.println("No next page found. Finished scraping");
                    vals.set_bool_RgcBuildingSupplies_true();

                    breakCondition = false;
                    vals.set_bool_RgcBuildingSupplies_true();

                }
            } while (breakCondition);
            vals.set_bool_RgcBuildingSupplies_true();
            System.out.println("DONE");
        } catch (StaleElementReferenceException e) {
            System.out.println("No next page found. Finished scraping page");
            vals.set_bool_RgcBuildingSupplies_true();

        } catch (InterruptedException e) {
            e.printStackTrace();
            vals.set_bool_RgcBuildingSupplies_true();

        }
    }
}
