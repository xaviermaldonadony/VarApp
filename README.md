# VarApp
This is the solution for Calculating Var. For either a trade or a portfolio using historical data.


### Getting Started

1. Open the project in a code editor and do a maven update
    * Eclipse - Right click on the project > Select Maven > Update Project

2. Open up the src/main/java/com.fdmpresentation folder

3. Run the Main.java file


### JUnit Test
  Right Click on project folder select Run as followed by JUnit Test.

### VaRApp Description
  Main class creates historical data for an asset. Asset name is Kellog.
	The historical data is created by an Utils class.
	VaR is calculated by this asset using method getVarForTrade.
	Method requires: confidence level, historical data and assets investment amount

	Logic, method sorts trades by date (Desc) calculates Percent Return using the previous trade price.
	Return Value for the asset is calculated with Percent return for that day multiply by asset's investment amount, for each day.
	VaR is the result of taking the Percentile of the return for each day and using 1 - confidence level as the thresh hold.


  To calculate for a portfolio, create historical data for asset named VFINX.
  Create Portfolio by adding the two historical data in the portfolio.

	Logic, Similar to calc for trade.
	Method requires: confidence level, historical data and assets investment amount for each asset.
	 The only difference is the historical data is  grouped by asset's Name and sorted by date.
	The return value is the addition of all the  assets for that day.
	Var is calculated as getVarForTrade method.

