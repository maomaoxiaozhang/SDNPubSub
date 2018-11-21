/*

Copyright (C) 2012  Observatoire de Paris

The JavaScript code in this page is free software: you can
redistribute it and/or modify it under the terms of the GNU
General Public License (GNU GPL) as published by the Free Software
Foundation, either version 3 of the License, or (at your option)
any later version.  The code is distributed WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS
FOR A PARTICULAR PURPOSE.  See the GNU GPL for more details.

As additional permission under GNU GPL version 3 section 7, you
may distribute non-source (e.g., minimized or compacted) forms of
that code without the copy of the GNU GPL normally required by
section 4, provided you include this license notice and a URL
through which recipients can access the Corresponding Source.

*/

contentLoaded(window, video_init);

/**
 * paramètres :
 * player : chemin vers le lecteur Flash de MPEG4 (FLV Player)
 * codebase : codebase pour l'applet Java Cortado lisant du Ogg Theora
 * archive : archive pour l'applet
 */
function video_init() {
    var nlvideos = document.getElementsByTagName('video');
    if (nlvideos.length < 1)
        return;
    
    // on met la liste dans un tableau, parce-que la nodelist va changer pendant la boucle
    // qui remplace les vidéos de la page
    var videos = new Array();
    for (var i=0; i<nlvideos.length; i++)
        videos.push(nlvideos[i]);
    
    var parametres = lecture_parametres('video.js');
    var htmlversplayer = parametres['player'] || 'player_flv_maxi.swf';
    var options_player = 'autoload=1&showstop=1&showvolume=1&showtime=1&showfullscreen=1&showiconplay=1';
    var codebase_applet = parametres['codebase'] || '.';
    var archive_applet = parametres['archive'] || 'cortado.jar';
    for (var i=0; i<videos.length; i++) {
        var video = videos[i];
        // Dans certains cas, canPlayType renvoie 'maybe' et ensuite il y a une erreur.
        // Chromium lance les erreurs source.onerror avant le body.onload, et ensuite on ne
        // peut savoir qu'il y a eu un problème qu'avec networkState == NETWORK_NO_SOURCE
        // (video.error n'est pas défini)
        // mais pour IE9, networkState == NETWORK_NO_SOURCE même quand il peut lire la vidéo
        // => on utilise contentLoaded pour faire les modifs avant les tentatives de lancement des vidéos,
        // et on remplace le code dès que onerror est appelé
        if (!lisible(video))
            remplacer(video, htmlversplayer, options_player, codebase_applet, archive_applet);
        else {
            var sources = video.getElementsByTagName('source');
            if (sources.length > 0) {
                for (var j=0; j<sources.length; j++) {
                    sources[j].addEventListener('error', function() {
                        var video = this.parentNode;
                        if (video.networkState == video.NETWORK_NO_SOURCE)
                            remplacer(video, htmlversplayer, options_player, codebase_applet, archive_applet);
                    }, false);
                }
            } else {
                video.addEventListener('error', function() {
                    var video = this;
                    if (video.networkState == video.NETWORK_NO_SOURCE)
                        remplacer(video, htmlversplayer, options_player, codebase_applet, archive_applet);
                }, false);
            }
        }
    }
}

// from contentLoaded.js by Diego Perini, MIT licence
function contentLoaded(win, fn) {

    var done = false, top = true,

    doc = win.document, root = doc.documentElement,

    add = doc.addEventListener ? 'addEventListener' : 'attachEvent',
    rem = doc.addEventListener ? 'removeEventListener' : 'detachEvent',
    pre = doc.addEventListener ? '' : 'on',

    init = function(e) {
        if (e.type == 'readystatechange' && doc.readyState != 'complete') return;
        (e.type == 'load' ? win : doc)[rem](pre + e.type, init, false);
        if (!done && (done = true)) fn.call(win, e.type || e);
    },

    poll = function() {
        try { root.doScroll('left'); } catch(e) { setTimeout(poll, 50); return; }
        init('poll');
    };

    if (doc.readyState == 'complete') fn.call(win, 'lazy');
    else {
        if (doc.createEventObject && root.doScroll) {
            try { top = !win.frameElement; } catch(e) { }
            if (top) poll();
        }
        doc[add](pre + 'DOMContentLoaded', init, false);
        doc[add](pre + 'readystatechange', init, false);
        win[add](pre + 'load', init, false);
    }

}

