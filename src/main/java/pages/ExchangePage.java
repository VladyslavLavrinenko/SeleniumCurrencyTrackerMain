package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ExchangePage {

    private WebDriver driver;
    private final By exchangeRateSpan = By.tagName("span");

    public ExchangePage(WebDriver driver) {
        this.driver = driver;
    }

    public double getExchangeRateFromWebsite() {
        WebElement exchangeRateElement = driver.findElement(exchangeRateSpan);
        String exchangeRateText = exchangeRateElement.getText();
        return Double.parseDouble(exchangeRateText);
    }
}
