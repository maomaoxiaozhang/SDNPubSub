<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns="http://www.w3.org/1999/xhtml" version="1.0">

<!-- Auteur : Damien Guillaume  ( Observatoire de Paris )-->
<!-- Contributeurs : Pierre Brochard & Didier Courtaud ( CEA )-->

<!-- <xsl:output doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" doctype-system="http://www.w3.org/TR/xhtml1/loose.dtd" encoding="ISO-8859-1" indent="yes" method="xhtml"/> -->
<xsl:output indent="yes" method="xml"/> 

<!-- options (valeurs: oui|non) -->
<xsl:param name="commentaires"><xsl:choose>
    <xsl:when test="/EXPOSE/METADONNEES/OPTIONS/COMMENTAIRES"><xsl:value-of select="/EXPOSE/METADONNEES/OPTIONS/COMMENTAIRES"/></xsl:when>
    <xsl:otherwise>non</xsl:otherwise></xsl:choose>
</xsl:param>

<xsl:param name="impression"><xsl:choose>
    <xsl:when test="/EXPOSE/METADONNEES/OPTIONS/IMPRESSION"><xsl:value-of select="/EXPOSE/METADONNEES/OPTIONS/IMPRESSION"/></xsl:when>
    <xsl:otherwise>non</xsl:otherwise></xsl:choose>
</xsl:param>

<xsl:template match="/">
	
        <xsl:apply-templates select="EXPOSE"/>
	
</xsl:template>
    
<xsl:template match="PAGE_TITRE" >
		
	    <!-- <div class="cover"> -->
	        <div class="slide" id="cover">
		<table width="100%">

		    <tr>
		      <td align="left" class="vingt"><xsl:apply-templates select="../LOGO_GAUCHE"/></td>
                      <td align="center" class="soixante">
                      </td>
                      <td align="right" class="vingt"><tt><xsl:apply-templates select="../LOGO_DROIT"/></tt></td>

		    </tr>

                </table>
		
		<table width="100%" height="80%">
		<tbody>
		<tr><td><xsl:if test="normalize-space(TITRE)!=''">
                        <h1><xsl:value-of select="TITRE"/></h1>
                      </xsl:if></td></tr>
		<tr><td><h2> <xsl:apply-templates select="SOUS_TITRE"/></h2></td></tr>
		<tr><td><h5> <xsl:value-of select="AUTEUR"/></h5></td></tr>
		</tbody>
		<tfoot>
		<tr><td><h6><tt> <xsl:apply-templates select="EMAIL"/></tt></h6></td></tr>
		</tfoot>
		</table>
		
		<table class="bas" width="100%">
		    <tr>
			    <td align="left" class="tiers"><tt><xsl:apply-templates select="../PIEDPAGE_GAUCHE"/></tt></td>
			    <td align="center" class="tiers"><tt><xsl:value-of select="DATE_EXPOSE"/></tt></td>
			    <td align="right" class="tiers"><tt><xsl:apply-templates select="../PIEDPAGE_DROIT"/></tt></td>
                     </tr>
	    </table>
		
	    </div>
	    
</xsl:template>

<xsl:template match="PARTIE">
	
	<xsl:variable name="nbpart"> <xsl:value-of select="count(preceding::PARTIE)+1"/>  </xsl:variable>
	
	<div class="slide" id="partie{$nbpart}">
		
		<table width="100%">

		    <tr>
		      <td align="left" class="vingt"><xsl:apply-templates select="../LOGO_GAUCHE"/></td>
                      <td align="center" class="soixante">
                      </td>
                      <td align="right" class="vingt"><tt><xsl:apply-templates select="../LOGO_DROIT"/></tt></td>

		    </tr>

                </table>
		
		<table width="100%" height="60%">
		<tbody>
		<tr>
			<td>
				
				<xsl:choose>
					
                                <xsl:when test="normalize-space(TITRE)!=''">
					
					<h1><xsl:value-of select="TITRE"/></h1>
					
				</xsl:when>
				
                                <xsl:otherwise>
					
					<h1>Partie <xsl:value-of select="position()"/></h1>
					
				</xsl:otherwise>
				
                            </xsl:choose>
				
			</td>
		</tr>
		</tbody>
		
		</table>
		
		<table class="bas" width="100%">
		    <tr>
			    <td align="left" class="tiers"><tt><xsl:apply-templates select="../PIEDPAGE_GAUCHE"/></tt></td>
			    <td align="center" class="tiers"><tt><xsl:value-of select="DATE_EXPOSE"/></tt></td>
			    <td align="right" class="tiers"><tt><xsl:apply-templates select="../PIEDPAGE_DROIT"/></tt></td>
                     </tr>
	    </table>
	</div>
	
	<xsl:apply-templates select="SECTION"/>
	
	
