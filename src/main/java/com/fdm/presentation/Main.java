package com.fdm.presentation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.fdm.model.Asset;
import com.fdm.service.VarCalculation;
import com.fdm.utils.Utils;

public class Main {

	public static void main(String[] args) {
		VarCalculation varCalc = new VarCalculation();

		String assetKellog = "Kellog";
		String assetVFINX = "VFINX";
		long kellogInvestmentAmount = 300000;
		long VFINXInvestmentAmount = 700000;

		double[] kellogPrice = { 65.05, 64.39, 64.56, 63.86, 63.98, 63.85, 63.73, 63.61, 63.67 };
		double[] VFINXPrice = { 194.25, 192.05, 193.83, 193.54, 194.53, 195.01, 195.45, 194.64, 195.2 };

		List<Asset> kellogHistoricalData = Utils.createHistoricalData(assetKellog, kellogPrice);
		List<Asset> VFINXHisotricalData = Utils.createHistoricalData(assetVFINX, VFINXPrice);

		Hashtable<String, Long> assetInvestmentAmountHT = new Hashtable<>();

		assetInvestmentAmountHT.put(assetKellog, kellogInvestmentAmount);
		assetInvestmentAmountHT.put(assetVFINX, VFINXInvestmentAmount);

		List<Asset> portfolioHistoricalData = new ArrayList<>();

		LocalDate date = LocalDate.of(2023, 5, 22);

		Asset asset = new Asset(date.atStartOfDay(), 85.00, assetKellog);
		Asset asset2 = new Asset(date.atStartOfDay(), 199.55, assetVFINX);

		portfolioHistoricalData.addAll(VFINXHisotricalData);
		portfolioHistoricalData.addAll(kellogHistoricalData);
		portfolioHistoricalData.add(asset2);
		portfolioHistoricalData.add(asset);


		try {
			double kellogVar = varCalc.getVarForTrade(99.00, kellogHistoricalData, kellogInvestmentAmount);
			double portfolioVar = varCalc.getVarForPortfolio(99.00, portfolioHistoricalData, assetInvestmentAmountHT);
			System.out.println("The VaR for " + assetKellog + " is: " +  kellogVar);
			System.out.println("The VaR for the portfolio is: " + portfolioVar);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
