<?xml version='1.0' encoding='ISO-8859-1'?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema">

<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>

<xsl:param name="jaxe-uri-xml"/>

<xsl:template match="xs:include|xs:import|xs:redefine">
    <xsl:param name="pile">|<xsl:call-template name="nom-fichier-uri"><xsl:with-param name="uri" select="$jaxe-uri-xml"/></xsl:call-template>|</xsl:param>
    <xsl:variable name="schemaLocation" select="@schemaLocation"/>
    <xsl:if test="not(contains($pile,concat('|',$schemaLocation,'|')))">
        <xsl:for-each select="document($schemaLocation, .)/xs:schema">
            <xsl:apply-templates select="xs:include|xs:import">
                <xsl:with-param name="pile" select="concat($pile,$schemaLocation,'|')"/>
            </xsl:apply-templates>
            <xsl:copy-of select="xs:simpleType|xs:complexType|xs:group|xs:attributeGroup|xs:element|xs:attribute"/>
        </xsl:for-each>
    </xsl:if>
</xsl:template>

<xsl:template match="@*|node()">
    <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
</xsl:template>

<!-- renvoit le nom d'un fichier à partir de l'URI -->
<xsl:template name="nom-fichier-uri">
    <xsl:param name="uri"/>
    <xsl:choose>
        <xsl:when test="contains($uri,'/')">
            <xsl:call-template name="nom-fichier-uri"><xsl:with-param name="uri" select="substring-after($uri,'/')"/></xsl:call-template>
        </xsl:when>
        <xsl:otherwise>
            <xsl:value-of select="$uri"/>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

</xsl:stylesheet>
