package com.fdm.utils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fdm.model.Asset;

public class Utils {

	public static List<Asset> createHistoricalData(String assetName, double[] assetPrice) {

		List<Asset> assetList = new ArrayList<>();
		LocalDate date = LocalDate.of(2023, 5, 31).minusDays(assetPrice.length - 1);

		for (int i = 0; i < assetPrice.length; i++) {
			Asset temp = new Asset(date.plusDays(i).atStartOfDay(), assetPrice[i], assetName);
			assetList.add(temp);
		}

		return assetList;
	}
}