function lecture_parametres(nom_script) {
    var scripts = document.getElementsByTagName('script');
    var myScript = null;
    for (var i=0; i<scripts.length; i++) {
        var src = scripts[i].getAttribute('src');
        if (src != null && src.indexOf(nom_script) > -1) {
            myScript = scripts[i];
            break;
        }
    }
    if (myScript == null)
        return(new Object());
    var query = myScript.src.replace(/^[^\?]+\??/,'');
    var params = new Object();
    if (! query)
        return params;
    var Pairs = query.split(/[;&]/);
    for (var i=0; i<Pairs.length; i++) {
        var KeyVal = Pairs[i].split('=');
        if (! KeyVal || KeyVal.length != 2)
            continue;
        var key = unescape(KeyVal[0]);
        var val = unescape(KeyVal[1]);
        val = val.replace(/\+/g, ' ');
        params[key] = val;
    }
    return(params);
}

function lisible(video) {
    if (!video.canPlayType)
        return(false);
    var sources = video.getElementsByTagName('source');
    if (sources.length == 0) {
        var src = video.getAttribute('src');
        if (src == null)
            return(false);
        var mime = mime_depuis_nom_fichier(src);
        if (mime == null) // cas d'un script PHP, par exemple: on ne peut pas déterminer le type MIME
            return(true); // on essaye
        return(video.canPlayType(mime).replace('no', '') != '');
    }
    for (var i=0; i<sources.length; i++) {
        var source = sources[i];
        // valeurs possibles pour canPlayType: '', 'no', 'maybe', 'probably'
        if (video.canPlayType(source.getAttribute('type')).replace('no', '') != '')
            return(true);
    }
    return(false);
}

function mime_depuis_nom_fichier(nom) {
    var ind = nom.lastIndexOf('.');
    if (ind == -1)
        return(null);
    var ext = nom.substring(ind + 1).toLowerCase();
    if (ext == 'ogv')
        return('video/ogg');
    if (ext == 'webm')
        return('video/webm');
    if (ext == 'mp4')
        return('video/mp4');
    return(null);
}

// remplace la vidéo par du Flash pour MPEG4, et par une applet Java pour Ogg Theora
// htmlversplayer: chemin depuis le fichier HTML vers le lecteur vidéo en Flash
function remplacer(video, htmlversplayer, options_player, codebase_applet, archive_applet) {
    var srcogg = '', srcmpeg4 = ''; // pas de remplacement pour WEBM
    var sources = video.getElementsByTagName('source');
    // IE7 ne comprend pas que source est dans video parce-qu'il pense que video est un élément vide
    // il faut donc regarder les éléments suivants video dans le DOM
    if (sources.length == 0 && !video.canPlayType) {
        sources = new Array();
        var next = video.nextSibling;
        while (next != null && next.nodeName == 'SOURCE') {
            sources.push(next);
            next = next.nextSibling;
        }
    }
    if (sources.length == 0) {
        var src = video.getAttribute('src');
        if (src != null) {
            var mime = mime_depuis_nom_fichier(src);
            if (mime == 'video/ogg')
                srcogg = video.getAttribute('src');
            else if (mime == 'video/mp4')
                srcmpeg4 = video.getAttribute('src');
        }
    } else {
        for (var i=0; i<sources.length; i++) {
            var source = sources[i];
            if (source.getAttribute('type').indexOf('video/ogg') == 0)
                srcogg = source.getAttribute('src');
            else if (source.getAttribute('type').indexOf('video/mp4') == 0)
                srcmpeg4 = source.getAttribute('src');
        }
    }
    if (srcogg == '' && srcmpeg4 == '')
        return;
    var width = video.getAttribute('width');
    var height = video.getAttribute('height');
    
    var IE = (navigator.appName == 'Microsoft Internet Explorer');
    
    // MPEG4 avec Flash en priorité
    if (srcmpeg4 != '') {
        // object / lien
        var playerversfichier = chemin_player_fichier(htmlversplayer, srcmpeg4);
        var flashvars = 'flv=' + playerversfichier + '&' + options_player;
        var obj;
        if (IE) {
            // méthode pour ce navigateur pourri
            var div = document.createElement("div");
            var ieml = '<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="' + width + '" height="' + height +'">';
            // on désactive le cache, sinon le Flash ne se charge pas bien lors d'un rechargement de la page
            ieml += '<param name="movie" value="' + htmlversplayer + '?nocache=' + Math.random() + '">';
            ieml += '<param name="flashvars" value="' + flashvars.replace('&', '&amp;') + '">';
            ieml += '<param name="allowFullScreen" value="true">';
            ieml += 'Video: <a href="' + srcmpeg4 + '">' + nom_fichier(srcmpeg4) + '</a>';
            ieml += '</object>';
            div.innerHTML = ieml;
            obj = div.firstChild;
        } else {
            // méthode standard
            obj = document.createElement('object');
            obj.setAttribute('type', 'application/x-shockwave-flash');
            obj.setAttribute('data', htmlversplayer);
            obj.setAttribute('width', width);
            obj.setAttribute('height', height);
            ajouter_param(obj, 'movie', htmlversplayer);
            ajouter_param(obj, 'flashvars', flashvars);
            ajouter_param(obj, 'allowFullScreen', true);
            
            obj.appendChild(document.createTextNode('Video: '));
            var a = document.createElement('a');
            a.setAttribute('href', srcmpeg4);
            a.appendChild(document.createTextNode(nom_fichier(srcmpeg4)));
            obj.appendChild(a);
        }
        
        video.parentNode.replaceChild(obj, video);
    } else if (srcogg != '') {
        // applet / lien
        var obj;
        if (IE) {
            var div = document.createElement("div");
            var ieml = '<object classid="clsid:8AD9C840-044E-11D1-B3E9-00805F499D93" width="' + width + '" height="' + height +'">';
            ieml += '<param name="code" value="com.fluendo.player.Cortado">';
            ieml += '<param name="codebase" value="' + codebase_applet + '">';
            ieml += '<param name="archive" value="' + archive_applet + '">';
            ieml += '<param name="url" value="' + srcogg + '">';
            ieml += '<param name="seekable" value="false">';
            ieml += '<param name="autoPlay" value="false">';
            ieml += '<param name="showStatus" value="show">';
            ieml += '<param name="statusHeight" value="16">';
            ieml += 'Video: <a href="' + srcogg + '">' + nom_fichier(srcogg) + '</a>';
            ieml += '</object>';
            div.innerHTML = ieml;
            obj = div.firstChild;
        } else {
            obj = document.createElement('object');
            obj.setAttribute('type', 'application/x-java-applet');
            obj.setAttribute('width', width);
            obj.setAttribute('height', Number(height) + 16);
            ajouter_param(obj, 'code', 'com.fluendo.player.Cortado');
            ajouter_param(obj, 'codebase', codebase_applet);
            ajouter_param(obj, 'archive', archive_applet);
            
            ajouter_param(obj, 'url', srcogg);
            ajouter_param(obj, 'seekable', false);
            ajouter_param(obj, 'autoPlay', false);
            ajouter_param(obj, 'showStatus', 'show');
            ajouter_param(obj, 'statusHeight', '16');
            
            obj.appendChild(document.createTextNode('Video: '));
            var a = document.createElement('a');
            a.setAttribute('href', srcogg);
            a.appendChild(document.createTextNode(nom_fichier(srcogg)));
            obj.appendChild(a);
        }
        
        video.parentNode.replaceChild(obj, video);
    }
}

