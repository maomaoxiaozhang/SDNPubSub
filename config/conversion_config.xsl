<?xml version='1.0' encoding='ISO-8859-1'?>

<!--
    2 choses à faire à la main après utilisation :
    * ajouter la langue pour l'élément STRINGS
    * ajouter les titres dans les différentes langues à partir des fichiers .properties s'ils étaient utilisés
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output method="xml" indent="yes" encoding="ISO-8859-1"/>
    
<xsl:template match="JAXECFG">
    <CONFIG_JAXE>
        <xsl:call-template name="LANGAGE"/>
        
        <xsl:call-template name="ENREGISTREMENT"/>
        
        <xsl:call-template name="MENUS"/>
        
        <xsl:call-template name="AFFICHAGE_NOEUDS"/>
        
        <xsl:call-template name="EXPORTS"/>
        
        <xsl:call-template name="STRINGS"/>
    </CONFIG_JAXE>
</xsl:template>

<xsl:template name="LANGAGE">
    <LANGAGE>
        <xsl:choose>
            <xsl:when test="FICHIERSCHEMA">
                <FICHIER_SCHEMA nom="{FICHIERSCHEMA/@nom}"/>
            </xsl:when>
            <xsl:otherwise>
                <SCHEMA_SIMPLE>
                    <xsl:for-each select="RACINE/BALISE[count(../../MENU//BALISE[@nom=current()/@nom])=0] | MENU//BALISE">
                        <xsl:variable name="texte"><xsl:choose>
                            <xsl:when test="count(TEXTE) &gt; 0">autorise</xsl:when>
                            <xsl:when test="count(//ENSEMBLE[@nom=current()/SOUSBALISE/@ensemble]/TEXTE) &gt; 0">autorise</xsl:when>
                            <xsl:otherwise>interdit</xsl:otherwise>
                        </xsl:choose></xsl:variable>
                        <ELEMENT nom="{@nom}" texte="{$texte}">
                            <xsl:for-each select="SOUSBALISE">
                                <xsl:choose>
                                    <xsl:when test="@nom!=''">
                                        <SOUS-ELEMENT element="{@nom}"/>
                                    </xsl:when>
                                    <xsl:when test="@ensemble!=''">
                                        <SOUS-ENSEMBLE ensemble="{@ensemble}"/>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:for-each>
                            <xsl:for-each select="ATTRIBUT">
                                <ATTRIBUT nom="{@nom}" presence="{@presence}">
                                    <xsl:copy-of select="VALEUR"/>
                                </ATTRIBUT>
                            </xsl:for-each>
                        </ELEMENT>
                    </xsl:for-each>
                    <xsl:for-each select="ENSEMBLE">
                        <ENSEMBLE nom="{@nom}">
                            <xsl:for-each select="SOUSBALISE">
                                <xsl:choose>
                                    <xsl:when test="@nom!=''">
                                        <SOUS-ELEMENT element="{@nom}"/>
                                    </xsl:when>
                                    <xsl:when test="@ensemble!=''">
                                        <SOUS-ENSEMBLE ensemble="{@ensemble}"/>
                                    </xsl:when>
                                </xsl:choose>
                            </xsl:for-each>
                        </ENSEMBLE>
                    </xsl:for-each>
                </SCHEMA_SIMPLE>
            </xsl:otherwise>
        </xsl:choose>
        <xsl:for-each select="RACINE">
            <RACINE element="{BALISE[1]/@nom}"/>
        </xsl:for-each>
        <xsl:for-each select="CONFIG">
            <AUTRE_CONFIG nom="{@nom}"/>
        </xsl:for-each>
    </LANGAGE><xsl:text>

</xsl:text>
</xsl:template>

<xsl:template name="ENREGISTREMENT">
    <xsl:if test="ENCODAGE | DOCTYPE | ESPACE">
        <ENREGISTREMENT>
            <xsl:if test="ENCODAGE">
                <xsl:copy-of select="ENCODAGE"/>
            </xsl:if>
            <xsl:if test="DOCTYPE">
                <xsl:copy-of select="DOCTYPE"/>
            </xsl:if>
            <xsl:if test="ESPACE and ESPACE[1]/@prefixe!=''">
                <PREFIXE_ESPACE prefixe="{ESPACE[1]/@prefixe}" uri="{ESPACE[1]/@uri}"/>
            </xsl:if>
        </ENREGISTREMENT><xsl:text>

</xsl:text>
    </xsl:if>
</xsl:template>