</xsl:template>

<xsl:template name="sommaire">

	
		
	<div class="slide" id="sommaire">


                  <table width="100%">

		    <tr>
		      <td align="left" class="vingt"><xsl:apply-templates select="LOGO_GAUCHE"/></td>
                      <td align="center" class="soixante">
		      <h1> Plan </h1>
                      </td>
                      <td align="right" class="vingt"><tt><xsl:apply-templates select="LOGO_DROIT"/></tt></td>

		    </tr>

             </table>

                <xsl:if test="count(METADONNEES/AUTEUR)&gt;0">
                    <div class="auteur">- <xsl:apply-templates select="METADONNEES/AUTEUR"/></div>
                </xsl:if> 


		
                <ul class="accordion">
                          
			  
		        <xsl:for-each select="PARTIE">

                        
			<xsl:variable name="nbsec"> <xsl:value-of select="count(preceding::SECTION)+count(preceding::PAGE_TITRE)"/>  </xsl:variable>
				
			 			  
			  
                         <li class="elsommaire">
				<span>
			        
                                
				<xsl:choose> 
                                <xsl:when test="normalize-space(TITRE)!=''">
					<xsl:value-of select="TITRE"/>
				</xsl:when>
                                <xsl:otherwise>
					Partie <xsl:value-of select="position()"/>
				</xsl:otherwise>
                            </xsl:choose>
				
				
			<ul id="liste_sections">
				<xsl:for-each select="SECTION">
				
				<xsl:variable name="nbsec2"> <xsl:value-of select="count(preceding::SECTION)+count(preceding::PAGE_TITRE)"/>  </xsl:variable>
				
				
				<li class="elsommaire2">
				
				<span>
					
				<a href="#slide{$nbsec2}">
					
				<xsl:choose> 
                                <xsl:when test="normalize-space(TITRE)!=''">
					<xsl:value-of select="TITRE"/>
				</xsl:when>
                                <xsl:otherwise>
					Section<xsl:value-of select="position()"/>
				</xsl:otherwise>
				</xsl:choose>
				
				</a>
				
				</span>
					
				</li>
				
				</xsl:for-each>
				
			</ul>

			 
			</span>
			
			 </li>

			
			</xsl:for-each> 



                </ul>
		
                <xsl:if test="$commentaires='oui'">
                    <div class="commentaire">
                        note: les commentaires sont affichés
                    </div>
                </xsl:if>
		
		
        </div>
	    


</xsl:template>

