package tasks;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class scrapeScrewfix {
    public static void scrape(String query) {
        boolean breakCondition = true;
        String itemClassname = "product_box_";
        String cookieButtonXpath = "Accept Cookies";
        List<WebElement> listing = null;

        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("permissions.default.image", 2);
        FirefoxDriver driver = new FirefoxDriver(options);

        WebDriverWait wait = new WebDriverWait(driver,5);


        driver.navigate().to("https://www.screwfix.com/search?search=" + query);
        System.out.println("Navigated correctly");
        System.out.println("Checking for cookie message");
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText(cookieButtonXpath)));

            if(driver.findElement(By.xpath(cookieButtonXpath)).isDisplayed()) {
                driver.findElement(By.xpath(cookieButtonXpath)).click();

                System.out.println("Cookie button clicked");
            } else {
                System.out.println("No cookie button");
            }

        } catch (TimeoutException t) {
            System.out.println("Cookie message not found");
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(itemClassname + "1")));
        System.out.println("Found element");

        do {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className(itemClassname)));
            for (int i=0; i < 20; i++) {
                listing.add((WebElement) driver.findElements(By.className(itemClassname + i)));
            }
            System.out.println("Found items: " + listing.size());

            if (listing.size() == 0) {
                break;
            }
            for (WebElement item : listing) {
                System.out.println("Scraping for info");
                WebElement priceString = item.findElement(By.className("lii_price"));
                WebElement priceVatString = item.findElement(By.cssSelector("span.sp-exvat"));
                WebElement nameString = item.findElement(By.className("slii__title"));
                String linkString = item.findElement(By.cssSelector("a")).getAttribute("href");

                System.out.println("Product name: " + nameString.getText().substring(nameString.getText().indexOf("\n")+1));
                System.out.println("Product price(VAT): " + priceString.getText());
                System.out.println("Product price(exVAT): " + priceVatString.getText().substring(8));
                System.out.println("Product link : " + linkString);
            }
            System.out.println("Finished scraping, navigating to next page....");
            try {
                WebElement nextButtonele = driver.findElementById("next_page_link");
                // WebElement element = driver.findElementByClassName(nextButtonClass);
                driver.executeScript("arguments[0].click();", nextButtonele);
                System.out.println("clicked element");
            } catch (NoSuchElementException e) {
                System.out.println("No next page found. Finished scraping");
                breakCondition = false;
            }
        } while(breakCondition);
        System.out.println("DONE");
    }
}