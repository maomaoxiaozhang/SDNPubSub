<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" extension-element-prefixes="xalanredirect exsl" xmlns:xalanredirect="http://xml.apache.org/xalan/redirect" xmlns:exsl="http://exslt.org/common">
    
    <xsl:output method="html" indent="yes" encoding="UTF-8"/>
    <xsl:strip-space elements="CODE"/> <!-- pour NBSP (sinon un retour à la ligne est ajouté après le template) -->
    
    <!-- <xsl:key name="xpages" match="XPAGES" use="INFORMATIONS/LABEL"/> --> <!-- bug XALANJ-2534 avec XSLTC -->
    
    <xsl:param name="jaxe-fichier-xml"/>
    <xsl:param name="jaxe-uri-xsl"/>
    <xsl:param name="jaxe-uri-xml"/>
    <xsl:param name="jaxe-fichier-destination"/>
    <xsl:param name="jaxe-uri-destination"/>
    
    <xsl:param name="sortie">public</xsl:param> <!-- public|production|tuteurs -->
    <!-- les solutions des exercices d'évaluation et les commentaires sont affichées si sortie=production -->
    
    <!-- séparateur de chemins ( / ou \ ) -->
    <xsl:variable name="sep"><xsl:choose>
        <xsl:when test="contains($jaxe-fichier-xml, '\')">\</xsl:when>
        <xsl:otherwise>/</xsl:otherwise>
    </xsl:choose></xsl:variable>
    
    <!-- chemin du répertoire du fichier XML -->
    <xsl:variable name="chemin-xml"><xsl:call-template name="rep-fichier"><xsl:with-param name="chemin" select="$jaxe-fichier-xml"/></xsl:call-template></xsl:variable>
    <xsl:variable name="uri-chemin-xml"><xsl:call-template name="rep-uri"><xsl:with-param name="chemin" select="$jaxe-uri-xml"/></xsl:call-template></xsl:variable>
    
    <!-- URI du fichier XSL -->
    <xsl:variable name="uri-xsl"><xsl:call-template name="rep-uri"><xsl:with-param name="chemin" select="$jaxe-uri-xsl"/></xsl:call-template></xsl:variable>
    
    <!-- chemin du répertoire dans lequel les fichiers HTML seront générés -->
    <xsl:param name="chemin-site"><xsl:choose>
        <xsl:when test="$jaxe-fichier-destination!=''"><xsl:call-template name="rep-fichier"><xsl:with-param name="chemin" select="$jaxe-fichier-destination"/></xsl:call-template></xsl:when>
        <xsl:otherwise><xsl:value-of select="$chemin-xml"/></xsl:otherwise>
    </xsl:choose><xsl:value-of select="concat($sep, 'site')"/></xsl:param>
    <xsl:param name="uri-site"><xsl:choose>
        <xsl:when test="$jaxe-uri-destination!=''"><xsl:call-template name="rep-uri"><xsl:with-param name="chemin" select="$jaxe-uri-destination"/></xsl:call-template></xsl:when>
        <xsl:otherwise><xsl:value-of select="$uri-chemin-xml"/></xsl:otherwise>
    </xsl:choose>/site</xsl:param>
    
    <xsl:param name="rubriques">oui</xsl:param> <!-- oui/non -->
    
    <!-- chemin du répertoire contribXML (dans lequel se trouvent toutes les contributions) -->
    <xsl:variable name="chemin-contrib"><xsl:call-template name="rep-fichier"><xsl:with-param name="chemin" select="$chemin-xml"/></xsl:call-template></xsl:variable>
    
    <xsl:variable name="feuille-de-style">style.css</xsl:variable>
    
    <xsl:variable name="interface_par_defaut">peinture</xsl:variable>
    
    <!-- langue du document -->
    <xsl:variable name="langue"><xsl:choose>
        <xsl:when test="/XPAGES/INFORMATIONS/LANGUE"><xsl:value-of select="/XPAGES/INFORMATIONS/LANGUE"/></xsl:when>
        <xsl:otherwise>fr</xsl:otherwise>
    </xsl:choose></xsl:variable>
    <!-- document avec les messages dans la langue de l'ensemble -->
    <xsl:variable name="messages" select="document(concat($uri-xsl, '/XPAGES_messages_', $langue, '.xml'))/messages"/>
    
    <xsl:variable name="images" select="document(concat($uri-site, '/images.xml'))/IMAGES"/>
    
    <xsl:variable name="roles" select ="'|introduction|conclusion|prerequis|objectifs|rappel|definition|demonstration|exemple|conseil|remarque|attention|complement|methode|activite|exercice|bibliographie|'"/>
    
    
    
    <xsl:template match="/">
        
        <!-- nom du fichier XML (qui doit être égal au label de l'ensemble) -->
        <xsl:variable name="nomfichierxml"><xsl:call-template name="nom-fichier">
            <xsl:with-param name="chemin" select="$jaxe-fichier-xml"/>
        </xsl:call-template></xsl:variable>
        
        <!-- nom du répertoire du fichier XML (qui doit être égal au label de l'ensemble) -->
        <xsl:variable name="nomrep"><xsl:call-template name="nom-fichier"><xsl:with-param name="chemin" select="$chemin-xml"/></xsl:call-template></xsl:variable>
        
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
        <html>
        <head>
            <title><xsl:value-of select="$messages/message[@label='Aperçu']"/></title>
        </head>
        <body>
        
        <xsl:call-template name="infos-erreurs"/>
        
        <xsl:call-template name="fichier-index"/>
        
        <xsl:if test="$nomrep = XPAGES/INFORMATIONS/LABEL and $nomfichierxml = concat($nomrep, '.xml')">
            
            <xsl:apply-templates/>
            
            <p><a href="site/index.html"><xsl:value-of select="$messages/message[@label='aller à la page d_accueil du site']"/></a></p>
            <p><a href="site/pages_{XPAGES/INFORMATIONS/LABEL}/sommaire.html"><xsl:value-of select="$messages/message[@label='aller au sommaire']"/></a></p>
            <p><a href="site/pages_{XPAGES/INFORMATIONS/LABEL}/impression.html"><xsl:value-of select="$messages/message[@label='aller à la page d_impression']"/></a></p>
        </xsl:if>
        
        </body>
        </html>
        
        <xsl:if test="$sortie='production'">
            <xsl:call-template name="mauvais-liens"/>
            <xsl:call-template name="labels"/>
            <xsl:call-template name="commentaires"/>
            <xsl:call-template name="credits"/>
            <xsl:call-template name="definitions-glossaire"/>
        </xsl:if>
        <xsl:if test="$sortie='production' or $sortie='tuteurs'">
            <xsl:call-template name="liste-exercices"/>
        </xsl:if>
    </xsl:template>
    
    
    <xsl:template name="infos-erreurs">
        <!-- nom du fichier XML (qui doit être égal au label de l'ensemble) -->
        <xsl:variable name="nomfichierxml"><xsl:call-template name="nom-fichier">
            <xsl:with-param name="chemin" select="$jaxe-fichier-xml"/>
        </xsl:call-template></xsl:variable>
        
        <!-- nom du répertoire du fichier XML (qui doit être égal au label de l'ensemble) -->
        <xsl:variable name="nomrep"><xsl:call-template name="nom-fichier"><xsl:with-param name="chemin" select="$chemin-xml"/></xsl:call-template></xsl:variable>
        
        <xsl:choose>
            <xsl:when test="count(/XPAGES/INFORMATIONS) = 0">
                <p><b><xsl:value-of select="$messages/message[@label='Attention !']"/></b>&#xA0;<xsl:value-of select="$messages/message[@label='L_élément INFORMATIONS est obligatoire sous la racine XPAGE. C_est un élément à insérer en premier dans le document, et qui permet de spécifier son label et son titre.']"/></p>
            </xsl:when>
            
            <xsl:when test="count(/XPAGES/INFORMATIONS/LABEL) = 0">
                <p><b><xsl:value-of select="$messages/message[@label='Attention !']"/></b>&#xA0;<xsl:value-of select="$messages/message[@label='Le label est obligatoire dans les informations. Il permet d_identifier le document, et de faire des liens vers ce document depuis d_autres documents XPAGES. Ce label doit correspondre au nom du fichier XML et au nom du dossier parent.']"/></p>
            </xsl:when>
            
            <xsl:when test="$nomrep != /XPAGES/INFORMATIONS/LABEL or $nomfichierxml != concat($nomrep, '.xml')">
                <p><b><xsl:value-of select="$messages/message[@label='Attention !']"/></b>&#xA0;<xsl:value-of select="$messages/message[@label='Le nom du dossier doit être égal au label du document, et le nom du fichier XML doit être le label avec l_extension .xml.']"/></p>
                <p><xsl:value-of select="$messages/message[@label='Actuellement, le nom du dossier est']"/>&#xA0;<tt><xsl:value-of select="$nomrep"/></tt><xsl:value-of select="$messages/message[@label=', le label du document est']"/>&#xA0;<tt><xsl:value-of select="/XPAGES/INFORMATIONS/LABEL"/></tt><xsl:value-of select="$messages/message[@label=', et le nom du fichier est']"/>&#xA0;<tt><xsl:value-of select="$nomfichierxml"/></tt></p>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template name="fichier-index">
        <!-- fichier site/index.html -->
        <xsl:for-each select="/XPAGES">
            <xsl:choose>
                <xsl:when test="count(PAGE[@label='index']) &gt; 0">
                    <xsl:for-each select="PAGE[@label='index']">
                        <xsl:call-template name="ecrire-fichier">
                            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'index.html')"/>
                            <xsl:with-param name="contenu">
                                <xsl:call-template name="page-entree"/>
                            </xsl:with-param>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="ecrire-fichier">
                        <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'index.html')"/>
                        <xsl:with-param name="contenu">
                            <xsl:call-template name="page-entree"/>
                        </xsl:with-param>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    
    
    <xsl:template match="XPAGES">
        
        <xsl:variable name="prefixe">../</xsl:variable>
        <xsl:variable name="interface">interface/<xsl:choose>
            <xsl:when test="count(ancestor-or-self::XPAGES[INFORMATIONS/INTERFACE!='']) &gt; 0"><xsl:value-of select="ancestor-or-self::XPAGES[INFORMATIONS/INTERFACE!=''][1]/INFORMATIONS/INTERFACE"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="$interface_par_defaut"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        
        <xsl:variable name="nom-fichier-index"><xsl:choose>
            <xsl:when test="@contribution='oui'">index.html</xsl:when>
            <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/>_index.html</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:if test="@contribution!='oui' or count(PAGE[@label='index'])=0">
            <xsl:call-template name="ecrire-fichier">
                <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'pages_', @labelfichiers, $sep, $nom-fichier-index)"/>
                <xsl:with-param name="contenu">
                    <xsl:call-template name="page-index">
                        <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                    </xsl:call-template>
                </xsl:with-param>
            </xsl:call-template>
        </xsl:if>
        
        <xsl:apply-templates select="PAGE|XPAGES"/>
        
        <xsl:for-each select="PAGE//EXERCICE | PAGE//QCM">
            <xsl:call-template name="fichiers-aide">
                <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
            </xsl:call-template>
        </xsl:for-each>
        
        <xsl:if test="count(ancestor::XPAGES)=0">
            <xsl:call-template name="ecrire-fichier">
                <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'pages_', INFORMATIONS/LABEL, $sep, 'sommaire.html')"/>
                <xsl:with-param name="contenu">
                    <xsl:call-template name="page-sommaire">
                        <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                    </xsl:call-template>
                </xsl:with-param>
            </xsl:call-template>
            
            <xsl:if test="//GLOSSAIRE">
                <xsl:call-template name="ecrire-fichier">
                    <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'pages_', INFORMATIONS/LABEL, $sep, 'glossaire.html')"/>
                    <xsl:with-param name="contenu">
                        <xsl:call-template name="page-glossaire">
                            <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                        </xsl:call-template>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </xsl:if>
        
        <xsl:for-each select="PAGE//ENVIMAGE">
            <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
            <xsl:variable name="nomsfichiers">|<xsl:for-each select=".//FICHIER">
                <xsl:value-of select="concat(@nom, '|')"/>
            </xsl:for-each></xsl:variable>
            <xsl:variable name="localisation" select="@localisation"/>
            <xsl:variable name="redim" select="$images/IMAGE[@contrib=$label-fichiers and contains($nomsfichiers, concat('|',@nom,'|')) and @localisation=$localisation][1]/@redim"/>
            <xsl:if test="($redim='oui' or $localisation='icône')">
            <!-- les vidéos ne peuvent pas être redimensionnées mais il faut un fichier HTML si elles sont en icône -->
                <xsl:call-template name="html-figure">
                    <xsl:with-param name="interface" select="$interface"/>
                </xsl:call-template>
            </xsl:if>
        </xsl:for-each>
        
        <xsl:variable name="nom-fichier-impression"><xsl:choose>
            <xsl:when test="@contribution='oui'">impression.html</xsl:when>
            <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/>_impression.html</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'pages_', @labelfichiers, $sep, $nom-fichier-impression)"/>
            <xsl:with-param name="contenu">
                <xsl:call-template name="contenu-impression">
                    <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                </xsl:call-template>
            </xsl:with-param>
        </xsl:call-template>
        
    </xsl:template>
    
    
    <xsl:template name="zone_bandeau">
        <xsl:variable name="nb" select="count(ancestor-or-self::XPAGES)"/>
        <div class="titre_site_1"><xsl:value-of select="ancestor-or-self::XPAGES[$nb]/INFORMATIONS/TITRE"/></div>
        <div class="titre_site_2"><xsl:value-of select="ancestor-or-self::XPAGES[$nb]/INFORMATIONS/TITRE"/></div>
    </xsl:template>
    
    
    <xsl:template name="zone_rubriques">
        <xsl:param name="prefixe">../</xsl:param>
        <div class="zone_rubriques">
        <table class="table_rubriques"><tr>
            <xsl:variable name="labelsel" select="ancestor-or-self::XPAGES[last()-1]/INFORMATIONS/LABEL"/>
            <xsl:for-each select="ancestor-or-self::XPAGES[last()]/XPAGES">
                <xsl:choose>
                    <xsl:when test="INFORMATIONS/LABEL=$labelsel">
                        <td class="rubriquesel">
                            <xsl:value-of select="INFORMATIONS/TITRE"/>
                        </td>
                    </xsl:when>
                    <xsl:otherwise>
                        <td class="rubrique">
                            <xsl:variable name="titre" select="INFORMATIONS/TITRE"/>
                            <xsl:for-each select="./descendant::PAGE[1]">
                                <a href="{concat($prefixe,'pages_', ancestor::XPAGES[1]/@labelfichiers, '/', @label,'.html')}"><xsl:value-of select="$titre"/></a>
                            </xsl:for-each>
                        </td>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </tr></table>
        </div>
    </xsl:template>
    
    
    <xsl:template name="page-index">
        <xsl:param name="interface"/>
        
        <xsl:variable name="prefixe">../</xsl:variable>
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
        <html>
        <head>
            <xsl:if test="INFORMATIONS/TITRE!=''">
                <title><xsl:value-of select="INFORMATIONS/TITRE"/></title>
            </xsl:if>
            <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/{$feuille-de-style}"/>
            <xsl:for-each select="preceding::PAGE[1]">
                <link rel="Prev"  href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html"/>
            </xsl:for-each>
            <xsl:variable name="nb" select="count(ancestor-or-self::XPAGES)"/>
            <link rel="Contents"  href="{$prefixe}pages_{ancestor-or-self::XPAGES[$nb]/INFORMATIONS/LABEL}/sommaire.html"/>
            <link rel="Start"  href="{PAGE[1]/@label}.html"/>
            <xsl:for-each select="PAGE[1]">
                <link rel="Next"  href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html"/>
            </xsl:for-each>
            <xsl:choose>
                <xsl:when test="count(ancestor::XPAGES) &gt; 1">
                    <xsl:for-each select="ancestor::XPAGES[1]">
                        <xsl:variable name="nom-fichier-index"><xsl:choose>
                            <xsl:when test="@contribution='oui'">index.html</xsl:when>
                            <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/>_index.html</xsl:otherwise>
                        </xsl:choose></xsl:variable>
                        <link rel="Up" href="{$prefixe}pages_{INFORMATIONS/LABEL}/{$nom-fichier-index}"/>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <link rel="Up" href="{$prefixe}index.html"/>
                </xsl:otherwise>
            </xsl:choose>
        </head>
        <body class="page">
            <xsl:call-template name="contenu-body">
                <xsl:with-param name="interface" select="$interface"/>
            </xsl:call-template>
        </body>
        </html>
    </xsl:template>
    
    <xsl:template name="texte-index">
        <xsl:param name="prefixe">../</xsl:param>
        <!-- liste des pages de la contribution -->
        <ol>
            <xsl:for-each select="PAGE|XPAGES">
                <li>
                    <xsl:choose>
                        <xsl:when test="self::PAGE">
                            <a href="{@label}.html" class="lien"><xsl:choose>
                                <xsl:when test="@titre!=''"><xsl:value-of select="@titre"/></xsl:when>
                                <xsl:otherwise><xsl:value-of select="$messages/message[@label='page']"/> <xsl:value-of select="position()"/></xsl:otherwise>
                            </xsl:choose></a>
                        </xsl:when>
                        <xsl:when test="self::XPAGES">
                            <xsl:choose>
                                <xsl:when test="*[self::PAGE|self::XPAGES][1][self::PAGE]">
                                    <a href="{$prefixe}pages_{@labelfichiers}/{PAGE[1]/@label}.html" class="lien"><xsl:value-of select="INFORMATIONS/TITRE"/></a>
                                </xsl:when>
                                <xsl:otherwise>
                                    <xsl:variable name="nom-fichier-index"><xsl:choose>
                                        <xsl:when test="@contribution='oui'">index.html</xsl:when>
                                        <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/>_index.html</xsl:otherwise>
                                    </xsl:choose></xsl:variable>
                                    <a href="{$prefixe}pages_{ancestor-or-self::XPAGES[1]/@labelfichiers}/{$nom-fichier-index}" class="lien"><xsl:value-of select="INFORMATIONS/TITRE"/></a>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:when>
                    </xsl:choose>
                </li>
            </xsl:for-each>
        </ol>
    </xsl:template>
    
    
    <xsl:template match="PAGE">
        <xsl:variable name="label-contrib" select="ancestor::XPAGES[1]/@labelfichiers"/>
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'pages_', $label-contrib, $sep, @label,'.html')"/>
            <xsl:with-param name="contenu">
                <xsl:call-template name="contenu-page"/>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    
    <xsl:template name="titre-page">
        <xsl:choose>
            <xsl:when test="self::XPAGES"><xsl:value-of select="INFORMATIONS/TITRE"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="@titre"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template name="autres-scripts">
        <!-- peut être modifié pour ajouter des scripts dans head -->
    </xsl:template>
    
    
    <xsl:template name="contenu-page">
        <xsl:variable name="prefixe">../</xsl:variable>
        <xsl:variable name="interface">interface/<xsl:choose>
            <xsl:when test="count(ancestor-or-self::XPAGES[INFORMATIONS/INTERFACE!='']) &gt; 0"><xsl:value-of select="ancestor-or-self::XPAGES[INFORMATIONS/INTERFACE!=''][1]/INFORMATIONS/INTERFACE"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="$interface_par_defaut"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
        <html>
        <head>
            <xsl:choose>
                <xsl:when test="@titre!=''">
                    <title><xsl:call-template name="titre-page"/></title>
                </xsl:when>
                <xsl:otherwise>
                    <title>page <xsl:value-of select="position()"/></title>
                </xsl:otherwise>
            </xsl:choose>
            <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/{$feuille-de-style}"/>
            <xsl:for-each select="preceding::PAGE[1][@label!='index' or count(ancestor::XPAGES) &gt; 1]">
                <link rel="Prev"  href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html"/>
            </xsl:for-each>
            <xsl:variable name="nom-fichier-index-parent"><xsl:choose>
                <xsl:when test="../@contribution='oui'">index.html</xsl:when>
                <xsl:otherwise><xsl:value-of select="../INFORMATIONS/LABEL"/>_index.html</xsl:otherwise>
            </xsl:choose></xsl:variable>
            <link rel="Contents"  href="{$nom-fichier-index-parent}"/>
            <xsl:for-each select="descendant::PAGE[1] | following::PAGE[1]">
                <link rel="Next"  href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html"/>
            </xsl:for-each>
            <xsl:choose>
                <xsl:when test="count(ancestor::XPAGES) &gt; 1">
                    <link rel="Up" href="{$nom-fichier-index-parent}"/>
                </xsl:when>
                <xsl:otherwise>
                    <link rel="Up" href="{$prefixe}index.html"/>
                </xsl:otherwise>
            </xsl:choose>
            <!-- ce meta est ajouté automatiquement -->
            <!--<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1"/>-->
            <xsl:if test=".//EXERCICE or .//QCM">
                <xsl:call-template name="script-aide-solution"/>
            </xsl:if>
            <xsl:if test=".//QCM">
                <xsl:call-template name="script-qcm"/>
            </xsl:if>
            <xsl:if test=".//ENVIMAGE[@localisation='page']">
                <xsl:call-template name="script-imagettes">
                    <xsl:with-param name="interface" select="$interface"/>
                </xsl:call-template>
            </xsl:if>
            <xsl:if test=".//ENVIMAGE[@localisation='texte' or @localisation='page']">
                <xsl:variable name="formats">|<xsl:for-each select=".//ENVIMAGE[@localisation='texte' or @localisation='page']//FICHIER">
                    <xsl:call-template name="format-fichier">
                        <xsl:with-param name="nom" select="@nom"/>
                    </xsl:call-template><xsl:text>|</xsl:text>
                </xsl:for-each></xsl:variable>
                <xsl:if test="contains($formats, '|MPEG4|') or contains($formats, '|OGG|') or contains($formats, '|WEBM|')">
                    <xsl:call-template name="script-video">
                        <xsl:with-param name="prefixe" select="$prefixe"/>
                    </xsl:call-template>
                </xsl:if>
            </xsl:if>
            <xsl:call-template name="autres-scripts"/>
        </head>
        <body class="page">
            <xsl:call-template name="contenu-body">
                <xsl:with-param name="interface" select="$interface"/>
            </xsl:call-template>
        </body>
        </html>
    </xsl:template>
    
    
    <xsl:template name="zone_logo">
        <xsl:param name="prefixe">../</xsl:param>
        <div class="zone_logo">
            <xsl:for-each select="ancestor-or-self::XPAGES/INFORMATIONS/LOGO[1]">
                <xsl:variable name="prefsrcimg"><xsl:if test="not(starts-with(@fichier,'http://'))"><xsl:value-of select="concat($prefixe, 'pages_', ../LABEL, '/')"/></xsl:if></xsl:variable>
                <xsl:choose>
                    <xsl:when test="@lien!=''"><a href="{@lien}"><img src="{$prefsrcimg}{@fichier}" alt="{@fichier}" border="0"/></a></xsl:when>
                    <xsl:otherwise><img src="{$prefsrcimg}{@fichier}" alt="{@fichier}"/></xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
        </div>
    </xsl:template>
    
    
    <xsl:template name="zone_sommaire">
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:param name="prefixe">../</xsl:param>
        <div class="zone_sommaire">
            <div class="zone_mininav">
                <table style="width: 100%"><tr>
                    <xsl:variable name="nb" select="count(ancestor-or-self::XPAGES)"/>
                    <td style="text-align: center">
                        <a href="{$prefixe}index.html" title="{$messages/message[@label='Entrée du site']}"><img src="{$prefixe}{$interface}/home.png" width="25" height="25" alt="{$messages/message[@label='Entrée du site']}" border="0"/></a>
                    </td>
                    <td style="text-align: center">
                        <a href="{$prefixe}pages_{ancestor-or-self::XPAGES[$nb]/INFORMATIONS/LABEL}/sommaire.html" title="{$messages/message[@label='Sommaire']}"><img src="{$prefixe}{$interface}/sommaire_petit.png" width="25" height="25" border="0" alt="{$messages/message[@label='Sommaire']}"/></a>
                    </td>
                    <xsl:if test="//GLOSSAIRE">
                        <td style="text-align: center">
                            <a href="{$prefixe}pages_{ancestor-or-self::XPAGES[$nb]/INFORMATIONS/LABEL}/glossaire.html" title="{$messages/message[@label='Glossaire']}"><img src="{$prefixe}{$interface}/glossaire_petit.png" width="25" height="25" border="0" alt="{$messages/message[@label='Glossaire']}"/></a>
                        </td>
                    </xsl:if>
                    <td style="text-align: center">
                        <xsl:for-each select="ancestor-or-self::XPAGES[1]">
                            <xsl:variable name="nom-fichier-impression"><xsl:choose>
                                <xsl:when test="@contribution='oui'">impression.html</xsl:when>
                                <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/>_impression.html</xsl:otherwise>
                            </xsl:choose></xsl:variable>
                            <a href="{$prefixe}pages_{@labelfichiers}/{$nom-fichier-impression}" title="{INFORMATIONS/TITRE} : {$messages/message[@label='Page pour l_impression']}" target="_blank"><img src="{$prefixe}{$interface}/impression.png" width="25" height="25" border="0" alt="{$messages/message[@label='Page pour l_impression']}"/></a>
                        </xsl:for-each>
                    </td>
                    <td style="text-align: center">
                        <xsl:for-each select="preceding::PAGE[1][@label!='index' or count(ancestor::XPAGES) &gt; 1]">
                            <a href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html" accesskey="j" title="{$messages/message[@label='Page précédente']}"><img src="{$prefixe}{$interface}/petiteflechegauche.png" width="25" height="16" alt="&lt;-" border="0"/></a>
                        </xsl:for-each>
                    </td>
                    <td style="text-align: center">
                        <xsl:for-each select="./descendant::PAGE[1] | following::PAGE[current()/self::PAGE and position()=1]">
                            <a href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html" accesskey="l" title="{$messages/message[@label='Page suivante']}"><img src="{$prefixe}{$interface}/petiteflechedroite.png" width="25" height="16" alt="-&gt;" border="0"/></a>
                        </xsl:for-each>
                    </td>
                </tr></table>
            </div>
            
            <xsl:call-template name="sommaire-page">
                <xsl:with-param name="interface" select="$interface"/>
            </xsl:call-template>
        </div>
    </xsl:template>
    
    
    <xsl:template name="zone_titre">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <div class="zone_titre">
            <table><tr>
            <td><h1>
                <xsl:if test="self::PAGE and @role != '' and contains($roles, concat('|', @role, '|'))">
                    <img src="{$prefixe}{$interface}/icones_sections/{@role}.png" width="40" height="40" alt="{@role}" class="icone_page"/>
                </xsl:if>
                <xsl:call-template name="titre-page"/>
            </h1></td>
            </tr></table>
        </div>
    </xsl:template>
    
    
    <xsl:template name="contenu-body">
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>

    <!--
    Organisation des divs:
    
         ______________________
        |    titre site   | lo |
        |_________________| go |
        |outils |  titre  |    |
        |       |_________|____|
        |       |              |
        |  som  |  corps du    |
        | maire |  texte       |
        |       |              |
        |       |              |
        |       |              |
        |_______|______________|
    
    -->
    
        <xsl:variable name="prefixe">../</xsl:variable>
        
        <xsl:call-template name="zone_logo"/>
        
        <xsl:call-template name="zone_bandeau"/>
        
        <xsl:variable name="rub" select="$rubriques='oui' and (not(ancestor-or-self::XPAGES[INFORMATIONS/RUBRIQUES]) or ancestor-or-self::XPAGES[INFORMATIONS/RUBRIQUES!=''][1]/INFORMATIONS/RUBRIQUES='oui')"/>
        <xsl:if test="count(ancestor-or-self::XPAGES[last()]/XPAGES) &gt; 1 and $rub">
            <xsl:call-template name="zone_rubriques"/>
        </xsl:if>
        
        <xsl:call-template name="zone_sommaire">
            <xsl:with-param name="interface" select="$interface"/>
        </xsl:call-template>
        
        <xsl:call-template name="zone_titre">
            <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
            <xsl:with-param name="interface" select="$interface"/>
        </xsl:call-template>
        
        <div class="zone_contenu">
            
            <xsl:if test="count(ancestor-or-self::XPAGES[1]/INFORMATIONS/AUTEUR) &gt; 0">
                <div class="auteurs">
                    <xsl:call-template name="auteurs"/>
                </div>
            </xsl:if>
            
            <xsl:choose>
                <xsl:when test="self::XPAGES">
                    <div class="zone_texte">
                        <xsl:call-template name="texte-index">
                            <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                        </xsl:call-template>
                    </div>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:if test=".//ENVIMAGE[@localisation='page']">
                        <xsl:call-template name="figures-page">
                            <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                            <xsl:with-param name="interface" select="$interface"/>
                        </xsl:call-template>
                    </xsl:if>
                    <div class="zone_texte">
                        <xsl:apply-templates>
                            <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                            <xsl:with-param name="affichage" select="'web'"/>
                            <xsl:with-param name="interface" select="$interface"/>
                        </xsl:apply-templates>
                    </div>
                </xsl:otherwise>
            </xsl:choose>
            
            <div class="spacer"></div>
            
            <div class="zone_liens">
                <table style="width: 100%"><tr>
                    <td>
                        <xsl:for-each select="preceding::PAGE[1][@label!='index' or count(ancestor::XPAGES) &gt; 1]">
                            <a href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html" title="{$messages/message[@label='Page précédente']}"><img src="{$prefixe}{$interface}/flechegauche.png" width="48" height="31" alt="{$messages/message[@label='Page précédente']}" border="0"/></a>
                        </xsl:for-each>
                    </td>
                    <td style="text-align: right">
                        <xsl:for-each select="./descendant::PAGE[1] | following::PAGE[current()/self::PAGE and position()=1]">
                            <a href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html" title="{$messages/message[@label='Page suivante']}"><img src="{$prefixe}{$interface}/flechedroite.png" width="48" height="31" alt="{$messages/message[@label='Page suivante']}" border="0"/></a>
                        </xsl:for-each>
                    </td>
                </tr></table>
            </div>
        </div>
    </xsl:template>
    
    
    <xsl:template name="contenu-impression">
        <xsl:param name="interface"/>
        <xsl:variable name="prefixe">../</xsl:variable>
        
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
        <html>
        <head>
            <title><xsl:value-of select="concat(INFORMATIONS/TITRE, ' : ', $messages/message[@label='Page pour l_impression'])"/></title>
            <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/{$feuille-de-style}"/>
        </head>
        <body>
            <h1><xsl:value-of select="INFORMATIONS/TITRE"/></h1>
            <xsl:if test="count(INFORMATIONS/AUTEUR) &gt; 0">
                <div class="auteurs">
                    <xsl:call-template name="auteurs"/>
                </div>
            </xsl:if>
            <ul>
                <xsl:apply-templates select="PAGE[@label!='index']|XPAGES" mode="sommaire-impression"/>
            </ul>
            <hr/>
            <div style="page-break-after: always"/>
            <xsl:for-each select=".//*[self::XPAGES|self::PAGE]">
                <xsl:choose>
                    <xsl:when test="self::XPAGES">
                        <h1><xsl:value-of select="INFORMATIONS/TITRE"/></h1>
                        
                        <xsl:if test="count(INFORMATIONS/AUTEUR) &gt; 0">
                            <div class="auteurs">
                                <xsl:call-template name="auteurs"/>
                            </div>
                        </xsl:if>
                        <hr/>
                    </xsl:when>
                    <xsl:otherwise>
                        <h1><xsl:value-of select="@titre"/></h1>
                        
                        <div class="zone_texte">
                            <xsl:apply-templates>
                                <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                                <xsl:with-param name="affichage" select="'impression'"/>
                                <xsl:with-param name="interface" select="$interface"/>
                            </xsl:apply-templates>
                        </div>
                        
                        <div class="spacer"></div>
                        <xsl:if test="position()!=last()">
                            <hr/>
                            <xsl:if test="count(following-sibling::PAGE)=0">
                                <div style="page-break-after: always"/> <!-- pour les navigateurs comprenant CSS 2 -->
                            </xsl:if>
                        </xsl:if>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each>
            
            <xsl:if test=".//QCM">
                <hr/>
                <div style="page-break-after: always"/>
                
                <h2>Réponses aux QCM</h2>
                <xsl:for-each select=".//QCM">
                    <p style="font-family: monospace">pages_<xsl:value-of select="ancestor::XPAGES[1]/INFORMATIONS/LABEL"/>/<xsl:value-of select="ancestor::PAGE/@label"/>.html</p>
                    <h3>QCM
                    <xsl:if test="@titre!=''">
                        <xsl:text> </xsl:text>'<xsl:value-of select="@titre"/>'
                    </xsl:if>
                    </h3>
                    <ul>
                        <xsl:for-each select="QUESTIONQCM">
                            <li>Question <xsl:value-of select="position()"/><br/>
                                <xsl:for-each select="AIDE">
                                    <i>Aide : </i> <xsl:apply-templates/><br/>
                                </xsl:for-each>
                                <xsl:for-each select="REPONSEQCM[@bonne='oui']">
                                    <i>Solution : </i> réponse <xsl:number from="QCM" count="REPONSEQCM"/>)
                                    <xsl:if test="COMMENTAIREREP!=''">
                                        <xsl:text> </xsl:text>(<xsl:value-of select="COMMENTAIREREP"/>)
                                    </xsl:if>
                                    <br/>
                                </xsl:for-each>
                            </li>
                        </xsl:for-each>
                    </ul>
                </xsl:for-each>
            </xsl:if>
            
            <xsl:if test=".//EXERCICE[@type='auto-évaluation'] or (.//EXERCICE and ($sortie='production' or $sortie='tuteurs')) or .//EXERCICE/QUESTION/AIDE">
                <hr/>
                <div style="page-break-after: always"/>
                
                <h2>Réponses aux exercices</h2>
                <xsl:for-each select=".//EXERCICE[count(QUESTION/AIDE) &gt; 0 or ((@type='auto-évaluation' or $sortie='production' or $sortie='tuteurs') and count(QUESTION/SOLUTION) &gt; 0)]">
                    <xsl:if test="position() &gt; 1">
                        <hr/>
                    </xsl:if>
                    <p style="font-family: monospace">pages_<xsl:value-of select="ancestor::XPAGES[1]/INFORMATIONS/LABEL"/>/<xsl:value-of select="ancestor::PAGE/@label"/>.html</p>
                    <h3>Exercice
                        <xsl:if test="@titre!=''">
                            <xsl:text> </xsl:text>'<xsl:value-of select="@titre"/>'
                        </xsl:if>
                    </h3>
                    <ul>
                        <xsl:for-each select="QUESTION">
                            <li><b>Question <xsl:value-of select="position()"/></b><br/>
                                <xsl:for-each select="AIDE">
                                    <i>Aide : </i> <xsl:apply-templates/><br/>
                                </xsl:for-each>
                                <xsl:if test="../@type='auto-évaluation' or $sortie='production' or $sortie='tuteurs'">
                                    <xsl:for-each select="SOLUTION">
                                        <i>Solution : </i> <xsl:apply-templates/><br/>
                                    </xsl:for-each>
                                </xsl:if>
                            </li>
                        </xsl:for-each>
                    </ul>
                </xsl:for-each>
            </xsl:if>
        </body>
        </html>
    </xsl:template>
    
    <xsl:template match="XPAGES" mode="sommaire-impression">
        <li>
            <xsl:value-of select="INFORMATIONS/TITRE"/>
            <ul>
                <xsl:apply-templates select="PAGE[@label!='index']|XPAGES" mode="sommaire-impression"/>
            </ul>
        </li>
    </xsl:template>
    
    <xsl:template match="PAGE" mode="sommaire-impression">
        <li><xsl:value-of select="@titre"/></li>
    </xsl:template>
    
    
    <!-- Sommaire -->
    
    <xsl:template name="sous-sommaire">
        <xsl:param name="pos"/>
        <xsl:param name="interface"/>
        
        <xsl:variable name="prefixe">../</xsl:variable>
        <xsl:choose>
            <xsl:when test="$pos &gt; 1 and $pos = count(ancestor-or-self::XPAGES)">
                <xsl:call-template name="sous-sommaire">
                    <xsl:with-param name="pos"><xsl:value-of select="$pos - 1"/></xsl:with-param>
                    <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="$pos &gt; 1">
                <img height="11" width="11" alt="-" border="0" src="{$prefixe}{$interface}/listeactive.png"/>
                <xsl:for-each select="ancestor-or-self::XPAGES[position()=$pos]">
                    <xsl:choose>
                        <xsl:when test="*[self::PAGE|self::XPAGES][1][self::PAGE]">
                            <a href="{$prefixe}pages_{@labelfichiers}/{PAGE[1]/@label}.html" class="sommaire2">&#xA0;<xsl:value-of select="INFORMATIONS/TITRE"/></a>
                        </xsl:when>
                        <xsl:otherwise>
                            <xsl:variable name="nom-fichier-index"><xsl:choose>
                                <xsl:when test="@contribution='oui'">index.html</xsl:when>
                                <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/>_index.html</xsl:otherwise>
                            </xsl:choose></xsl:variable>
                            <a href="{$prefixe}pages_{@labelfichiers}/{$nom-fichier-index}" class="sommaire2">&#xA0;<xsl:value-of select="INFORMATIONS/TITRE"/></a>
                        </xsl:otherwise>
                    </xsl:choose>
                </xsl:for-each>
                <div class="divsommaire">
                    <xsl:call-template name="sous-sommaire">
                        <xsl:with-param name="pos"><xsl:value-of select="$pos - 1"/></xsl:with-param>
                        <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                    </xsl:call-template>
                </div>
            </xsl:when>
            <xsl:when test="self::XPAGES">
                <div class="fondsel1sommaire">
                    <img height="11" width="11" alt="-" border="0" src="{$prefixe}{$interface}/listeactive.png"/>&#xA0;<span class="sommaire2select"><xsl:value-of select="INFORMATIONS/TITRE"/></span>
                    <div class="divsommaire">
                        <xsl:for-each select="PAGE|XPAGES">
                            <xsl:call-template name="element-sommaire">
                                <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                            </xsl:call-template>
                        </xsl:for-each>
                    </div>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <div class="fondsel1sommaire">
                    <xsl:for-each select="ancestor::XPAGES[2]">
                        <xsl:choose>
                            <xsl:when test="*[self::PAGE|self::XPAGES][1][self::PAGE]">
                                <a href="{$prefixe}pages_{@labelfichiers}/{PAGE[1]/@label}.html"><img height="11" width="11" alt="-" border="0" src="{$prefixe}{$interface}/listeactive.png"/></a>
                            </xsl:when>
                            <xsl:otherwise>
                                <xsl:variable name="nom-fichier-index"><xsl:choose>
                                    <xsl:when test="@contribution='oui'">index.html</xsl:when>
                                    <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/>_index.html</xsl:otherwise>
                                </xsl:choose></xsl:variable>
                                <a href="{$prefixe}pages_{@labelfichiers}/{$nom-fichier-index}"><img height="11" width="11" alt="-" border="0" src="{$prefixe}{$interface}/listeactive.png"/></a>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:for-each>
                    <a href="{$prefixe}pages_{../@labelfichiers}/{../PAGE[1]/@label}.html" class="sommaire2">&#xA0;<xsl:value-of select="../INFORMATIONS/TITRE"/></a>
                    <div class="divsommaire">
                        <xsl:variable name="labelpage" select="@label"/>
                        <xsl:for-each select="../*[(self::PAGE and @label!='index') or self::XPAGES]">
                            <xsl:call-template name="element-sommaire">
                                <xsl:with-param name="labelpage"><xsl:value-of select="$labelpage"/></xsl:with-param>
                                <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                            </xsl:call-template>
                        </xsl:for-each>
                    </div>
                </div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="element-sommaire">
        <xsl:param name="labelpage"/>
        <xsl:param name="interface"/>
        
        <xsl:variable name="prefixe">../</xsl:variable>
        <xsl:choose>
            <xsl:when test="self::PAGE and @label=$labelpage">
                <div class="fondsel2sommaire">
                    <span class="sommairepage">&#x2022; <xsl:value-of select="@titre"/></span>
                </div>
            </xsl:when>
            <xsl:when test="self::XPAGES">
                <div class="fondsel1sommaire"><img src="{$prefixe}{$interface}/listeinactive.png" border="0" alt="+" width="11" height="11"/>
                    <xsl:choose>
                        <xsl:when test="*[self::PAGE|self::XPAGES][1][self::PAGE]">
                            <a href="{$prefixe}pages_{@labelfichiers}/{PAGE[1]/@label}.html" class="sommaire3">&#xA0;<xsl:value-of select="INFORMATIONS/TITRE"/></a>
                        </xsl:when>
                        <xsl:otherwise>
                            <a href="{$prefixe}pages_{INFORMATIONS/LABEL}/index.html" class="sommaire3">&#xA0;<xsl:value-of select="INFORMATIONS/TITRE"/></a>
                        </xsl:otherwise>
                    </xsl:choose>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <div class="fondsel1sommaire">
                    <a href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html" class="sommairepage">&#x2022; <xsl:value-of select="@titre"/></a>
                </div>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="sommaire-page">
        <xsl:param name="interface"/>
        <div class="sommaire">
            <xsl:call-template name="sous-sommaire">
                <xsl:with-param name="pos"><xsl:value-of select="count(ancestor::XPAGES)"/></xsl:with-param>
                <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
            </xsl:call-template>
        </div>
    </xsl:template>
    
    
    <!-- Métadonnées -->
    
    <xsl:template match="INFORMATIONS">
    </xsl:template>
    
    <xsl:template match="TITRE">
        <!-- template inutilisé -->
        <h1><xsl:value-of select="."/></h1>
    </xsl:template>
    
    <xsl:template name="auteurs">
        <xsl:for-each select="ancestor-or-self::XPAGES[1]/INFORMATIONS">
            <xsl:choose>
                <xsl:when test="count(AUTEUR)=1">
                    <xsl:value-of select="$messages/message[@label='Auteur:']"/><xsl:text> </xsl:text>
                    <em><xsl:value-of select="AUTEUR"/></em><br/>
                </xsl:when>
                <xsl:when test="count(AUTEUR)&gt;1">
                    <xsl:value-of select="$messages/message[@label='Auteurs:']"/><xsl:text> </xsl:text>
                    <em><xsl:for-each select="AUTEUR"><xsl:value-of select="."/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each></em><br/>
                </xsl:when>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template match="DATECRE">
        <xsl:value-of select="$messages/message[@label='Date de création&#xA0;:']"/> <i><xsl:apply-templates/></i>
    </xsl:template>
    
    <xsl:template match="DATEMAJ">
        <xsl:value-of select="$messages/message[@label='Date de mise à jour&#xA0;:']"/> <i><xsl:apply-templates/></i>
    </xsl:template>
    
    
    <!-- Eléments de bloc -->
    
    <xsl:template match="SECTION">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:variable name="classe"><xsl:choose>
            <xsl:when test="(@role='introduction' or @role='conclusion' or @role='exercice') and @importance='grande'"><xsl:value-of select="@role"/></xsl:when>
            <xsl:when test="@importance='grande'">section_cadre</xsl:when>
            <xsl:otherwise>section_normale</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <div class="{$classe}">
            <xsl:if test="@titre!=''">
                <xsl:element name="h{count(ancestor::SECTION)+2}">
                    <xsl:if test="contains($roles, concat('|', @role, '|'))">
                        <img src="{$prefixe}{$interface}/icones_sections/{@role}.png" width="40" height="40" alt="{@role}" class="icone_section"/>
                    </xsl:if>
                    <xsl:value-of select="@titre"/>
                </xsl:element>
            </xsl:if>
            <xsl:apply-templates>
                <xsl:with-param name="prefixe" select="$prefixe"/>
                <xsl:with-param name="affichage" select="$affichage"/>
                <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
            </xsl:apply-templates>
        </div>
    </xsl:template>
    
    
    <xsl:template match="PARAGRAPHE">
        <xsl:param name="prefixe">../</xsl:param>
        <p><xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
        </xsl:apply-templates></p>
    </xsl:template>
    
    
    <xsl:template match="LISTE">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:if test="@titre!=''">
            <h2><xsl:value-of select="@titre"/></h2>
        </xsl:if>
        <xsl:choose>
            <xsl:when test="@type='numéros'">
                <ol>
                    <xsl:apply-templates select="ITEM">
                        <xsl:with-param name="prefixe" select="$prefixe"/>
                    </xsl:apply-templates>
                </ol>
            </xsl:when>
            <xsl:otherwise>
                <ul>
                    <xsl:apply-templates select="ITEM">
                        <xsl:with-param name="prefixe" select="$prefixe"/>
                    </xsl:apply-templates>
                </ul>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template match="ITEM">
        <xsl:param name="prefixe">../</xsl:param>
        <li><xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
        </xsl:apply-templates></li>
    </xsl:template>
    
    
    <xsl:template match="TABLEAU">
        <xsl:param name="prefixe">../</xsl:param>
        <table class="tableau">
            <xsl:if test="@titre!=''">
                <caption><xsl:value-of select="@titre"/></caption>
            </xsl:if>
            <xsl:apply-templates>
                <xsl:with-param name="prefixe" select="$prefixe"/>
            </xsl:apply-templates>
        </table>
    </xsl:template>
    
    
    <xsl:template match="TR">
        <xsl:param name="prefixe">../</xsl:param>
        <tr>
        <xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
        </xsl:apply-templates>
        </tr>
    </xsl:template>
    
    
    <xsl:template match="TH">
        <xsl:param name="prefixe">../</xsl:param>
        <th>
        <xsl:if test="@colspan"><xsl:attribute name="colspan"><xsl:value-of select="@colspan"/></xsl:attribute></xsl:if>
        <xsl:if test="@rowspan"><xsl:attribute name="rowspan"><xsl:value-of select="@rowspan"/></xsl:attribute></xsl:if>
        <xsl:if test="@align"><xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute></xsl:if>
        <xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
        </xsl:apply-templates>
        </th>
    </xsl:template>
    
    
    <xsl:template match="TD">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:variable name="trpos"><xsl:number from="TABLEAU" count="TR"/></xsl:variable>
        <td class="tableaux{1+($trpos mod 2)}">
        <xsl:if test="@colspan"><xsl:attribute name="colspan"><xsl:value-of select="@colspan"/></xsl:attribute></xsl:if>
        <xsl:if test="@rowspan"><xsl:attribute name="rowspan"><xsl:value-of select="@rowspan"/></xsl:attribute></xsl:if>
        <xsl:if test="@align"><xsl:attribute name="align"><xsl:value-of select="@align"/></xsl:attribute></xsl:if>
        <xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
        </xsl:apply-templates>
        </td>
    </xsl:template>
    
    
    <xsl:template match="ENVIMAGE">
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:variable name="localisation" select="@localisation"/>
        <xsl:variable name="titre" select="@titre"/>
        <xsl:choose>
            <xsl:when test="$localisation='icône' and $affichage='web'">
                <xsl:for-each select=".//FICHIER[1]">
                    <xsl:variable name="nomfichier" select="@nom"/>
                    <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
                    <xsl:variable name="numero"><xsl:number level="any" from="XPAGES[@contribution='oui']" count="ENVIMAGE"/></xsl:variable>
                    <xsl:variable name="fichierhtml" select="concat($prefixe,'pages_', $label-fichiers, '/', 'html_images/envimage', $numero, '.html')"/>
                    <xsl:variable name="format"><xsl:call-template name="format-fichier">
                        <xsl:with-param name="nom" select="$nomfichier"/>
                    </xsl:call-template></xsl:variable>
                    <!-- on prend le format (pour choisir l'icône) et les dimensions
                    (pour choisir la taille de la fenêtre) de la première image -->
                    <xsl:for-each select="$images/IMAGE[@contrib=$label-fichiers and @nom=$nomfichier and @localisation=$localisation][1]">
                        <xsl:variable name="largeur_fenetre"><xsl:choose>
                            <xsl:when test="@largeur1 &lt; 460">500</xsl:when>
                            <xsl:otherwise><xsl:value-of select="@largeur1+40"/></xsl:otherwise>
                        </xsl:choose></xsl:variable>
                        <a href="{$fichierhtml}" target="_blank" onclick="window.open('{$fichierhtml}', '', 'scrollbars=yes,scrolling=auto,toolbar=no,directories=no,menubar=no,status=no,width={$largeur_fenetre},height={@hauteur1+200},left=' + (100+(typeof window.screenX != 'undefined' ? window.screenX : window.screenLeft)) + ',top=' + (100+(typeof window.screenY != 'undefined' ? window.screenY : window.screenTop))); return false">
                            <xsl:if test="$titre!=''">
                                <xsl:attribute name="title"><xsl:value-of select="$titre"/></xsl:attribute>
                            </xsl:if>
                            <xsl:choose>
                                <xsl:when test="$format='PNG' or $format='GIF' or $format='JPEG'">
                                    <img src="{$prefixe}pages_{$label-fichiers}/images_icone/{$nomfichier}" width="{@largeur2}" height="{@hauteur2}" alt="{$nomfichier}" style="float: right" class="iconeimage"/>
                                </xsl:when>
                                <xsl:when test="$format='MPEG' or $format='MPEG4' or $format='OGG' or $format='WEBM'">
                                    <img src="{$prefixe}{$interface}/video.png" width="50" height="50" alt="vidéo" style="float: right" border="0"/>
                                </xsl:when>
                                <xsl:otherwise><xsl:value-of select="$messages/message[@label='format inconnu (vérifier l_extension du nom du fichier)']"/></xsl:otherwise>
                            </xsl:choose>
                        </a>
                    </xsl:for-each> <!-- IMAGE -->
                    <!-- html-figure est maintenant appelé plus haut pour éviter les imbrications de ecrire-fichier -->
                </xsl:for-each> <!--  FICHIER -->
            </xsl:when>
            <xsl:when test="$localisation!='page' or $affichage='impression'">
                <xsl:call-template name="contenu-envimage">
                    <xsl:with-param name="affichage" select="$affichage"/>
                    <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                    <xsl:with-param name="interface"><xsl:value-of select="$interface"/></xsl:with-param>
                </xsl:call-template>
            </xsl:when>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template name="script-imagettes">
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:param name="prefixe">../</xsl:param>
        <script type="text/javascript" language="JavaScript">
            <xsl:comment>
    function imagette(n) {
        var imagettes = document.getElementById('imagettes');
        if (imagettes != null) {
            var liste = imagettes.getElementsByTagName('a');
            for (var i=0; i &lt; liste.length; i++) {
                if (i+1 == n) {
                    var img = liste[i].getElementsByTagName('img')[0];
                    img.src = '<xsl:value-of select="concat($prefixe, $interface)"/>/imagesel.png';
                    var div = document.getElementById('figure'+(i+1));
                    div.style.display = 'block';
                } else {
                    var img = liste[i].getElementsByTagName('img')[0];
                    img.src = '<xsl:value-of select="concat($prefixe, $interface)"/>/image.png';
                    var div = document.getElementById('figure'+(i+1));
                    div.style.display='none';
                }
            }
        }
    }
            //</xsl:comment>
        </script>
    </xsl:template>
    
    
    <!-- création de l'encart pour la figure en page, appelé par contenu-body -->
    <xsl:template name="figures-page">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <div class="zone_figure">
            <xsl:if test="count(.//ENVIMAGE[@localisation='page']) &gt; 1">
                <div id="imagettes">
                    <xsl:for-each select=".//ENVIMAGE[@localisation='page']">
                        <a href="#" onclick="imagette({position()}); return false;">
                            <xsl:if test="@titre!=''"><xsl:attribute name="title"><xsl:value-of select="@titre"/></xsl:attribute></xsl:if>
                            <xsl:choose>
                                <xsl:when test="position()=1">
                                    <img src="{$prefixe}{$interface}/imagesel.png" width="59" height="52" alt="image {position()}" border="0"/>
                                </xsl:when>
                                <xsl:otherwise>
                                    <img src="{$prefixe}{$interface}/image.png" width="59" height="52" alt="image {position()}" border="0"/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </a>
                    </xsl:for-each>
                </div>
            </xsl:if>
            <xsl:for-each select=".//ENVIMAGE[@localisation='page']">
                <xsl:call-template name="contenu-envimage">
                    <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                </xsl:call-template>
            </xsl:for-each>
        </div>
    </xsl:template>
    
    <xsl:template name="largeur-fichier">
        <!-- utilisé pour obtenir largeur-max -->
        <xsl:param name="affichage">web</xsl:param>
        <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
        <xsl:variable name="nomfichier" select="@nom"/>
        <xsl:for-each select="$images/IMAGE[@contrib=$label-fichiers and @nom=$nomfichier and @localisation='texte'][1]">
            <xsl:choose>
                <xsl:when test="$affichage='impression' and @anim='oui' and @largeur1*2 &gt;= 700">700</xsl:when>
                <xsl:when test="$affichage='impression' and @anim='oui' and @largeur1*2 &lt; 700"><xsl:value-of select="@largeur1*2"/></xsl:when>
                <xsl:when test="@redim='oui'"><xsl:value-of select="@largeur2"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="@largeur1"/></xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>
    
    <xsl:template name="largeur-max">
        <xsl:param name="affichage">web</xsl:param>
        <xsl:variable name="largeur-ce-fichier">
            <xsl:call-template name="largeur-fichier">
                <xsl:with-param name="affichage" select="$affichage"/>
            </xsl:call-template>
        </xsl:variable>
        <xsl:choose>
            <xsl:when test="following-sibling::FICHIER">
                <xsl:variable name="max-suivants">
                    <xsl:for-each select="following-sibling::FICHIER[1]">
                        <xsl:call-template name="largeur-max">
                            <xsl:with-param name="affichage" select="$affichage"/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:variable>
                <xsl:choose>
                    <xsl:when test="$largeur-ce-fichier &gt; $max-suivants"><xsl:value-of select="$largeur-ce-fichier"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$max-suivants"/></xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise><xsl:value-of select="$largeur-ce-fichier"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="contenu-envimage">
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:variable name="classe-div"><xsl:choose>
            <xsl:when test="@localisation='page' and $affichage='web'">cadre_zone_figure</xsl:when>
            <xsl:otherwise>cadre_figure_texte</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:if test="$classe-div='cadre_figure_texte' and @label!=''">
            <a name="{@label}"/>
        </xsl:if>
        <div class="{$classe-div}">
            <xsl:if test="@localisation='page' and $affichage='web'">
                <xsl:variable name="pos"><xsl:number level="any" from="PAGE" count="ENVIMAGE[@localisation='page']"/></xsl:variable>
                <xsl:attribute name="id">figure<xsl:value-of select="$pos"/></xsl:attribute>
                <xsl:if test="$pos &gt; 1">
                    <xsl:attribute name="style">display:none;</xsl:attribute>
                </xsl:if>
            </xsl:if>
            <xsl:if test="$affichage='impression' or @localisation='texte'">
                <xsl:variable name="largeur-max">
                    <xsl:for-each select=".//FICHIER[1]">
                        <xsl:call-template name="largeur-max">
                            <xsl:with-param name="affichage" select="$affichage"/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:variable>
                <xsl:variable name="largeur-div">
                    <xsl:choose>
                        <xsl:when test="$largeur-max &gt;= 500"><xsl:value-of select="$largeur-max + 20"/></xsl:when>
                        <xsl:otherwise>520</xsl:otherwise>
                    </xsl:choose>
                </xsl:variable>
                <xsl:attribute name="style">width: <xsl:value-of select="$largeur-div"/>px</xsl:attribute>
            </xsl:if>
            <xsl:if test="@titre!=''">
                <div class="titrefigure"><xsl:value-of select="@titre"/></div>
            </xsl:if>
            
            <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
            <xsl:variable name="localisation"><xsl:choose>
                <xsl:when test="$affichage='impression'">texte</xsl:when>
                <xsl:otherwise><xsl:value-of select="@localisation"/></xsl:otherwise>
            </xsl:choose></xsl:variable>
            <xsl:for-each select="FICHIER|FORMATS">
                <xsl:choose>
                    <xsl:when test="self::FICHIER">
                        <xsl:call-template name="html-fichier-redim">
                            <xsl:with-param name="affichage" select="$affichage"/>
                            <xsl:with-param name="prefixe" select="$prefixe"/>
                            <xsl:with-param name="interface" select="$interface"/>
                            <xsl:with-param name="localisation" select="$localisation"/>
                            <xsl:with-param name="label-fichiers" select="$label-fichiers"/>
                        </xsl:call-template>
                    </xsl:when> <!-- FICHIER -->
                    <xsl:otherwise> <!-- FORMATS -->
                        <xsl:variable name="format1"><xsl:call-template name="format-fichier">
                            <xsl:with-param name="nom" select="FICHIER[1]/@nom"/>
                        </xsl:call-template></xsl:variable>
                        <xsl:choose>
                            <!-- si le premier est une image, on affiche simplement la première image -->
                            <xsl:when test="$format1='PNG' or $format1='GIF' or $format1='JPEG'">
                                <xsl:for-each select="FICHIER[1]">
                                    <xsl:call-template name="html-fichier-redim">
                                        <xsl:with-param name="affichage" select="$affichage"/>
                                        <xsl:with-param name="prefixe" select="$prefixe"/>
                                        <xsl:with-param name="interface" select="$interface"/>
                                        <xsl:with-param name="localisation" select="$localisation"/>
                                        <xsl:with-param name="label-fichiers" select="$label-fichiers"/>
                                    </xsl:call-template>
                                </xsl:for-each> <!-- FICHIER[1] -->
                            </xsl:when>
                            <xsl:otherwise> <!-- si c'est une vidéo, on utilise l'élément video -->
                                <xsl:apply-templates select=".">
                                    <xsl:with-param name="prefixe" select="$prefixe"/>
                                    <xsl:with-param name="localisation" select="$localisation"/>
                                    <xsl:with-param name="label-fichiers" select="$label-fichiers"/>
                                </xsl:apply-templates>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:for-each> <!-- FICHIER|FORMATS -->
            <xsl:apply-templates select="LEGENDE|CREDIT">
                <xsl:with-param name="prefixe" select="$prefixe"/>
            </xsl:apply-templates>
        </div>
    </xsl:template>
    
    <!-- création du fichier HTML pour la grande image -->
    <!-- appelé sur un élément ENVIMAGE -->
    <xsl:template name="html-figure">
        <xsl:param name="interface"/>
        <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
        <xsl:variable name="prefixe">../../</xsl:variable>
        <xsl:variable name="numero"><xsl:number level="any" from="XPAGES[@contribution='oui']" count="ENVIMAGE"/></xsl:variable>
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'pages_', $label-fichiers, $sep, 'html_images', $sep, 'envimage', $numero, '.html')"/>
            <xsl:with-param name="contenu">
                <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                <html>
                <head>
                    <title><xsl:value-of select="@titre"/></title>
                    <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/{$feuille-de-style}"/>
                    <xsl:variable name="formats">|<xsl:for-each select="FICHIER">
                        <xsl:call-template name="format-fichier">
                            <xsl:with-param name="nom" select="@nom"/>
                        </xsl:call-template><xsl:text>|</xsl:text>
                    </xsl:for-each></xsl:variable>
                    <xsl:if test="contains($formats, '|MPEG4|') or contains($formats, '|OGG|') or contains($formats, '|WEBM|')">
                        <xsl:call-template name="script-video">
                            <xsl:with-param name="prefixe" select="$prefixe"/>
                        </xsl:call-template>
                    </xsl:if>
                </head>
                <body>
                    <xsl:if test="@titre!=''">
                        <div class="titrefigure"><xsl:value-of select="@titre"/></div>
                    </xsl:if>
                    <div style="text-align: center">
                        <xsl:variable name="localisation" select="@localisation"/>
                        <xsl:for-each select="FICHIER|FORMATS">
                            <xsl:choose>
                                <xsl:when test="self::FICHIER">
                                    <xsl:variable name="cheminfichier" select="@nom"/>
                                    <xsl:variable name="format"><xsl:call-template name="format-fichier">
                                        <xsl:with-param name="nom" select="@nom"/>
                                    </xsl:call-template></xsl:variable>
                                    <xsl:for-each select="$images/IMAGE[@contrib=$label-fichiers and @nom=$cheminfichier and @localisation=$localisation][1]">
                                        <xsl:call-template name="html-fichier">
                                            <xsl:with-param name="affichage" select="'web'"/>
                                            <xsl:with-param name="prefixe" select="$prefixe"/>
                                            <xsl:with-param name="interface" select="$interface"/>
                                            <xsl:with-param name="label-fichiers" select="$label-fichiers"/>
                                            <xsl:with-param name="cheminfichier" select="$cheminfichier"/>
                                            <xsl:with-param name="format" select="$format"/>
                                            <xsl:with-param name="largeur" select="@largeur1"/>
                                            <xsl:with-param name="hauteur" select="@hauteur1"/>
                                        </xsl:call-template>
                                    </xsl:for-each> <!-- IMAGE -->
                                </xsl:when> <!-- FICHIER -->
                                <xsl:otherwise> <!-- FORMATS -->
                                    <xsl:variable name="format1"><xsl:call-template name="format-fichier">
                                        <xsl:with-param name="nom" select="FICHIER[1]/@nom"/>
                                    </xsl:call-template></xsl:variable>
                                    <xsl:choose>
                                        <!-- si le premier est une image, on affiche simplement la première image -->
                                        <xsl:when test="$format1='PNG' or $format1='GIF' or $format1='JPEG'">
                                            <xsl:for-each select="FICHIER[1]">
                                                <xsl:variable name="cheminfichier" select="@nom"/>
                                                <xsl:variable name="format"><xsl:call-template name="format-fichier">
                                                    <xsl:with-param name="nom" select="@nom"/>
                                                </xsl:call-template></xsl:variable>
                                                <xsl:for-each select="$images/IMAGE[@contrib=$label-fichiers and @nom=$cheminfichier and @localisation=$localisation][1]">
                                                    <xsl:call-template name="html-fichier">
                                                        <xsl:with-param name="affichage" select="'web'"/>
                                                        <xsl:with-param name="prefixe" select="$prefixe"/>
                                                        <xsl:with-param name="interface" select="$interface"/>
                                                        <xsl:with-param name="label-fichiers" select="$label-fichiers"/>
                                                        <xsl:with-param name="cheminfichier" select="$cheminfichier"/>
                                                        <xsl:with-param name="format" select="$format"/>
                                                        <xsl:with-param name="largeur" select="@largeur1"/>
                                                        <xsl:with-param name="hauteur" select="@hauteur1"/>
                                                    </xsl:call-template>
                                                </xsl:for-each> <!-- IMAGE -->
                                            </xsl:for-each> <!-- FICHIER[1] -->
                                        </xsl:when>
                                        <xsl:otherwise> <!-- si c'est une vidéo, on utilise l'élément video -->
                                            <xsl:apply-templates select=".">
                                                <xsl:with-param name="prefixe" select="$prefixe"/>
                                                <xsl:with-param name="localisation" select="$localisation"/>
                                                <xsl:with-param name="label-fichiers" select="$label-fichiers"/>
                                            </xsl:apply-templates>
                                        </xsl:otherwise>
                                    </xsl:choose>
                                </xsl:otherwise>
                            </xsl:choose>
                        </xsl:for-each> <!-- FICHIER|FORMATS -->
                    </div>
                    <xsl:apply-templates select="LEGENDE">
                        <xsl:with-param name="prefixe" select="$prefixe"/>
                    </xsl:apply-templates>
                    <xsl:apply-templates select="CREDIT">
                        <xsl:with-param name="prefixe" select="$prefixe"/>
                    </xsl:apply-templates>
                </body>
                </html>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template name="script-video">
        <xsl:param name="prefixe">../</xsl:param>
        
        <xsl:variable name="htmlversplayer" select="concat($prefixe, 'interface/applications/player.swf')"/>
        <xsl:variable name="codebaseverscortado" select="'interface/applications/cortado.jar'"/>
        <xsl:variable name="htmlversvideojs" select="concat($prefixe, 'interface/applications/video.js')"/>
        
        <script type="text/javascript" src="{$htmlversvideojs}?player={$htmlversplayer}&amp;codebase={$prefixe}&amp;archive={$codebaseverscortado}"></script>
    </xsl:template>
    
    <!-- code HTML pour l'affichage d'un fichier potentiellement redimensionné -->
    <xsl:template name="html-fichier-redim">
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:param name="localisation">texte</xsl:param>
        <xsl:param name="label-fichiers"/>
        
        <xsl:variable name="nomfichier" select="@nom"/>
        <xsl:variable name="format"><xsl:call-template name="format-fichier">
            <xsl:with-param name="nom" select="$nomfichier"/>
        </xsl:call-template></xsl:variable>
        <xsl:variable name="numero"><xsl:number level="any" from="XPAGES[@contribution='oui']" count="ENVIMAGE"/></xsl:variable>
        <xsl:for-each select="$images/IMAGE[@contrib=$label-fichiers and @nom=$nomfichier and @localisation=$localisation][1]">
            <xsl:choose>
                <xsl:when test="$affichage='impression' and @anim='oui'">
                    <img alt="{$nomfichier}" src="{$prefixe}pages_{$label-fichiers}/images_anims/{$nomfichier}"/>
                </xsl:when>
                <xsl:when test="@redim='oui'">
                    <xsl:variable name="href" select="concat($prefixe, 'pages_', $label-fichiers, '/', 'html_images/envimage', $numero, '.html')"/>
                    <xsl:choose>
                        <xsl:when test="$format='PNG' or $format='GIF' or $format='JPEG'">
                            <xsl:variable name="largeur_fenetre"><xsl:choose>
                                <xsl:when test="@largeur1 &lt; 460">500</xsl:when>
                                <xsl:otherwise><xsl:value-of select="@largeur1+40"/></xsl:otherwise>
                            </xsl:choose></xsl:variable>
                            <a href="{$href}" target="_blank" onclick="window.open('{$href}', '', 'scrollbars=yes,scrolling=auto,toolbar=no,directories=no,menubar=no,status=no,resizable=no,width={$largeur_fenetre},height={@hauteur1+200},left=' + (100+(typeof window.screenX != 'undefined' ? window.screenX : window.screenLeft)) + ',top=' + (100+(typeof window.screenY != 'undefined' ? window.screenY : window.screenTop))); return false"><img alt="{$nomfichier}" src="{$prefixe}pages_{$label-fichiers}/images_{$localisation}/{$nomfichier}" width="{@largeur2}" height="{@hauteur2}" border="0"/></a>
                        </xsl:when>
                        <!-- les vidéos ne sont jamais redimensionnées -->
                        <xsl:otherwise><xsl:value-of select="$messages/message[@label='format inconnu (vérifier l_extension du nom du fichier)']"/></xsl:otherwise>
                    </xsl:choose>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="html-fichier">
                        <xsl:with-param name="affichage" select="$affichage"/>
                        <xsl:with-param name="prefixe" select="$prefixe"/>
                        <xsl:with-param name="interface" select="$interface"/>
                        <xsl:with-param name="label-fichiers" select="$label-fichiers"/>
                        <xsl:with-param name="cheminfichier" select="$nomfichier"/>
                        <xsl:with-param name="format" select="$format"/>
                        <xsl:with-param name="largeur" select="@largeur1"/>
                        <xsl:with-param name="hauteur" select="@hauteur1"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each> <!-- IMAGE -->
    </xsl:template>
    
    <!-- code HTML pour l'affichage d'un fichier, sans lien -->
    <xsl:template name="html-fichier">
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:param name="label-fichiers"/>
        <xsl:param name="cheminfichier"/> <!-- @nom -->
        <xsl:param name="format"/>
        <xsl:param name="largeur"/>
        <xsl:param name="hauteur"/>
        
        <xsl:variable name="nomfichier"><xsl:call-template name="nom-fichier">
            <xsl:with-param name="chemin" select="$cheminfichier"/>
        </xsl:call-template></xsl:variable>
        <xsl:variable name="chemindepuishtml" select="concat($prefixe, 'pages_', $label-fichiers, '/', $cheminfichier)"/>
        
        <xsl:choose>
            <xsl:when test="$format='PNG' or $format='GIF' or $format='JPEG'">
                <img alt="{$nomfichier}" src="{$chemindepuishtml}" width="{$largeur}" height="{$hauteur}"/>
            </xsl:when>
            <xsl:when test="$format='MPEG' and $affichage='web'">
                <embed src="{$chemindepuishtml}" width="{$largeur}" height="{$hauteur}">
                    <noembed><xsl:value-of select="$messages/message[@label='Erreur: ce navigateur ne gère pas EMBED']"/></noembed>
                </embed>
            </xsl:when>
            <xsl:when test="$affichage='web' and ($format='MPEG4' or $format='OGG' or $format='WEBM')">
                <video src="{$chemindepuishtml}" controls="controls" width="{$largeur}" height="{$hauteur}"/>
            </xsl:when>
            <xsl:when test="($format='MPEG' or $format='MPEG4' or $format='OGG' or $format='WEBM') and $affichage!='web'">
                <p><img src="{$prefixe}{$interface}/video.png" width="50" height="50" alt="vidéo {$format}" border="0"/></p>
            </xsl:when>
            <xsl:otherwise><xsl:value-of select="$messages/message[@label='format inconnu (vérifier l_extension du nom du fichier)']"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <!-- code HTML pour l'affichage d'un élément FORMATS avec des vidéos -->
    <xsl:template match="FORMATS">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="localisation">texte</xsl:param>
        <xsl:param name="label-fichiers"/>
        
        <xsl:variable name="nomfichier1" select="FICHIER[1]/@nom"/>
        <xsl:variable name="largeur" select="$images/IMAGE[@contrib=$label-fichiers and @nom=$nomfichier1 and @localisation=$localisation][1]/@largeur1"/>
        <xsl:variable name="hauteur" select="$images/IMAGE[@contrib=$label-fichiers and @nom=$nomfichier1 and @localisation=$localisation][1]/@hauteur1"/>
        <video controls="controls" width="{$largeur}" height="{$hauteur}">
            <xsl:for-each select="FICHIER">
                <xsl:variable name="format"><xsl:call-template name="format-fichier">
                    <xsl:with-param name="nom" select="@nom"/>
                </xsl:call-template></xsl:variable>
                <xsl:variable name="mime"><xsl:choose>
                    <xsl:when test="$format='MPEG4'">video/mp4</xsl:when>
                    <xsl:when test="$format='OGG'">video/ogg</xsl:when>
                    <xsl:when test="$format='WEBM'">video/webm</xsl:when>
                </xsl:choose></xsl:variable>
                <source src="{$prefixe}pages_{$label-fichiers}/{@nom}" type="{$mime}"/>
            </xsl:for-each>
        </video>
    </xsl:template>
    
    
    <xsl:template match="FICHIER">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
        <img alt="{@nom}" src="{$prefixe}pages_{$label-fichiers}/{@nom}"/>
    </xsl:template>
    
    
    <xsl:template match="LEGENDE">
        <xsl:param name="prefixe">../</xsl:param>
        <div class="legende">
            <xsl:apply-templates>
                <xsl:with-param name="prefixe" select="$prefixe"/>
            </xsl:apply-templates>
        </div>
    </xsl:template>
    
    
    <xsl:template match="CREDIT">
        <xsl:param name="prefixe">../</xsl:param>
        <div class="credit"><em><xsl:value-of select="$messages/message[@label='Crédit :']"/> </em><xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
        </xsl:apply-templates></div>
    </xsl:template>
    
    
    <xsl:template match="APPLICATION">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        
        <xsl:variable name="icone"><xsl:choose>
            <xsl:when test="@type='applet'">applet.png</xsl:when>
            <xsl:when test="@type='flash'">flash.png</xsl:when>
            <xsl:otherwise>application.png</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:choose>
            <xsl:when test="$affichage='impression'">
                <p><xsl:if test="@titre!=''"><xsl:value-of select="concat(@titre, ' ')"/></xsl:if><img src="{$prefixe}{$interface}/{$icone}" width="50" height="50" border="0" alt="{$icone}"/></p>
            </xsl:when>
            <xsl:when test="@localisation='icône'">
                <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
                <xsl:variable name="rep" select="concat($chemin-site, '/pages_', $label-fichiers)"/>
                <xsl:variable name="numero"><xsl:number level="any" from="XPAGES[@contribution='oui']" count="APPLICATION|APPLET|FLASH"/></xsl:variable>
                <xsl:variable name="nomfichiermedia"><xsl:value-of select="concat('media',$numero)"/>.html</xsl:variable>
                <xsl:variable name="fichiermedia"><xsl:value-of select="concat($rep,$sep,$nomfichiermedia)"/></xsl:variable>
                <xsl:variable name="largeur_fenetre"><xsl:choose>
                    <xsl:when test="@largeur &lt; 450">500</xsl:when>
                    <xsl:otherwise><xsl:value-of select="@largeur + 50"/></xsl:otherwise>
                </xsl:choose></xsl:variable>
                <xsl:variable name="hauteur_fenetre"><xsl:choose>
                    <xsl:when test="LEGENDE"><xsl:value-of select="@hauteur + 200"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="@hauteur + 100"/></xsl:otherwise>
                </xsl:choose></xsl:variable>
                <div style="text-align: center; margin: 1em"><xsl:if test="@titre!=''"><xsl:value-of select="concat(@titre, ' ')"/></xsl:if><a href="{$nomfichiermedia}" target="_blank" onclick="window.open('{$nomfichiermedia}', '', 'scrollbars=yes,scrolling=auto,resizable=yes,toolbar=no,directories=no,menubar=no,status=no,width={$largeur_fenetre},height={$hauteur_fenetre},left=' + (100+(typeof window.screenX != 'undefined' ? window.screenX : window.screenLeft)) + ',top=' + (100+(typeof window.screenY != 'undefined' ? window.screenY : window.screenTop))); return false"><img src="{$prefixe}{$interface}/{$icone}" width="50" height="50" border="0" alt="{$icone}" style="vertical-align: middle"/></a></div>
                <xsl:call-template name="ecrire-fichier">
                    <xsl:with-param name="fichier" select="$fichiermedia"/>
                    <xsl:with-param name="contenu">
                        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                        <html>
                        <head>
                            <meta name="viewport" content="width={$largeur_fenetre},maximum-scale=1.0"/>
                            <title><xsl:choose>
                                <xsl:when test="@titre!=''"><xsl:value-of select="@titre"/></xsl:when>
                                <xsl:otherwise><xsl:value-of select="concat('application ',$numero)"/></xsl:otherwise>
                            </xsl:choose></title>
                            <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/{$feuille-de-style}"/>
                        </head>
                        <body>
                            <xsl:call-template name="contenu-media">
                                <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                            </xsl:call-template>
                        </body>
                        </html>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="@localisation='page'">
                <div class="zone_figure">
                    <xsl:call-template name="contenu-media">
                        <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                    </xsl:call-template>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="contenu-media">
                    <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="APPLET|FLASH">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        
        <xsl:choose>
            <xsl:when test="$affichage='impression'">
                <xsl:variable name="icone"><xsl:choose>
                    <xsl:when test="self::APPLET">applet.png</xsl:when>
                    <xsl:when test="self::FLASH">flash.png</xsl:when>
                </xsl:choose></xsl:variable>
                <p><img src="{$prefixe}{$interface}/{$icone}" width="45" height="45" border="0" alt="{$icone}"/></p>
            </xsl:when>
            <xsl:when test="@localisation='icône'">
                <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
                <xsl:variable name="rep" select="concat($chemin-site, '/', 'pages_', $label-fichiers)"/>
                <xsl:variable name="numero"><xsl:number level="any" from="XPAGES[@contribution='oui']" count="APPLICATION|APPLET|FLASH"/></xsl:variable>
                <xsl:variable name="nomfichiermedia"><xsl:value-of select="concat('media',$numero)"/>.html</xsl:variable>
                <xsl:variable name="fichiermedia"><xsl:value-of select="concat($rep,$sep,$nomfichiermedia)"/></xsl:variable>
                <xsl:variable name="icone"><xsl:choose>
                    <xsl:when test="self::APPLET">applet.png</xsl:when>
                    <xsl:when test="self::FLASH">flash.png</xsl:when>
                </xsl:choose></xsl:variable>
                <br/><a href="{$nomfichiermedia}" target="_blank"><img src="{$prefixe}{$interface}/{$icone}" width="45" height="45" border="0" alt="{$icone}"/></a>
                <xsl:call-template name="ecrire-fichier">
                    <xsl:with-param name="fichier" select="$fichiermedia"/>
                    <xsl:with-param name="contenu">
                        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                        <html>
                        <head>
                            <title><xsl:value-of select="concat('applet ',$numero)"/></title>
                            <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/{$feuille-de-style}"/>
                        </head>
                        <body>
                            <xsl:call-template name="contenu-media">
                                <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                            </xsl:call-template>
                        </body>
                        </html>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:when>
            <xsl:when test="@localisation='page'">
                <div class="zone_figure">
                    <xsl:call-template name="contenu-media">
                        <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                    </xsl:call-template>
                </div>
            </xsl:when>
            <xsl:otherwise>
                <xsl:call-template name="contenu-media">
                    <xsl:with-param name="prefixe"><xsl:value-of select="$prefixe"/></xsl:with-param>
                </xsl:call-template>
            </xsl:otherwise>
        </xsl:choose>
        <br/>
    </xsl:template>
    
    <xsl:template name="contenu-media">
        <xsl:param name="prefixe">../</xsl:param>
        
        <xsl:variable name="classe-div"><xsl:choose>
            <xsl:when test="@localisation='page'">cadre_zone_figure</xsl:when>
            <xsl:otherwise>cadre_applet_texte</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <div class="{$classe-div}">
            <xsl:if test="@localisation!='page'">
                <xsl:attribute name="style">width: <xsl:value-of select="@largeur + 12"/>px;</xsl:attribute>
            </xsl:if>
            <xsl:if test="@titre!=''">
                <div class="titrefigure"><xsl:value-of select="@titre"/></div>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="self::APPLICATION and @type='applet'">
                    <xsl:variable name="fichier-application">
                        <xsl:choose>
                            <xsl:when test="@contribution!=''"><xsl:value-of select="concat('pages_', @contribution, '/', @fichier)"/></xsl:when>
                            <xsl:otherwise><xsl:value-of select="@fichier"/></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <applet code="{PARAM[@nom='code']/@valeur}" archive="{$fichier-application}" width="{@largeur}" height="{@hauteur}">
                        <xsl:if test="@contribution!=''">
                            <xsl:attribute name="codebase"><xsl:value-of select="$prefixe"/></xsl:attribute>
                        </xsl:if>
                        <xsl:apply-templates select="PARAM"/>
                        <xsl:value-of select="$messages/message[@label='Java n_est pas activé.']"/>
                    </applet>
                </xsl:when>
                <xsl:when test="self::APPLICATION and @type='flash'">
                    <xsl:variable name="fichier-application">
                        <xsl:choose>
                            <xsl:when test="@contribution!=''"><xsl:value-of select="concat($prefixe, 'pages_', @contribution, '/', @fichier)"/></xsl:when>
                            <xsl:otherwise><xsl:value-of select="@fichier"/></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <object type="application/x-shockwave-flash" data="{$fichier-application}" width="{@largeur}" height="{@hauteur}" title="{@titre}">
                        <param name="movie" value="{@fichier}"/>
                        <xsl:variable name="parametres"><xsl:for-each select="PARAM">
                            <xsl:value-of select="concat(@nom,'=',@valeur,'&#38;')" />
                        </xsl:for-each></xsl:variable>
                        <xsl:if test="$parametres != ''">
                            <param name="flashvars" value="{$parametres}"/>
                        </xsl:if>
                        <xsl:value-of select="$messages/message[@label='Erreur: plugin Flash absent ou désactivé ?']"/>
                    </object>
                </xsl:when>
                <xsl:when test="self::APPLICATION and @type='html'">
                    <xsl:variable name="fichier-application">
                        <xsl:choose>
                            <xsl:when test="@contribution!=''"><xsl:value-of select="concat($prefixe, 'pages_', @contribution, '/', @fichier)"/></xsl:when>
                            <xsl:otherwise><xsl:value-of select="@fichier"/></xsl:otherwise>
                        </xsl:choose>
                    </xsl:variable>
                    <xsl:variable name="chemin-fichiers"><xsl:call-template name="chemin-inverse">
                            <xsl:with-param name="chemin"><xsl:value-of select="@fichier"/></xsl:with-param>
                        </xsl:call-template><xsl:if test="@contribution!=''">../pages_<xsl:value-of select="ancestor::XPAGES[1]/@labelfichiers"/>/</xsl:if></xsl:variable>
                    <xsl:variable name="lienhtml">
                        <xsl:value-of select="concat($fichier-application, '?chemin_fichiers=', $chemin-fichiers)"/>
                        <xsl:for-each select="PARAM">&amp;<xsl:value-of select="concat(@nom, '=', @valeur)"/></xsl:for-each>
                    </xsl:variable>
                    <iframe src="{$lienhtml}" width="{@largeur}" height="{@hauteur}" style="border:none" frameborder="0"/>
                </xsl:when>
                
                <xsl:when test="self::APPLET">
                    <applet code="{@classe}" archive="{@archive}" width="{@largeur}" height="{@hauteur}">
                        <xsl:apply-templates select="PARAM"/>
                        <xsl:value-of select="$messages/message[@label='Java n_est pas activé.']"/>
                    </applet>
                </xsl:when>
                <xsl:when test="self::FLASH">
                    <object type="application/x-shockwave-flash" data="{@fichier}" width="{@largeur}" height="{@hauteur}" title="{@titre}">
                        <param name="movie" value="{@fichier}"/>
                        <xsl:variable name="parametres"><xsl:for-each select="PARAM">
                            <xsl:value-of select="concat(@nom,'=',@valeur,'&#38;')" />
                        </xsl:for-each></xsl:variable>
                        <xsl:if test="$parametres != ''">
                            <param name="flashvars" value="{$parametres}"/>
                        </xsl:if>
                        <xsl:value-of select="$messages/message[@label='Erreur: plugin Flash absent ou désactivé ?']"/>
                    </object>
                </xsl:when>
            </xsl:choose>
            <xsl:apply-templates select="LEGENDE"/>
            <xsl:apply-templates select="CREDIT"/>
        </div>
    </xsl:template>
    
    <xsl:template name="chemin-inverse">
    <!-- renvoie '../..' pour le chemin 'a/b/c' -->
    <!-- cas non résolus: chemins '/a' et '../a' -->
        <xsl:param name="chemin"/>
        <xsl:variable name="before" select="substring-before($chemin, '/')"/>
        <xsl:variable name="after" select="substring-after($chemin, '/')"/>
        <xsl:choose>
            <xsl:when test="$before='' and $after=''"></xsl:when>
            <xsl:when test="$before='.'"><xsl:call-template name="chemin-inverse">
                <xsl:with-param name="chemin"><xsl:value-of select="$after"/></xsl:with-param>
            </xsl:call-template></xsl:when>
            <xsl:otherwise><xsl:call-template name="chemin-inverse">
                <xsl:with-param name="chemin"><xsl:value-of select="$after"/></xsl:with-param>
            </xsl:call-template>../</xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template match="PARAM">
        <param name="{@nom}" value="{@valeur}"/>
    </xsl:template>
    
    
    <xsl:template match="COMMENTAIRE">
        <xsl:if test="$sortie='production'">
            <div class="commentaire">
                <xsl:value-of select="."/>
            </div>
        </xsl:if>
    </xsl:template>
    
    
    <!-- Exercices -->
    
    <xsl:template match="EXERCICE">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <div class="exercice">
            <xsl:call-template name="auteurs-exo"/>
            <xsl:element name="h{count(ancestor::SECTION)+2}">
                <img src="{$prefixe}{$interface}/icones_sections/exercice.png" width="40" height="40" alt="exercice" class="icone_section"/>
                <xsl:choose>
                    <xsl:when test="@titre!=''"><xsl:value-of select="@titre"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$messages/message[@label='Exercice']"/></xsl:otherwise>
                </xsl:choose>
            </xsl:element>
            <xsl:if test="DIFFICULTE | TEMPS">
                <p>
                    <xsl:if test="DIFFICULTE">
                        <xsl:apply-templates select="DIFFICULTE"/>&#xA0;&#xA0;
                    </xsl:if>
                    <xsl:if test="TEMPS">
                        <xsl:value-of select="concat($messages/message[@label='Temps :'], ' ', TEMPS)"/>
                    </xsl:if>
                </p>
            </xsl:if>
            <xsl:apply-templates select="ENONCE">
                <xsl:with-param name="affichage" select="$affichage"/>
                <xsl:with-param name="interface" select="$interface"/>
            </xsl:apply-templates>
            <xsl:apply-templates select="QUESTION">
                <xsl:with-param name="affichage" select="$affichage"/>
            </xsl:apply-templates>
        </div>
    </xsl:template>
    
    <xsl:template name="auteurs-exo">
        <xsl:if test="count(AUTEUR)&gt;0">
            <div class="auteurs">
                <xsl:choose>
                    <xsl:when test="count(AUTEUR)=1">
                        <xsl:value-of select="$messages/message[@label='Auteur:']"/><xsl:text> </xsl:text>
                        <em><xsl:value-of select="AUTEUR"/></em><br/>
                    </xsl:when>
                    <xsl:when test="count(AUTEUR)&gt;1">
                        <xsl:value-of select="$messages/message[@label='Auteurs:']"/><xsl:text> </xsl:text>
                        <em><xsl:for-each select="AUTEUR"><xsl:value-of select="."/><xsl:if test="position()!=last()">, </xsl:if></xsl:for-each></em><br/>
                    </xsl:when>
                </xsl:choose>
            </div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="DIFFICULTE">
        <xsl:value-of select="concat($messages/message[@label='Difficulté :'], ' ')"/>
        <span class="symbole"><xsl:value-of select="translate(., '*', '&#x2606;')"/></span>
    </xsl:template>
    
    <xsl:template match="ENONCE">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
            <xsl:with-param name="affichage" select="$affichage"/>
            <xsl:with-param name="interface" select="$interface"/>
        </xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="QUESTION">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="affichage">web</xsl:param>
        <xsl:variable name="numquestion"><xsl:number level="any" from="EXERCICE" count="QUESTION"/></xsl:variable>
        <div class="question">
            <b><xsl:value-of select="concat($messages/message[@label='Question'], ' ', $numquestion)"/>)</b>
            <xsl:apply-templates select="ENONCE">
                <xsl:with-param name="prefixe" select="$prefixe"/>
                <xsl:with-param name="affichage" select="$affichage"/>
            </xsl:apply-templates>
            <p>
                <xsl:if test="$affichage='web'">
                    <xsl:apply-templates select="AIDE" mode="lien"/>
                </xsl:if>
                <xsl:if test="$affichage='web' and (../@type='auto-évaluation' or $sortie='production' or $sortie='tuteurs')">
                    <xsl:apply-templates select="SOLUTION" mode="lien"/>
                </xsl:if>
                <xsl:apply-templates select="POINTS"/>
            </p>
            <xsl:apply-templates select="AIDE | SOLUTION" mode="afficher-div"/>
        </div>
    </xsl:template>
    
    
    <xsl:template name="script-aide-solution">
        <script language="JavaScript" type="text/javascript">
            <xsl:comment>            
            function afficher_masquer(idlien) {
                
                <xsl:variable name="nb" select="count(.//AIDE)+count(.//SOLUTION)"/>
                var nbAS = <xsl:value-of select="$nb"/>;
            
                <xsl:variable name="numaide"><xsl:number level="any" from="XPAGES[@contribution='oui']" count="AIDE | SOLUTION"/></xsl:variable>
                var pos = <xsl:value-of select="$numaide"/>;
                    
                for (var i=0; i &lt; nbAS; i++) {
                    var a = document.getElementById('idlien'+(pos+i+1));
                    var c = a.className;
                    var div = document.getElementById('idaide'+(pos+i+1));
                    if (('idlien'+(pos+i+1)) == idlien &amp;&amp; div.style.display == "none") {
                        div.style.display = 'block';
                        if (c.indexOf("selectionlien", 0) &lt; 0)
                            a.className = c+' selectionlien';
                    } else {
                        div.style.display = 'none';
                        var t = c.split(' ');
                        if (t != null) {
                            c = t[0];
                            a.className = c;
                        }
                    }
                }
            }
            </xsl:comment>            
        </script>
    </xsl:template>
    
    
    <xsl:template match="AIDE | SOLUTION" mode="afficher-div">
        <xsl:variable name="numaide"><xsl:number level="any" from="XPAGES[@contribution='oui']" count="AIDE | SOLUTION"/></xsl:variable>
        <xsl:variable name="idaide" select="concat('idaide',$numaide)"/>
        <div id="{$idaide}" style="display:none" class="contenu-aide-solution"><xsl:apply-templates/></div>
    </xsl:template>
    
    
    <xsl:template match="AIDE | SOLUTION" mode="lien">
        <xsl:variable name="msglabel"><xsl:choose>
        <xsl:when test="name() = 'AIDE'">Aide</xsl:when>
        <xsl:when test="name() = 'SOLUTION'">Solution</xsl:when>
        </xsl:choose></xsl:variable>
        
        <xsl:variable name="classelien"><xsl:choose>
        <xsl:when test="name() = 'AIDE'">lienaide</xsl:when>
        <xsl:when test="name() = 'SOLUTION'">liensolution</xsl:when>
        </xsl:choose></xsl:variable>
        
        <xsl:variable name="numaide"><xsl:number level="any" from="XPAGES[@contribution='oui']" count="AIDE | SOLUTION"/></xsl:variable>
        <xsl:variable name="nomFichier" select="concat('aide',$numaide,'.html')"/>
        <xsl:variable name="idlien" select="concat('idlien',$numaide)"/>
        <a id="{$idlien}" href="{$nomFichier}" target="_blank" class="{$classelien}" onclick="afficher_masquer('{$idlien}'); return false;"><xsl:value-of select="$messages/message[@label=$msglabel]"/></a>
    </xsl:template>
    
    
    <xsl:template match="SOLUTION">
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:apply-templates>
            <xsl:with-param name="interface" select="$interface"/>
        </xsl:apply-templates>
    </xsl:template>
    
    <xsl:template match="POINTS">
        [<em><xsl:value-of select="concat(., ' ', $messages/message[@label='points'])"/></em>]
    </xsl:template>
    
    
    <xsl:template match="QCM">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <div class="qcm">
            <xsl:call-template name="auteurs-exo"/>
            <xsl:element name="h{count(ancestor::SECTION)+2}">
                <img src="{$prefixe}{$interface}/icones_sections/qcm.png" width="40" height="40" alt="qcm" class="icone_section"/>
                <xsl:choose>
                    <xsl:when test="@titre!=''"><xsl:value-of select="@titre"/></xsl:when>
                    <xsl:otherwise><xsl:value-of select="$messages/message[@label='QCM']"/></xsl:otherwise>
                </xsl:choose>
            </xsl:element>
            <xsl:apply-templates select="ENONCE"/>
            <xsl:if test="DIFFICULTE | TEMPS">
                <p>
                    <xsl:if test="DIFFICULTE">
                        <xsl:apply-templates select="DIFFICULTE"/>&#xA0;&#xA0;
                    </xsl:if>
                    <xsl:if test="TEMPS">
                        <xsl:value-of select="concat($messages/message[@label='Temps :'], ' ', TEMPS)"/>
                    </xsl:if>
                </p>
            </xsl:if>
            <xsl:choose>
                <xsl:when test="$affichage='web'">
                    <xsl:variable name="numqcm"><xsl:number level="any" from="PAGE" count="QCM" /></xsl:variable>
                    <!-- remarque: le onsubmit ne marchera pas sur de vieux navigateurs, mais c'est le seul
                        moyen d'avoir un code valide -->
                    <form name="qcm{$numqcm}" action="#" onsubmit="resultat({$numqcm}); return false">
                        <xsl:apply-templates select="QUESTIONQCM">
                            <xsl:with-param name="affichage" select="$affichage"/>
                        </xsl:apply-templates>
                        <br/>
                        <input value="{$messages/message[@label='Solution']}" type="submit"/>&#xA0;
                    </form>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:apply-templates select="QUESTIONQCM">
                        <xsl:with-param name="affichage" select="$affichage"/>
                    </xsl:apply-templates>
                </xsl:otherwise>
            </xsl:choose>
        </div>
    </xsl:template>
    