<xsl:template match="EXPOSE">
    
	

	
        <!-- <xsl:processing-instruction name="xml"/> -->
	<xsl:processing-instruction name="DOCTYPE">html</xsl:processing-instruction>
	<!-- <html xmlns="http://www.w3.org/1999/xhtml"
	      xmlns:smil="http://www.w3.org/ns/SMIL"> -->
	<html>
	<head>
     
        
	    <xsl:choose>
		<xsl:when test="normalize-space(PAGE_TITRE/TITRE)!=''">
			<title><xsl:value-of select="PAGE_TITRE/TITRE"/></title>
		</xsl:when>
		<xsl:otherwise>
			<title>Presentation</title>
		</xsl:otherwise>
	    </xsl:choose>
	    
	    
	    <xsl:choose>
	    <xsl:when test="normalize-space(PAGE_TITRE/IDENTIFIANT)!=''">
		<META name="id" content="{normalize-space(PAGE_TITRE/IDENTIFIANT)}"/>
		<META name="type" content="{normalize-space(PAGE_TITRE/IDENTIFIANT/@TYPE)}"/>
	    </xsl:when>
	    </xsl:choose>
	    
	    
	    
	    <style type="text/css">
		<xsl:call-template name="css"/>
	    </style>
	    
           
	    
	        <meta charset="UTF-8" />
	    	
		
		<!-- <link rel="stylesheet" type="text/css" href="jaxe_custom.css"><xsl:text> </xsl:text></link> -->
		
		<link rel="stylesheet" type="text/css" href="config_exposeV3/jaxe_expose_transitions.css"><xsl:text> </xsl:text></link>
		<link rel="stylesheet" type="text/css" href="config_exposeV3/jaxe_expose.css"><xsl:text> </xsl:text></link>
		<link rel="timesheet" type="application/smil+xml" href="config_exposeV3/jaxe_expose.smil"><xsl:text> </xsl:text></link>
		

		<script type="text/javascript" src="config_exposeV3/timesheets.js"><xsl:text> </xsl:text></script>
	        <script type="text/javascript" src="config_exposeV3/timesheets-navigation.js"><xsl:text> </xsl:text></script>
	
		<xsl:call-template name="javascript"/>
		 	    
        </head>

	    
	<xsl:variable name="body_transition">
		
		<xsl:choose>
			
			<xsl:when test="normalize-space(@transition)!=''">
				<xsl:value-of select="@transition"/>
			</xsl:when>
			
			<xsl:otherwise>
				<xsl:value-of select="'carousel'"/>
			</xsl:otherwise>
			
		</xsl:choose>
		
	</xsl:variable> 
	 
	 
	   
	<body class="{$body_transition}">
		
  
		
	<div id="slideshow">
		    
	    
	<xsl:apply-templates select="PAGE_TITRE"/>
	
	<xsl:call-template name="sommaire"/>
	
	<xsl:apply-templates select="PARTIE"/>

        
	    
	<p id="navigation_par" class="menu">
		
		<button id="first" title="first slide">       |«     </button>
		<button id="prev"  title="previous slide"> &lt; prev </button>
		<button id="next"  title="next slide">     next &gt; </button>
		<button id="last"  title="last slide">         »|    </button>
		<button id="plan"  title="Plan" onclick="location.href='#sommaire'"> Plan </button>
		<button id="print" title="Print" onclick="make_print();"> Print </button>
		
	</p>
	    
	</div>    
	    
	    
        </body>
        </html>
	

	
    </xsl:template>
  

    
    <xsl:template name="css">
