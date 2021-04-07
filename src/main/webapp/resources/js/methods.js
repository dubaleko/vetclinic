function getIdByName(elements,name){
    let id;
    elements.forEach(element=>{
        if (name == element.name){
            id = element.id;
        }
    });
    return id;
}
function getVariableFromQuery(elements, query){
    let value;
    elements.forEach(element=>{
        if (element.id == query){
            query = null;
            value = element.name;
        }
    })
    return value;
}
function pushNewState(firstList, firstValue, secondList, secondValue, page, name, secondName){
    let id = getIdByName(firstList,firstValue);
    let secondId = getIdByName(secondList,secondValue);
    if (secondId){
        if (id){
            window.history.pushState("", "Title", '/'+page+'?'+secondName+'=' + secondId +
                '&'+name+'=' + id);
        }
        else {
            window.history.pushState("", "Title", '/'+page+'?'+secondName+'='+secondId);
        }
    }
    else {
        if (id){
            window.history.pushState("", "Title",  '/'+page+'?'+name+'='+id);
        }
        else {
            window.history.pushState("", "Title", '/'+page);
        }
    }
}

export {getIdByName, getVariableFromQuery,pushNewState}