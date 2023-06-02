package com.fdm.model;

import java.time.LocalDateTime;

/**
 * Class Asset defines an asset data type. Instances of this class are
 * price date and name.
 * 
 * @author Xavier.Maldonado
 */
public class Asset implements Comparable<Asset> {

	private double price;
	private LocalDateTime date;
	private String name;

	/**
	 * Creates an instance of Asset.
	 * 
	 * @param date set instance date
	 * @param price set instance price
	 * @param name set instance name
	 * 
	 */
	public Asset(LocalDateTime date, double price, String name) {
		this.date = date;
		this.price = price;
		this.name = name;
	}

	/**
	 * Retrieves the price of this asset.
	 * 
	 * @return this asset's price.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Sets the price of this asset.
	 * 
	 * @param price for this asset.
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Retrieves the date of this asset.
	 * 
	 * @return this asset's date.
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Sets the date of this asset.
	 * 
	 * @param date is set to this asset.
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * Retrieves the name of this asset.
	 * 
	 * @return this asset's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of this asset.
	 * 
	 * @param name is set to this asset.
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		return ((Asset) obj).getDate().equals(getDate());
	}

	@Override
	public int compareTo(Asset asset) {
		return getDate().compareTo(asset.getDate());
	}

	public String toString() {
		return "Asset Name: " + this.name + ", Price: " + this.price + ", Date: " + this.date + "\n";
	}
}
