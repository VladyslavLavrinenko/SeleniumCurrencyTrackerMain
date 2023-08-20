import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.testng.annotations.AfterClass;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;





public class Main {

    private WebDriver driver;
    private ExtentReports extentReports;
    private ExtentTest extentTest;


    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Pamadmin\\Desktop\\Pop\\chromedriver_win32\\chromedriver.exe");
        driver = new ChromeDriver();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("C:\\Users\\Pamadmin\\Desktop\\SparkReport" + timeStamp + ".html");

        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);


    }

    @Test
    public void testExchangeRate() {


        String currency = "USD"; // EUR CAD
        String dateEx = "20210819"; // Дата курсу НБУ
        String site = "https://minfin.com.ua/currency/nbu/";

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Pamadmin\\Desktop\\Pop\\chromedriver_win32\\chromedriver.exe");
        driver.get(site);

        WebElement element = driver.findElement(By.cssSelector(".sc-1x32wa2-9.fevpFL"));
        String currencyText = element.getText().split("\n")[0].replace(",", ".");
        double webExchangeRate = Double.parseDouble(currencyText);
        System.out.println("Курс " + currency + ": " + webExchangeRate + " з сайту " + site);


        String apiUrl = "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?valcode=" + currency + "&date=" + dateEx + "&json";
        Response response = RestAssured.get(apiUrl);
        double apiExchangeRate = response.jsonPath().getDouble("[0].rate");
        System.out.println("Курс " + currency + " З сайту НБУ " + apiExchangeRate);

        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        driver.quit();
        extentTest = extentReports.createTest("Test Exchange Rate");
        if (Math.abs(webExchangeRate - apiExchangeRate) < 0.01) {
            System.out.println("Курс USD однакові.");
            extentTest.log(Status.PASS, "Курс USD однакові." + " Це " + webExchangeRate + " за обрану дату " + dateEx);
        } else {
            System.out.println("Курс USD не збігаеться");
            extentTest.log(Status.FAIL, "Курс USD не збігаеться" + " з сайтом " + site + " курс на сайті " + webExchangeRate  + " за обрану дату " + dateEx );
        }
    }


    @AfterClass
    public void tearDown() {
        extentReports.flush();
        if (driver != null) {
            driver.quit();
        }
    }

}

