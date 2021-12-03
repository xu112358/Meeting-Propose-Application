document.querySelector("#finalized-filter").onchange=function(){
    let select=this.value;
    console.log(select);
    let terms=document.querySelectorAll("tbody tr");
    if(select=="all"){
        console.log("inall");
        for(let i=0;i<terms.length;i++){
            let cur_term=terms[i];
            console.log(cur_term.classList);
            if(cur_term.classList.contains("noshow")){
                cur_term.classList.remove("noshow");
            }


        }

    }else if(select=="finalized"){
        let select1 = "finalized responded";
        let select2 = "finalized not responded";
        for(let i=0;i<terms.length;i++){
            let cur_term=terms[i];
            if(cur_term.children[2].innerText!=select1 && cur_term.children[2].innerText!=select2){
                cur_term.classList.add("noshow");
            }
            else{
                cur_term.classList.remove("noshow");
            }
        }
    }else{
        for(let i=0;i<terms.length;i++){
            let cur_term=terms[i];

            if(cur_term.children[2].innerText!=select){
                cur_term.classList.add("noshow");
            }
            else{
                cur_term.classList.remove("noshow");
            }
        }
    }

    // for(let i=0;i<terms.length;i++){
    //     let cur_term=terms[i];
    //     console.log(cur_term.children[2]);
    // }
}


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