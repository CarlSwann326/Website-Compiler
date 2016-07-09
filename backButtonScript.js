function logNavigation(functionName){

  if(typeof(Storage) !== "undefined") {


        if (sessionStorage.getItem('loadStack') !== null) {

        	var newStack = {functionName};
		    sessionStorage.setItem('loadStack', JSON.stringify(newStack));
          

        } else {

        	var currentStack = sessionStorage.getItem('loadStack');
        	currentStack.push(functionName);
        	sessionStorage.setItem('loadStack', JSON.stringify(currentStack));

        }    

    }     
}

function goBack(){

 if(typeof(Storage) !== "undefined") {

		var loadStack = JSON.parse(sessionStorage.getItem('loadStack'));

		eval(loadStack[loadStack.length - 2]);

	}
}