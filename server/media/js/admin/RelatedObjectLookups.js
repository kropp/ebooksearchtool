function checkboxDismissRelatedLookupPopup(win, chosenId, chosenName) {
    var name = windowname_to_id(win.name) + "_0";
    var elem = document.getElementById(name);

    ul = elem.parentNode.parentNode.parentNode;

    var li = document.createElement('li'); 

    var chkbox = document.createElement('input');    
    chkbox.type = "checkbox";
    chkbox.id = 'id_author' + chosenId;
    chkbox.name = 'author';
    chkbox.checked="checked";
    chkbox.value=chosenId;

    var label = document.createElement('label');    
    label.htmlFor = 'id_author' + chosenId;
    label.appendChild(chkbox);
    label.appendChild( document.createTextNode(chosenName));
    li.appendChild(label);        
    ul.appendChild(li);    

    win.close();
}

function annotationDismissRelatedLookupPopup(win, chosenId, chosenName) {
    alert (chosenName);
    var name = windowname_to_id(win.name) + "_0";
    var ann_name = document.getElementById("id_name");
    alert (ann_name);
    var elem = document.getElementById(name);
//    elem.parentNode.innerHTML = "1234";
//    ul = elem.parentNode.parentNode.parentNode;

//    var li = document.createElement('li'); 

//    var chkbox = document.createElement('input');    
//    chkbox.type = "checkbox";
//    chkbox.id = 'id_author' + chosenId;
//    chkbox.name = 'author';
//    chkbox.checked="checked";
//    chkbox.value=chosenId;

//    var label = document.createElement('label');    
//    label.htmlFor = 'id_author' + chosenId;
//    label.appendChild(chkbox);
//    label.appendChild( document.createTextNode(chosenName));
//    li.appendChild(label);        
//    ul.appendChild(li);    

    win.close();
}
