<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:text="urn:oasis:names:tc:opendocument:xmlns:text:1.0" xmlns:draw="urn:oasis:names:tc:opendocument:xmlns:drawing:1.0"
	version="1.0">

	<xsl:strip-space elements="*" />
	<xsl:output indention="no" />

	<!-- copy everything that has no other pattern defined -->
	<xsl:template match="* | @*">
		<xsl:copy>
			<xsl:copy-of select="@*" />
			<xsl:apply-templates />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="//draw:frame[@draw:name='Rahmen${frameNumber}']/draw:text-box">
		<xsl:text>
		<draw:text-box>
		<!-- START -->
		<!-- blank line -->
		<text:p text:style-name="P1" />
		<text:p text:style-name="P1">
			<text:s text:c="${leadingBlanks}" />
			${address.givenName} ${address.familyName}
		</text:p>
		<text:p text:style-name="P1">
			<text:s text:c="${leadingBlanks}" />
			${address.street}
		</text:p>
		<!-- blank line -->
		<text:p text:style-name="P1" />
		<text:p text:style-name="P1">
			<text:s text:c="${leadingBlanks}" />
			${address.postalCode} ${address.city}
		</text:p>
		</draw:text-box>
		<!-- END -->
		</xsl:text>
	</xsl:template>

</xsl:stylesheet>