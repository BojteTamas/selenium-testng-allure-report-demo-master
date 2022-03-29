package util;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.awaitility.core.ConditionTimeoutException;
import org.awaitility.core.ThrowingRunnable;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;
import static org.testng.AssertJUnit.fail;


@Slf4j
public class AssertHelpers {

  private static final String PROCESS_ID =
      String.valueOf(ManagementFactory.getRuntimeMXBean().getPid());

  public static final String PROCESS_ID_STRING = String.format("[PROCESS_ID:%s]", PROCESS_ID);


  @SneakyThrows
  public void assertWithPoll (ThrowingRunnable throwingRunnable, WebDriver driver, int seconds) {

    try {
      await()
          .pollInterval(fibonacci(TimeUnit.MILLISECONDS))
          .ignoreExceptions()
          .catchUncaughtExceptions()
          .timeout(Duration.ofSeconds(seconds))
          .untilAsserted(throwingRunnable);
    } catch (ConditionTimeoutException e) {
      log.error(e.getMessage());
      log.error(Arrays.toString(e.getStackTrace()));
      File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      String projectDir = System.getProperty("user.dir");
      File destFileName =
          new File(
              projectDir + File.separator + "screenshots/" + System.currentTimeMillis() + ".jpg");
      log.error("{} screenshot with name: {}", PROCESS_ID_STRING, destFileName.getName());
      FileUtils.copyFile(srcFile, destFileName);
      fail(e.getCause().getLocalizedMessage());
    }
  }


  public void assertWithPollThirtySeconds (WebDriver driver, ThrowingRunnable throwingRunnable) {

    assertWithPoll(throwingRunnable, driver, 30);
  }

}
