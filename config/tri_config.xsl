<?xml version='1.0' encoding='ISO-8859-1'?>

<!-- tri des affichages et strings d'un fichier de config à partir de l'ordre des menus -->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>

<xsl:template match="CONFIG_JAXE">
    <CONFIG_JAXE>
        <xsl:apply-templates/>
    </CONFIG_JAXE>
</xsl:template>

<xsl:template match="LANGAGE">
    <xsl:copy-of select="."/>
</xsl:template>

<xsl:template match="ENREGISTREMENT">
    <xsl:copy-of select="."/>
</xsl:template>

<xsl:template match="MENUS">
    <xsl:copy-of select="."/>
</xsl:template>

<xsl:template match="AFFICHAGE_NOEUDS">
    <AFFICHAGE_NOEUDS>
        <xsl:for-each select="../MENUS//MENU_INSERTION">
            <xsl:if test="count(preceding::MENU_INSERTION[@nom=current()/@nom])=0">
                <xsl:copy-of select="/CONFIG_JAXE/AFFICHAGE_NOEUDS/AFFICHAGE_ELEMENT[@element=current()/@nom]"/>
            </xsl:if>
            <xsl:if test="not(following-sibling::MENU_INSERTION)">
                <xsl:text>

</xsl:text>
            </xsl:if>
        </xsl:for-each>
        <xsl:for-each select="*">
            <xsl:variable name="nom" select="@element"/>
            <xsl:if test="name()!='AFFICHAGE_ELEMENT' or count(/CONFIG_JAXE/MENUS//MENU_INSERTION[@nom=$nom])=0">
                <xsl:copy-of select="."/>
            </xsl:if>
        </xsl:for-each>
    </AFFICHAGE_NOEUDS>
</xsl:template>

<xsl:template match="EXPORTS">
    <xsl:copy-of select="."/>
</xsl:template>

<xsl:template match="STRINGS">
    <STRINGS>
        <xsl:copy-of select="@*"/>
        <xsl:variable name="langue_pays" select="concat(@langue, '_', @pays)"/>
        <xsl:copy-of select="DESCRIPTION_CONFIG"/>
        <xsl:if test="STRINGS_MENU">
            <xsl:for-each select="../MENUS//*[self::MENU|self::MENU_INSERTION|self::MENU_FONCTION]">
                <xsl:variable name="type_menu" select="name()"/>
                <xsl:if test="count(preceding::*[name()=$type_menu and @nom=current()/@nom])=0">
                    <xsl:copy-of select="/CONFIG_JAXE/STRINGS[concat(@langue, '_', @pays)=$langue_pays]/STRINGS_MENU[@menu=current()/@nom]"/>
                </xsl:if>
            </xsl:for-each>
            <xsl:text>

</xsl:text>
        </xsl:if>
        <xsl:if test="STRINGS_ELEMENT">
            <xsl:for-each select="../MENUS//MENU_INSERTION">
                <xsl:if test="count(preceding::MENU_INSERTION[@nom=current()/@nom])=0">
                    <xsl:copy-of select="/CONFIG_JAXE/STRINGS[concat(@langue, '_', @pays)=$langue_pays]/STRINGS_ELEMENT[@element=current()/@nom]"/>
                </xsl:if>
                <xsl:if test="not(following-sibling::MENU_INSERTION)">
                    <xsl:text>

</xsl:text>
                </xsl:if>
            </xsl:for-each>
            <xsl:for-each select="STRINGS_ELEMENT">
                <xsl:if test="count(/CONFIG_JAXE/MENUS//MENU_INSERTION[@nom=current()/@element])=0">
                    <xsl:copy-of select="."/>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
        <xsl:copy-of select="STRINGS_EXPORT"/>
    </STRINGS>
</xsl:template>

</xsl:stylesheet>
