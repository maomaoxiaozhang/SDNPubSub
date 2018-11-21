<?xml version="1.0" encoding='ISO-8859-1' ?>

<xsl:stylesheet version="1.0"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                exclude-result-prefixes="xs">
  
  <xsl:output method="html" indent="yes"
              doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"/>

  <xsl:template match="xs:schema">
    <html>
      <head>
        <title>Schéma XML</title>
        <style type="text/css">
body { color: #000000; background: #FFFFFF }
pre { color: #005500 }
.grandlien { font-size: 120%; font-weight: bold }
.nomtitre { color: #550000 }
.nomattribut { color: #000055; font-weight: bold }
.indentation { margin-left: 2em; margin-top: 1em; margin-bottom: 1em }
        </style>
      </head>
      <body>
        <div align="center">
        <h1>Schéma XML</h1>
        </div>
        <br/>
        <span class="grandlien">
        <a href="#index">Aller à l'index</a><br/>
        </span>
        <hr/>
        <xsl:apply-templates/>
        <br/>
        <a name="index"/>
        <h3>Index</h3>
        <xsl:for-each select="*[@name]">
          <xsl:sort select="@name"/>
          <a href="#{@name}"><xsl:value-of select="@name"/></a><br/>
        </xsl:for-each>
      </body>
    </html>
  </xsl:template>
  
  <xsl:template name="print-ref">
    <xsl:choose>
    <xsl:when test="contains(@ref,':')">
      <a href="#{substring-after(@ref,':')}"><xsl:value-of select="@ref"/></a>
    </xsl:when>
    <xsl:otherwise>
      <a href="#{@ref}"><xsl:value-of select="@ref"/></a>
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="xs:element">
    <xsl:choose>
    <xsl:when test="@ref">
      * <xsl:call-template name="print-ref"/><br/>
    </xsl:when>
    <xsl:otherwise>
      <a name="{@name}"/>
      <h3>Elément <span class="nomtitre"><xsl:value-of select="@name"/></span></h3>
      <xsl:if test="@type">
        type: <xsl:call-template name="nomtype"/><br/>
      </xsl:if>
      <xsl:apply-templates/>
      <br/>
      <xsl:for-each select="//xs:element[.//xs:element[@ref=current()/@name or substring-after(@ref,':')=current()/@name]] | //xs:group[.//xs:element[@ref=current()/@name or substring-after(@ref,':')=current()/@name]]">
        <xsl:if test="position() = 1">
          Parents:
        </xsl:if>
        <xsl:if test="position() != 1">, </xsl:if>
        <a href="#{@name}"><xsl:value-of select="@name"/></a> 
      </xsl:for-each>
      <br/>
      <hr/>
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="xs:group">
    <xsl:choose>
    <xsl:when test="@ref">
      * <xsl:call-template name="print-ref"/>
      <br/>
    </xsl:when>
    <xsl:otherwise>
      <a name="{@name}"/>
      <h3>Groupe <span class="nomtitre"><xsl:value-of select="@name"/></span></h3>
      <xsl:apply-templates/>
      <br/>
      <xsl:for-each select="//xs:element[.//xs:group[@ref=current()/@name or substring-after(@ref,':')=current()/@name]] | //xs:group[.//xs:group[@ref=current()/@name or substring-after(@ref,':')=current()/@name]]">
        <xsl:if test="position() = 1">
          Parents:
        </xsl:if>
        <xsl:if test="position() != 1">, </xsl:if>
        <a href="#{@name}"><xsl:value-of select="@name"/></a> 
      </xsl:for-each>
      <br/>
      <hr/>
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="xs:documentation">
    <pre><xsl:value-of select="."/></pre>
    <br/>
  </xsl:template>
  
  <xsl:template match="xs:choice">
    Choix parmi:
    <div class="indentation">
      <xsl:apply-templates/>
    </div>
  </xsl:template>
  
  <xsl:template match="xs:sequence">
    Séquence parmi:
    <div class="indentation">
      <xsl:apply-templates/>
    </div>
  </xsl:template>
  
  <xsl:template match="xs:complexType">
    <!--Type complexe-->
    <xsl:if test="@name">
      <a name="type_{@name}"/>
      <h3>Type complexe <span class="nomtitre"><xsl:value-of select="@name"/></span></h3>
    </xsl:if>
    <xsl:if test="@ref">
      <a href="{@ref}"><xsl:value-of select="@ref"/></a><br/>
    </xsl:if>
    <xsl:if test="@mixed='true'">
      Peut contenir du texte<br/>
    </xsl:if>
    <div class="indentation">
    <xsl:apply-templates/>
    </div>
    <xsl:if test="@name">
      <hr/>
    </xsl:if>
  </xsl:template>
  
  <xsl:template match="xs:attribute">
    Attribut <span class="nomattribut"><xsl:value-of select="@name"/></span>:
    <div class="indentation">
    <xsl:choose>
    <xsl:when test="@use='required'">
      obligatoire<br/>
    </xsl:when>
    <xsl:otherwise>
      facultatif<br/>
    </xsl:otherwise>
    </xsl:choose>
    <xsl:if test="@type">
      type: <xsl:call-template name="nomtype"/><br/>
    </xsl:if>
    <xsl:apply-templates/>
    </div>
  </xsl:template>
  
  <xsl:template match="xs:simpleType">
    <xsl:choose>
      <xsl:when test="@name!=''">
        <a name="type_{@name}"/>
        <h3>Type simple <span class="nomtitre"><xsl:value-of select="@name"/></span></h3>
        <xsl:apply-templates/>
        <hr/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  <xsl:template match="xs:restriction">
    Restriction des valeurs:
    <ul>
      <xsl:apply-templates/>
    </ul>
  </xsl:template>
  
  <xsl:template match="xs:enumeration">
    <li><tt><xsl:value-of select="@value"/></tt></li>
  </xsl:template>
  
  <xsl:template match="xs:pattern">
    <li><tt><xsl:value-of select="@value"/></tt></li>
  </xsl:template>
  
  <xsl:template match="xs:attributeGroup">
    <xsl:choose>
    <xsl:when test="@ref">
      * <xsl:call-template name="print-ref"/>
      <br/>
    </xsl:when>
    <xsl:otherwise>
      <a name="{@name}"/>
      <h3>Groupe d'attributs <span class="nomtitre"><xsl:value-of select="@name"/></span></h3>
      <xsl:apply-templates/>
      <br/>
      <xsl:for-each select="//xs:element[.//xs:attributeGroup[@ref=current()/@name or substring-after(@ref,':')=current()/@name]] | //xs:attributeGroup[.//xs:attributeGroup[@ref=current()/@name or substring-after(@ref,':')=current()/@name]] | //xs:complexType[.//xs:attributeGroup[@ref=current()/@name or substring-after(@ref,':')=current()/@name] and @name]">
        <xsl:if test="position() = 1">
          Parents:
        </xsl:if>
        <xsl:if test="position() != 1">, </xsl:if>
        <a href="#{@name}"><xsl:value-of select="@name"/></a> 
      </xsl:for-each>
      <br/>
      <hr/>
    </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="nomtype">
    <xsl:choose>
        <xsl:when test="/xs:schema/xs:simpleType[@name=current()/@type]|/xs:schema/xs:complexType[@name=current()/@type]">
          <a href="#type_{@type}"><xsl:value-of select="@type"/></a>
        </xsl:when>
        <xsl:otherwise>
          <tt><xsl:value-of select="@type"/></tt>
        </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
</xsl:stylesheet>
