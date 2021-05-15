function getIdByName(elements,name){
    let id;
    elements.forEach(element=>{
        if (name == element.name){
            id = element.id;
        }
    });
    return id;
}
function getObjectByName(elements,name){
    let object;
    elements.forEach(element=>{
        if (element.name == name){
            object = element;
        }
    })
    return object;
}
function getVariableFromQuery(elements, query){
    let value;
    elements.forEach(element=>{
        if (element.id == query){
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
function checkQueryParameter(elements,element,query,page,text) {
    let url = "";
    if (element){
        if (element != text) {
            let id = getIdByName(elements, element);
            url += '&'+page+'=' + id;
        }
    }
    else if(query){
        url += '&'+page+'='+query;
    }
    return url;
}

function formatDate (date) {
    if (!date) return null
    const [year, month, day] = date.split('-')
    return `${day}/${month}/${year}`
}

export {getIdByName, getObjectByName, getVariableFromQuery,pushNewState, checkQueryParameter, formatDate}