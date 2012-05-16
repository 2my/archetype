<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>

<xsl:output method="xml" encoding="UTF-8" indent="yes" />

<!-- will convert export to old format -->
<xsl:template match="dataset" >
	<dataset>
		<xsl:apply-templates select="*" />
	</dataset>
</xsl:template>

<!-- These have no primary keys, and so cannot be imported by REFRESH -->
<xsl:template match="ICARD_BACKUP" />
<xsl:template match="ICARD_COMPANY" />
<xsl:template match="ICARD_ENGAGE" />
<xsl:template match="WEB_APPLICATION_LOG" />

<xsl:template match="*" >
	<xsl:copy-of select="." />
</xsl:template>

</xsl:stylesheet>