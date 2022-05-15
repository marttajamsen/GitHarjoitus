<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="scripts/main.js"></script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<title>Asiakkaan muutos</title>
</head>
<body onkeydown="tutkiKey(event)">
<body>
<form id="tiedot">
	<table>
		<thead>	
			<tr>
				<th colspan="3" id="ilmo"></th>
				<th colspan="2" class="oikealle"><a href="listaaasiakkaat.jsp" id="takaisin">Takaisin listaukseen</a></th>
			</tr>		
			<tr>
				<th>Etunimi</th>
				<th>Sukunimi</th>
				<th>Puhelin</th>
				<th>Sähköposti</th>
				<th></th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td><input type="text" name="etunimi" id="etunimi"></td>
				<td><input type="text" name="sukunimi" id="sukunimi"></td>
				<td><input type="text" name="puhelin" id="puhelin"></td>
				<td><input type="text" name="sposti" id="sposti"></td> 
				<td><input type="button" id="tallenna" value="Hyvaksy" onclick="vieTiedot()"></td>
			</tr>
		</tbody>
	</table>
	<input type="hidden" name="vanhapuhelin" id="vanhapuhelin">	
</form>
<span id="ilmo"></span>
</body>
<script>
function tutkiKeyX(event){
	if(event.keyCode==13){
		vieTiedot();
	}		
}

var tutkiKey = (event) => {
	if(event.keyCode==13){
		vieTiedot();
	}	
}

document.getElementById("puhelin").focus();

var puhelin = requestURLParam("puhelin");  
fetch("asiakkaat/haeyksi/" + puhelin,{
      method: 'GET'	      
    })
.then( function (response) {
	return response.json()
})
.then( function (responseJson) {
	console.log(responseJson);
	document.getElementById("etunimi").value = responseJson.etunimi;		
	document.getElementById("sukunimi").value = responseJson.sukunimi;	
	document.getElementById("puhelin").value = responseJson.puhelin;	
	document.getElementById("sposti").value = responseJson.sposti;	
	document.getElementById("vanhapuhelin").value = responseJson.puhelin;	
});	

function vieTiedot(){	
	var ilmo="";
	if(document.getElementById("puhelin").value.length<3){
		ilmo="Puhelinnumero ei kelpaa!";		
	}else if(document.getElementById("etunimi").value.length<2){
		ilmo="Etunimi ei kelpaa!";		
	}else if(document.getElementById("sukunimi").value.length<1){
		ilmo="Sukunimi ei kelpaa!";		
	}else if(document.getElementById("sposti").value.length<4){
		ilmo="Sähköposti ei kelpaa!";	
	}
	if(ilmo!=""){
		document.getElementById("ilmo").innerHTML=ilmo;
		setTimeout(function(){ document.getElementById("ilmo").innerHTML=""; }, 3000);
		return;
	}
	document.getElementById("etunimi").value=siivoa(document.getElementById("etunimi").value);
	document.getElementById("sukunimi").value=siivoa(document.getElementById("sukunimi").value);
	document.getElementById("puhelin").value=siivoa(document.getElementById("puhelin").value);
	document.getElementById("sposti").value=siivoa(document.getElementById("sposti").value);	
		
	var formJsonStr=formDataToJSON(document.getElementById("tiedot")); 
	fetch("asiakkaat",{
	      method: 'PUT',
	      body:formJsonStr
	    })
	.then( function (response) {	
		return response.json()
	})
	.then( function (responseJson) {	
		var vastaus = responseJson.response;		
		if(vastaus==0){
			document.getElementById("ilmo").innerHTML= "Asiakkaan lisääminen epäonnistui";
      	}else if(vastaus==1){	        	
      		document.getElementById("ilmo").innerHTML= "Asiakkaan lisääminen onnistui";			      	
		}
		setTimeout(function(){ document.getElementById("ilmo").innerHTML=""; }, 5000);
	});	
	document.getElementById("tiedot").reset(); 
}
</script>
</html>