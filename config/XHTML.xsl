<?xml version="1.0" encoding='ISO-8859-1' ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html" indent="no"
              doctype-public="-//W3C//DTD HTML 4.01//EN"
              doctype-system="http://www.w3.org/TR/html4/strict.dtd"
              encoding="ISO-8859-1"/>

  <xsl:template match="*">
    <xsl:element name="{local-name()}" namespace="">
      <xsl:for-each select="@*">
        <xsl:if test="namespace-uri(.)='' or namespace-uri(.)=namespace-uri(..)">
          <xsl:copy-of select="."/>
        </xsl:if>
      </xsl:for-each>
      <xsl:apply-templates/>
    </xsl:element>
  </xsl:template>

</xsl:stylesheet>