<xsl:template name="MENUS">
    <MENUS>
        <xsl:apply-templates select="MENU"/>
    </MENUS><xsl:text>

</xsl:text>
</xsl:template>

<xsl:template match="MENU">
    <MENU nom="{@titre}">
        <xsl:for-each select="MENU|BALISE|FONCTION|SEPARATEUR">
            <xsl:choose>
                <xsl:when test="self::MENU">
                    <xsl:apply-templates select="."/>
                </xsl:when>
                <xsl:when test="self::BALISE and (not(@cache) or @cache='false')">
                    <xsl:variable name="type_noeud"><xsl:choose>
                        <xsl:when test="@noeudtype='instruction'">instruction</xsl:when>
                        <xsl:otherwise>element</xsl:otherwise>
                    </xsl:choose></xsl:variable>
                    <MENU_INSERTION nom="{@nom}" type_noeud="{$type_noeud}">
                        <xsl:if test="@commande">
                            <xsl:attribute name="raccourci"><xsl:value-of select="@commande"/></xsl:attribute>
                        </xsl:if>
                    </MENU_INSERTION>
                </xsl:when>
                <xsl:when test="self::FONCTION">
                    <MENU_FONCTION nom="{@titre}" classe="{@classe}">
                        <xsl:if test="@commande">
                            <xsl:attribute name="raccourci"><xsl:value-of select="@commande"/></xsl:attribute>
                        </xsl:if>
                        <xsl:copy-of select="PARAMETRE"/>
                    </MENU_FONCTION>
                </xsl:when>
                <xsl:when test="self::SEPARATEUR">
                    <SEPARATEUR/>
                </xsl:when>
            </xsl:choose>
        </xsl:for-each>
    </MENU>
</xsl:template>

<xsl:template name="AFFICHAGE_NOEUDS">
    <AFFICHAGE_NOEUDS>
        <xsl:for-each select="RACINE/BALISE[count(../../MENU//BALISE[@nom=current()/@nom])=0] | MENU//BALISE">
            <xsl:choose>
                <xsl:when test="@noeudtype='instruction' and @type='plugin'">
                    <PLUGIN_INSTRUCTION cible="{@nom}">
                        <xsl:copy-of select="PARAMETRE"/>
                    </PLUGIN_INSTRUCTION>
                </xsl:when>
                <xsl:otherwise>
                    <AFFICHAGE_ELEMENT element="{@nom}" type="{@type}">
                        <xsl:copy-of select="PARAMETRE"/>
                    </AFFICHAGE_ELEMENT>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </AFFICHAGE_NOEUDS><xsl:text>

</xsl:text>
</xsl:template>

<xsl:template name="EXPORTS">
    <xsl:if test="FICHIERXSL">
        <EXPORTS>
            <EXPORT nom="HTML" sortie="HTML">
                <xsl:for-each select="FICHIERXSL">
                    <FICHIER_XSL nom="{@nom}">
                        <xsl:copy-of select="PARAMETRE"/>
                    </FICHIER_XSL>
                </xsl:for-each>
            </EXPORT>
        </EXPORTS><xsl:text>

</xsl:text>
    </xsl:if>
</xsl:template>

<xsl:template name="STRINGS">
    <STRINGS> <!-- pb : attribut langue ? -->
        <DESCRIPTION_CONFIG><xsl:value-of select="DESCRIPTION"/></DESCRIPTION_CONFIG>
        <xsl:choose>
            <xsl:when test="OPTIONS/@titres='titres'">
                <xsl:for-each select="RACINE/BALISE[count(../../MENU//BALISE[@nom=current()/@nom])=0] | MENU//BALISE">
                    <xsl:if test="@titre!='' and not(@noeudtype='instruction')">
                        <STRINGS_ELEMENT element="{@nom}">
                            <TITRE><xsl:value-of select="@titre"/></TITRE>
                        </STRINGS_ELEMENT>
                    </xsl:if>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:for-each select="RACINE/BALISE[count(../../MENU//BALISE[@nom=current()/@nom])=0] | MENU//BALISE">
                    <xsl:if test="@titre!='' and not(@noeudtype='instruction')">
                        <STRINGS_MENU menu="{@nom}">
                            <TITRE><xsl:value-of select="@titre"/></TITRE>
                        </STRINGS_MENU>
                    </xsl:if>
                </xsl:for-each>
            </xsl:otherwise>
        </xsl:choose>
    </STRINGS>
</xsl:template>

</xsl:stylesheet>
