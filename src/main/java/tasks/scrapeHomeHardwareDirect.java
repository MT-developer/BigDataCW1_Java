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

public class scrapeHomeHardwareDirect extends Thread {

    public void run() {
        String query = "power";
        boolean breakCondition = true;
        String itemClassname = "Tile";

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);

        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);
        WebDriverWait wait = new WebDriverWait(driver, 5);

        driver.navigate().to("https://homehardwaredirect.co.uk/Products?SearchTxt=" + query);
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
                    WebElement priceString = item.findElement(By.className("pPrice"));

                    String nameString = item.findElement(By.cssSelector("a.thumb")).getAttribute("title");
                    String linkString = item.findElement(By.cssSelector("a")).getAttribute("href");
                    WebElement imageLink = item.findElement(By.cssSelector("a.thumb > div"));

                    String imageString = imageLink.getCssValue("background-image");

                    System.out.println("Product name: " + nameString);
                    System.out.println("Product price(VAT): " + priceString.getText());
                    System.out.println("Product link : " + linkString);
                    System.out.println("Product image : " + imageString);

                    int priceInt = Integer.parseInt(priceString.getText().replaceAll("[\\D]", ""));
                    objects.item tempItem = new item(nameString, priceInt, imageString,linkString, "HomeHardwareDirect");

                    vals.arr_itemList.add(tempItem);
                }
                System.out.println("Finished scraping, navigating to next page....");

                try {
                    WebElement nextButton = driver.findElementByLinkText("Next");
                    // WebElement element = driver.findElementByClassName(nextButtonClass);
                    driver.executeScript("arguments[0].click();", nextButton);
                    System.out.println("clicked element");
                    Thread.sleep(4000);
                } catch (NoSuchElementException e) {
                    System.out.println("No next page found. Finished scraping");
                    breakCondition = false;
                    vals.set_bool_HomeHardwareDirect_true();

                }
            } while (breakCondition);
            vals.set_bool_HomeHardwareDirect_true();
            System.out.println("DONE");
        } catch (StaleElementReferenceException e) {
            System.out.println("No next page found. Finished scraping page");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
