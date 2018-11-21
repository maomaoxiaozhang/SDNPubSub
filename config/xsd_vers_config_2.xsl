<?xml version='1.0' encoding='ISO-8859-1'?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs">

<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>

<xsl:param name="jaxe-uri-xml"/>

<xsl:variable name="nom-fichier-xsd"><xsl:call-template name="nom-fichier-uri"><xsl:with-param name="uri" select="$jaxe-uri-xml"/></xsl:call-template></xsl:variable>

<xsl:template match="xs:schema">
    <CONFIG_JAXE>
        <LANGAGE>
            <FICHIER_SCHEMA nom="{$nom-fichier-xsd}"/>
            <xsl:for-each select="xs:element">
                <xsl:variable name="nom" select="@name"/>
                <xsl:if test="not(@substitutionGroup) and count(//xs:element[(@ref=$nom or substring-after(@ref,':')=$nom) and not(ancestor::xs:element[@name=$nom])])=0">
                    <RACINE element="{$nom}"/>
                </xsl:if>
            </xsl:for-each>
        </LANGAGE><xsl:text>

</xsl:text>
        <MENUS>
            <MENU nom="Eléments">
                <xsl:for-each select=".//xs:element[@name!='' and not(@abstract='true')]">
                    <xsl:variable name="nom" select="@name"/>
                    <xsl:if test="count(preceding::xs:element[@name=$nom])=0">
                        <MENU_INSERTION nom="{@name}"/>
                    </xsl:if>
                </xsl:for-each>
                <xsl:apply-templates select="xs:include" mode="menus"/>
            </MENU>
        </MENUS><xsl:text>

</xsl:text>
        <AFFICHAGE_NOEUDS>
            <xsl:for-each select=".//xs:element[@name!='' and not(@abstract='true')]">
                <xsl:variable name="nom" select="@name"/>
                <xsl:if test="count(preceding::xs:element[@name=$nom])=0">
                    <xsl:apply-templates select="." mode="affichage"/>
                </xsl:if>
            </xsl:for-each>
            <xsl:apply-templates select="xs:include" mode="affichage"/>
        </AFFICHAGE_NOEUDS><xsl:text>

</xsl:text>
        <STRINGS>
            <xsl:if test="@xml:lang!=''">
                <xsl:attribute name="langue"><xsl:value-of select="@xml:lang"/></xsl:attribute>
            </xsl:if>
            <DESCRIPTION_CONFIG>???</DESCRIPTION_CONFIG>
            <xsl:for-each select=".//xs:element[@name!='' and not(@abstract='true')]">
                <xsl:variable name="nom" select="@name"/>
                <xsl:if test="count(preceding::xs:element[@name=$nom])=0">
                    <xsl:apply-templates select="." mode="strings"/>
                </xsl:if>
            </xsl:for-each>
            <xsl:apply-templates select="xs:include" mode="strings"/>
        </STRINGS>
    </CONFIG_JAXE>
</xsl:template>

<xsl:template match="xs:element" mode="affichage">
    <xsl:variable name="type"><xsl:choose>
        <xsl:when test="contains(@type, ':')"><xsl:value-of select="substring-after(@type,':')"/></xsl:when>
        <xsl:otherwise><xsl:value-of select="@type"/></xsl:otherwise>
    </xsl:choose></xsl:variable>
    <xsl:variable name="nom" select="@name"/>
    <xsl:choose>
        <xsl:when test="parent::xs:schema and not(@substitutionGroup) and count(//xs:element[@ref=$nom or substring-after(@ref,':')=$nom])=0">
            <AFFICHAGE_ELEMENT element="{$nom}" type="division">
                <xsl:call-template name="titre-affichage"/>
            </AFFICHAGE_ELEMENT>
        </xsl:when>
        <xsl:when test="xs:complexType and not(xs:complexType/@mixed='true') and not(.//xs:element|.//xs:group|.//xs:extension)">
            <AFFICHAGE_ELEMENT element="{$nom}" type="vide">
                <xsl:call-template name="titre-affichage"/>
            </AFFICHAGE_ELEMENT>
        </xsl:when>
        <xsl:when test="not(@type) and not(xs:simpleType) and not(xs:complexType)">
            <AFFICHAGE_ELEMENT element="{$nom}" type="vide"/>
        </xsl:when>
        <xsl:when test="@type='xs:string'">
            <AFFICHAGE_ELEMENT element="{$nom}" type="string">
                <xsl:call-template name="titre-affichage"/>
            </AFFICHAGE_ELEMENT>
        </xsl:when>
        <xsl:when test="xs:simpleType or ../xs:simpleType[@name=$type and xs:restriction/@base='xs:string']">
            <AFFICHAGE_ELEMENT element="{$nom}" type="typesimple"/>
        </xsl:when>
        <xsl:when test="$type!='' and count(//xs:complexType[@name=$type])=0">
            <AFFICHAGE_ELEMENT element="{$nom}" type="typesimple"/>
        </xsl:when>
        <xsl:otherwise>
            <AFFICHAGE_ELEMENT element="{$nom}" type="zone">
                <xsl:call-template name="titre-affichage"/>
            </AFFICHAGE_ELEMENT>
        </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<xsl:template name="titre-affichage">
    <xsl:choose>
        <xsl:when test="count(xs:complexType/xs:attribute)=1">
            <PARAMETRE nom="titreAtt" valeur="{xs:complexType/xs:attribute/@name}"/>
        </xsl:when>
        <xsl:when test="count(xs:complexType/xs:attribute[@use='required'])=1">
            <PARAMETRE nom="titreAtt" valeur="{xs:complexType/xs:attribute[@use='required']/@name}"/>
        </xsl:when>
    </xsl:choose>
</xsl:template>

<xsl:template match="xs:element" mode="strings">
    <STRINGS_ELEMENT element="{@name}">
        <TITRE><xsl:value-of select="@name"/></TITRE>
        <xsl:if test="xs:annotation/xs:documentation!=''">
            <DOCUMENTATION><xsl:value-of select="xs:annotation/xs:documentation"/></DOCUMENTATION>
        </xsl:if>
        <xsl:for-each select="xs:complexType/xs:attribute">
            <STRINGS_ATTRIBUT attribut="{@name|@ref}">
                <TITRE><xsl:value-of select="@name|@ref"/></TITRE>
                <xsl:if test="xs:annotation/xs:documentation!=''">
                    <DOCUMENTATION><xsl:value-of select="xs:annotation/xs:documentation"/></DOCUMENTATION>
                </xsl:if>
            </STRINGS_ATTRIBUT>
        </xsl:for-each>
    </STRINGS_ELEMENT>
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