body { background: #FFFFFF }
h1 { text-align: center; font-size: 150%; font-family: sans-serif; color: #963232 }
h2 { text-align: center; font-size: 110%; font-weight: bold; font-family: sans-serif; color: #963232 }
h3 { font-size: 100%; font-weight: bold; font-family: serif; margin-bottom: 0.5em }
h5 { text-align: center; font-size: 90%; font-weight: bold; font-family: serif; margin-bottom: 0.5em; color: #963232 }
h6 { text-align: center; font-size: 70%; font-weight: bold; font-family: serif; margin-bottom: 0.5em; color: #963232 }
.auteur { text-align: center }
.date { text-align: left }
.sommaire { page-break-after: always; background: #F5F5FF; margin: 0.5em; padding: 0.5em; <xsl:if test="$impression='non'">border: gray outset</xsl:if> }
.cover { page-break-after: always; background: #F5F5FF; margin: 0.5em; padding: 0.5em; <xsl:if test="$impression='non'">border: gray outset</xsl:if> }
.section { page-break-after: always; background: #FFFFEB; margin: 2em 0.5em; padding: 0.5em; <xsl:if test="$impression='non'">border: gray outset</xsl:if> }
.slide { page-break-after: always; background: #FFFFEB; margin: 2em 0.5em; padding: 0.5em; <xsl:if test="$impression='non'">border: gray outset</xsl:if> }
.titreimage { text-align: center; font-weight: bold; font-family: sans-serif; background-color: #E0E0FF }
.envimage { text-align: center; border: gray outset; padding: 0; margin: 1em; background: #EBFFEB }
.legende { font-family: sans-serif; margin-top: 1em }
p { text-indent: 1em; text-align: justify; margin-top: 0.5em }
pre { background: #EBFFEB; padding-left: 2em; padding-bottom: 0.5em }
tt { color: #963232 }
ul { margin-top: 0.5em }
/* li { margin-bottom: 0.5em; font-size: 1.2em; } */
li { margin-bottom: 0.5em; }
dt { font-weight: bold; }
dd { margin-bottom: 0.5em; }
th { background: #FFF0E0 }
.tableau1 { background: #FFFAE0 }
.tableau2 { background: #FFF5E0 }
.elsommaire { font-size: 150%; color: #963232 ; text-decoration: none; font-weight: bold }
.elsommaire:hover { font-size: 110%; color: #663399; text-decoration: none; font-weight: bold }
.elsommaire2 { font-size: 90%; color: #000000; text-decoration: none; font-weight: bold }
.commentaire { margin: 1em; color: #196419 }
.equation { vertical-align : middle }
td.tiers { width: 33% }
td.vingt { width: 20% }
td.soixante { width: 60% }
table.bas { position: absolute; bottom: 2% ; left:0 }



#slideshow div.slide {
  position: absolute;
  left: 10%;
  top: 3%;
  width:80%;
  height:80%;  
}
#slideshow div.print {
  position: relative !important;
}

#slideshow div.image {
align:center;
}

#navigation_par.print {
  display: none;
  opacity: 0;
}

/* navigation menu */
#slideshow .menu {
  position: absolute;
  /*bottom: 0;*/
  /*right: 2em;*/
  /*font-size: 0.75em;*/
  bottom: 0%;
  right: 9%;
	margin: 0;
  border-radius         : 0px;
  -o-border-radius      : 0px;
  -moz-border-radius    : 0px;
  -webkit-border-radius : 0px;
  box-shadow         : none;
  -o-box-shadow      : none;
  -moz-box-shadow    : none;
  -webkit-box-shadow : none;
  z-index: 2;
}
/* no transition for the menu! */
#slideshow .menu {
  opacity: 1 ;
  visibility: visible ;
}
#slideshow .menu button {
  opacity: 1;
  transition         : none;
  -o-transition      : none;
  -moz-transition    : none;
  -webkit-transition : none;
}
#slideshow .menu button[smil=idle] {
  opacity: 0.5;
  transform         : none;
  -o-transform      : none;
  -moz-transform    : none;
  -webkit-transform : none;
}



    </xsl:template>
    
<xsl:template name="javascript">
<script type="text/javascript">
<xsl:text disable-output-escaping="yes">
	
function make_print() {

    var liste_div = document.getElementsByTagName('div');
    
    for (var i=0; i &lt; liste_div.length; i++) {
        var classe = liste_div[i].getAttribute('class');
        if (classe == 'slide' || classe == 'sommaire' || classe == "cover" || classe== "image") {
	     liste_div[i].setAttribute("class",classe.concat(' print'));
	     liste_div[i].setAttribute("smil","active");
        }
    }
    var par = document.getElementById("navigation_par");
        var class_par = par.getAttribute('class');
	par.setAttribute("class",class_par.concat(' print'));
	
    var liste_li = document.getElementsByTagName('li');
    

    for (var i=0; i &lt; liste_li.length; i++) {
    
	var li_style = liste_li[i].getAttribute("style");
	
	if (li_style) {
	
		var li_reg=new RegExp("hidden","g");
		var li_newstyle = li_style.replace(li_reg,"visible");
		liste_li[i].setAttribute("style",li_newstyle);
	}
	

	liste_li[i].setAttribute("smil","active");			
     }
	
    var liste_h3 = document.getElementsByTagName('h3');
    

    for (var i=0; i &lt; liste_h3.length; i++) {
    
	var h3_style = liste_h3[i].getAttribute("style");
	
	if (h3_style) {
	
		var h3_reg=new RegExp("hidden","g");
		var h3_newstyle = h3_style.replace(h3_reg,"visible");
		liste_h3[i].setAttribute("style",h3_newstyle);
	}
	

	liste_h3[i].setAttribute("smil","active");			
     }
    
    var liste_ol = document.getElementsByTagName('ol');
    for (var i=0; i &lt; liste_ol.length; i++) {
 
	liste_ol[i].setAttribute("style","display: block");
	liste_ol[i].setAttribute("smil","active");
    }
    
    var liste_ul = document.getElementsByTagName('ul');
    for (var i=0; i &lt; liste_ul.length; i++) {
    
	if( liste_ul[i].id != "liste_sections" ) {
	

		liste_ul[i].setAttribute("style","display: block");
		liste_ul[i].setAttribute("smil","active");
	
	}
	
	else {
	
		liste_ul[i].setAttribute("style","display: none");	
	
	}
    }
}

	
</xsl:text>
</script>
</xsl:template>

 
    
    <xsl:template match="LOGO_GAUCHE">
	
        <xsl:choose>
		
            <xsl:when test="@url!=''">
                <a href="{@url}"><img align="left" alt="{@alt}" border="0" src="{@fichier}" height="{@hauteur_SVG}%" title="{@alt}" width="{@largeur_SVG}%"/></a>
            </xsl:when>
	    
	   <xsl:otherwise>
                <img align="left" alt="{@alt}" src="{@fichier}" height="{@hauteur_SVG}%" title="{@alt}" width="{@largeur_SVG}%"/>
            </xsl:otherwise>
	   
        </xsl:choose>
	
    </xsl:template>


        <xsl:template match="LOGO_DROIT">
		
        <xsl:choose>
		
            <xsl:when test="@url!=''">
                <a href="{@url}"><img align="right" alt="{@alt}" border="0" src="{@fichier}" height="{@hauteur_SVG}%" title="{@alt}" width="{@largeur_SVG}%"/></a>
            </xsl:when>
	    
            <xsl:otherwise>
                <img align="right" alt="{@alt}" src="{@fichier}" height="{@hauteur_SVG}%" title="{@alt}" width="{@largeur_SVG}%"/>
            </xsl:otherwise>
	    
        </xsl:choose>
	
    </xsl:template>

    
    <xsl:template match="SECTION">

	    
	<xsl:variable name="numero"> <xsl:value-of select="count(preceding::SECTION)+1"/>  </xsl:variable>
	
	<div class="slide" id="slide{$numero}">

	    <table width="100%">

		    <tr>
		      <td align="left" class="vingt"><xsl:apply-templates select="../../LOGO_GAUCHE"/></td>
                      <td align="center" class="soixante"><h2><xsl:value-of select="TITRE"/></h2></td>
                      <td align="right" class="vingt"><xsl:apply-templates select="../../LOGO_DROIT"/></td>

		    </tr>

             </table>



            <xsl:apply-templates/>


	    <table class="bas" width="100%">
		    <tr>
			    <td align="left" class="tiers"><tt><xsl:apply-templates select="../../PIEDPAGE_GAUCHE"/></tt></td>
			    <td align="center" class="tiers"><tt><xsl:value-of select="$numero"/>/<xsl:value-of select="count(//SECTION)"/></tt></td>
			    <td align="right" class="tiers"><tt><xsl:apply-templates select="../../PIEDPAGE_DROIT"/></tt></td>
                     </tr>
	    </table>
	    
        </div>
	
    </xsl:template>
    
    <xsl:template match="AUTEUR">
        <xsl:value-of select="."/> -
    </xsl:template>
    
    <xsl:template match="DATECRE">
        <div class="date"><xsl:value-of select="."/></div>
    </xsl:template>
    
    <xsl:template match="TITRE">
    </xsl:template>
    
    <xsl:template match="PARAGRAPHE">
        <xsl:if test="normalize-space(TITRE)!=''">
            <h3><xsl:value-of select="TITRE"/></h3>
        </xsl:if>
        <p><xsl:apply-templates/></p>
    </xsl:template>

    <xsl:template match="EXEMPLE">
        <xsl:if test="normalize-space(@label)!=''">
            <h3><xsl:value-of select="@label"/></h3>
        </xsl:if>
        <pre><xsl:apply-templates/></pre>
    </xsl:template>
    
    <xsl:template match="CODE">
        <tt><xsl:apply-templates/></tt>
    </xsl:template>
    
    <xsl:template match="EMPHASE">
        <em><xsl:apply-templates/></em>
    </xsl:template>
    
    <xsl:template match="LIEN_INTERNE">
	   
	    <a href="#slide{@num}"><xsl:apply-templates/></a>
	    
    </xsl:template>

    <xsl:template match="LIEN_EXTERNE">
    
	    <a href="{@href}"><xsl:apply-templates/></a>

    </xsl:template>

    <xsl:template match="LISTE">
	
        <xsl:if test="normalize-space(TITRE)!=''">
		
		<xsl:choose>
			<xsl:when test="normalize-space(@mode)='incremental_ombre'">
				
				<h3 class="shadowinc"><xsl:value-of select="TITRE"/></h3>
				
			</xsl:when>
			
			<xsl:when test="normalize-space(@mode)='incremental'">
				
				<h3 class="incremental"><xsl:value-of select="TITRE"/></h3>
				
			</xsl:when>
			
			<xsl:otherwise>
				
				<h3><xsl:value-of select="TITRE"/></h3>
				
			</xsl:otherwise>
			
		</xsl:choose>
            
        </xsl:if>
	


	<xsl:choose>
	
		<xsl:when test="normalize-space(@mode)='pliage'">
			
			<xsl:choose>
				
				<xsl:when test="count(ancestor::LISTE)!=0">
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul type="{@type}">
								
								<xsl:apply-templates select="EL"/>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul>
								
								<xsl:apply-templates select="EL"/>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>

			
				</xsl:when>
				
				<xsl:otherwise>
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul class="outline" type="{@type}">
								
								<xsl:apply-templates select="EL"/>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul class="outline">
								
								<xsl:apply-templates select="EL"/>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>
					
					
					
					
				</xsl:otherwise>
			
			</xsl:choose>
				
			
		
		</xsl:when>
		
		
		<xsl:when test="normalize-space(@mode)='accordeon'">
			
			<xsl:choose>
				
				<xsl:when test="count(ancestor::LISTE)!=0">
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul type="{@type}">
								
								<xsl:apply-templates select="EL"/>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul>
								
								<xsl:apply-templates select="EL"/>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>

			
				</xsl:when>
				
				<xsl:otherwise>
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul class="accordion" type="{@type}">
								
								<xsl:apply-templates select="EL"/>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul class="accordion">
								
								<xsl:apply-templates select="EL"/>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>
					
					
					
					
				</xsl:otherwise>
			
			</xsl:choose>
				
			
		
		</xsl:when>
		
		
		
		
		

		<xsl:when test="normalize-space(@mode)='incremental'">
			
			<xsl:choose>
				
				<xsl:when test="count(ancestor::LISTE)!=0">
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul type="{@type}">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul>
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>

			
				</xsl:when>
				
				<xsl:otherwise>
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul class="incremental" type="{@type}">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul class="incremental">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>
					
					
					
					
				</xsl:otherwise>
			
			</xsl:choose>
				
			
		
		</xsl:when>


		<xsl:when test="normalize-space(@mode)='incremental_ombre'">
			
			<xsl:choose>
				
				<xsl:when test="count(ancestor::LISTE)!=0">
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul type="{@type}">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul>
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>

			
				</xsl:when>
				
				<xsl:otherwise>
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul class="shadowinc" type="{@type}">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul class="shadowinc">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>
					
					
					
					
				</xsl:otherwise>
			
			</xsl:choose>
				
			
		
		</xsl:when>



		<xsl:when test="normalize-space(@mode)='progressif'">
			
			<xsl:choose>
				
				<xsl:when test="count(ancestor::LISTE)!=0">
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul type="{@type}">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul>
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>

			
				</xsl:when>
				
				<xsl:otherwise>
					
					
					<xsl:choose>
						
						<xsl:when test="normalize-space(@type)!=''">
							
							<ul class="progressive" type="{@type}">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
							</ul>
								
						</xsl:when>
						
						<xsl:otherwise>
							
							<ul class="progressive">
								
								<xsl:apply-templates select="EL"/>
								<li class="signeplus"> <img src="config_exposeV3/signe_plus.jpg"/></li>
								
							</ul>
							
						</xsl:otherwise>
							
					</xsl:choose>
					
					
					
					
				</xsl:otherwise>
			
			</xsl:choose>
				
			
		
		</xsl:when>

	
		
			
		<xsl:otherwise>
			
			
			<xsl:choose>
						
				<xsl:when test="normalize-space(@type)!=''">
							
					<ul type="{@type}">
								
						<xsl:apply-templates select="EL"/>
						
					</ul>
								
				</xsl:when>
						
				<xsl:otherwise>
							
					<ul>
								
						<xsl:apply-templates select="EL"/>
								
					</ul>
							
				</xsl:otherwise>
							
			</xsl:choose>
		
			
		
		</xsl:otherwise>
	
	</xsl:choose>
		
	
	
    </xsl:template>




    <xsl:template match="EL">
	
	<xsl:variable name="npart"> <xsl:value-of select="count(ancestor-or-self::PARTIE)"/>  </xsl:variable>
	<xsl:variable name="nsect"> <xsl:value-of select="count(ancestor-or-self::SECTION)"/>  </xsl:variable>
	<xsl:variable name="nlist"> <xsl:value-of select="count(ancestor-or-self::LISTE)"/>  </xsl:variable>
	<xsl:variable name="nitem"> <xsl:value-of select="count(preceding::EL)"/>  </xsl:variable>
		
	<xsl:variable name="id_el"> <xsl:value-of select="concat('l',$npart,$nsect,$nlist,$nitem)"/> </xsl:variable>
	<xsl:variable name="id_span"> <xsl:value-of select="concat('s',$npart,$nsect,$nlist,$nitem)"/> </xsl:variable>
	
	
	
	<li id="{$id_el}">
		
		
		<span id="{$id_span}">
			
		<xsl:choose>
			
			<xsl:when test="count(descendant::LISTE)=0">
				
				<xsl:apply-templates/>
				
			</xsl:when>
			
			<xsl:otherwise>
				
				<xsl:apply-templates select="text()|EMPHASE"/>
				
			</xsl:otherwise>
				
		</xsl:choose>
		
		</span>
		
		<xsl:if test="count(descendant::LISTE)!=0"> <xsl:apply-templates select="child::*[name()!='EMPHASE']"/> </xsl:if> 
		

		
		<xsl:if test="normalize-space(../@mode)='pliage'">
			
				<xsl:if test="count(descendant::LISTE)!=0">
					
					<img class="plus" src="config_exposeV3/signe_plus.jpg"/>	
					
				</xsl:if>	
			
		</xsl:if>
		
		<xsl:if test="normalize-space(../@mode)='accordeon'">
			
				<img class="plus" src="config_exposeV3/signe_plus.jpg"/>
			
		</xsl:if>
		
	</li>
	
	
<!--  Couleurs des puces et des textes -->

	<xsl:choose>
	
		<xsl:when test="normalize-space(../@image_puce)!=''">
		
		
			<script type="text/javascript">
				
				
				var li<xsl:value-of select="$id_el"/> = document.getElementById("<xsl:value-of select="$id_el"/>");
				li<xsl:value-of select="$id_el"/>.style.listStyleImage = "url(<xsl:value-of select="../@image_puce"/>)";
	
				
				
			</script>
			        

		</xsl:when>
	
		<xsl:when test="normalize-space(../@couleur_puce)!=''">
			
			<xsl:variable name="coltext">
				<xsl:choose>
					<xsl:when test="normalize-space(../@couleur_texte)!=''">
						<xsl:value-of select="../@couleur_texte"/>
					</xsl:when>
					<xsl:otherwise>
						<xsl:value-of select="'black'"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:variable>
				
				

			
			<script type="text/javascript">
				
				
				var li<xsl:value-of select="$id_el"/> = document.getElementById("<xsl:value-of select="$id_el"/>");
				var span<xsl:value-of select="$id_span"/> = document.getElementById("<xsl:value-of select="$id_span"/>");
				
				li<xsl:value-of select="$id_el"/>.style.color = "<xsl:value-of select="../@couleur_puce"/>";
				span<xsl:value-of select="$id_span"/>.style.color = "<xsl:value-of select="$coltext"/>";
				
				
			</script>
			
		</xsl:when>
		
		
		<xsl:when test="normalize-space(../@couleur_texte)!=''">
			
			
			<script type="text/javascript">
				
				
				var li<xsl:value-of select="$id_el"/> = document.getElementById("<xsl:value-of select="$id_el"/>");
				var span<xsl:value-of select="$id_span"/> = document.getElementById("<xsl:value-of select="$id_span"/>");
				
				li<xsl:value-of select="$id_el"/>.style.color = "black";
				span<xsl:value-of select="$id_span"/>.style.color = "<xsl:value-of select="../@couleur_texte"/>";
				
				
			</script>
			
		</xsl:when>
		
		<xsl:otherwise>
			
			<script type="text/javascript">
				
				
				var li<xsl:value-of select="$id_el"/> = document.getElementById("<xsl:value-of select="$id_el"/>");
				var span<xsl:value-of select="$id_span"/> = document.getElementById("<xsl:value-of select="$id_span"/>");
				
				li<xsl:value-of select="$id_el"/>.style.color = "black";
				span<xsl:value-of select="$id_span"/>.style.color = "black";
				
				
			</script>
			
			
			
		</xsl:otherwise>
		
	</xsl:choose>
	
	

	
	
    </xsl:template>
    
    <xsl:template match="LISTEDEF">
        <xsl:if test="normalize-space(TITRE)!=''">
            <h3><xsl:value-of select="TITRE"/></h3>
        </xsl:if>
        <dl>
            <xsl:apply-templates select="TERME | DEF"/>
        </dl>
    </xsl:template>
    
    <xsl:template match="TERME">
        <dt><xsl:apply-templates/></dt>
    </xsl:template>
    
    <xsl:template match="DEF">
        <dd><xsl:apply-templates/></dd>
    </xsl:template>
    
    <xsl:template match="ESP"><xsl:text>&#xA0;</xsl:text></xsl:template>
    
    <xsl:template match="BR"><br/></xsl:template>
    
    <xsl:template match="IMAGE">
        <img alt="{@alt}" src="{@fichier}"/>
    </xsl:template>
    
    <xsl:template match="TABLEAU">
        <table border="1" width="100%">
            <xsl:apply-templates/>
        </table>
    </xsl:template>
    
    <xsl:template match="LT">
        <tr><xsl:apply-templates/></tr>
    </xsl:template>
    
    <xsl:template match="ET">
        <th>
            <xsl:if test="@colspan"><xsl:attribute name="colspan"><xsl:value-of select="@colspan"/></xsl:attribute></xsl:if>
            <xsl:apply-templates/>
        </th>
    </xsl:template>
    
    <xsl:template match="CT">
        <xsl:variable name="ltpos"><xsl:number count="LT" from="TABLEAU"/></xsl:variable>
        <td class="tableau{1+($ltpos mod 2)}">
            <xsl:if test="@colspan"><xsl:attribute name="colspan"><xsl:value-of select="@colspan"/></xsl:attribute></xsl:if>
            <xsl:if test="@rowspan"><xsl:attribute name="rowspan"><xsl:value-of select="@rowspan"/></xsl:attribute></xsl:if>
            <xsl:apply-templates/>
        </td>
    </xsl:template>
    
    <xsl:template match="EQUATION">
        <img alt="{@texte}" class="equation" src="{@image}"/>
    </xsl:template>
    
    <xsl:template match="ENVIMAGE">
        <div class="image" align="center">
		<table border="0" cellspacing="10" class="envimage">
			
			<xsl:if test="normalize-space(TITRE)!=''">
				
				<tr>
					<td>
						<p class="titreimage"><xsl:value-of select="TITRE"/></p>
					</td>
				</tr>
            </xsl:if>
			
				<tr>
					<td>
						<xsl:apply-templates/>
					</td>
				</tr>
		</table>
	</div>
	
    </xsl:template>
    
    <xsl:template match="LEGENDE">
        <div class="legende">
            <xsl:apply-templates/>
        </div>
    </xsl:template>
    
    <xsl:template match="COMMENTAIRE">
        <xsl:if test="$commentaires='oui'">
            <div class="commentaire">
                <xsl:apply-templates/>
            </div>
        </xsl:if>
    </xsl:template>
    
    <xsl:template match="APPLET">
        <applet archive="{@archive}" code="{@classe}" height="{@hauteur}" width="{@largeur}">
            <xsl:apply-templates/>
        </applet>
        <br/>
    </xsl:template>
    
    <xsl:template match="PARAM">
        <param name="{@nom}" value="{@valeur}"/>
    </xsl:template>
    
    <xsl:template match="HTML">
	<xsl:value-of select="." disable-output-escaping="yes"/> 	
    </xsl:template>
    
</xsl:stylesheet>
