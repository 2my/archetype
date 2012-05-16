<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<xsl:output method="xml" encoding="UTF-8" indent="yes" />

<!-- will convert dbunit old format export to new format -->
<!-- out:
<dataset>
  <web_scorecard CREDIT_APPLICATION_ID="1" WEB_FIRSTNAME="NinaDbunitWS" WEB_LASTNAME="SalojärviWS" WEB_POSTADDRESS="City" WEB_POSTCODE="011" WEB_ADDRESS="Address" WEB_2_POSTADDRESS="City" WEB_2_POSTCODE="011" WEB_2_ADDRESS="Address" WEB_TIME_STAMP="2007-01-12" WEB_CONDITIONS_EMPLOYMENT="4" WEB_CREDIT_APPLIED="15000" WEB_JOB_POSITION="Occupation" WEB_MOBILE_TELEPHONE="1111111" WEB_NATIONAL_CITIZEN="Yes" WEB_PRODUCT="DEMOPRODUKTK" WEB_SOCIAL_SECURITY_NUMBER="121122B162U" WEB_TOTAL_DEBT="0" WEB_TOTAL_DEBT_MONTH="0" WEB_2_NATIONAL_CITIZEN="Yes" WEB_2_SOCIAL_SECURITY_NUMBER="101010+3410" WEB_STORE_NUMBER="1" WEB_CARD_PROFILE="1" WEB_EMPLOYER="Employer" WEB_EMPLOYED_SINCE="2000-11-21" WEB_ACCOMODATION_STATUS="2" WEB_SOURCE="1" WEB_LOGIN="ale" WEB_IP_ADDRESS="127.0.0.1 / 127.0.0.1 / null" APPLICATION_COMPLETE="N" WEB_2_ELECTRONIC_MARKETING="Y" WEB_2_ADDRESS_CO_NAME="co-address" WEB_2_LANGUAGE="se" WEB_2_MORTGAGE="0" WEB_ELECTRONIC_MARKETING="Y" WEB_ADDRESS_CO_NAME="co-address" WEB_RENTAL_COST="0" WEB_MONTLY_DUE_DATE="0" WEB_FAMILY_SIZE="0" WEB_MORTGAGE="0" WEB_MOBILE_AREA_CODE="011" WEB_PAYMENT_PLAN="0" WEB_PRIOR_EMPLOYMENT_DURATION="0" WEB_2_EMAIL_MARKETING="Y" WEB_2_MOBILE_MARKETING="Y" WEB_EMAIL_MARKETING="Y" WEB_MOBILE_MARKETING="Y" WEB_2_RENTAL_COST="0" WEB_2_INCOME_MONTH="0" WEB_INCOME_MONTH="10000" APPLICATION_TYPE="2" WEB_2_PRIOR_EMPLOYMENT_DUR="0" WEB_BANK_CLIENT_PERIOD_YEARS="0" WEB_2_BANK_CLIENT_PERIOD_YEARS="0" WEB_LANGUAGE="se"/>

  ...
-->
<!-- in:

<dataset>
	<table name="web_application">
		<column>CREDIT_APPLICATION_ID</column>
		...
		<row>
			<value>1</value>
			...
-->
<xsl:template match="dataset" >
	<dataset>
		<xsl:apply-templates select="*" />
	</dataset>
</xsl:template>

<xsl:template match="*" >
	<table name="{name()}">
		<xsl:apply-templates select="@*" mode="column" />
		<row>
			<xsl:apply-templates select="@*" mode="row" />
		</row>
	</table>
</xsl:template>

<xsl:template match="@*" mode="column" >
	<column><xsl:value-of select="name()" /></column>
</xsl:template>
<xsl:template match="@*" mode="row" >
	<value><xsl:value-of select="." /></value><xsl:comment><xsl:value-of select="name()" /></xsl:comment>
</xsl:template>

</xsl:stylesheet>