package tasks;

import objects.item;
import org.hibernate.type.descriptor.sql.SmallIntTypeDescriptor;
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

public class scrapeToolstation extends Thread {

    public void run() {
        String query = "power";
        boolean breakCondition = true;
        String itemClassname = "ais-hits--item";
        FirefoxBinary firefoxBinary = new FirefoxBinary();
        firefoxBinary.addCommandLineOptions("--headless");
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary(firefoxBinary);

        FirefoxDriver driver = new FirefoxDriver(firefoxOptions);
        WebDriverWait wait = new WebDriverWait(driver, 5);

        driver.navigate().to("https://www.toolstation.com/search?q=" + query);
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
                    WebElement priceString = item.findElement(By.cssSelector("span.sp-price"));
                    WebElement priceVatString = item.findElement(By.cssSelector("span.sp-exvat"));
                    WebElement nameString = item.findElement(By.className("search-product-name"));
                    String linkString = item.findElement(By.cssSelector("a")).getAttribute("href");
                    WebElement imageLink = item.findElement(By.className("product-img"));

                    System.out.println("Product name: " + nameString.getText().substring(nameString.getText().indexOf("\n") + 1));
                    System.out.println("Product price(VAT): " + priceString.getText());
                    System.out.println("Product price(exVAT): " + priceVatString.getText().substring(8));
                    System.out.println("Product link : " + linkString);
                    System.out.println("Product image : " + imageLink.getAttribute("src"));

                    int priceInt = Integer.parseInt(priceString.getText().replaceAll("[\\D]", ""));
                    objects.item tempItem = new item(nameString.getText().substring(nameString.getText().indexOf("\n") + 1), priceInt, imageLink.getAttribute("src"), linkString, "Tool Station");

                    vals.arr_itemList.add(tempItem);

                }
                System.out.println("Finished scraping, navigating to next page....");
                try {
                    WebElement nextButton = driver.findElementByLinkText("Next Page");
                    // WebElement element = driver.findElementByClassName(nextButtonClass);
                    driver.executeScript("arguments[0].click();", nextButton);
                    System.out.println("clicked element");
                } catch (NoSuchElementException e) {
                    System.out.println("No next page found. Finished scraping");
                    breakCondition = false;
                    vals.set_bool_Toolstation_true();

                }
            } while (breakCondition);
            System.out.println("DONE");
            vals.set_bool_Toolstation_true();
        } catch (StaleElementReferenceException e) {
            System.out.println("No next page found. Finished scraping page");
            vals.set_bool_Toolstation_true();

        }
    }
}
