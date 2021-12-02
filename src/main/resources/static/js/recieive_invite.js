document.querySelector("#invite_sort").onchange=function(){
    let select=this.value;
    let terms=document.querySelectorAll("tbody tr");
    var termArray = Array.prototype.slice.call(terms, 0);
    console.log(terms);
    console.log(termArray);
    terms=termArray
    if(select=="name"){
        terms.sort((a,b) => (a.children[0].innerText > b.children[0].innerText) ? 1 : ((b.children[0].innerText > a.children[0].innerText) ? -1 : 0));
        let tbody=document.querySelector("tbody");
        tbody.innerHTML="";
        for(let i=0;i<terms.length;i++){
            tbody.append(terms[i]);
        }
    }
    else if(select=="date"){
        terms.sort((a,b) => (a.children[1].innerText > b.children[1].innerText) ? 1 : ((b.children[1].innerText > a.children[1].innerText) ? -1 : 0));
        let tbody=document.querySelector("tbody");
        tbody.innerHTML="";
        for(let i=0;i<terms.length;i++){
            tbody.append(terms[i]);
        }
    }
}