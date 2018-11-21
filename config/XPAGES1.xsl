<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
    
    <xsl:param name="jaxe-uri-xml"/>
    
    <!-- séparateur de chemins ( / ou \ avec file:// sur Windows ) -->
    <xsl:variable name="sep"><xsl:choose>
        <xsl:when test="contains($jaxe-uri-xml, '\')">\</xsl:when>
        <xsl:otherwise>/</xsl:otherwise>
    </xsl:choose></xsl:variable>
    
    <!-- création d'un fichier XML contenant toutes les contributions, à partir des références REFXPAGES -->
    <!-- tous les dossiers des contributions doivent se trouver dans le même dossier -->
    
    <xsl:variable name="uri-chemin-xml"><xsl:call-template name="rep-uri"><xsl:with-param name="chemin" select="$jaxe-uri-xml"/></xsl:call-template></xsl:variable>
    
    <xsl:template match="XPAGES">
        <xsl:variable name="contribution"><xsl:choose>
            <xsl:when test="parent::XPAGES">non</xsl:when>
            <xsl:otherwise>oui</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:variable name="labelfichiers"><xsl:choose>
            <xsl:when test="parent::XPAGES"><xsl:value-of select="ancestor::XPAGES[count(parent::XPAGES)=0]/INFORMATIONS/LABEL"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <XPAGES contribution="{$contribution}" labelfichiers="{$labelfichiers}">
            <xsl:apply-templates/>
        </XPAGES>
    </xsl:template>
    
    <xsl:template match="REFXPAGES">
        <xsl:variable name="contrib" select="concat($uri-chemin-xml, $sep, '..', $sep, @label, $sep, @label, '.xml')"/>
        <XPAGES contribution="oui" labelfichiers="{@label}">
            <xsl:for-each select="document($contrib)/*">
                <xsl:apply-templates/>
            </xsl:for-each>
        </XPAGES>
    </xsl:template>
    
    
    <xsl:template match = "@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    
    <xsl:template name="rep-uri">
        <!-- renvoit le chemin du répertoire d'un fichier à partir de l'URI complète, sous forme d'URI  -->
        <xsl:param name="chemin"/>
        <xsl:choose>
            <xsl:when test="contains($chemin,$sep) and contains(substring-after($chemin,$sep),$sep)">
                <xsl:value-of select="substring-before($chemin,$sep)"/><xsl:value-of select="$sep"/><xsl:call-template name="rep-uri"><xsl:with-param name="chemin" select="substring-after($chemin,$sep)"/></xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="substring-before($chemin,$sep)"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>