<xsl:template name="script-qcm">
    <script language="JavaScript" type="text/javascript">
    <xsl:comment>
    function resultat(numqcm)
    {
        var reponses='';
        
        <xsl:variable name="reponses">
        var formulaire = document.forms['qcm'+numqcm];
        for (var irep=0; irep &lt; formulaire.elements.length; irep++) {
            var checked = formulaire.elements[irep].checked;
            if (checked || formulaire.elements[irep].type == "checkbox") {
                var valrep = formulaire.elements[irep].value;
                var numeros = formulaire.elements[irep].name.substring(1);
                var ind1 = numeros.indexOf('_');
                var numq;
                var numr = '';
                if (ind1 == -1)
                    numq = numeros;
                else {
                    numq = numeros.substring(0, ind1);
                    numr = numeros.substring(ind1 + 1);
                }
                var vrai = (valrep.substring(0,1) == 'V');
                if (checked || vrai) {
                    reponses = reponses + '<xsl:value-of select="$messages/message[@label='Question']"/> ' + numq;
                    if (numr != '')
                        reponses = reponses + ' - ' + numr
                    if (checked &amp;&amp; vrai)
                        reponses = reponses + ' : <xsl:value-of select="$messages/message[@label='juste']"/>';
                    else
                        reponses = reponses + ' : <xsl:value-of select="$messages/message[@label='faux']"/>';
                    if (valrep.substring(1) != '')
                        reponses = reponses + ' : ' + valrep.substring(1);
                    reponses = reponses + '\n';
                }
            }
        }
        </xsl:variable>
        <xsl:value-of select="$reponses"/>
        
        alert(reponses);
    }
    //</xsl:comment>
    <xsl:text>
