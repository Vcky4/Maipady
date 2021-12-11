package com.example.maipady.models

object DummyData {
    val results = listOf(
        TableItems("", "MTH111","A", "4"),
        TableItems("", "PHY111","B", "3"),
        TableItems("", "CHEM111","D", "1"),
        TableItems("", "PHY112","c", "1"),
        TableItems("", "CHEM111","B", "3")
    )

    private val results2 = listOf(
        TableItems("", "MTH121","A", "4"),
        TableItems("", "PHY121","B", "3"),
        TableItems("", "CHEM121","D", "1"),
        TableItems("", "PHY122","c", "1"),
        TableItems("", "CHEM121","B", "3")
    )

    private val results3 = listOf(
        TableItems("", "GRE211","A", "3"),
        TableItems("", "MEE211","B", "2"),
        TableItems("", "GRE213","D", "1"),
        TableItems("", "MEE212","C", "2"),
        TableItems("", "ELE211","B", "2")
    )

    private val results4 = listOf(
        TableItems("", "GRE221","A", "3"),
        TableItems("", "MEE221","B", "2"),
        TableItems("", "GRE223","D", "1"),
        TableItems("", "MEE222","C", "2"),
        TableItems("", "ELE221","B", "2")
    )

    private val results5 = listOf(
        TableItems("", "GRE311","A", "3"),
        TableItems("", "ELE311","B", "2"),
        TableItems("", "GRE313","D", "2"),
        TableItems("", "ELE312","C", "2"),
        TableItems("", "ELE313","B", "2")
    )

    val listR = listOf(
        Results("First semester", "100", "4.35", results),
        Results("Second semester", "100", "3.50", results2),
        Results("First semester", "200", "3.00", results3),
        Results("Second semester", "200", "4.50", results4),
        Results("First semester", "300", "4.00", results5)
    )
}