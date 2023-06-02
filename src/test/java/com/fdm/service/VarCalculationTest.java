package com.fdm.service;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.junit.jupiter.api.*;
import com.fdm.model.Asset;
import com.fdm.utils.Utils;

class VarCalculationTest {

	VarCalculation varCalc;
	String assetKellog;
	String assetVFINX;

	long kellogInvestmentAmount;
	long VFINXInvestmentAmount;

	double[] kellogPrice;
	double[] VFINXPrice;

	List<Asset> kellogHistoricalData;
	List<Asset> VFINXHisotricalData;
	Hashtable<String, Long> assetInvestmentAmountHT;
	List<Asset> portfolioHistoricalData;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
    @BeforeEach
    public void setUp() throws Exception {
		varCalc = new VarCalculation();
		assetKellog = "Kellog";
		assetVFINX = "VFINX";
		kellogInvestmentAmount = 300000;
		VFINXInvestmentAmount = 700000;

		kellogPrice = new double[] { 65.05, 64.39, 64.56, 63.86, 63.98, 63.85, 63.73, 63.61, 63.67 };
		VFINXPrice = new double[] { 194.25, 192.05, 193.83, 193.54, 194.53, 195.01, 195.45, 194.64, 195.2 };

		kellogHistoricalData = Utils.createHistoricalData(assetKellog, kellogPrice);
		VFINXHisotricalData = Utils.createHistoricalData(assetVFINX, VFINXPrice);

		assetInvestmentAmountHT = new Hashtable<>();
		assetInvestmentAmountHT.put(assetKellog, kellogInvestmentAmount);
		assetInvestmentAmountHT.put(assetVFINX, VFINXInvestmentAmount);

		portfolioHistoricalData = new ArrayList<>();

		portfolioHistoricalData.addAll(VFINXHisotricalData);
		portfolioHistoricalData.addAll(kellogHistoricalData);
    }


	@Test
	void testgetVarForTradeInvalidHistoricalData() {

	    Throwable exception = assertThrows(RuntimeException.class, () -> varCalc.getVarForTrade(99, null, 7000000));
	    assertEquals("Encountered null or invalid input argument.", exception.getMessage());
	}

	@Test
	void testgetVarForTradeInvalidConfidenceLevel() {

	    Throwable exception = assertThrows(RuntimeException.class, () -> varCalc.getVarForTrade(-1, null, 0));
	    assertEquals("Encountered null or invalid input argument.", exception.getMessage());
	}

	@Test
	void testVarForTradeInvalidInvestmentAmount() {

	    Throwable exception = assertThrows(RuntimeException.class, () -> varCalc.getVarForTrade(99, kellogHistoricalData, 0));
	    assertEquals("Encountered null or invalid input argument.", exception.getMessage());
	}


	@Test
	void testVarForTradeInvalidEmptyHistoricalData() {

		List<Asset> emptyHistoricalData = new ArrayList<>();
	    Throwable exception = assertThrows(RuntimeException.class, () -> varCalc.getVarForTrade(99, emptyHistoricalData, 700000));
	    assertEquals("Encountered null or invalid input argument.", exception.getMessage());
	}

	@Test
	void testVarForSingleTrade() {

		double expectedValue = -3271.0;
	    double result = varCalc.getVarForTrade(99, kellogHistoricalData, kellogInvestmentAmount);
	    assertEquals(result, expectedValue);
	}
	
	@Test
	void testVarForSingleTradeWithMoreHistory() {

		LocalDate date = LocalDate.of(2023, 5, 22);

		Asset asset = new Asset(date.atStartOfDay(), 85.00, assetKellog);
		kellogHistoricalData.add(asset);

		double expectedValue = -80249;
	    double result = varCalc.getVarForTrade(99, kellogHistoricalData, kellogInvestmentAmount);
	    assertEquals(result, expectedValue);
	}

	@Test
	void testVarForPortfolioInvalidHistoricalData() {
	    Throwable exception = assertThrows(RuntimeException.class, () -> varCalc.getVarForPortfolio(99, null, assetInvestmentAmountHT));
	    assertEquals("Encountered null or invalid input argument.", exception.getMessage());
	}

	@Test
	void testVarForPortfolioEmptyHistoricalData() {

		List<Asset> emptyHistoricalData = new ArrayList<>();
	    Throwable exception = assertThrows(RuntimeException.class, () -> varCalc.getVarForPortfolio(99, emptyHistoricalData, assetInvestmentAmountHT));
	    assertEquals("Encountered null or invalid input argument.", exception.getMessage());
	}

	@Test
	void testVarForPortfolioInvalidInvestmentAmountHT() {

	    Throwable exception = assertThrows(RuntimeException.class, () -> varCalc.getVarForPortfolio(99, portfolioHistoricalData, null));
	    assertEquals("Encountered null or invalid input argument.", exception.getMessage());
	}

	@Test
	void testVarForPortfolioEmptyInvestmentAmountHT() {

		Hashtable<String, Long> emptyInvestmentAmountHT = new Hashtable<>();

	    Throwable exception = assertThrows(RuntimeException.class, () -> varCalc.getVarForPortfolio(99, portfolioHistoricalData, emptyInvestmentAmountHT));
	    assertEquals("Encountered null or invalid input argument.", exception.getMessage());
	}

	@Test
	void testVarForPortfolio() {

		double expectedValue = -11032.0;
	    double result = varCalc.getVarForPortfolio(99, portfolioHistoricalData, assetInvestmentAmountHT);
	    assertEquals(result, expectedValue);
	}
	
	@Test
	void testVarForPortfolioMoreHistory() {

		LocalDate date = LocalDate.of(2023, 5, 22);
		Asset tempAssetKellog = new Asset(date.atStartOfDay(), 85.00, assetKellog);
		Asset tempAssetVFINX = new Asset(date.atStartOfDay(), 199.55, assetVFINX);

		portfolioHistoricalData.add(tempAssetKellog);
		portfolioHistoricalData.add(tempAssetVFINX);

		double expectedValue = -99092.0;
	    double result = varCalc.getVarForPortfolio(99, portfolioHistoricalData, assetInvestmentAmountHT);
	    assertEquals(result, expectedValue);
	}
}