</xsl:text>
    </script>
</xsl:template>

    <xsl:template match="QUESTIONQCM">
        <xsl:param name="affichage">web</xsl:param>
        <div class="questionqcm">
            <xsl:apply-templates select="ENONCEQCM"/>
            <xsl:apply-templates select="REPONSEQCM"/>
            <br/>
            <xsl:if test="$affichage='web'">
                <xsl:if test="AIDE"><br/></xsl:if>
                <xsl:apply-templates select="AIDE" mode="lien"/>
            </xsl:if>
            <xsl:apply-templates select="AIDE | SOLUTION" mode="afficher-div"/>
        </div>
    </xsl:template>
    
    <xsl:template match="ENONCEQCM">
        <xsl:variable name="numquestion"><xsl:number level="any" from="QCM" count="QUESTIONQCM"/></xsl:variable>
        <!--<div class="enonceqcm">-->
            <xsl:value-of select="$numquestion"/>)&#xA0;<xsl:apply-templates/>
        <!--</div>-->
    </xsl:template>
    
    <xsl:template match="REPONSEQCM">
        <br/>
        <xsl:variable name="vraifaux"><xsl:choose>
            <xsl:when test="@bonne='oui'">V</xsl:when>
            <xsl:when test="@bonne='non'">F</xsl:when>
            <xsl:otherwise><xsl:choose>
                <xsl:when test="COMMENTAIREREP='ok'">V</xsl:when>
                <xsl:otherwise>F</xsl:otherwise>
            </xsl:choose></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:variable name="numqcm"><xsl:number level="any" from="PAGE" count="QCM"/></xsl:variable>
        <xsl:variable name="numquestion"><xsl:number level="any" from="QCM" count="QUESTIONQCM"/></xsl:variable>
        <xsl:variable name="numrep"><xsl:number level="any" from="QUESTIONQCM" count="REPONSEQCM"/></xsl:variable>
        <xsl:variable name="id" select="concat('r',$numqcm,'_',$numquestion,'_',$numrep)"/>
        <xsl:variable name="type"><xsl:choose>
            <xsl:when test="count(../REPONSEQCM[@bonne='oui']) &gt; 1">checkbox</xsl:when>
            <xsl:otherwise>radio</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:variable name="name"><xsl:choose>
            <xsl:when test="$type='radio'"><xsl:value-of select="concat('r',$numquestion)"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="concat('r',$numquestion,'_',$numrep)"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <input type="{$type}" name="{$name}" value="{$vraifaux}{normalize-space(COMMENTAIREREP)}" id="{$id}"/>
        <label for="{$id}">
            <xsl:apply-templates/>
        </label>
    </xsl:template>
    
    <xsl:template match="TEXTEREP">
        <span class="reptexte">
            <xsl:apply-templates/>
        </span>
    </xsl:template>
    
    <xsl:template match="COMMENTAIREREP">
    </xsl:template>
    
    <xsl:template name="fichiers-aide">
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:variable name="prefixe">../</xsl:variable>
        <xsl:variable name="rep" select="concat($chemin-site, $sep, 'pages_', ancestor::XPAGES[1]/@labelfichiers)"/>
        <xsl:for-each select=".//AIDE | .//SOLUTION">
            <xsl:if test="self::AIDE or $sortie='production' or $sortie='tuteurs' or ancestor::EXERCICE/@type='auto-évaluation'">
                <xsl:variable name="numaide">
                    <xsl:number level="any" from="XPAGES[@contribution='oui']" count="AIDE | SOLUTION"/>
                </xsl:variable>
                <xsl:call-template name="ecrire-fichier">
                    <xsl:with-param name="fichier" select="concat($rep,'/aide',$numaide,'.html')"/>
                    <xsl:with-param name="contenu">
                        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                        <html>
                        <head>
                            <title><xsl:value-of select="name()"/></title>
                            <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/style.css"/>
                <!-- ne marche pas avec IE6 (la fenêtre se ferme quand on clique sur la barre de défilement)
                            <script language="JavaScript" type="text/javascript">
                                window.onblur = fermeture;
                                function fermeture() { window.close(); }
                            </script>
                -->
                        </head>
                        <body>
                            <xsl:apply-templates>
                                <xsl:with-param name="interface" select="$interface"/>
                            </xsl:apply-templates>
                        </body>
                        </html>
                    </xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
    
    
    <!-- Eléments mélangés à du texte  -->
    
    <xsl:template match="LIENPAGE">
        <xsl:param name="affichage">web</xsl:param>
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:variable name="url"><xsl:choose>
            <xsl:when test="@contribution='' or not(@contribution)">
                <!-- on remet préfixe/contrib parce-que la contrib actuelle peut être différente dans le cas des fichiers index et impression -->
                <xsl:value-of select="concat($prefixe, 'pages_', ancestor::XPAGES[1]/@labelfichiers, '/', @page, '.html')"/>
            </xsl:when>
            <xsl:when test="@page='' or not(@page)">
                <xsl:for-each select="//XPAGES[INFORMATIONS/LABEL=current()/@contribution][1]/descendant::PAGE[1]">
                    <xsl:value-of select="concat($prefixe,'pages_', ancestor::XPAGES[1]/@labelfichiers, '/', @label,'.html')"/>
                </xsl:for-each>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="concat($prefixe, 'pages_', //XPAGES[INFORMATIONS/LABEL=current()/@contribution][1]/@labelfichiers, '/', @page, '.html')"/>
            </xsl:otherwise>
        </xsl:choose></xsl:variable>
        <a href="{$url}" class="lien"><xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
        </xsl:apply-templates></a>
    </xsl:template>
    
    
    <xsl:template match="LIENWEB">
        <xsl:param name="prefixe">../</xsl:param>
        <a href="{@url}" class="lien" target="_blank"><xsl:apply-templates>
            <xsl:with-param name="prefixe" select="$prefixe"/>
        </xsl:apply-templates></a>
    </xsl:template>
    
    
    <xsl:template match="LIENIMAGE">
        <xsl:variable name="label" select="@label"/>
        <xsl:choose>
            <xsl:when test="ancestor::PAGE[1]//ENVIMAGE[@label=$label]">
                <xsl:variable name="localisation" select="ancestor::PAGE[1]//ENVIMAGE[@label=$label][1]/@localisation"/>
                <xsl:variable name="numero"><xsl:for-each select="ancestor::PAGE[1]//ENVIMAGE[@label=$label][1]">
                    <xsl:number level="any" from="XPAGES[@contribution='oui']" count="ENVIMAGE"/>
                </xsl:for-each></xsl:variable>
                <xsl:choose>
                    <xsl:when test="$localisation='icône'">
                        <xsl:variable name="nom" select="ancestor::PAGE[1]//ENVIMAGE[@label=$label][1]//FICHIER[1]/@nom"/>
                        <xsl:variable name="lien" select="concat('../pages_', ancestor::XPAGES[1]/@labelfichiers, '/', 'html_images/envimage', $numero, '.html')"/>
                        <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
                        <xsl:variable name="largeur1" select="$images/IMAGE[@contrib=$label-fichiers and @nom=$nom]/@largeur1"/>
                        <xsl:variable name="hauteur1" select="$images/IMAGE[@contrib=$label-fichiers and @nom=$nom]/@hauteur1"/>
                        <xsl:variable name="largeur_fenetre"><xsl:choose>
                            <xsl:when test="$largeur1 &lt; 460">500</xsl:when>
                            <xsl:otherwise><xsl:value-of select="$largeur1+40"/></xsl:otherwise>
                        </xsl:choose></xsl:variable>
                        <a href="{$lien}" class="lienimage" target="_blank" onclick="window.open('{$lien}', '', 'scrollbars=yes,scrolling=auto,toolbar=no,directories=no,menubar=no,status=no,width={$largeur_fenetre},height={$hauteur1+200},left=' + (100+(typeof window.screenX != 'undefined' ? window.screenX : window.screenLeft)) + ',top=' + (100+(typeof window.screenY != 'undefined' ? window.screenY : window.screenTop))); return false"><xsl:apply-templates/></a>
                    </xsl:when>
                    <xsl:when test="$localisation='page' and count(ancestor::PAGE[1]//ENVIMAGE[@localisation='page'])=1">
                        <xsl:variable name="nom" select="ancestor::PAGE[1]//ENVIMAGE[@label=$label][1]//FICHIER[1]/@nom"/>
                        <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
                        <xsl:variable name="redim" select="$images/IMAGE[@contrib=$label-fichiers and @nom=$nom and @localisation=$localisation][1]/@redim"/>
                        <xsl:choose>
                            <xsl:when test="$redim='oui'">
                                <xsl:variable name="lien" select="concat('../pages_', ancestor::XPAGES[1]/@labelfichiers, '/', 'html_images/envimage', $numero, '.html')"/>
                                <a href="{$lien}" class="lienimage" target="_blank"><xsl:apply-templates/></a><!-- pas idéal mais mieux que rien -->
                            </xsl:when>
                            <xsl:otherwise>
                                <a href="#{$label}" class="lienimage"><xsl:apply-templates/></a>
                            </xsl:otherwise>
                        </xsl:choose>
                    </xsl:when>
                    <xsl:when test="$localisation='page' and count(ancestor::PAGE[1]//ENVIMAGE[@localisation='page'])&gt;1">
                        <xsl:variable name="position">
                            <xsl:for-each select="ancestor::PAGE[1]//ENVIMAGE[@label=$label][1]">
                                <xsl:number level="any" from="PAGE" count="ENVIMAGE[@localisation='page']"/>
                            </xsl:for-each>
                        </xsl:variable>
                        <a href="#" onclick="imagette({$position}); return false;" class="lienimage"><xsl:apply-templates/></a>
                    </xsl:when>
                    <xsl:otherwise>
                        <a href="#{$label}" class="lienimage"><xsl:apply-templates/></a>
                    </xsl:otherwise>
                </xsl:choose>
            </xsl:when>
            <xsl:otherwise>
                <a href="???" class="lienimage"><xsl:apply-templates/></a>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template name="sortie-symbole">
        <xsl:param name="nomlettre"/>
        <xsl:param name="prefixe"/>
        
        <xsl:variable name="nomsansext" select="substring-before($nomlettre,'.')"/>
        <xsl:choose>
            <!-- pour chaque symbole on donne l'équivalent sous forme de caractère UNICODE,
                ou d'une image pour les caractères qui ne s'affichent pas bien partout -->
            <xsl:when test="$nomsansext='Alpha'">&#x391;</xsl:when>
            <xsl:when test="$nomsansext='Beta'">&#x392;</xsl:when>
            <xsl:when test="$nomsansext='Gamma'">&#x393;</xsl:when>
            <xsl:when test="$nomsansext='Delta'">&#x394;</xsl:when>
            <xsl:when test="$nomsansext='Epsilon'">&#x395;</xsl:when>
            <xsl:when test="$nomsansext='Zeta'">&#x396;</xsl:when>
            <xsl:when test="$nomsansext='Eta'">&#x397;</xsl:when>
            <xsl:when test="$nomsansext='Theta'">&#x398;</xsl:when>
            <xsl:when test="$nomsansext='Iota'">&#x399;</xsl:when>
            <xsl:when test="$nomsansext='Kappa'">&#x39A;</xsl:when>
            <xsl:when test="$nomsansext='Lambda'">&#x39B;</xsl:when>
            <xsl:when test="$nomsansext='Mu'">&#x39C;</xsl:when>
            <xsl:when test="$nomsansext='Nu'">&#x39D;</xsl:when>
            <xsl:when test="$nomsansext='Xi'">&#x39E;</xsl:when>
            <xsl:when test="$nomsansext='Omicron'">&#x39F;</xsl:when>
            <xsl:when test="$nomsansext='Pi'">&#x3A0;</xsl:when>
            <xsl:when test="$nomsansext='Rho'">&#x3A1;</xsl:when>
            <xsl:when test="$nomsansext='Sigma'">&#x3A3;</xsl:when>
            <xsl:when test="$nomsansext='Tau'">&#x3A4;</xsl:when>
            <xsl:when test="$nomsansext='Upsilon'">&#x3A5;</xsl:when>
            <xsl:when test="$nomsansext='Phi'">&#x3A6;</xsl:when>
            <xsl:when test="$nomsansext='Chi'">&#x3A7;</xsl:when>
            <xsl:when test="$nomsansext='Psi'">&#x3A8;</xsl:when>
            <xsl:when test="$nomsansext='Omega'">&#x3A9;</xsl:when>
            <xsl:when test="$nomsansext='alpha'">&#x3B1;</xsl:when>
            <xsl:when test="$nomsansext='beta'">&#x3B2;</xsl:when>
            <xsl:when test="$nomsansext='gamma'">&#x3B3;</xsl:when>
            <xsl:when test="$nomsansext='delta'">&#x3B4;</xsl:when>
            <xsl:when test="$nomsansext='epsilon'">&#x3B5;</xsl:when>
            <xsl:when test="$nomsansext='zeta'">&#x3B6;</xsl:when>
            <xsl:when test="$nomsansext='eta'">&#x3B7;</xsl:when>
            <xsl:when test="$nomsansext='theta'">&#x3B8;</xsl:when>
            <xsl:when test="$nomsansext='iota'">&#x3B9;</xsl:when>
            <!--<xsl:when test="$nomsansext='kappa'">&#x3BA;</xsl:when>  03BA confondu avec 03F0 sur MacOS à cause de la police Times -->
            <xsl:when test="$nomsansext='lambda'">&#x3BB;</xsl:when>
            <xsl:when test="$nomsansext='mu'">&#x3BC;</xsl:when>
            <xsl:when test="$nomsansext='nu'">&#x3BD;</xsl:when>
            <xsl:when test="$nomsansext='xi'">&#x3BE;</xsl:when>
            <xsl:when test="$nomsansext='omicron'">&#x3BF;</xsl:when>
            <xsl:when test="$nomsansext='pi'">&#x3C0;</xsl:when>
            <xsl:when test="$nomsansext='rho'">&#x3C1;</xsl:when>
            <!--<xsl:when test="$nomsansext='sigmaf'">&#x3C2;</xsl:when> pas bien affiché sur Firefox Mac -->
            <xsl:when test="$nomsansext='sigma'">&#x3C3;</xsl:when>
            <xsl:when test="$nomsansext='tau'">&#x3C4;</xsl:when>
            <xsl:when test="$nomsansext='upsilon'">&#x3C5;</xsl:when>
            <!--<xsl:when test="$nomsansext='phi'">&#x3D5;</xsl:when> phi = unicode 03C6 != 03D5, inversé sur MacOS à cause de la police Times, 03D5 ne s'affiche pas sur IE6 XP -->
            <!--<xsl:when test="$nomsansext='phi2'">&#x3C6;</xsl:when> unicode 03C6 != 03D5, inversé sur MacOS -->
            <xsl:when test="$nomsansext='chi'">&#x3C7;</xsl:when>
            <xsl:when test="$nomsansext='psi'">&#x3C8;</xsl:when>
            <xsl:when test="$nomsansext='omega'">&#x3C9;</xsl:when>
            <!--<xsl:when test="$nomsansext='thetasym'">&#x3D1;</xsl:when> pas bien affiché sur IE6 Windows et Firefox Mac -->
            <!--<xsl:when test="$nomsansext='upsih'">&#x3D2;</xsl:when> pas bien affiché sur IE6 Windows -->
            <!--<xsl:when test="$nomsansext='piv'">&#x3D6;</xsl:when> pas bien affiché sur IE6 Windows et Firefox Mac -->
            
            <xsl:when test="$nomsansext='asymp'">&#x2248;</xsl:when>
            <xsl:when test="$nomsansext='cap'">&#x2229;</xsl:when>
            <!-- <xsl:when test="$nomsansext='cup'">&#x222A;</xsl:when> ne s'affiche pas sur IE6 et IE7 XP -->
            <!-- <xsl:when test="$nomsansext='exist'">&#x2203;</xsl:when> ne s'affiche pas sur IE6 et IE7 XP -->
            <!-- <xsl:when test="$nomsansext='forall'">&#x2200;</xsl:when> ne s'affiche pas sur IE6 et IE7 XP -->
            <xsl:when test="$nomsansext='ge'">&#x2265;</xsl:when>
            <xsl:when test="$nomsansext='harr'">&#x2194;</xsl:when>
            <!-- <xsl:when test="$nomsansext='hdarr'">&#x21D4;</xsl:when> ne s'affiche pas sur IE6 et IE7 XP -->
            <xsl:when test="$nomsansext='infin'">&#x221E;</xsl:when>
            <!-- <xsl:when test="$nomsansext='isin'">&#x2208;</xsl:when> ne s'affiche pas sur IE6 et IE7 XP -->
            <xsl:when test="$nomsansext='larr'">&#x2190;</xsl:when>
            <!-- ldarr = symbole HTML lArr ne s'affiche pas avec IE 6 -->
            <xsl:when test="$nomsansext='le'">&#x2264;</xsl:when>
            <!-- <xsl:when test="$nomsansext='nabla'">&#x2207;</xsl:when> ne s'affiche pas sur IE6 et IE7 XP -->
            <xsl:when test="$nomsansext='ne'">&#x2260;</xsl:when>
            <xsl:when test="$nomsansext='not'">&#xAC;</xsl:when>
            <xsl:when test="$nomsansext='part'">&#x2202;</xsl:when>
            <xsl:when test="$nomsansext='plusmn'">&#xB1;</xsl:when>
            <xsl:when test="$nomsansext='rarr'">&#x2192;</xsl:when>
            <!-- <xsl:when test="$nomsansext='rdarr'">&#x21D2;</xsl:when> ne s'affiche pas sur IE6 et IE7 XP -->
            <!-- sim ne s'affiche pas sur IE6 et IE7 XP-->
            <!-- <xsl:when test="$nomsansext='sub'">&#x2282;</xsl:when> ne s'affiche pas sur IE6 et IE7 XP -->
            
            <xsl:otherwise>
                <img src="{$prefixe}interface/{@nom}" border="0" alt="{$nomlettre}" class="maths"/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="SYMBOLE">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:choose>
            <xsl:when test="@nom != ''">
                <xsl:variable name="nomlettre"><xsl:call-template name="nom-fichier"><xsl:with-param name="chemin" select="@nom"/></xsl:call-template></xsl:variable>
                <xsl:call-template name="sortie-symbole">
                    <xsl:with-param name="nomlettre" select="$nomlettre"/>
                    <xsl:with-param name="prefixe" select="$prefixe"/>
                </xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <span class="symbole"><xsl:value-of select="."/></span>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template match="EQUATION|EQUATEX">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:variable name="label-ensemble" select="ancestor::XPAGES[1]/INFORMATIONS/LABEL"/>
        <xsl:variable name="label-fichiers" select="ancestor::XPAGES[1]/@labelfichiers"/>
        <!--
        <xsl:variable name="largeur1" select="$images/IMAGE[@contrib=$label-ensemble and @nom=current()/@image]/@largeur1"/>
        <xsl:variable name="hauteur1" select="$images/IMAGE[@contrib=$label-ensemble and @nom=current()/@image]/@hauteur1"/>
        pour l'instant les dimensions ne sont pas dans ce fichier
        -->
        <xsl:variable name="numero"><xsl:number from="XPAGES" count="EQUATION|EQUATEX" level="any"/></xsl:variable>
        <img alt="{@texte}" src="{$prefixe}pages_{$label-fichiers}/equations_{$label-ensemble}/equation{$numero}.png" class="maths"/>
    </xsl:template>
    
    
    <xsl:template match="GLOSSAIRE">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:variable name="motref"><xsl:choose>
            <xsl:when test="@ref!=''"><xsl:value-of select="@ref"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="@mot"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:variable name="definition"><xsl:choose>
            <xsl:when test="normalize-space(.)!=''"><xsl:apply-templates select="." mode="texte-simple"/></xsl:when>
            <xsl:otherwise><xsl:apply-templates select="//GLOSSAIRE[@mot=$motref or @ref=$motref][normalize-space(.)!=''][1]" mode="texte-simple"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:variable name="motreftrans" select="translate($motref,' àâéèêîïôöùû','_aaeeeiioouu')"/>
        <a href="{$prefixe}pages_{ancestor::XPAGES[last()]/INFORMATIONS/LABEL}/glossaire.html#{$motreftrans}" title="{$definition}" class="glossaire" target="_blank"><xsl:value-of select="@mot"/></a>
    </xsl:template>
    
    <xsl:template match="GLOSSAIRE" mode="texte-simple"><xsl:apply-templates mode="texte-simple"/></xsl:template>
    
    <xsl:template match="EM" mode="texte-simple">*<xsl:apply-templates mode="texte-simple"/>*</xsl:template>
    
    <xsl:template match="SUP" mode="texte-simple">^(<xsl:value-of select="."/>)</xsl:template>
    
    <xsl:template match="SUB" mode="texte-simple">_(<xsl:value-of select="."/>)</xsl:template>
    
    <xsl:template match="CODE" mode="texte-simple"><xsl:apply-templates mode="texte-simple"/></xsl:template>
    
    <xsl:template match="NBSP" mode="texte-simple"><xsl:text> </xsl:text></xsl:template>
    
    
    <xsl:template match="BIBLIOGRAPHIE">
        <xsl:param name="prefixe">../</xsl:param>
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:element name="h{count(ancestor::SECTION)+2}">
            <img src="{$prefixe}{$interface}/icones_sections/bibliographie.png" width="40" height="40" alt="bibliographie" class="icone_section"/>
            <xsl:choose>
                <xsl:when test="@titre!=''"><xsl:value-of select="@titre"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="$messages/message[@label='Bibliographie']"/></xsl:otherwise>
            </xsl:choose>
        </xsl:element>
        <ul>
            <xsl:apply-templates>
                <xsl:sort select="@premierAuteur"/>
            </xsl:apply-templates>
        </ul>
    </xsl:template>
    
    
    <xsl:template match="REFOUVRAGE">
        <li>
            <xsl:value-of select="@premierAuteur"/>
            <xsl:if test="@autresAuteurs">
                ; <xsl:value-of select="@autresAuteurs"/>
            </xsl:if>
            <xsl:text>. </xsl:text>
            <b><xsl:value-of select="@titre"/></b>.
            <xsl:if test="@sous-titre">
                <xsl:value-of select="@sous-titre"/>.
            </xsl:if>
            <xsl:if test="@volume">
                <xsl:value-of select="@volume"/>.
            </xsl:if>
            <xsl:if test="@lieu">
                <xsl:value-of select="@lieu"/> :
            </xsl:if>
            <xsl:value-of select="@editeur"/>
            <xsl:if test="@annee">
                <xsl:text>, </xsl:text><xsl:value-of select="@annee"/>
            </xsl:if>
            <xsl:text>.</xsl:text>
            <xsl:if test="@pagination">
                <xsl:text> </xsl:text><xsl:value-of select="@pagination"/>.
            </xsl:if>
            <xsl:if test="@collection">
                coll. <xsl:value-of select="@collection"/>.
            </xsl:if>
            <xsl:if test="@isbn">
                ISBN <xsl:value-of select="@isbn"/>.
            </xsl:if>
            <xsl:if test="@url">
                <a href="{@url}" target="_blank"><xsl:value-of select="@url"/></a>
            </xsl:if>
            <xsl:if test="@autre">
                - <xsl:value-of select="@autre"/>
            </xsl:if>
        </li>
    </xsl:template>
    
    
    <xsl:template match="REFARTICLE">
        <li>
            <xsl:value-of select="@premierAuteur"/>
            <xsl:if test="@autresAuteurs">
                ; <xsl:value-of select="@autresAuteurs"/>
            </xsl:if>
            <xsl:text>. </xsl:text>
            <b><xsl:value-of select="@titreArticle"/></b>.
            <i><xsl:value-of select="@titrePeriodique"/></i>,
            <xsl:value-of select="@annee"/>
            <xsl:if test="@volume">
                <xsl:text>, </xsl:text><xsl:value-of select="@volume"/>
            </xsl:if>
            <xsl:if test="@numero">
                <xsl:text>, n°</xsl:text><xsl:value-of select="@numero"/>,
            </xsl:if>
            <xsl:if test="@pagination">
                <xsl:value-of select="@pagination"/>.
            </xsl:if>
            <xsl:if test="@url">
                <a href="{@url}" target="_blank"><xsl:value-of select="@url"/></a>
            </xsl:if>
            <xsl:if test="@autre">
                - <xsl:value-of select="@autre"/>
            </xsl:if>
        </li>
    </xsl:template>
    
    
    <xsl:template match="REFWEB">
        <li>
            <xsl:value-of select="@auteurs"/>.
            <b><xsl:value-of select="@titre"/></b>.
            <xsl:if test="@titreSite">
                In <i><xsl:value-of select="@titreSite"/></i>.
            </xsl:if>
            <xsl:if test="@dateConsultation">
                [<xsl:value-of select="$messages/message[@label='consulté le']"/>
                <xsl:text> </xsl:text><xsl:value-of select="@dateConsultation"/>].
            </xsl:if>
            <a href="{@url}" target="_blank"><xsl:value-of select="@url"/></a>.
            <xsl:if test="@description">
                <xsl:value-of select="@description"/>.
            </xsl:if>
        </li>
    </xsl:template>
    
    
    <xsl:template match="EM">
        <em><xsl:apply-templates/></em>
    </xsl:template>
    
    
    <xsl:template match="SUB">
        <sub><xsl:apply-templates/></sub>
    </xsl:template>
    
    
    <xsl:template match="SUP">
        <sup><xsl:apply-templates/></sup>
    </xsl:template>
    
    
    <xsl:template match="CODE">
        <xsl:choose>
            <xsl:when test="parent::PAGE or parent::SECTION or ((parent::ITEM or parent::TD) and (contains(.,'&#xA;') or contains(.,'&#xD;')))">
                <pre>
                    <xsl:apply-templates/>
                </pre>
            </xsl:when>
            <xsl:otherwise><span class="code"><xsl:apply-templates/></span></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    
    <xsl:template match="NBSP">&#xA0;</xsl:template>
    
    
    
    <!-- Page d'entrée du site -->
    
    <xsl:template name="page-entree">
        <xsl:variable name="interface">interface/<xsl:choose>
            <xsl:when test="count(ancestor-or-self::XPAGES[INFORMATIONS/INTERFACE!='']) &gt; 0"><xsl:value-of select="ancestor-or-self::XPAGES[INFORMATIONS/INTERFACE!=''][1]/INFORMATIONS/INTERFACE"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="$interface_par_defaut"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
        <html>
        <head>
            <title><xsl:value-of select="ancestor-or-self::XPAGES[last()]/INFORMATIONS/TITRE"/></title>
            <link type="text/css" rel="stylesheet" href="{$interface}/style.css"/>
            <xsl:if test=".//ENVIMAGE[@localisation='page']">
                <xsl:call-template name="script-imagettes">
                    <xsl:with-param name="interface" select="$interface"/>
                    <xsl:with-param name="prefixe"></xsl:with-param>
                </xsl:call-template>
            </xsl:if>
        </head>
        
        <body class="page">
            <xsl:call-template name="zone_logo">
                <xsl:with-param name="prefixe"></xsl:with-param>
            </xsl:call-template>
            
            <!-- pas de bandeau pour éviter une répétition avec le titre -->
            
            <xsl:variable name="rub" select="$rubriques='oui' and (not(ancestor-or-self::XPAGES[INFORMATIONS/RUBRIQUES]) or ancestor-or-self::XPAGES[INFORMATIONS/RUBRIQUES!=''][1]/INFORMATIONS/RUBRIQUES='oui')"/>
            <xsl:if test="count(ancestor-or-self::XPAGES[last()]/XPAGES) &gt; 1 and $rub">
                <xsl:call-template name="zone_rubriques">
                    <xsl:with-param name="prefixe"></xsl:with-param>
                </xsl:call-template>
            </xsl:if>
            
            <div class="zone_sommaire">
                <div class="zone_outils">
                    <a href="pages_{ancestor-or-self::XPAGES[1]/INFORMATIONS/LABEL}/sommaire.html" title="{$messages/message[@label='Sommaire']}"><img src="{$interface}/sommaire_petit.png" width="25" height="25" border="0" alt="{$messages/message[@label='Sommaire']}"/></a>
                    <xsl:if test="//GLOSSAIRE">
                        &#xA0;&#xA0;
                        <a href="pages_{ancestor-or-self::XPAGES[1]/INFORMATIONS/LABEL}/glossaire.html" title="{$messages/message[@label='Glossaire']}"><img src="{$interface}/glossaire_petit.png" width="25" height="25" border="0" alt="{$messages/message[@label='Glossaire']}"/></a>
                    </xsl:if>
                </div>
                
            </div>
            
            <div class="zone_titre_centre">
                <table><tr>
                <td><h1><xsl:value-of select="ancestor-or-self::XPAGES[last()]/INFORMATIONS/TITRE"/></h1></td>
                </tr></table>
            </div>
            
            <div class="zone_contenu_entree">
                <xsl:if test="self::PAGE and .//ENVIMAGE[@localisation='page']">
                    <xsl:call-template name="figures-page">
                        <xsl:with-param name="prefixe"></xsl:with-param>
                    </xsl:call-template>
                </xsl:if>
                <div class="zone_texte">
                    <xsl:call-template name="infos-erreurs"/>
                    <xsl:choose>
                        <xsl:when test="self::PAGE">
                            <xsl:apply-templates>
                                <xsl:with-param name="prefixe"></xsl:with-param>
                            </xsl:apply-templates>
                        </xsl:when>
                        <xsl:otherwise>
                            <p><xsl:value-of select="$messages/message[@label='Vous pouvez définir une page d_accueil du site en créant une page avec le label _index_.']"/></p>
                        </xsl:otherwise>
                    </xsl:choose>
                </div>
            </div>
        </body>
        </html>
    </xsl:template>
    
    
    <!-- Sommaire et Glossaire du site -->
    
    <xsl:template name="page-sommaire">
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:variable name="prefixe">../</xsl:variable>
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
        <html>
        <head>
            <title><xsl:value-of select="$messages/message[@label='Sommaire']"/></title>
            <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/style.css"/>
        </head>
        
        <body class="page">
            <xsl:call-template name="zone_logo"/>
            
            <xsl:call-template name="zone_bandeau"/>
            
            <xsl:variable name="rub" select="$rubriques='oui' and (not(/XPAGES/INFORMATIONS/RUBRIQUES='non'))"/>
            <xsl:if test="count(XPAGES) &gt; 1 and $rub">
                <xsl:call-template name="zone_rubriques"/>
            </xsl:if>
            
            <div class="zone_sommaire">
                <div class="zone_outils">
                    <a href="{$prefixe}index.html" title="{$messages/message[@label='Entrée du site']}"><img src="{$prefixe}{$interface}/home.png" width="25" height="25" alt="home" border="0"/></a>
                    <xsl:if test="//GLOSSAIRE">
                        &#xA0;&#xA0;
                        <a href="glossaire.html" title="{$messages/message[@label='Glossaire']}"><img src="{$prefixe}{$interface}/glossaire_petit.png" width="25" height="25" border="0" alt="{$messages/message[@label='Glossaire']}"/></a>
                    </xsl:if>
                </div>
                
            </div>
            
            <div class="zone_titre_centre">
                <table><tr>
                <td><h1><xsl:value-of select="$messages/message[@label='Sommaire']"/></h1></td>
                </tr></table>
            </div>
            
            <div class="zone_contenu_sommaire">
                <div class="zone_texte">
                    <ul>
                        <xsl:apply-templates select="PAGE[@label!='index']|XPAGES" mode="sommaire-site"/>
                    </ul>
                </div>
            </div>
        </body>
        </html>
    </xsl:template>
    
    <xsl:template match="XPAGES" mode="sommaire-site">
        <xsl:param name="prefixe">../</xsl:param>
        <li>
            <xsl:variable name="nom-fichier-index"><xsl:choose>
                <xsl:when test="@contribution='oui'">index.html</xsl:when>
                <xsl:otherwise><xsl:value-of select="INFORMATIONS/LABEL"/>_index.html</xsl:otherwise>
            </xsl:choose></xsl:variable>
            <a href="{$prefixe}pages_{@labelfichiers}/{$nom-fichier-index}" class="sommaire2"><xsl:value-of select="INFORMATIONS/TITRE"/></a>
            <xsl:if test="count(ancestor::XPAGES) &lt; 3">
                <ul>
                    <xsl:apply-templates select="PAGE[@label!='index']|XPAGES" mode="sommaire-site">
                        <xsl:with-param name="prefixe" select="$prefixe"/>
                    </xsl:apply-templates>
                </ul>
            </xsl:if>
        </li>
    </xsl:template>
    
    <xsl:template match="PAGE" mode="sommaire-site">
        <xsl:param name="prefixe">../</xsl:param>
        <li>
            <a href="{$prefixe}pages_{ancestor::XPAGES[1]/@labelfichiers}/{@label}.html" class="sommairepage"><xsl:value-of select="@titre"/></a>
        </li>
    </xsl:template>
    
    
    <xsl:template name="page-glossaire">
        <xsl:param name="interface">interface/<xsl:value-of select="$interface_par_defaut"/></xsl:param>
        <xsl:variable name="prefixe">../</xsl:variable>
        <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
        <html>
        <head>
            <title><xsl:value-of select="$messages/message[@label='Glossaire']"/></title>
            <link type="text/css" rel="stylesheet" href="{$prefixe}{$interface}/style.css"/>
        </head>
        <body class="page">
            <xsl:call-template name="zone_logo"/>
            
            <xsl:call-template name="zone_bandeau"/>
            
            <xsl:variable name="rub" select="$rubriques='oui' and (not(/XPAGES/INFORMATIONS/RUBRIQUES='non'))"/>
            <xsl:if test="count(XPAGES) &gt; 1 and $rub">
                <xsl:call-template name="zone_rubriques"/>
            </xsl:if>
            
            <div class="zone_sommaire">
                <div class="zone_outils">
                    <a href="{$prefixe}index.html" title="{$messages/message[@label='Entrée du site']}"><img src="{$prefixe}{$interface}/home.png" width="25" height="25" alt="{$messages/message[@label='Entrée du site']}" border="0"/></a>
                    &#xA0;&#xA0;
                    <a href="sommaire.html" title="{$messages/message[@label='Sommaire']}"><img src="{$prefixe}{$interface}/sommaire_petit.png" width="25" height="25" border="0" alt="{$messages/message[@label='Sommaire']}"/></a>
                    <!--
                    &#xA0;&#xA0;
                    <img src="{$prefixe}{$interface}/glossaire_petit.png" width="25" height="25" border="0" alt="{$messages/message[@label='Glossaire']}"/>
                    -->
                </div>
                
            </div>
            
            <div class="zone_titre_centre">
                <table><tr>
                <td><h1><xsl:value-of select="$messages/message[@label='Glossaire']"/></h1></td>
                </tr></table>
            </div>
            
            <div class="zone_contenu_glossaire">
                <div class="zone_texte">
                    <xsl:call-template name="lettres-glossaire">
                        <xsl:with-param name="lettres">ABCDEFGHIJKLMNOPQRSTUVWXYZ</xsl:with-param>
                    </xsl:call-template>
                    <br/><br/><br/><br/><br/><br/> <!-- pour que les liens pointent vers le haut de la page -->
                </div>
            </div>
        </body>
        </html>
    </xsl:template>
    
    
    <xsl:template name="lettres-glossaire">
        <xsl:param name="lettres"/>
        
        <xsl:variable name="lettre" select="substring($lettres,1,1)"/>
        
        <h2>- <xsl:value-of select="$lettre"/> -</h2>
        <xsl:variable name="mots" select="//GLOSSAIRE[translate(substring(@ref,1,1),'aàbcçdeéèêfghiïîjklmnoôpqrstuùvwxyz','AABCCDEEEEFGHIIIJKLMNOOPQRSTUUVWXYZ')=$lettre or (not(@ref) and translate(substring(@mot,1,1),'aàbcçdeéèêfghiïîjklmnoôpqrstuùvwxyz','AABCCDEEEEFGHIIIJKLMNOOPQRSTUUVWXYZ')=$lettre)]"/>
        <xsl:if test="$mots">
            <dl>
                <xsl:apply-templates select="$mots" mode="glossaire-site">
                    <xsl:sort select="concat(@ref,@mot)"/>
                </xsl:apply-templates>
            </dl>
        </xsl:if>
        <xsl:if test="string-length($lettres)&gt;1">
            <xsl:call-template name="lettres-glossaire">
                <xsl:with-param name="lettres"><xsl:value-of select="substring($lettres,2)"/></xsl:with-param>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="GLOSSAIRE" mode="glossaire-site">
        <xsl:variable name="motref"><xsl:choose>
            <xsl:when test="@ref!=''"><xsl:value-of select="@ref"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="@mot"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        
        <xsl:if test="$motref != ''">
            <xsl:if test="normalize-space(.)!=''">
                <xsl:variable name="motreftrans" select="translate($motref,' àâéèêîïôöùû','_aaeeeiioouu')"/>
                <dt><a name="{$motreftrans}"><b><xsl:value-of select="$motref"/></b></a></dt>
                <dd><xsl:apply-templates select="node()"/><br/><br/></dd>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    
    
    <!-- Templates outils -->
    
    <xsl:template name="format-fichier">
    <!-- renvoit le format supposé d'un fichier d'image/animation à partir de son nom, en utilisant l'extension -->
    <!-- formats reconnus: PNG, JPEG, GIF, MPEG, OGG, WEBM -->
    <!-- renvoit '?' si le format n'est pas reconnu -->
        <xsl:param name="nom"/>
        <xsl:variable name="ext"><xsl:call-template name="extension-fichier"><xsl:with-param name="nom" select="$nom"/></xsl:call-template></xsl:variable>
        <xsl:variable name="extmin" select="translate($ext, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')"/>
        <xsl:choose>
            <xsl:when test="$extmin='png'">PNG</xsl:when>
            <xsl:when test="$extmin='jpg' or $extmin='jpeg'">JPEG</xsl:when>
            <xsl:when test="$extmin='gif'">GIF</xsl:when>
            <xsl:when test="$extmin='mpg' or $extmin='mpeg'">MPEG</xsl:when>
            <xsl:when test="$extmin='mp4'">MPEG4</xsl:when>
            <xsl:when test="$extmin='ogg' or $extmin='ogv'">OGG</xsl:when>
            <xsl:when test="$extmin='webm'">WEBM</xsl:when>
            <xsl:otherwise>?</xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="extension-fichier">
    <!-- renvoit l'extension d'un fichier à partir de son nom -->
        <xsl:param name="nom"/>
        <xsl:choose>
            <xsl:when test="contains($nom,'.')">
                <xsl:call-template name="extension-fichier"><xsl:with-param name="nom" select="substring-after($nom,'.')"/></xsl:call-template>
            </xsl:when>
            <xsl:otherwise><xsl:value-of select="$nom"/></xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
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
    
    <xsl:template name="rep-uri">
        <!-- renvoit le chemin du répertoire d'un fichier à partir de l'URI complète, sous forme d'URI  -->
        <xsl:param name="chemin"/>
        <!-- séparateur de chemins ( normalement / mais parfois \ avec file:// sur Windows ? ) -->
        <xsl:variable name="sepuri"><xsl:choose>
            <xsl:when test="contains($chemin, '\')">\</xsl:when>
            <xsl:otherwise>/</xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:choose>
            <xsl:when test="contains($chemin,$sepuri) and contains(substring-after($chemin,$sepuri),$sepuri)">
                <xsl:value-of select="substring-before($chemin,$sepuri)"/><xsl:value-of select="$sepuri"/><xsl:call-template name="rep-uri"><xsl:with-param name="chemin" select="substring-after($chemin,$sepuri)"/></xsl:call-template>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="substring-before($chemin,$sepuri)"/>
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
        <xsl:param name="encodage" select="'UTF-8'"/> <!-- Sablotron ne connait pas autre chose -->
        <xsl:param name="indentation" select="'no'"/>
        <xsl:choose>
            <!-- Xalan et XSLTC -->
            <!-- attention, Xalan gère les chemins relatifs mais pas XSLTC -->
            <xsl:when test="element-available('xalanredirect:write')">
                <xalanredirect:write file="{$fichier}">
                    <xsl:copy-of select="$contenu"/>
                </xalanredirect:write>
            </xsl:when>
            
            <!-- EXSLT -->
            <!-- attention, href doit être une URI -->
            <xsl:when test="element-available('exsl:document')">
                <exsl:document href="file://{$fichier}" method="{$methode}" encoding="{$encodage}" indent="{$indentation}">
                    <xsl:copy-of select="$contenu"/>
                </exsl:document>
            </xsl:when>
            
            <xsl:otherwise>
                <xsl:message terminate="yes">
                    <xsl:value-of select="$messages/message[@label='Impossible de créer un fichier avec le processeur XSLT de']"/> 
                    <xsl:value-of select="system-property('xsl:vendor')"/>
                </xsl:message>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template name="mauvais-liens">
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'mauvais_liens.html')"/>
            <xsl:with-param name="contenu">
                <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                <html>
                <head>
                    <title><xsl:value-of select="$messages/message[@label='Liste des liens non résolus']"/></title>
                </head>
                <body>
                    <div style="text-align: center"><h1><xsl:value-of select="$messages/message[@label='Liste des liens non résolus']"/></h1></div>
                    <ul>
                        <xsl:apply-templates select=".//LIENPAGE" mode="mauvais-liens"/>
                        <xsl:apply-templates select=".//LIENIMAGE" mode="mauvais-liens"/>
                    </ul>
                    <p></p>
                    <div style="text-align: center"><h1><xsl:value-of select="$messages/message[@label='Mots du glossaire non définis']"/></h1></div>
                    <ul>
                        <xsl:apply-templates select=".//GLOSSAIRE" mode="mauvais-liens">
                            <xsl:sort select="@ref|@mot"/>
                        </xsl:apply-templates>
                    </ul>
                    <p></p>
                    <div style="text-align: center"><h1><xsl:value-of select="$messages/message[@label='Mots du glossaire définis plus d_une fois']"/></h1></div>
                    <ul>
                        <xsl:apply-templates select=".//GLOSSAIRE" mode="doublons">
                            <xsl:sort select="@ref|@mot"/>
                        </xsl:apply-templates>
                    </ul>
                    <p><a href="definitions_glossaire.html"><xsl:value-of select="$messages/message[@label='Localisation des définitions du glossaire']"/></a></p>
                </body>
                </html>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="LIENPAGE" mode="mauvais-liens">
        <xsl:variable name="page" select="@page"/>
        <xsl:variable name="contribution" select="@contribution"/>
        <xsl:choose>
            <xsl:when test="@contribution='' or not(@contribution)">
                <xsl:if test="count(ancestor::XPAGES[INFORMATIONS/LABEL=@labelfichiers][1]//PAGE[@label=$page]) != 1">
                    <li>
                        <a href="{concat('pages_',ancestor::XPAGES[1]/@labelfichiers)}/{ancestor::PAGE/@label}.html" class="lien"><xsl:value-of select="$messages/message[@label='page']"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="@page"/>
                        <xsl:text> </xsl:text><xsl:value-of select="$messages/message[@label='de la même contribution']"/></a>
                        <br/>
                    </li>
                </xsl:if>
            </xsl:when>
            <xsl:when test="@page='' or not(@page)">
                <xsl:if test="count(//XPAGES[INFORMATIONS/LABEL=$contribution]) != 1">
                    <li>
                        <a href="{concat('pages_',ancestor::XPAGES[1]/@labelfichiers)}/{ancestor::PAGE/@label}.html" class="lien"><xsl:value-of select="$messages/message[@label='contribution']"/>
                        <xsl:text> </xsl:text>
                        <xsl:value-of select="@contribution"/></a>
                        <xsl:if test="count(//XPAGES[INFORMATIONS/LABEL=$contribution]) &gt; 1">
                            <xsl:text> </xsl:text>
                            <xsl:value-of select="$messages/message[@label='(il y a plus d_une contribution avec ce label)']"/>
                        </xsl:if>
                        <br/>
                    </li>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:if test="count(//XPAGES[INFORMATIONS/LABEL=$contribution]//PAGE[@label=$page]) != 1">
                    <li>
                        <xsl:value-of select="$messages/message[@label='page']"/><xsl:text> </xsl:text>
                        <a href="{concat('pages_',ancestor::XPAGES[1]/@labelfichiers)}/{ancestor::PAGE/@label}.html" class="lien"><xsl:value-of select="concat($contribution, '/', $page)"/></a>
                        <br/>
                    </li>
                </xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>
    
    <xsl:template match="LIENIMAGE" mode="mauvais-liens">
        <xsl:variable name="label" select="@label"/>
        <xsl:variable name="contribution" select="ancestor::XPAGES[1]/INFORMATIONS/LABEL"/>
        <xsl:variable name="page" select="ancestor::PAGE[1]/@label"/>
        <xsl:if test="count(//XPAGES[INFORMATIONS/LABEL=$contribution]/PAGE[@label=$page]//ENVIMAGE[@label=$label]) != 1">
            <li>
                <xsl:value-of select="$messages/message[@label='Lien vers l_image']"/><xsl:text> </xsl:text>
                <xsl:value-of select="$label"/><xsl:text> </xsl:text>
                <xsl:value-of select="$messages/message[@label='à la page']"/><xsl:text> </xsl:text>
                <a href="{concat('pages_',$contribution,'/',$page,'.html')}" class="lien"><xsl:value-of select="concat($contribution, '/', $page)"/></a>
                <br/>
            </li>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="GLOSSAIRE" mode="mauvais-liens">
        <xsl:variable name="motref"><xsl:choose>
            <xsl:when test="@ref!=''"><xsl:value-of select="@ref"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="@mot"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:if test="$motref != ''">
            <xsl:if test="normalize-space(.)=''">
                <xsl:if test="count(//GLOSSAIRE[(@ref=$motref or @mot=$motref) and normalize-space(.)!=''])=0">
                    <li><xsl:value-of select="$motref"/> (<xsl:value-of select="$messages/message[@label='utilisé page']"/><xsl:text> </xsl:text><xsl:value-of select="concat(ancestor::XPAGES[1]/INFORMATIONS/LABEL, '_', ancestor::PAGE[1]/@label)"/>)</li>
                </xsl:if>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="GLOSSAIRE" mode="doublons">
        <xsl:variable name="motref"><xsl:choose>
            <xsl:when test="@ref!=''"><xsl:value-of select="@ref"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="@mot"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:if test="$motref != ''">
            <xsl:if test="normalize-space(.)!=''">
                <xsl:if test="count(//GLOSSAIRE[(@ref=$motref or (not(@ref) and @mot=$motref)) and normalize-space(.)!=''])&gt;1">
                    <li><xsl:value-of select="$motref"/> (<xsl:value-of select="$messages/message[@label='utilisé page']"/><xsl:text> </xsl:text><xsl:value-of select="concat(ancestor::XPAGES[1]/INFORMATIONS/LABEL, '_', ancestor::PAGE[1]/@label)"/>)</li>
                </xsl:if>
            </xsl:if>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="labels">
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'labels.html')"/>
            <xsl:with-param name="contenu">
                <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                <html>
                    <head>
                        <title><xsl:value-of select="$messages/message[@label='Labels utilisés']"/></title>
                    </head>
                    <body>
                        <p>
                            <a href="#erreurs"><xsl:value-of select="$messages/message[@label='Erreurs']"/></a><xsl:text> - </xsl:text>
                            <a href="#contributions"><xsl:value-of select="$messages/message[@label='Contributions']"/></a><xsl:text> - </xsl:text>
                            <a href="#pages"><xsl:value-of select="$messages/message[@label='Pages']"/></a><xsl:text> - </xsl:text>
                            <a href="#index"><xsl:value-of select="$messages/message[@label='Index']"/></a>
                        </p>
                        
                        <hr/>
                        
                        <xsl:call-template name="labels-erreurs"/>
                        
                        <hr/>
                        
                        <a name="contributions"/>
                        <h2><xsl:value-of select="$messages/message[@label='Contributions']"/></h2>
                        <xsl:call-template name="labels-xpages"/>
                        
                        <hr/>
                        
                        <a name="pages"/>
                        <h2><xsl:value-of select="$messages/message[@label='Pages']"/></h2>
                        <xsl:call-template name="labels-pages"/>
                        
                        <hr/>
                        
                        <a name="index"/>
                        <h1><xsl:value-of select="$messages/message[@label='Index']"/></h1>
                        <xsl:for-each select="//XPAGES | //PAGE">
                            <xsl:sort select="INFORMATIONS/LABEL|@label"/>
                            <xsl:variable name="label" select="INFORMATIONS/LABEL|@label"/>
                            <xsl:variable name="titre" select="INFORMATIONS/TITRE|@titre"/>
                            <xsl:variable name="type" select="translate(name(),'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')"/>
                            &#xA0;&#xA0;&#xA0;&#xA0;
                            <xsl:choose>
                                <xsl:when test="$label!=''"><a href="#{$type}{$label}"><tt><xsl:value-of select="$label"/></tt></a></xsl:when>
                                <xsl:otherwise><em><xsl:value-of select="$messages/message[@label='sans label']"/></em></xsl:otherwise>
                            </xsl:choose>
                            &#xA0;&#xA0;&#xA0;&#xA0;
                            <xsl:value-of select="$type"/> &quot;<xsl:value-of select="$titre"/>&quot;<br/>
                        </xsl:for-each>
                    </body>
                </html>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template name="labels-xpages">
        <xsl:if test="XPAGES">
            <ul>
                <xsl:for-each select="XPAGES">
                    <li>
                        <a name="xpages{INFORMATIONS/LABEL}"/>XPAGES &quot;<xsl:value-of select="INFORMATIONS/TITRE"/>&quot;: <tt><xsl:value-of select="INFORMATIONS/LABEL"/></tt>
                        <xsl:call-template name="labels-xpages"/>
                    </li>
                </xsl:for-each>
            </ul>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="labels-pages">
        <xsl:if test="XPAGES|PAGE">
            <ul>
                <xsl:for-each select="*[self::XPAGES|self::PAGE]">
                    <li>
                        <xsl:choose>
                            <xsl:when test="self::XPAGES">
                                XPAGES &quot;<xsl:value-of select="INFORMATIONS/TITRE"/>&quot;: <tt><xsl:value-of select="INFORMATIONS/LABEL"/></tt>
                            </xsl:when>
                            <xsl:when test="self::PAGE">
                                <a name="page{@label}"/>PAGE &quot;<xsl:value-of select="@titre"/>&quot;: <tt><xsl:value-of select="@label"/></tt>
                            </xsl:when>
                        </xsl:choose>
                        <xsl:call-template name="labels-pages"/>
                    </li>
                </xsl:for-each>
            </ul>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="labels-erreurs">
        <a name="erreurs"/>
        <h1><xsl:value-of select="$messages/message[@label='Erreurs']"/></h1>
        <p><xsl:value-of select="$messages/message[@label='Un lien utilisant un label avec un doublon ne fonctionnera pas, et apparaîtra sur la page des']"/>
        <xsl:text> </xsl:text><a href="mauvais_liens.html"><xsl:value-of select="$messages/message[@label='mauvais liens']"/></a>.</p>
        <xsl:if test="//XPAGES[INFORMATIONS/LABEL=following::XPAGES/INFORMATIONS/LABEL]"> <!-- bug XSLTC XALANJ-2031 -->
            <h2><xsl:value-of select="$messages/message[@label='Doublons d_ensembles XPAGES']"/></h2>
            <ul>
                <xsl:for-each select="//XPAGES">
                    <xsl:if test="count(//XPAGES[INFORMATIONS/LABEL=current()/INFORMATIONS/LABEL])&gt;1">
                       <li>XPAGES &quot;<xsl:value-of select="INFORMATIONS/TITRE"/>&quot;: <tt><xsl:value-of select="INFORMATIONS/LABEL"/></tt>
                       <xsl:text> - </xsl:text>
                       <a href="#xpages{INFORMATIONS/LABEL}"><xsl:value-of select="$messages/message[@label='lien vers le premier avec ce label']"/></a></li>
                    </xsl:if>
                </xsl:for-each>
            </ul>
        </xsl:if>
        <xsl:if test="//PAGE[@label=following::PAGE/@label]">
            <h2><xsl:value-of select="$messages/message[@label='Doublons de pages']"/></h2>
            <ul>
                <xsl:for-each select="//PAGE">
                    <xsl:if test="count(//PAGE[@label=current()/@label])&gt;1">
                       <li><xsl:value-of select="$messages/message[@label='page']"/> &quot;<xsl:value-of select="@titre"/>&quot;: <tt><xsl:value-of select="@label"/></tt> - <a href="#page{@label}"><xsl:value-of select="$messages/message[@label='lien vers la première avec ce label']"/></a></li>
                    </xsl:if>
                </xsl:for-each>
            </ul>
        </xsl:if>
        <xsl:if test="//XPAGES[normalize-space(translate(INFORMATIONS/LABEL,'abcdefghijklmnopqrstuvwxyz0123456789- ','                                     x'))!=''] or //PAGE[normalize-space(translate(@label,'abcdefghijklmnopqrstuvwxyz0123456789- ','                                     x'))!=''] or //ENVIMAGE[@label!='' and normalize-space(translate(@label,'abcdefghijklmnopqrstuvwxyz0123456789- ','                                     x'))!='']">
            <h2><xsl:value-of select="$messages/message[@label='Labels invalides']"/></h2>
            <ul>
                <xsl:for-each select="//XPAGES">
                    <xsl:if test="normalize-space(translate(INFORMATIONS/LABEL,'abcdefghijklmnopqrstuvwxyz0123456789- ','                                     x'))!=''">
                       <li><xsl:value-of select="name()"/> &quot;<xsl:value-of select="INFORMATIONS/TITRE"/>&quot;:
                       <tt><xsl:value-of select="INFORMATIONS/LABEL"/></tt></li>
                    </xsl:if>
                </xsl:for-each>
                <xsl:for-each select="//PAGE|//ENVIMAGE[@label!='']">
                    <xsl:if test="normalize-space(translate(@label,'abcdefghijklmnopqrstuvwxyz0123456789- ','                                     x'))!=''">
                       <li><xsl:value-of select="name()"/> &quot;<xsl:value-of select="@titre"/>&quot;<xsl:text> </xsl:text><xsl:value-of select="$messages/message[@label='contribution']"/><xsl:text> </xsl:text><tt><xsl:value-of select="ancestor::XPAGES[1]/@labelfichiers"/></tt>:
                       <tt><xsl:value-of select="@label"/></tt></li>
                    </xsl:if>
                </xsl:for-each>
            </ul>
        </xsl:if>
    </xsl:template>
    
    <xsl:template name="commentaires">
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'commentaires.html')"/>
            <xsl:with-param name="contenu">
                <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                <html>
                    <head>
                        <title><xsl:value-of select="$messages/message[@label='Commentaires des auteurs']"/></title>
                    </head>
                    <body>
                        <h1><xsl:value-of select="$messages/message[@label='Commentaires des auteurs']"/></h1>
                        <ul>
                            <xsl:for-each select="//COMMENTAIRE">
                                <li><xsl:apply-templates/> - <xsl:value-of select="$messages/message[@label='page']"/><xsl:text> </xsl:text><a href="pages_{concat(ancestor::XPAGES[1]/@labelfichiers, '/', ancestor::PAGE[1]/@label, '.html')}"><xsl:value-of select="ancestor::PAGE/@titre"/></a></li>
                            </xsl:for-each>
                        </ul>
                    </body>
                </html>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template name="credits">
        <xsl:variable name="interface">interface/<xsl:choose>
            <xsl:when test="/XPAGES/INFORMATIONS/INTERFACE"><xsl:value-of select="/XPAGES/INFORMATIONS/INTERFACE"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="$interface_par_defaut"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'credits.html')"/>
            <xsl:with-param name="contenu">
                <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                <html>
                    <head>
                        <title><xsl:value-of select="$messages/message[@label='Liste des images et des crédits, triée par crédit']"/></title>
                        <link type="text/css" rel="stylesheet" href="{$interface}/{$feuille-de-style}"/>
                    </head>
                    <body>
                        <h1><xsl:value-of select="$messages/message[@label='Liste des images et des crédits, triée par crédit']"/></h1>
                        <table class="tableau">
                            <tr>
                                <th><xsl:value-of select="$messages/message[@label='Crédit']"/></th>
                                <th><xsl:value-of select="$messages/message[@label='Image']"/></th>
                                <th><xsl:value-of select="$messages/message[@label='Titre']"/></th>
                                <th><xsl:value-of select="$messages/message[@label='Nom fichier']"/></th>
                                <th><xsl:value-of select="$messages/message[@label='Légende']"/></th>
                                <th><xsl:value-of select="$messages/message[@label='page']"/></th>
                            </tr>
                            <xsl:apply-templates select=".//ENVIMAGE" mode="credits">
                                <xsl:sort select="CREDIT"/>
                            </xsl:apply-templates>
                        </table>
                    </body>
                </html>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template match="ENVIMAGE" mode="credits">
        <tr>
            <xsl:variable name="format"><xsl:call-template name="format-fichier">
                <xsl:with-param name="nom" select=".//FICHIER[1]/@nom"/>
            </xsl:call-template></xsl:variable>
            <xsl:variable name="nomf"><xsl:choose>
                <xsl:when test="$format='MPEG' or $format='MPEG4' or $format='OGG' or $format='WEBM'">animation</xsl:when>
                <xsl:otherwise><xsl:value-of select=".//FICHIER[1]/@nom"/></xsl:otherwise>
            </xsl:choose></xsl:variable>
            <xsl:variable name="url" select="concat('pages_', ancestor::XPAGES[1]/@labelfichiers, '/', ancestor::PAGE[1]/@label, '.html')"/>
            <td><xsl:value-of select="CREDIT"/></td>
            <td><a href="{$url}"><xsl:choose>
                <xsl:when test="$nomf='animation'"><xsl:value-of select="$messages/message[@label='animation']"/></xsl:when>
                <xsl:otherwise><img src="{concat('pages_', ancestor::XPAGES[1]/@labelfichiers, '/', $nomf)}" alt="{$nomf}" width="200"/></xsl:otherwise>
            </xsl:choose></a></td>
            <td><xsl:value-of select="@titre"/></td>
            <td><xsl:value-of select=".//FICHIER[1]/@nom"/></td>
            <td><xsl:apply-templates select="LEGENDE"/></td>
            <td><a href="{$url}"><xsl:value-of select="ancestor::PAGE[1]/@titre"/></a></td>
        </tr>
    </xsl:template>
    
    <xsl:template name="definitions-glossaire">
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'definitions_glossaire.html')"/>
            <xsl:with-param name="contenu">
                <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                <html>
                    <head>
                        <title><xsl:value-of select="$messages/message[@label='Localisation des définitions du glossaire']"/></title>
                    </head>
                    <body>
                        <h1><xsl:value-of select="$messages/message[@label='Localisation des définitions du glossaire']"/></h1>
                        <ul>
                            <xsl:for-each select="//GLOSSAIRE[.!='']">
                                <xsl:sort select="concat(@ref,@mot)"/>
                                <li><em><xsl:choose>
                                    <xsl:when test="@ref!=''"><xsl:value-of select="@ref"/></xsl:when>
                                    <xsl:otherwise><xsl:value-of select="@mot"/></xsl:otherwise>
                                </xsl:choose></em> : <xsl:value-of select="$messages/message[@label='contribution']"/><xsl:text> </xsl:text><tt><xsl:value-of select="ancestor::XPAGES[1]/@labelfichiers"/></tt>,
                                <xsl:if test="ancestor::XPAGES[1]/INFORMATIONS/LABEL != ancestor::XPAGES[1]/@labelfichiers">
                                    XPAGES <tt><xsl:value-of select="ancestor::XPAGES[1]/INFORMATIONS/LABEL"/></tt>,
                                </xsl:if>
                                PAGE <tt><xsl:value-of select="ancestor::PAGE[1]/@label"/></tt></li>
                            </xsl:for-each>
                        </ul>
                    </body>
                </html>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
    <xsl:template name="liste-exercices">
        <xsl:variable name="interface">interface/<xsl:choose>
            <xsl:when test="/XPAGES/INFORMATIONS/INTERFACE"><xsl:value-of select="/XPAGES/INFORMATIONS/INTERFACE"/></xsl:when>
            <xsl:otherwise><xsl:value-of select="$interface_par_defaut"/></xsl:otherwise>
        </xsl:choose></xsl:variable>
        <xsl:call-template name="ecrire-fichier">
            <xsl:with-param name="fichier" select="concat($chemin-site, $sep, 'liste_exercices.html')"/>
            <xsl:with-param name="contenu">
                <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
                <html>
                    <head>
                        <title><xsl:value-of select="$messages/message[@label='Liste des exercices']"/></title>
                        <link type="text/css" rel="stylesheet" href="{$interface}/{$feuille-de-style}"/>
                    </head>
                    <body>
                        <h1><xsl:value-of select="$messages/message[@label='Liste des exercices']"/></h1>
                        <table class="tableau">
                            <xsl:for-each select="//XPAGES[.//QCM | .//EXERCICE]">
                                <xsl:if test="count(ancestor::XPAGES) &gt; 0">
                                    <tr>
                                        <td><xsl:element name="h{count(ancestor::XPAGES)+2}"><xsl:value-of select="INFORMATIONS/TITRE"/></xsl:element></td>
                                        <td></td>
                                        <td></td>
                                    </tr>
                                </xsl:if>
                                <xsl:if test="not(.//XPAGES)">
                                    <xsl:for-each select=".//PAGE[.//EXERCICE|.//QCM]">
                                        <tr>
                                            <xsl:variable name="urlpage" select="concat('pages_', ancestor::XPAGES[1]/@labelfichiers, '/', @label, '.html')"/>
                                            <td><a href="{$urlpage}"><xsl:value-of select="@titre"/></a></td>
                                            <td><!--<tt><xsl:value-of select="$urlpage"/></tt>--></td>
                                            <td></td>
                                        </tr>
                                        <xsl:for-each select=".//*[self::EXERCICE|self::QCM]">
                                            <tr>
                                                <td></td>
                                                <td><xsl:choose>
                                                    <xsl:when test="self::EXERCICE">
                                                        <xsl:variable name="type"><xsl:choose>
                                                            <xsl:when test="@type!=''"><xsl:value-of select="@type"/></xsl:when>
                                                            <xsl:otherwise>évaluation</xsl:otherwise>
                                                        </xsl:choose></xsl:variable>
                                                        <xsl:value-of select="$messages/message[@label=concat('Exercice d_', $type)]"/>
                                                    </xsl:when>
                                                    <xsl:when test="self::QCM"><xsl:value-of select="$messages/message[@label='QCM']"/></xsl:when>
                                                </xsl:choose></td>
                                                <td><xsl:if test="@titre!=''">
                                                    <i><xsl:value-of select="@titre"/></i>
                                                </xsl:if></td>
                                            </tr>
                                        </xsl:for-each>
                                    </xsl:for-each>
                                </xsl:if>
                            </xsl:for-each>
                        </table>
                    </body>
                </html>
            </xsl:with-param>
        </xsl:call-template>
    </xsl:template>
    
</xsl:stylesheet>
