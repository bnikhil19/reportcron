package com.igp.reports.models;

import java.util.Arrays;
import java.util.List;

public class Row
{
	List<String> colList;
	//highlight color

	public Row(){}

	public Row(List<String> colList)
	{
		this.colList = colList;
	}

	public List<String> getColList()
	{
		return colList;
	}

	public void setColList(List<String> colList)
	{
		this.colList = colList;
	}
	public void setColList(String... values)
	{
		this.colList = Arrays.asList(values);
	}
}
