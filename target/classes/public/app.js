function loadGetMsg() {
    let nameVar = document.getElementById("name").value;
    const xhttp = new XMLHttpRequest();
    xhttp.onload = function() {
        document.getElementById("getrespmsg").innerHTML = this.responseText;
    }
    xhttp.open("GET", "/hello?name=" + nameVar);
    xhttp.send();
}

function loadPostMsg() {
    let name = document.getElementById("postname").value;
    fetch("/hellopost?name=" + name, { method: 'POST' })
        .then(x => x.text())
        .then(y => document.getElementById("postrespmsg").innerHTML = y);
}
