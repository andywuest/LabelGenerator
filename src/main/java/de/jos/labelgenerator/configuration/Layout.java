package de.jos.labelgenerator.configuration;

import java.util.Properties;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Layout {

	private String name = "undefined";

	private Integer rows = null;
	private Integer columns = null;

	private Double width = null;
	private Double height = null;

	private Double marginTop = null;
	private Double marginLeft = null;

	private Double widthNext = null;
	private Double heightNext = null;

	private Double textMarginTop = null;
	private Double textMarginLeft = null;
	private Double textMargin = null;

	private String fontName = null;
	private Float fontSize = null;

	public Layout() {

	}

	public Layout(Properties properties) {
		this.name = properties.getProperty("layout.name");
		this.rows = Integer.valueOf(properties.getProperty("layout.rows"));
		this.columns = Integer.valueOf(properties.getProperty("layout.columns"));
		this.width = Double.valueOf(properties.getProperty("layout.width"));
		this.height = Double.valueOf(properties.getProperty("layout.height"));

		this.marginTop = Double.valueOf(properties.getProperty("layout.marginTop"));
		this.marginLeft = Double.valueOf(properties.getProperty("layout.marginLeft"));

		this.widthNext = Double.valueOf(properties.getProperty("layout.widthNext"));
		this.heightNext = Double.valueOf(properties.getProperty("layout.heightNext"));

		this.textMarginTop = Double.valueOf(properties.getProperty("layout.text.marginTop"));
		this.textMarginLeft = Double.valueOf(properties.getProperty("layout.text.marginLeft"));
		this.textMargin = Double.valueOf(properties.getProperty("layout.text.margin"));

		this.fontName = properties.getProperty("layout.font.name");
		this.fontSize = Float.valueOf(properties.getProperty("layout.font.size"));
	}

	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

	public String getName() {
		return name;
	}

	public Integer getRows() {
		return rows;
	}

	public Integer getColumns() {
		return columns;
	}

	public Double getWidth() {
		return width;
	}

	public Double getHeight() {
		return height;
	}

	public Double getMarginTop() {
		return marginTop;
	}

	public Double getMarginLeft() {
		return marginLeft;
	}

	public Double getWidthNext() {
		return widthNext;
	}

	public Double getHeightNext() {
		return heightNext;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public void setColumns(Integer columns) {
		this.columns = columns;
	}

	public void setWidth(Double width) {
		this.width = width;
	}

	public void setHeight(Double height) {
		this.height = height;
	}

	public void setMarginTop(Double marginTop) {
		this.marginTop = marginTop;
	}

	public void setMarginLeft(Double marginLeft) {
		this.marginLeft = marginLeft;
	}

	public void setWidthNext(Double widthNext) {
		this.widthNext = widthNext;
	}

	public void setHeightNext(Double heightNext) {
		this.heightNext = heightNext;
	}

	public Double getTextMarginTop() {
		return textMarginTop;
	}

	public void setTextMarginTop(Double textMarginTop) {
		this.textMarginTop = textMarginTop;
	}

	public Double getTextMarginLeft() {
		return textMarginLeft;
	}

	public void setTextMarginLeft(Double textMarginLeft) {
		this.textMarginLeft = textMarginLeft;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public Float getFontSize() {
		return fontSize;
	}

	public void setFontSize(Float fontSize) {
		this.fontSize = fontSize;
	}

	public Double getTextMargin() {
		return textMargin;
	}

	public void setTextMargin(Double textMargin) {
		this.textMargin = textMargin;
	}

}
