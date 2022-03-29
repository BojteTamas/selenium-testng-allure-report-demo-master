package test;


import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import page.HomePage;
import util.ItemType;

import java.util.List;

import static util.ItemType.WITHOUT_DISCOUNT;
import static util.ItemType.WITH_DISCOUNT;
import static util.driver.DriverHolder.getDriver;


public class CollectDataTest extends BaseTest {

  private HomePage homePage;


  @DataProvider(name = "data-provider")
  public Object[][] dpMethod () {

    return new Object[][]{
        {40, "Preis aufsteigend", WITH_DISCOUNT},
        {50, "Preis absteigend", WITHOUT_DISCOUNT}
    };
  }


  @BeforeMethod
  public void searchBeforeMethod () {

    homePage = new HomePage(getDriver());
  }


  @Test(dataProvider = "data-provider")
  public void testSearchResultCount (int resultsPerPage, String sortOption, ItemType itemType) {

    homePage.acceptCookies();
    homePage.selectResultPerPage(resultsPerPage);
    homePage.selectSortOption(sortOption);
    List items = homePage.collectItems(itemType);
    System.out.println(items);
  }
}
