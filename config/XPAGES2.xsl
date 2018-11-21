<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" extension-element-prefixes="xalanredirect exsl faire-images" xmlns:xalanredirect="http://xml.apache.org/xalan/redirect" xmlns:exsl="http://exslt.org/common" xmlns:faire-images="xalan://xpages.FaireImages">

    <xsl:output method="xml" indent="yes" encoding="US-ASCII"/>
    <!-- US-ASCII parce-que XSLTC ne gère pas autre chose avec l'extension redirect -->
    
    <xsl:param name="jaxe-fichier-xml"/>
    <xsl:param name="jaxe-fichier-xsl"/>
    <xsl:param name="jaxe-fichier-destination"/>
    <xsl:param name="jaxe-uri-destination"/>
    
    <!-- Recopie du fichier XML, copie des fichiers, réduction des images, création de images.xml -->
    <!-- (on ne peut pas faire ça dans XPAGES3.xsl à cause de la valeur différente pour output method) -->
    
    <!-- séparateur de chemins ( / ou \ ) -->
    <xsl:variable name="sep"><xsl:choose>
        <xsl:when test="contains($jaxe-fichier-xml, '\')">\</xsl:when>
        <xsl:otherwise>/</xsl:otherwise>
    </xsl:choose></xsl:variable>
    
    <!-- chemin du répertoire du fichier XML -->
    <xsl:variable name="chemin-xml"><xsl:call-template name="rep-fichier"><xsl:with-param name="chemin" select="$jaxe-fichier-xml"/></xsl:call-template></xsl:variable>
    
    <!-- chemin du répertoire du fichier XSL -->
    <xsl:variable name="chemin-xsl"><xsl:call-template name="rep-fichier"><xsl:with-param name="chemin" select="$jaxe-fichier-xsl"/></xsl:call-template></xsl:variable>
    
    <!-- chemin du répertoire dans lequel les fichiers HTML seront générés -->
    <xsl:param name="chemin-site"><xsl:choose>
        <xsl:when test="$jaxe-fichier-destination!=''"><xsl:call-template name="rep-fichier"><xsl:with-param name="chemin" select="$jaxe-fichier-destination"/></xsl:call-template></xsl:when>
        <xsl:otherwise><xsl:value-of select="$chemin-xml"/></xsl:otherwise>
    </xsl:choose><xsl:value-of select="concat($sep, 'site')"/></xsl:param>
    
    <!-- chemin du répertoire contribXML (dans lequel se trouvent toutes les contributions) -->
    <xsl:variable name="chemin-contrib"><xsl:call-template name="rep-fichier"><xsl:with-param name="chemin" select="$chemin-xml"/></xsl:call-template></xsl:variable>
    
    
    <xsl:template match="/">
        
        <!-- nom du fichier XML (qui doit être égal au label de l'ensemble) -->
        <xsl:variable name="nomfichierxml"><xsl:call-template name="nom-fichier">
            <xsl:with-param name="chemin" select="$jaxe-fichier-xml"/>
        </xsl:call-template></xsl:variable>
        
        <!-- nom du répertoire du fichier XML (qui doit être égal au label de l'ensemble) -->
        <xsl:variable name="nomrep"><xsl:call-template name="nom-fichier">
            <xsl:with-param name="chemin" select="$chemin-xml"/>
        </xsl:call-template></xsl:variable>
        
        <xsl:if test="$nomrep = XPAGES/INFORMATIONS/LABEL and $nomfichierxml = concat($nomrep, '.xml')">
            <xsl:call-template name="copier-fichiers-contrib"/>
            <xsl:call-template name="copier-dossier-interface"/>
            <xsl:call-template name="reduire-images"/>
            <xsl:call-template name="faire-equations"/>
        </xsl:if>
        
        <xsl:apply-templates/>
    </xsl:template>
    
    
    <xsl:template match = "@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    
    
    <!-- copie l'ensemble des fichiers des contributions vers un dossier site à l'intérieur -->
    <xsl:template name="copier-fichiers-contrib">
        <xsl:if test="function-available('faire-images:copierDossierContrib')">
            <xsl:for-each select="//XPAGES">
                <xsl:variable name="label-fichiers" select="@labelfichiers"/>
                <xsl:if test="$label-fichiers!='' and not(preceding-sibling::XPAGES/@labelfichiers=$label-fichiers)">
                    <xsl:variable name="res" select="faire-images:copierDossierContrib(string(concat($chemin-contrib, $sep, $label-fichiers)), string($chemin-site))"/>
                    <xsl:if test="$res!='ok'">
                        <xsl:message>Erreur à la copie du dossier contrib <xsl:value-of select="$label-fichiers"/></xsl:message>
                    </xsl:if>
                </xsl:if>
            </xsl:for-each>
        </xsl:if>
    </xsl:template>
    
    <!-- copie tout le dossier des interfaces de XPAGES -->
    <xsl:template name="copier-dossier-interface">
        <xsl:if test="function-available('faire-images:copierDossier')">
            <xsl:variable name="chemin1" select="concat($chemin-xsl, $sep, 'XPAGES_interface')"/>
            <xsl:variable name="chemin2" select="concat($chemin-site, $sep, 'interface')"/>
            <xsl:variable name="res" select="faire-images:copierDossier(string($chemin1), string($chemin2))"/>
            <xsl:if test="$res!='ok'">
                <xsl:message>Erreur à la copie du dossier d'interface</xsl:message>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    
    <!-- Copie des images. Les images trop grandes (en fonction de la localisation) sont réduites. -->
    <xsl:template name="reduire-images">
        
        <!-- si l'extension faire-images n'est pas disponible, on suppose que le fichier images.xml
                a déjà été créé et que les images ont déjà été copiées -->
        <xsl:if test="function-available('faire-images:transfo')">
            <!-- pb ici: tout ce qui est déclaré dans xsl:output est utilisé (method, doctype, ...) -->
            <xsl:call-template name="ecrire-fichier">
                <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'images.xml')"/>
                <xsl:with-param name="contenu">
                    <IMAGES><xsl:text>
</xsl:text>
                        <xsl:for-each select="//FICHIER[@nom!='']|//LOGO[@fichier!='' and not(starts-with(@fichier,'http://'))]">
                            <xsl:variable name="label-ensemble" select="ancestor::XPAGES[1]/INFORMATIONS/LABEL"/>
                            <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
                            <xsl:variable name="localisation"><xsl:choose>
                                <xsl:when test="ancestor::ENVIMAGE[1]/@localisation!=''"><xsl:value-of select="ancestor::ENVIMAGE[1]/@localisation"/></xsl:when>
                                <xsl:otherwise>texte</xsl:otherwise>
                            </xsl:choose></xsl:variable>
                            <xsl:variable name="dossierfichier" select="concat($chemin-contrib, $sep, $label-fichiers)"/>
                            <xsl:variable name="dossier-res" select="concat($chemin-site, '/pages_', $label-fichiers)"/>
                            <xsl:variable name="res" select="faire-images:transfo(string($dossierfichier), string(@nom|@fichier), string($localisation), string($dossier-res))"/>
                            <xsl:if test="faire-images:resok($res)='erreur'">
                                <xsl:message>Erreur à la réduction de l'image <xsl:value-of select="@nom|@fichier"/> de la contrib <xsl:value-of select="$label-fichiers"/></xsl:message>
                            </xsl:if>
                            <xsl:variable name="redim"><xsl:choose>
                                <xsl:when test="faire-images:resok($res)='réduit'">oui</xsl:when>
                                <xsl:otherwise>non</xsl:otherwise>
                            </xsl:choose></xsl:variable>
                            <IMAGE nom="{@nom|@fichier}" contrib="{$label-fichiers}" localisation="{$localisation}" redim="{$redim}" largeur1="{faire-images:largeur1($res)}" hauteur1="{faire-images:hauteur1($res)}">
                                <xsl:if test="$redim='oui'">
                                    <xsl:attribute name="largeur2"><xsl:value-of select="faire-images:largeur2($res)"/></xsl:attribute>
                                    <xsl:attribute name="hauteur2"><xsl:value-of select="faire-images:hauteur2($res)"/></xsl:attribute>
                                </xsl:if>
                                <xsl:if test="faire-images:animation($res)">
                                    <xsl:attribute name="anim">oui</xsl:attribute>
                                </xsl:if>
                            </IMAGE><xsl:text>
</xsl:text>
                            <!-- réduction pour l'impression (localisation: texte) -->
                            <xsl:if test="$localisation!='texte'">
                                <xsl:variable name="res2" select="faire-images:transfo(string($dossierfichier), string(@nom|@fichier), string('texte'), string($dossier-res))"/>
                                <xsl:if test="faire-images:resok($res2)='erreur'">
                                    <xsl:message>Erreur à la réduction pour l'impression de l'image <xsl:value-of select="@nom|@fichier"/> de la contrib <xsl:value-of select="$label-fichiers"/></xsl:message>
                                </xsl:if>
                                <xsl:variable name="redim2"><xsl:choose>
                                    <xsl:when test="faire-images:resok($res2)='réduit'">oui</xsl:when>
                                    <xsl:otherwise>non</xsl:otherwise>
                                </xsl:choose></xsl:variable>
                                <IMAGE nom="{@nom|@fichier}" contrib="{$label-fichiers}" localisation="texte" redim="{$redim2}" largeur1="{faire-images:largeur1($res2)}" hauteur1="{faire-images:hauteur1($res2)}">
                                    <xsl:if test="$redim2='oui'">
                                        <xsl:attribute name="largeur2"><xsl:value-of select="faire-images:largeur2($res2)"/></xsl:attribute>
                                        <xsl:attribute name="hauteur2"><xsl:value-of select="faire-images:hauteur2($res2)"/></xsl:attribute>
                                    </xsl:if>
                                    <xsl:if test="faire-images:animation($res2)">
                                        <xsl:attribute name="anim">oui</xsl:attribute>
                                    </xsl:if>
                                </IMAGE><xsl:text>
</xsl:text>
                            </xsl:if>
                        </xsl:for-each>
                    </IMAGES>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    <!-- Création des images des équations à partir du contenu en PNG base64 -->
    <xsl:template name="faire-equations">
        <xsl:for-each select="//EQUATION | //EQUATEX">
            <xsl:variable name="label-ensemble" select="ancestor::XPAGES[1]/INFORMATIONS/LABEL"/>
            <xsl:variable name="numero"><xsl:number from="XPAGES" count="EQUATION|EQUATEX" level="any"/></xsl:variable>
            <xsl:variable name="dossier-res" select="concat($chemin-site, '/pages_', ancestor::XPAGES[1]/@labelfichiers)"/>
            <xsl:variable name="res" select="faire-images:creerImageEquation(string(.), string($numero), string($label-ensemble), string($dossier-res))"/>
            <xsl:if test="$res!='ok'">
                <xsl:message>Erreur à la copie du dossier contrib <xsl:value-of select="$label-ensemble"/></xsl:message>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    
    
    
    <!-- Templates outils -->
    
    <xsl:template name="nom-fichier">
    <!-- renvoit le nom d'un fichier à partir du chemin-->
        <xsl:param name="chemin"/>
        <xsl:choose>
            <xsl:when test="contains($chemin,$sep)">
                <xsl:call-template name="nom-fichier"><xsl:with-param name="chemin" select="substring-after($chemin,$sep)"/></xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$chemin"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="rep-fichier">
        <!-- renvoit le chemin du répertoire d'un fichier à partir du chemin complet vers le fichier-->
        <xsl:param name="chemin"/>
        <xsl:choose>
            <xsl:when test="contains($chemin,$sep) and contains(substring-after($chemin,$sep),$sep)">
                <xsl:value-of select="substring-before($chemin,$sep)"/><xsl:value-of select="$sep"/><xsl:call-template name="rep-fichier"><xsl:with-param name="chemin" select="substring-after($chemin,$sep)"/></xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="substring-before($chemin,$sep)"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="ecrire-fichier">
        <xsl:param name="fichier"/> <!-- chemin du fichier dans lequel on veut écrire -->
        <xsl:param name="contenu"/> <!-- contenu à écrire dans le fichier -->
        <xsl:param name="methode"><xsl:choose>
            <xsl:when test="translate(substring($fichier, string-length($fichier)-4, 4), 'XML', 'xml') = '.xml'">xml</xsl:when>
            <xsl:otherwise>html</xsl:otherwise>
        </xsl:choose></xsl:param>
        <xsl:param name="encodage" select="'ISO-8859-1'"/>
        <xsl:param name="indentation" select="'no'"/>
        <xsl:choose>
            <!-- Xalan et XSLTC -->
            <!-- attention, Xalan gère les chemins relatifs mais pas XSLTC -->
            <!-- attention, autre bug de XSLTC: il ne gère pas les encodages de caractères, et
                utilise l'encodage du système au lieu de celui spécifié par xsl:output !!! -->
            <xsl:when test="element-available('xalanredirect:write')">
                <xalanredirect:write file="{$fichier}">
                    <xsl:copy-of select="$contenu"/>
                </xalanredirect:write>
            </xsl:when>
            
            <!-- EXSLT -->
            <xsl:when test="element-available('exsl:document')">
                <exsl:document href="{$fichier}" method="{$methode}" encoding="{$encodage}" indent="{$indentation}">
                    <xsl:copy-of select="$contenu"/>
                </exsl:document>
            </xsl:when>
            
            <xsl:otherwise>
                <xsl:message terminate="yes">
                    <xsl:text>Impossible de créer un fichier avec le processeur XSLT </xsl:text>
                    <xsl:value-of select="system-property('xsl:vendor')"/>
                </xsl:message>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
</xsl:stylesheet>
