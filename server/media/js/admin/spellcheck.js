function addLoadEvent() {
    var elems = document.getElementsByTagName("textarea");
    for (var i = 0; i < elems.length; i++) {
        var elem = elems[i];
        var googie1 = new GoogieSpell("/site_media/googiespell/", "/spellcheck/?lang="); 
        googie1.decorateTextarea(elem.id);
    }
}

