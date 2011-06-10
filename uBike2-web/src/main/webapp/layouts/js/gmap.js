/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function loadGmapCoordSysList(context) {
    contextPath = context;

    if (GBrowserIsCompatible()) {
        map = new GMap2(document.getElementById("googleMap"));
        map.addControl(new GLargeMapControl());
        map.addControl(new GMapTypeControl());
        map.setMapType(G_HYBRID_MAP);
    
    }else{
        alert('Browser not compatible');
    }
}


function drawCoordinates(){
    if(placeRectPoly != undefined){
        map.removeOverlay(placeRectPoly);
    }
    placeRectPoly = new GPolygon([
        new GLatLng(document.getElementById('mainForm:y1Input').value,
            document.getElementById('mainForm:x1Input').value),
        new GLatLng(document.getElementById('mainForm:y2Input').value,
            document.getElementById('mainForm:x2Input').value),
        new GLatLng(document.getElementById('mainForm:y3Input').value,
            document.getElementById('mainForm:x3Input').value),
        new GLatLng(document.getElementById('mainForm:y4Input').value,
            document.getElementById('mainForm:x4Input').value),
        new GLatLng(document.getElementById('mainForm:y1Input').value,
            document.getElementById('mainForm:x1Input').value)
        ], "#f33f00", 1, 1, "#ff0000", 0.2);
    map.addOverlay(placeRectPoly);

}