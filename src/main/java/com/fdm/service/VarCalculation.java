package com.fdm.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.math3.stat.descriptive.rank.*;

import com.fdm.model.Asset;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Class VarCalculation calculates VaR for a single trade or a portfolio
 * performed on an input of historical data, confidence level and investment amount.
 * 
 * 
 * @author Xavier.Maldonado
 */
public class VarCalculation {

	private Percentile stat;

	/**
	 * Initialize VarCalculation with an empty constructor 
	 * 
	 */
	public VarCalculation() {
		stat = new Percentile();
	}

	/**
	 * Calculates VaR for a single trade.
	 * 
	 * @param confidenceLevel trade confidence level
	 * @param historicalData trade historical data
	 * @param investmentAmount trade investment amount
	 * 
	 * @return VaLue At Risk
	 * @throws IllegalArgumentException
	 * 	If confidence level, investment amount, historical data  is invalid
	 *             
	 */
	public double getVarForTrade(double confidenceLevel, List<Asset> historicalData, long investmentAmount) {

		if (!(historicalData != null && !historicalData.isEmpty() && confidenceLevel > 0 && confidenceLevel < 100 && investmentAmount > 0)) {
			RuntimeException e = new IllegalArgumentException("Encountered null or invalid input argument.");
			throw e;
		}

		double desiredPercentile = 100 - confidenceLevel;
		double[] dailyReturnValue = new double[historicalData.size() - 1];
		double valueAtRisk = 0;

		ArrayList<Asset> sortedData = historicalData.stream().sorted(Comparator.reverseOrder())
				.collect(Collectors.toCollection(ArrayList::new));

		for (int i = 0; i < sortedData.size() - 1; i++) {

			double price = sortedData.get(i).getPrice();
			double nextPrice = sortedData.get(i + 1).getPrice();

			double percentReturn = Math.log(price / nextPrice);
			dailyReturnValue[i] = Math.round(percentReturn * investmentAmount);
		}

		stat.setData(dailyReturnValue);
		valueAtRisk = stat.evaluate(desiredPercentile);

		return valueAtRisk;
	}

	/**
	 * Calculates VaR for a portfolio.
	 * 
	 * @param confidenceLevel portfolio confidence level
	 * @param historicalData portfolio historical data 
	 * @param assetInvestmentAmountHT asset's investment amount
	 * 
	 * @return VaLue At Risk
	 * @throws IllegalArgumentException
	 * 	If confidence level, asset investment amount, historical data  is invalid
	 *             
	 */
	public double getVarForPortfolio(double confidenceLevel, List<Asset> historicalData,
		Hashtable<String, Long> assetInvestmentAmountHT) {

		if (!(historicalData != null && !historicalData.isEmpty() && confidenceLevel > 0 && confidenceLevel < 100
				&& assetInvestmentAmountHT != null && !assetInvestmentAmountHT.isEmpty())) {
			RuntimeException e = new IllegalArgumentException("Encountered null or invalid input argument.");
			throw e;
		}

		long investmentAmount = 0;
		double valueAtRisk = 0;
		double desiredPercentile = 100 - confidenceLevel;

		List<Asset> groupedSortedData = historicalData.stream().sorted(Comparator.reverseOrder())
				.collect(Collectors.groupingBy(Asset::getName, Collectors.toList())).values().stream()
				.flatMap(Collection::stream).collect(Collectors.toList());

		Hashtable<LocalDateTime, Double> portfolioDailyReturnHT = new Hashtable<>();

		for (int i = 0; i < groupedSortedData.size() - 1; i++) {

			Asset asset = groupedSortedData.get(i);
			Asset nextAsset = groupedSortedData.get(i + 1);
			if (i == 0) {
				investmentAmount = getInvestmentAmount(assetInvestmentAmountHT, asset.getName());
			}

			String assetName = asset.getName();
			String nextAssetName = nextAsset.getName();

			if (!assetName.equals(nextAssetName)) {
				investmentAmount = getInvestmentAmount(assetInvestmentAmountHT, nextAssetName);
				continue;
			}

			double price = asset.getPrice();
			double nextPrice = nextAsset.getPrice();
			double percentReturn = Math.log(price / nextPrice);

			if (!portfolioDailyReturnHT.containsKey(asset.getDate())) {
				portfolioDailyReturnHT.put(asset.getDate(), 0.0);
			}
			double returnValue = Math.round(percentReturn * investmentAmount);
			double value = portfolioDailyReturnHT.get(asset.getDate());

			portfolioDailyReturnHT.put(asset.getDate(), value + returnValue);

		}

		double[] dailyReturnValue = ArrayUtils
				.toPrimitive(portfolioDailyReturnHT.values().toArray(new Double[portfolioDailyReturnHT.size()]));
		stat.setData(dailyReturnValue);
		System.out.println(Arrays.toString(dailyReturnValue));
		System.out.println(stat.evaluate(desiredPercentile));
		valueAtRisk = stat.evaluate(desiredPercentile);

		return valueAtRisk;
	}

	private long getInvestmentAmount(Hashtable<String, Long> assetInvestmentAmountHT, String name)  {

		long amount;
		if (assetInvestmentAmountHT.containsKey(name)) {
			amount = assetInvestmentAmountHT.get(name);
		} else {
			RuntimeException e = new IllegalArgumentException(name + " Does not Have an Invesment Amount");
			throw e;
		}

		return amount;
	}

}
