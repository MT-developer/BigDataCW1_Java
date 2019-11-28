package tasks;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class scrapeToolstation {

    public static void scrape(String query) throws InterruptedException {
        boolean breakCondition = true;
        String itemClassname = "ais-hits--item";

        FirefoxDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver,5);

        driver.navigate().to("https://www.toolstation.com/search?q=" + query);
        System.out.println("Navigated correctly");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(itemClassname)));
        System.out.println("Found element");

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



                System.out.println("Product name: " + nameString.getText().substring(nameString.getText().indexOf("\n")+1));
                System.out.println("Product price(VAT): " + priceString.getText());
                System.out.println("Product price(exVAT): " + priceVatString.getText().substring(8));
                System.out.println("Product link : " + linkString);
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
            }
        } while(breakCondition);
        System.out.println("DONE");
    }
}
