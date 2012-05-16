<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<xsl:output method="xml" encoding="UTF-8" indent="yes" />

<!-- will convert dbunit new format export to old format -->
<!-- in:
<dataset>
	<table name="web_application">
		<column>CREDIT_APPLICATION_ID</column>
		<column>WEB_FIRSTNAME</column>
		<row>
			<value>1</value>
			<null/>
		</row>
-->
<!-- out:
<dataset>
  <web_application CREDIT_APPLICATION_ID="1" />
  ...
-->
<xsl:template match="dataset" >
	<dataset>
		<xsl:apply-templates select="table" />
	</dataset>
</xsl:template>

<xsl:template match="table" >
	<xsl:apply-templates select="row" />
</xsl:template>

<xsl:template match="row" >
	<xsl:element name="{ancestor::table/@name}">
		<xsl:for-each select="value">
			<xsl:variable name="index" select="count( preceding-sibling::* )" />
			<xsl:variable name="name" select="ancestor::table/column[$index + 1]/text()" />
			<xsl:attribute name="{$name}"><xsl:value-of select="text()" /></xsl:attribute>
		</xsl:for-each>
	</xsl:element>
</xsl:template>

</xsl:stylesheet>