// calcule le chemin du player vers le fichier à partir du chemin html vers player et du chemin html vers fichier
// (en utilisant l'URL)
function chemin_player_fichier(htmlversplayer, htmlversfichier) {
    var url = document.URL;
    var chemin = normaliser_chemin(htmlversplayer);
    var playervershtml = '';
    var indexChemin = chemin.indexOf('/');
    var indexURL = url.lastIndexOf('/');
    url = url.substring(0, indexURL);
    indexURL = url.lastIndexOf('/');
    var dossier = '';
    while (indexChemin != -1) {
        dossier = chemin.substring(0, indexChemin);
        if (dossier == '..') {
            playervershtml = url.substring(indexURL+1) + '/' + playervershtml;
            url = url.substring(0, indexURL);
            indexURL = url.lastIndexOf('/');
        } else
            playervershtml = '../' + playervershtml;
        chemin = chemin.substring(indexChemin + 1);
        indexChemin = chemin.indexOf('/');
    }
    return(normaliser_chemin(playervershtml + htmlversfichier));
}

// transforme 'a/..' en '' à l'intérieur de chemins
function normaliser_chemin(chemin) {
    var c = '';
    var ind1 = chemin.indexOf('/');
    if (ind1 == -1)
        return(chemin);
    var p1 = chemin.substring(0, ind1);
    var ind2 = chemin.substring(ind1 + 1).indexOf('/');
    if (ind2 == -1)
        return(chemin);
    ind2 += ind1 + 1;
    var p2 = chemin.substring(ind1 + 1, ind2);
    if (p1 != '..' && p2 == '..')
        return(normaliser_chemin(chemin.substring(ind2 + 1)));
    else
        return(chemin.substring(0, ind1 + 1) + normaliser_chemin(chemin.substring(ind1 + 1)));
}

function nom_fichier(chemin) {
    var ind = chemin.indexOf('/');
    if (ind == -1)
        return(chemin);
    return(nom_fichier(chemin.substring(ind + 1)));
}

function ajouter_param(element, nom, valeur) {
    var param = document.createElement('param');
    param.setAttribute('name', nom);
    param.setAttribute('value', valeur);
    element.appendChild(param);
}
